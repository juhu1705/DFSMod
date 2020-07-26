package de.noisruker.dfs.world.gen.structures.giant_tree;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import de.noisruker.dfs.DfSMod;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.*;

public class GiantTreePools {

    public static void init() {
    }


    static {
        ImmutableList<StructureProcessor> immutablelist1 = ImmutableList.of(new RuleStructureProcessor(ImmutableList.of(new RuleEntry(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.getDefaultState()))));

        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_bottom"),
                new ResourceLocation("empty"), ImmutableList.of(
                        Pair.of(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_bottom", immutablelist1, JigsawPattern.PlacementBehaviour.RIGID), 1)),
                JigsawPattern.PlacementBehaviour.RIGID));

        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_left"),
                new ResourceLocation("empty"), ImmutableList.of(
                        new Pair<>(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_left/tree_left", immutablelist1, JigsawPattern.PlacementBehaviour.RIGID), 1)),
                JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_middle"),
                new ResourceLocation("empty"), ImmutableList.of(
                        new Pair<>(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_middle/tree_middle", immutablelist1, JigsawPattern.PlacementBehaviour.RIGID), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_top"),
                new ResourceLocation("empty"), ImmutableList.of(
                        new Pair<>(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_top/tree_top", immutablelist1, JigsawPattern.PlacementBehaviour.RIGID), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(DfSMod.MOD_ID, "tree_top_left"),
                new ResourceLocation("empty"), ImmutableList.of(
                        new Pair<>(new SingleJigsawPiece(DfSMod.MOD_ID + ":tree_top_left/tree_top_left", immutablelist1, JigsawPattern.PlacementBehaviour.RIGID), 1)), JigsawPattern.PlacementBehaviour.RIGID));
    }

    //ImmutableList.of(new RuleStructureProcessor(ImmutableList.of(new RuleEntry(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 1F),AlwaysTrueRuleTest.INSTANCE,Blocks.MOSSY_COBBLESTONE.getDefaultState())))),JigsawPattern.PlacementBehaviour.RIGID

}
