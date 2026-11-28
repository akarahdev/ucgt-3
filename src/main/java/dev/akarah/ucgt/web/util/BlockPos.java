package dev.akarah.ucgt.web.util;

import net.querz.nbt.tag.CompoundTag;

public record BlockPos(int x, int y, int z) {
    public static BlockPos fromCompoundXYZ(CompoundTag tag) {
        return new BlockPos(
                tag.getInt("x"),
                tag.getInt("y"),
                tag.getInt("z")
        );
    }
}
