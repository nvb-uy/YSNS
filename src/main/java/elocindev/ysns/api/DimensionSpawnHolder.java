package elocindev.ysns.api;

import net.minecraft.resources.ResourceLocation;

public class DimensionSpawnHolder {
    private String entityId;
    private String dimension;
    private float spawn_chance;
    
    public DimensionSpawnHolder(String entityId, String dimension, float spawn_chance) {
        this.entityId = entityId;
        this.dimension = dimension;
        this.spawn_chance = spawn_chance;
    }

    public ResourceLocation getEntityIdentifier() {
        return ResourceLocation.tryParse(entityId);
    }

    public String getDimension() {
        return dimension;
    }

    public float getSpawnChance() {
        return spawn_chance;
    }
}
