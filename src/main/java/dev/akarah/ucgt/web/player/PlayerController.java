package dev.akarah.ucgt.web.player;

import org.springframework.http.HttpStatusCode;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/player")
public class PlayerController {
    private final JdbcClient jdbcClient;

    public PlayerController(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @GetMapping("/{uuid}")
    public PlayerData getByUuid(@PathVariable UUID uuid) {
        JdbcClient.ResultQuerySpec spec = jdbcClient.sql("SELECT player, username FROM players WHERE player = :uuid")
                .param("uuid", uuid)
                .query();
        List<Map<String, Object>> rowList = spec.listOfRows();
        if(rowList.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }

        return new PlayerData(
                (UUID) rowList.getFirst().get("player"),
                (String) rowList.getFirst().get("username")
        );
    }

    @PostMapping("/{uuid}")
    public void setByUuid(
            @PathVariable UUID uuid,
            @RequestBody PlayerData playerData
    ) {
        jdbcClient.sql("INSERT INTO players (player, username) VALUES (:uuid, :username)")
                .param("uuid", playerData.uuid())
                .param("username", playerData.username())
                .update();
    }
}
