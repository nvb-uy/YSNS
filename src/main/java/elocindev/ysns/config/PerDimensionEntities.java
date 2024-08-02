package elocindev.ysns.config;

import java.nio.file.Path;
import java.util.List;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.necronomicon.config.Comment;
import elocindev.necronomicon.config.NecConfig;
import elocindev.ysns.api.DimensionSpawnHolder;

public class PerDimensionEntities {
    public static final String FOLDER = "ysns";
    public static final String FILE_NAME = "per_dimension_entities.json5";
    public static final int CURRENT_CONFIG_VERSION = 1;

    @NecConfig
    public static PerDimensionEntities INSTANCE;

    public static String getFile() {
        Path folder = Path.of(NecConfigAPI.getFile(FOLDER));

        if (!folder.toFile().exists())
            folder.toFile().mkdirs();
          

        return folder.toString()+"/"+FILE_NAME;
    }

    public String getFolder() {
        return FOLDER;
    }

    public String getFileName() {
        return FILE_NAME;
    }

    @Comment("----------------------------------------------------------------------------------------------------------------")
    @Comment("                                    You Shall Not Spawn by ElocinDev.")
    @Comment("                                      per_dimension_entities.json5")
    @Comment("----------------------------------------------------------------------------------------------------------------")
    @Comment(" ")
    @Comment(" entity: The entity's id you want to adjust. (For example: minecraft:zombie, regex can be used.)")
    @Comment(" dimension: The dimension id you want to adjust. (For example: minecraft:overworld, regex can be used.)")
    @Comment(" spawn_chance: The chance of the entity spawning. (For example: 0.1 is 10%, 0.5 is 50%, 0.0 will disable the spawn.)")
    @Comment(" ")
    @Comment(" The example below adds a modifier for the zombie, with 1.0 spawn chance (100%).")
    @Comment(" By default, this does nothing, but you for example set the spawn chance to 0.5, making zombies spawn half the time they usually do.")
    @Comment(" ")
    @Comment(" YSNS CAN'T INCREASE SPAWN RATES! ANYTHING ABOVE 1.0 WILL NOT INCREASE SPAWNRATE!")
    @Comment(" ")
    @Comment("Note: As a more advanced method, you can use regex by starting the entry with !")
    @Comment("With regex, you can do things such as disabling multiple entities in a single entry, or cover multiple (or all) dimensions")
    @Comment("Format: \"!{Regular expression}\"")
    @Comment("Example: \"!minecraft:.*\" will disable all entities from minecraft. (NOT RECOMMENDED, JUST AN EXAMPLE)")
    @Comment(" ")
    @Comment("Regex works on both entity and dimension entries.")
    public List<DimensionSpawnHolder> dimensions = List.of(
        new DimensionSpawnHolder("minecraft:zombie", "minecraft:overworld", 1.0f)
    );;

    @Comment("Don't touch this!")
    public int CONFIG_VERSION = 1;
}