package de.noisruker.dfs.world.gen.structures.giant_tree;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.registries.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;

public class GiantTreeStructure extends Structure<NoFeatureConfig> {

    private static final List<MobSpawnInfo.Spawners> GIANT_TREE_ENEMIES = Lists.newArrayList(new MobSpawnInfo.Spawners(EntityType.PILLAGER, 100, 1, 10), new MobSpawnInfo.Spawners(EntityType.CAT, 10, 1, 6), new MobSpawnInfo.Spawners(ModEntityTypes.ENTITY_SOUL.get(), 50, 2, 3), new MobSpawnInfo.Spawners(EntityType.VEX, 2, 1, 1));

    public GiantTreeStructure(Codec<NoFeatureConfig> pillageOutpostConfigIn) {
        super(pillageOutpostConfigIn);
    }

    public String getStructureName() {
        return DfSMod.MOD_ID + ":giant_tree_structure";
    }

    public int getSize() {
        return 12;
    }

    public List<MobSpawnInfo.Spawners> getSpawnList() {
        return GIANT_TREE_ENEMIES;
    }

    public boolean checkChunks(BiomeManager biomeManagerIn, ChunkPos chunkpos, Biome biomeIn) {
        for(int i = -2; i <= 2; i++)
            for(int i1 = -2; i1 <= 2; i1++)
                if(biomeManagerIn.getBiome(new BlockPos(chunkpos.asBlockPos().getX() + (16 * i), chunkpos.asBlockPos().getY(), chunkpos.asBlockPos().getZ() + (16 * i1))) != biomeIn)
                    return false;
        return true;
    }

    public Structure.IStartFactory getStartFactory() {
        return GiantTreeStructure.Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public static class Start extends MarginedStructureStart {
        public Start(Structure<?> p_i225815_1_, int p_i225815_2_, int p_i225815_3_, MutableBoundingBox p_i225815_4_, int p_i225815_5_, long p_i225815_6_) {
            super(p_i225815_1_, p_i225815_2_, p_i225815_3_, p_i225815_4_, p_i225815_5_, p_i225815_6_);
        }

        @Override
        public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, IFeatureConfig p_230364_7_) {
            int worldX = chunkX * 16;
            int worldZ = chunkZ * 16;
            BlockPos blockpos = new BlockPos(worldX, 90, worldZ);

            this.components.add(new GiantTreeStructuresPiece.GiantTree(templateManagerIn, blockpos, blockpos, new ResourceLocation(DfSMod.MOD_ID, "giant_tree_structure/tree_bottom"), 0));
            this.components.add(new GiantTreeStructuresPiece.GiantTree(templateManagerIn, new BlockPos(blockpos.getX() + 6, 90, blockpos.getZ() + 8), blockpos, new ResourceLocation(DfSMod.MOD_ID, "giant_tree_structure/tree_left"), 31));
            this.components.add(new GiantTreeStructuresPiece.GiantTree(templateManagerIn, new BlockPos(blockpos.getX() + 6, 90, blockpos.getZ() - 12), blockpos, new ResourceLocation(DfSMod.MOD_ID, "giant_tree_structure/tree_middle"), 31));
            this.components.add(new GiantTreeStructuresPiece.GiantTree(templateManagerIn, new BlockPos(blockpos.getX() - 11, 90, blockpos.getZ() - 12), blockpos, new ResourceLocation(DfSMod.MOD_ID, "giant_tree_structure/tree_top"), 31));
            this.components.add(new GiantTreeStructuresPiece.GiantTree(templateManagerIn, new BlockPos(blockpos.getX() - 11, 90, blockpos.getZ() + 8), blockpos, new ResourceLocation(DfSMod.MOD_ID, "giant_tree_structure/tree_top_left"), 31));
            this.recalculateStructureSize();
        }
    }
}