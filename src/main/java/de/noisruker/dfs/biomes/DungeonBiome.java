package de.noisruker.dfs.biomes;

import net.minecraft.world.biome.Biome;

public class DungeonBiome extends Biome {

    public static Builder biomeBuilder = new Biome.Builder()
            .temperature(0)
            .depth(2);

    public DungeonBiome() {
        super(biomeBuilder);
    }
}
