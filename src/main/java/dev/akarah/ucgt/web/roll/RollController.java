package dev.akarah.ucgt.web.roll;

import dev.akarah.ucgt.web.inventory.data.InventorySlot;
import dev.akarah.ucgt.web.registry.RegistryResource;
import dev.akarah.ucgt.web.registry.types.Banner;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RollController {
    private final RegistryResource registries;
    private final JdbcClient jdbcClient;

    public RollController(RegistryResource registries, JdbcClient jdbcClient) {
        this.registries = registries;
        this.jdbcClient = jdbcClient;
    }

    @GetMapping("/v1/roll/{uuid}/{bannerId}")
    public List<String> roll(
            @PathVariable String uuid,
            @PathVariable String bannerId,
            @RequestParam(required = false, defaultValue = "1") int count
    ) {
        Banner banner = this.registries.banners.getOrNull(bannerId);
        if(banner == null) {
            return List.of();
        }

        int weightSum = 0;
        for(Map.Entry<String, Integer> weight : banner.weights().entrySet()) {
            weightSum += weight.getValue();
        }

        List<String> items = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            int subtractor = (int) Math.floor(Math.random() * weightSum);
            for(Map.Entry<String, Integer> weight : banner.weights().entrySet()) {
                subtractor -= weight.getValue();
                if(subtractor <= 0) {
                    items.add(weight.getKey());
                    break;
                }
            }
        }

        StringBuilder valueBuilder = new StringBuilder();
        int idx = 0;
        for(String item : items) {
            valueBuilder.append("('").append(uuid).append("','").append(item).append("')");
            idx++;
            if(idx != items.size()) {
                valueBuilder.append(",");
            }
        }

        jdbcClient.sql("INSERT INTO inventory_slots (player, item) VALUES " + valueBuilder)
                .update();

        return items;
    }
}
