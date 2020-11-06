package de.noisruker.dfs.world.gen;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.registries.ModBlocks;
import de.noisruker.dfs.registries.ModStructures;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DfSGenerator {

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (event.getCategory() == Biome.Category.NETHER) {

        } else if (event.getCategory() == Biome.Category.THEEND) {

        } else {
            DfSGenerator.initOres(generation);
        }
        DfSGenerator.setupStructureGeneration(event.getCategory(), generation, event.getName());
    }

    private static void initOres(BiomeGenerationSettingsBuilder generation) {
        OreFeatureConfig oAncientStone = new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                ModBlocks.ANCIENT_STONE_BLOCK.get().getDefaultState(), 20);
        ConfiguredFeature<?, ?> cfAncientStone = Feature.ORE.withConfiguration(oAncientStone)
                .range(128).square().chance(200);

        OreFeatureConfig oMagicOre = new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                ModBlocks.MAGIC_ORE_BLOCK.get().getDefaultState(), 4);
        ConfiguredFeature<?, ?> cfMagicOre = Feature.ORE.withConfiguration(oMagicOre).range(14).square().chance(2);

        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, cfAncientStone);
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, cfMagicOre);
    }

    public static final List<ResourceLocation> DESERT_STRUCTURES_RESOURCES = Arrays.asList(new ResourceLocation(DfSMod.MOD_ID, "ancient_temple_0"),
            new ResourceLocation(DfSMod.MOD_ID, "ancient_rune_1"),
            new ResourceLocation(DfSMod.MOD_ID, "ancient_rune_2"));

    public static final List<Integer> Y_OFFSETS_OF_DESERT_STRUCTURES = Arrays.asList(0, 2, 7);

    public static IStructurePieceType DESERT_STRUCTURE_PIECE_TYPE = null;

    public static final List<ResourceLocation> GIANT_TREE_STRUCTURES_RESOURCES = Arrays.asList(new ResourceLocation(DfSMod.MOD_ID, "tree_bottom"));

    public static final List<Integer> Y_OFFSETS_OF_GIANT_TREE_STRUCTURES = Arrays.asList(0);

    public static IStructurePieceType GIANT_TREE_STRUCTURE_PIECE_TYPE = null;

    public static final List<ResourceLocation> PLAINS_STRUCTURES_RESOURCES = Arrays.asList(new ResourceLocation(DfSMod.MOD_ID, "ancient_rune_0"),
            new ResourceLocation(DfSMod.MOD_ID, "trap_0"),
            new ResourceLocation(DfSMod.MOD_ID, "trap_1"));

    public static final List<Integer> Y_OFFSETS_OF_PLAINS_STRUCTURES = Arrays.asList(4, 32, 5);

    public static IStructurePieceType PLAINS_STRUCTURE_PIECE_TYPE = null;

    public static IStructurePieceType MOUNTAIN_STRUCTURE_PIECE_TYPE = null;

    private static void setupStructureGeneration(Biome.Category category, BiomeGenerationSettingsBuilder biome, ResourceLocation biomeName) {
        if(category == Biome.Category.DESERT) {
            biome.withStructure(ModStructures.DESERT_STRUCTURES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            /*biome.withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES,
                    DESERT_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                            .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));*/
        }

        if(biomeName.equals(Biomes.GIANT_SPRUCE_TAIGA.getLocation()) || biomeName.equals(Biomes.GIANT_TREE_TAIGA.getLocation())) {
            biome.withStructure(ModStructures.GIANT_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        }
        if(category == Biome.Category.EXTREME_HILLS) {
            biome.withStructure(ModStructures.MOUNTAIN_CAVE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            /*biome.withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES,
                    MOUNTAIN_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                            .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));*/
        }
        if(category == Biome.Category.PLAINS) {
            biome.withStructure(ModStructures.PLAIN_TRAPS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            /*biome.withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES,
                    PLAINS_STRUCTURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                            .withPlacement(Placement.DUNGEONS.configure(new ChanceConfig(10000))));*/
        }
    }

    public static void registerStructureSeperations() {
        WorldGenRegistries.NOISE_SETTINGS.getValueForKey(DimensionSettings.field_242734_c).getStructures().func_236195_a_().put(ModStructures.GIANT_TREE.get(), new StructureSeparationSettings(20, 10, 342226440));

        WorldGenRegistries.NOISE_SETTINGS.getValueForKey(DimensionSettings.field_242734_c).getStructures().func_236195_a_().put(ModStructures.PLAIN_TRAPS.get(), new StructureSeparationSettings(20, 10, 342226440));

        WorldGenRegistries.NOISE_SETTINGS.getValueForKey(DimensionSettings.field_242734_c).getStructures().func_236195_a_().put(ModStructures.DESERT_STRUCTURES.get(), new StructureSeparationSettings(20, 10, 342226440));

        WorldGenRegistries.NOISE_SETTINGS.getValueForKey(DimensionSettings.field_242734_c).getStructures().func_236195_a_().put(ModStructures.MOUNTAIN_CAVE.get(), new StructureSeparationSettings(20, 10, 342226440));

        Structure.NAME_STRUCTURE_BIMAP.forcePut(DfSMod.MOD_ID + ":giant_tree_structure", ModStructures.GIANT_TREE.get());
        Structure.NAME_STRUCTURE_BIMAP.forcePut(DfSMod.MOD_ID + ":plains_structures", ModStructures.PLAIN_TRAPS.get());
        Structure.NAME_STRUCTURE_BIMAP.forcePut(DfSMod.MOD_ID + ":desert_structures", ModStructures.DESERT_STRUCTURES.get());
        Structure.NAME_STRUCTURE_BIMAP.forcePut(DfSMod.MOD_ID + ":mountain_structures", ModStructures.MOUNTAIN_CAVE.get());
    }

}
