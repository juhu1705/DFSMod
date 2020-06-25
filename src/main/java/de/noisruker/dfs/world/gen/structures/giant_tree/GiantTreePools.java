package de.noisruker.dfs.world.gen.structures.giant_tree;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.mojang.datafixers.util.Pair;
import de.noisruker.dfs.DfSMod;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.jigsaw.*;
import net.minecraft.world.gen.feature.structure.PillagerOutpostPieces;
import net.minecraft.world.gen.feature.template.*;

public class GiantTreePools {

    public static void init() {
    }


    static {
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_bottom"),
                new ResourceLocation("empty"), ImmutableList.of(
                        Pair.of(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_bottom"), 1)),
                JigsawPattern.PlacementBehaviour.RIGID));

        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_left"),
                new ResourceLocation("empty"), ImmutableList.of(
                        new Pair<>(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_left/tree_left"), 1)),
                JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_middle"),
                new ResourceLocation("empty"), ImmutableList.of(
                        new Pair<>(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_middle/tree_middle"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_top"),
                new ResourceLocation("empty"), ImmutableList.of(
                        new Pair<>(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_top/tree_top"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_top_left"),
                new ResourceLocation("empty"), ImmutableList.of(
                        new Pair<>(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_top_left/tree_top_left"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
    }

    //ImmutableList.of(new RuleStructureProcessor(ImmutableList.of(new RuleEntry(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 1F),AlwaysTrueRuleTest.INSTANCE,Blocks.MOSSY_COBBLESTONE.getDefaultState())))),JigsawPattern.PlacementBehaviour.RIGID

}
