package de.noisruker.dfs.world.gen.dimensions;

import de.noisruker.dfs.biomes.DungeonBiomeProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.EndChunkGenerator;
import net.minecraft.world.gen.EndGenerationSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DungeonDimension extends Dimension {


    public DungeonDimension(World world, DimensionType type) {
        super(world, type, 0.0f);
    }

    @Override
    @Nonnull
    public EndChunkGenerator createChunkGenerator() {
        //new FlatChunkGenerator(world, new DungeonBiomeProvider(), new FlatGenerationSettings());
        return new EndChunkGenerator(world, new DungeonBiomeProvider(), new EndGenerationSettings());
    }

    @Override
    public BlockPos findSpawn(@Nonnull ChunkPos chunkPosIn, boolean checkValid) {
        return null;
    }

    @Nullable
    @Override
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
        return null;
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    @Nonnull
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return Vec3d.ZERO;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }

    @Override
    public SleepResult canSleepAt(PlayerEntity player, BlockPos pos) {
        return SleepResult.BED_EXPLODES;
    }

}
