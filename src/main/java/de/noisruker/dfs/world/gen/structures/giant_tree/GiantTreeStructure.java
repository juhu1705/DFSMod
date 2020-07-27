package de.noisruker.dfs.world.gen.structures.giant_tree;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.registries.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class GiantTreeStructure extends ScatteredStructure<NoFeatureConfig> {

    private static final List<Biome.SpawnListEntry> GIANT_TREE_ENEMIES = Lists.newArrayList(new Biome.SpawnListEntry(EntityType.PILLAGER, 100, 1, 10), new Biome.SpawnListEntry(EntityType.CAT, 10, 1, 6), new Biome.SpawnListEntry(ModEntityTypes.ENTITY_SOUL.get(), 50, 2, 3), new Biome.SpawnListEntry(EntityType.VEX, 2, 1, 1));

    public GiantTreeStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> pillageOutpostConfigIn) {
        super(pillageOutpostConfigIn);
    }

    public String getStructureName() {
        return DfSMod.MOD_ID + ":giant_tree_structure";
    }

    public int getSize() {
        return 12;
    }

    public List<Biome.SpawnListEntry> getSpawnList() {
        return GIANT_TREE_ENEMIES;
    }

    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
        int i = chunkGenerator.getSettings().getVillageDistance();
        int j = chunkGenerator.getSettings().getVillageSeparation();
        int k = x + i * spacingOffsetsX;
        int l = z + i * spacingOffsetsZ;
        int i1 = k < 0 ? k - i + 1 : k;
        int j1 = l < 0 ? l - i + 1 : l;
        int k1 = i1 / i;
        int l1 = j1 / i;
        ((SharedSeedRandom)random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), k1, l1, 235461857);
        k1 = k1 * i;
        l1 = l1 * i;
        k1 = k1 + random.nextInt(i - j);
        l1 = l1 + random.nextInt(i - j);
        return new ChunkPos(k1, l1);
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

    @Override
    protected int getSeedModifier() {
        return 123456789;
    }

    public Structure.IStartFactory getStartFactory() {
        return GiantTreeStructure.Start::new;
    }

    public static class Start extends MarginedStructureStart {
        public Start(Structure<?> p_i225815_1_, int p_i225815_2_, int p_i225815_3_, MutableBoundingBox p_i225815_4_, int p_i225815_5_, long p_i225815_6_) {
            super(p_i225815_1_, p_i225815_2_, p_i225815_3_, p_i225815_4_, p_i225815_5_, p_i225815_6_);
        }

        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
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