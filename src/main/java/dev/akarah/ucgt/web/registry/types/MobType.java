package dev.akarah.ucgt.web.registry.types;

import java.util.Map;

public record MobType(
        String name,
        String type,
        int reward,
        Map<String, Double> stats
) {
}
