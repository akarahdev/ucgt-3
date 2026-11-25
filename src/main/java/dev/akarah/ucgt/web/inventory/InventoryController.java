package dev.akarah.ucgt.web.inventory;

import dev.akarah.ucgt.web.inventory.data.Inventory;
import dev.akarah.ucgt.web.inventory.data.InventorySlot;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    public Inventory getInvForPlayer(@PathVariable UUID uuid) {
        return getFromSpec(
                jdbcClient.sql("SELECT id, item FROM inventory_slots WHERE player = ?")
                        .params(uuid)
                        .query()
        );
    }

    @GetMapping("/inventory/{uuid}/{id}")
    public InventorySlot getSlotForPlayer(@PathVariable UUID uuid, @PathVariable int id) {
        return getSingleFromSpecOr404(
                jdbcClient.sql("SELECT id, item FROM inventory_slots WHERE player = ? AND id = ?")
                        .params(uuid, id)
                        .query()
        );
    }

    @PostMapping("/inventory/{uuid}")
    public void addItem(
            @PathVariable UUID uuid,
            @RequestParam String item
    ) {
        jdbcClient.sql("INSERT INTO inventory_slots (player, item) VALUES (?, ?)")
                .params(uuid, item)
                .update();
    }

    @PostMapping("/equipment/sword/{uuid}")
    public void putItemInSwordSlot(
            @PathVariable UUID uuid,
            @RequestParam int item
    ) {
        jdbcClient.sql("INSERT INTO equipment (player, sword) VALUES (:uuid, :item) ON DUPLICATE KEY UPDATE sword = :item")
                .param("uuid", uuid)
                .param("item", item)
                .update();
    }

    @GetMapping("/equipment/sword/{uuid}")
    public InventorySlot getItemInSwordSlot(
            @PathVariable UUID uuid
    ) {
        return getSingleFromSpecOr404(
                jdbcClient.sql("""
                SELECT id, item FROM inventory_slots
                WHERE player = :uuid
                AND id IN (SELECT sword FROM equipment WHERE equipment.player = :uuid)
                """)
                        .param("uuid", uuid)
                        .query()
        );
    }

    @PostMapping("/equipment/bow/{uuid}")
    public void putItemInBowSlot(
            @PathVariable UUID uuid,
            @RequestParam int item
    ) {
        jdbcClient.sql("INSERT INTO equipment (player, bow) VALUES (:uuid, :item) ON DUPLICATE KEY UPDATE bow = :item")
                .param("uuid", uuid)
                .param("item", item)
                .update();
    }

    @GetMapping("/equipment/bow/{uuid}")
    public InventorySlot getItemInBowSlot(
            @PathVariable UUID uuid
    ) {
        return getSingleFromSpecOr404(
                jdbcClient.sql("""
                SELECT id, item FROM inventory_slots
                WHERE player = :uuid
                AND id IN (SELECT bow FROM equipment WHERE equipment.player = :uuid)
                """)
                        .param("uuid", uuid)
                        .query()
        );
    }

    private Inventory getFromSpec(JdbcClient.ResultQuerySpec spec) {
        return new Inventory(
                spec.listOfRows().stream()
                        .map(x -> new InventorySlot(
                                (int) Objects.requireNonNull(x.get("id")),
                                (String) x.get("item")
                        ))
                        .toList()
        );
    }

    private InventorySlot getSingleFromSpecOr404(JdbcClient.ResultQuerySpec spec) {
        List<InventorySlot> slots = spec.listOfRows().stream()
                .map(x -> new InventorySlot(
                        (int) Objects.requireNonNull(x.get("id")),
                        (String) x.get("item")
                ))
                .toList();
        if(slots.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }
        return slots.getFirst();
    }
}
