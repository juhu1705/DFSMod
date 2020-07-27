package de.noisruker.dfs.world.gen.biomes;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.NoiseChunkGenerator;

public class DungeonChunkGenerator extends NoiseChunkGenerator<DungeonGenerationSettings> {

    public DungeonChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, DungeonGenerationSettings settingsIn) {
        super(worldIn, biomeProviderIn, 1, 1, 0, settingsIn, false);
    }

    @Override
    protected double[] getBiomeNoiseColumn(int noiseX, int noiseZ) {
        return new double[0];
    }

    @Override
    protected double func_222545_a(double p_222545_1_, double p_222545_3_, int p_222545_5_) {
        return 0;
    }

    @Override
    protected void fillNoiseColumn(double[] noiseColumn, int noiseX, int noiseZ) {

    }

    @Override
    public int getGroundHeight() {
        return 0;
    }
}
