package dev.akarah.ucgt.web.player;

import java.util.UUID;

public record PlayerData(
        UUID uuid,
        String username
) {
}
