package de.noisruker.dfs.world.gen;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.registries.ModBlocks;
import de.noisruker.dfs.registries.RegistryHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DfSGenerator {

    public static final List<Biome> DESERT = Arrays.asList(Biomes.BADLANDS,
            Biomes.BADLANDS_PLATEAU,
            Biomes.DESERT,
            Biomes.DESERT_HILLS,
            Biomes.DESERT_LAKES,
            Biomes.ERODED_BADLANDS,
            Biomes.MODIFIED_BADLANDS_PLATEAU,
            Biomes.WOODED_BADLANDS_PLATEAU,
            Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU);

    public static final List<Biome> FLAT_LANDS = Arrays.asList(Biomes.PLAINS,
            Biomes.FLOWER_FOREST,
            Biomes.SUNFLOWER_PLAINS,
            Biomes.SAVANNA,
            Biomes.SAVANNA_PLATEAU,
            Biomes.SHATTERED_SAVANNA,
            Biomes.SHATTERED_SAVANNA_PLATEAU,
            Biomes.ICE_SPIKES);

    public static final List<Biome> OCEAN = Arrays.asList(Biomes.DEFAULT,
            Biomes.COLD_OCEAN,
            Biomes.DEEP_COLD_OCEAN,
            Biomes.DEEP_FROZEN_OCEAN,
            Biomes.DEEP_LUKEWARM_OCEAN,
            Biomes.DEEP_OCEAN,
            Biomes.DEEP_WARM_OCEAN,
            Biomes.WARM_OCEAN,
            Biomes.LUKEWARM_OCEAN);

    public static final List<Biome> FORESTS = Arrays.asList(Biomes.TAIGA,
            Biomes.TAIGA_HILLS,
            Biomes.TAIGA_MOUNTAINS,
            Biomes.BAMBOO_JUNGLE,
            Biomes.BAMBOO_JUNGLE_HILLS,
            Biomes.BIRCH_FOREST,
            Biomes.BIRCH_FOREST_HILLS,
            Biomes.DARK_FOREST,
            Biomes.DARK_FOREST_HILLS,
            Biomes.JUNGLE,
            Biomes.JUNGLE_EDGE,
            Biomes.JUNGLE_HILLS,
            Biomes.MODIFIED_JUNGLE,
            Biomes.MODIFIED_JUNGLE_EDGE,
            Biomes.SNOWY_TAIGA,
            Biomes.SNOWY_TAIGA_HILLS,
            Biomes.FOREST);


    private static void setupOreGeneration() {
        for(Biome b: ForgeRegistries.BIOMES) {
            CountRangeConfig cAncientStone = new CountRangeConfig(100, 5, 5, 255);
            OreFeatureConfig oAncientStone = new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                    ModBlocks.ANCIENT_STONE_BLOCK.get().getDefaultState(), 5);
            ConfiguredFeature<?, ?> cfAncientStone = Feature.ORE.withConfiguration(oAncientStone)
                    .withPlacement(Placement.COUNT_RANGE.configure(cAncientStone));

            b.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, cfAncientStone);
        }
    }

    @ObjectHolder(DfSMod.MOD_ID + ":desert_structures")
    public static Structure<NoFeatureConfig> DESERT_STRUCTURE;

    public static final List<ResourceLocation> DESERT_STRUCTURES_RESOURCES = Arrays.asList(new ResourceLocation(DfSMod.MOD_ID, "ancient_temple_0"),
            new ResourceLocation(DfSMod.MOD_ID, "ancient_rune_1"),
            new ResourceLocation(DfSMod.MOD_ID, "ancient_rune_2"));

    public static final List<Integer> Y_OFFSETS_OF_DESERT_STRUCTURES = Arrays.asList(0, 2, 7);

    public static IStructurePieceType DESERT_STRUCTURE_PIECE_TYPE = null;

    @ObjectHolder(DfSMod.MOD_ID + ":plains_structures")
    public static Structure<NoFeatureConfig> PLAINS_STRUCTURE;

    public static final List<ResourceLocation> PLAINS_STRUCTURES_RESOURCES = Arrays.asList(new ResourceLocation(DfSMod.MOD_ID, "ancient_rune_0"),
            new ResourceLocation(DfSMod.MOD_ID, "trap_0"),
            new ResourceLocation(DfSMod.MOD_ID, "trap_1"));

    public static final List<Integer> Y_OFFSETS_OF_PLAINS_STRUCTURES = Arrays.asList(4, 32, 5);

    public static IStructurePieceType PLAINS_STRUCTURE_PIECE_TYPE = null;




    private static void setupStructureGeneration() {
        DeferredWorkQueue.runLater(() -> {
            Iterator<Biome> biomes = ForgeRegistries.BIOMES.iterator();
            biomes.forEachRemaining((biome) -> {
                if(DESERT.contains(biome)) {
                    biome.addStructure(DESERT_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES,
                            DESERT_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                                    .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
                }
                if(FORESTS.contains(biome)) {

                }
                if(OCEAN.contains(biome)) {

                }
                if(FLAT_LANDS.contains(biome)) {
                    biome.addStructure(PLAINS_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES,
                            PLAINS_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                                    .withPlacement(Placement.DUNGEONS.configure(new ChanceConfig(10000))));
                }
            });
        });
    }

    public static void generate() {
        DfSGenerator.setupOreGeneration();
        DfSGenerator.setupStructureGeneration();
    }

}
