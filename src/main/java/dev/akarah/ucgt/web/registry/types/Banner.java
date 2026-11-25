package dev.akarah.ucgt.web.registry.types;

import dev.akarah.ucgt.web.util.Location;

import java.util.Map;

public record Banner(
        String name,
        int cost,
        Location camera,
        Map<String, Integer> weights
) {
}
