package de.noisruker.dfs.world.gen.structures;

import com.mojang.serialization.Codec;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.world.gen.DfSGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class DesertStructure extends Structure<NoFeatureConfig> {

    public DesertStructure(Codec<NoFeatureConfig> p_i51440_1_) {
        super(p_i51440_1_);
    }

    @Override
    public Structure.IStartFactory getStartFactory() {
        return DesertStructure.Start::new;
    }

    public String getStructureName() {
        return DfSMod.MOD_ID + ":desert_structures";
    }

    public int getSize() {
        return 3;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public static class Start extends MarginedStructureStart {
        public Start(Structure<?> p_i225817_1_, int p_i225817_2_, int p_i225817_3_, MutableBoundingBox p_i225817_4_, int p_i225817_5_, long p_i225817_6_) {
            super(p_i225817_1_, p_i225817_2_, p_i225817_3_, p_i225817_4_, p_i225817_5_, p_i225817_6_);
        }

        @Override
        public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, IFeatureConfig p_230364_7_) {
            int worldX = chunkX * 16;
            int worldZ = chunkZ * 16;
            BlockPos blockpos = new BlockPos(worldX, 90, worldZ);
            int rand = new Random().nextInt(DfSGenerator.DESERT_STRUCTURES_RESOURCES.size());
            this.components.add(new DesertStructuresPiece.Piece(templateManagerIn, blockpos, DfSGenerator.DESERT_STRUCTURES_RESOURCES.get(rand), DfSGenerator.Y_OFFSETS_OF_DESERT_STRUCTURES.get(rand).intValue()));
            this.recalculateStructureSize();
        }
    }
}
