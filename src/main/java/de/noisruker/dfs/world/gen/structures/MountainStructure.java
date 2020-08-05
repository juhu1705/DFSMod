package de.noisruker.dfs.world.gen.structures;

import com.mojang.datafixers.Dynamic;
import de.noisruker.dfs.DfSMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Function;

public class MountainStructure extends ScatteredStructure<NoFeatureConfig> {

    public MountainStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i51440_1_) {
        super(p_i51440_1_);
    }

    @Override
    protected int getSeedModifier() {
        return 145743948;
    }

    @Override
    public IStartFactory getStartFactory() {
        return MountainStructure.Start::new;
    }

    public String getStructureName() {
        return DfSMod.MOD_ID + ":mountain_structures";
    }

    /**
     * decide whether the Structure can be generated
     */
    public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn) {
        ChunkPos chunkpos = this.getStartPositionForPosition(generatorIn, randIn, chunkX, chunkZ, 0, 0);
        return (chunkX == chunkpos.x && chunkZ == chunkpos.z) && generatorIn.hasStructure(biomeIn, this) && checkChunks(biomeManagerIn, chunkpos, biomeIn);
    }

    public boolean checkChunks(BiomeManager biomeManagerIn, ChunkPos chunkpos, Biome biomeIn) {
        for(int i = -2; i <= 2; i++)
            for(int i1 = -2; i1 <= 2; i1++)
                if(biomeManagerIn.getBiome(new BlockPos(chunkpos.asBlockPos().getX() + (16 * i), chunkpos.asBlockPos().getY(), chunkpos.asBlockPos().getZ() + (16 * i1))) != biomeIn)
                    return false;
        return true;
    }

    public int getSize() {
        return 3;
    }



    public static class Start extends StructureStart {
        public Start(Structure<?> p_i225817_1_, int p_i225817_2_, int p_i225817_3_, MutableBoundingBox p_i225817_4_, int p_i225817_5_, long p_i225817_6_) {
            super(p_i225817_1_, p_i225817_2_, p_i225817_3_, p_i225817_4_, p_i225817_5_, p_i225817_6_);
        }

        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            int worldX = chunkX * 16;
            int worldZ = chunkZ * 16;
            BlockPos blockpos = new BlockPos(worldX, 90, worldZ);
            this.components.add(new MountainStructuresPiece.Piece(templateManagerIn, blockpos, new ResourceLocation(DfSMod.MOD_ID, "mountain_rune"), 10));
            this.recalculateStructureSize();

        }
    }
}
