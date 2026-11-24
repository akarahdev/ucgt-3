package dev.akarah.ucgt.web.registry.types;

import dev.akarah.ucgt.web.util.Vec3;

public record Region(
        String name,
        Vec3 corner1,
        Vec3 corner2
) {
}
