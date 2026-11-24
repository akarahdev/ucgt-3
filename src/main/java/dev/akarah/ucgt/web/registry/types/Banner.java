package dev.akarah.ucgt.web.registry.types;

import java.util.Map;

public record Banner(
        String name,
        int cost,
        Map<String, Integer> weights
) {
}
