package dev.akarah.ucgt.web.structure;

import dev.akarah.ucgt.web.util.BlockPos;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;

import java.util.*;

public record LitematicaStructure(
        List<Region> regions
) {
    public record Region(
            BlockPos position,
            BlockPos size,
            List<BlockState> palette,
            Map<BlockPos, Integer> blockStateIndices
    ) {}

    public static LitematicaStructure of(CompoundTag tag) {
        Collection<Tag<?>> regionTags = tag.getCompoundTag("Regions").values();

        List<Region> regions = new ArrayList<>();
        for(Tag<?> subtag : regionTags) {
            regions.add(parseRegion((CompoundTag) subtag));
        }

        return new LitematicaStructure(regions);
    }

    private static Region parseRegion(CompoundTag tag) {
        BlockPos position = BlockPos.fromCompoundXYZ(tag.getCompoundTag("Position"));
        BlockPos size = BlockPos.fromCompoundXYZ(tag.getCompoundTag("Size"));

        List<BlockState> palette = new ArrayList<>();
        ListTag<CompoundTag> blockStatePalette = tag.getListTag("BlockStatePalette").asCompoundTagList();
        for(CompoundTag subtag : blockStatePalette) {
            palette.add(BlockState.fromLegacyVanillaFormat(subtag));
        }

        Map<BlockPos, Integer> blockStateIndicesMap = new HashMap<>();
        long[] blockStateIndices = tag.getLongArray("BlockStates");
        // ln(x) / ln(2) = log2(x)
        int bitsPerEntry = (int) Math.max(2, Math.ceil(Math.log(palette.size()) / Math.log(2)));
        for(int x = 0; x < size.x(); x++) {
            for(int y = 0; y < size.x(); y++) {
                for(int z = 0; z < size.x(); z++) {
                    int entryIdx = y * (size.x() * size.z()) + z * size.x() + x;
                    int paletteIdx = (int) readBitEntry(blockStateIndices, entryIdx, bitsPerEntry);
                    blockStateIndicesMap.put(new BlockPos(x, y, z), paletteIdx);
                }
            }
        }

        return new Region(
                position,
                size,
                palette,
                blockStateIndicesMap
        );
    }

    private static long readBitEntry(long[] arr, int index, int bits) {
        long mask = (1L << bits) - 1;
        long startBit = (long) index * bits;
        int startLong = (int) (startBit >>> 6);              // /64
        int endLong   = (int) (((index + 1L) * bits - 1) >>> 6);
        int offset    = (int) (startBit & 63);               // %64

        if (startLong == endLong) {
            return (arr[startLong] >>> offset) & mask;
        } else {                                             // spans two longs
            return ((arr[startLong] >>> offset) | (arr[endLong] << (64 - offset))) & mask;
        }
    }
}
