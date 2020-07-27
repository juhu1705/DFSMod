package de.noisruker.dfs.world.gen.biomes;


import com.google.common.collect.ImmutableSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;

import javax.annotation.Nonnull;
import java.util.Set;

public class DungeonBiomeProvider extends BiomeProvider {

    public DungeonBiomeProvider() {
        super(ImmutableSet.of(Biomes.PLAINS));
    }

    private static final Set<Biome> biomeList= ImmutableSet.of(Biomes.PLAINS);

    @Override
    @Nonnull
    public Biome getNoiseBiome(int x, int y, int z) {
        return Biomes.PLAINS;
    }
}