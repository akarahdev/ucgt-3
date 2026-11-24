package dev.akarah.ucgt.web.registry.types;

import java.util.Map;

public record ItemType(
        String name,
        String type,
        String model,
        String description,
        Map<String, Double> stats
) {
}
