package dev.akarah.ucgt.web.inventory;

import dev.akarah.ucgt.web.inventory.data.Inventory;
import dev.akarah.ucgt.web.inventory.data.InventorySlot;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/v1/storage")
public class InventoryController {
    private final JdbcClient jdbcClient;

    public InventoryController(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @GetMapping("/inventory/{uuid}")
    public Inventory getForPlayer(@PathVariable UUID uuid) {
        JdbcClient.ResultQuerySpec spec = jdbcClient.sql("SELECT id, item FROM inventory_slots WHERE player = ?")
                .params(uuid)
                .query();
        Inventory inv = new Inventory(
                spec.listOfRows().stream()
                        .map(x -> new InventorySlot(
                                (int) Objects.requireNonNull(x.get("id")),
                                (String) x.get("item")
                        ))
                        .toList()
        );
        LoggerFactory.getLogger(InventoryController.class).info(inv.toString());
        return inv;
    }

    @PostMapping("/inventory/{uuid}")
    public void addItem(
            @PathVariable UUID uuid,
            @RequestParam String item
    ) {
        jdbcClient.sql("INSERT INTO inventory_slots (player, item) VALUES (?, ?)")
                .params(uuid, item)
                .update();
        LoggerFactory.getLogger(InventoryController.class).info(uuid.toString());
        LoggerFactory.getLogger(InventoryController.class).info(item);
    }
}
