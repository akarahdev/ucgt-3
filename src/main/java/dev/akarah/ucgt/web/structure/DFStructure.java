package dev.akarah.ucgt.web.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record DFStructure(
        List<String> paletteData,
        List<String> blockData
) {
    public static DFStructure of(VanillaStructure structure) {
        MultiStringContainer paletteStr = new MultiStringContainer("|");

        for(BlockState state : structure.palette()) {
            paletteStr.add(
                    state.name() + ";"
                    + state.properties().entrySet().stream()
                            .map(x -> x.getKey() + "=" + x.getValue())
                            .collect(Collectors.joining(","))
            );
        }

        MultiStringContainer dataStr = new MultiStringContainer("|");
        for(VanillaStructure.BlockData block : structure.blocks()) {
            dataStr.add(block.state() + "," + block.x() + "," + block.y() + "," + block.z());
        }

        return new DFStructure(
                paletteStr.toStrings(),
                dataStr.toStrings()
        );
    }

    public static DFStructure of(LitematicaStructure structure) {
        if(structure.regions().isEmpty()) {
            return new DFStructure(List.of(), List.of());
        }

        MultiStringContainer paletteStr = new MultiStringContainer("|");
        MultiStringContainer dataStr = new MultiStringContainer("|");

        int paletteOffset = 0;
        for(LitematicaStructure.Region region : structure.regions()) {

        }

        throw new RuntimeException("TODO");
    }
}
