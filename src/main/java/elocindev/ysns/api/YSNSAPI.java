package elocindev.ysns.api;

import elocindev.necronomicon.api.NecUtilsAPI;
import elocindev.ysns.config.DisabledEntities;
import elocindev.ysns.config.PerDimensionEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class YSNSAPI {
    public static boolean isEntityDisabled(Entity entity, Level world) {
        if (entity == null || world == null) return false;

        String entity_id = NecUtilsAPI.getEntityId(entity);
        String dimension_id = world.dimension().location().toString();

        for (String id : DisabledEntities.INSTANCE.disabled) {
            if (parseEntry(id, entity_id)) {
                return true;
            }
        }

        for (DimensionSpawnHolder holder : PerDimensionEntities.INSTANCE.dimensions) {
            if (parseEntry(holder.getEntityIdentifier(), entity_id) && parseEntry(holder.getDimension(), dimension_id)) {
                return world.getRandom().nextFloat() > holder.getSpawnChance();
            }
        }
        
        return false;
    }

    public static boolean parseEntry(String entry, String compareTo) {
        if (entry.startsWith("!")) {
            return compareTo.matches(entry.substring(1));
        } else return compareTo.equals(entry);
    }
}
