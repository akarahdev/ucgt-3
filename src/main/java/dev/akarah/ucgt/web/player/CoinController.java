package dev.akarah.ucgt.web.player;

import org.springframework.http.HttpStatusCode;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/coins")
public class CoinController {
    private final JdbcClient jdbcClient;

    public CoinController(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @GetMapping("/{uuid}")
    public int getByUuid(@PathVariable UUID uuid) {
        JdbcClient.ResultQuerySpec spec = jdbcClient.sql("SELECT amount FROM coins WHERE player = :uuid")
                .param("uuid", uuid)
                .query();

        List<Map<String, Object>> rowList = spec.listOfRows();
        if(rowList.isEmpty()) {
            return 0;
        }

        return (int) rowList.getFirst().get("amount");
    }

    @PostMapping("/{uuid}")
    public void setByUuid(
            @PathVariable UUID uuid,
            @RequestParam int amount
    ) {
        jdbcClient.sql("INSERT INTO coins (player, amount) VALUES (:uuid, :amount) ON DUPLICATE KEY UPDATE amount = :amount")
                .param("uuid", uuid)
                .param("amount", amount)
                .update();
    }
}
