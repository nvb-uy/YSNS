package elocindev.ysns.config;

import java.nio.file.Path;
import java.util.List;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.necronomicon.config.Comment;
import elocindev.necronomicon.config.NecConfig;

public class DisabledEntities {
    public static final String FOLDER = "ysns";
    public static final String FILE_NAME = "disabled_entities.json5";
    public static final int CURRENT_CONFIG_VERSION = 1;

    @NecConfig
    public static DisabledEntities INSTANCE;

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
    @Comment("                                         disabled_entities.json5")
    @Comment("----------------------------------------------------------------------------------------------------------------")
    @Comment(" ")
    @Comment("Here you can disable entities from spawning globally, with no exceptions.")
    @Comment("Format: \"modid:entity_name\"")
    @Comment("Example: \"minecraft:zombie\"")
    @Comment(" ")
    @Comment("Note: As a more advanced method, you can use regex by starting the entry with !")
    @Comment("Format: \"!{Regular expression}\"")
    @Comment("Example: \"!minecraft:.*\" will disable all entities from minecraft. (NOT RECOMMENDED, JUST AN EXAMPLE)")
    @Comment(" ")
    public List<String> disabled = List.of(
        "example_mod:example_entity",
        "example_mod:another_example_entity"
    );

    @Comment("Don't touch this!")
    public int CONFIG_VERSION = 1;
}