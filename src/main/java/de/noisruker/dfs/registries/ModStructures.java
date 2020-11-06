package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.world.gen.structures.DesertStructure;
import de.noisruker.dfs.world.gen.structures.MountainStructure;
import de.noisruker.dfs.world.gen.structures.PlainsStructure;
import de.noisruker.dfs.world.gen.structures.giant_tree.GiantTreeStructure;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModStructures {

    public static final DeferredRegister<Structure<?>> FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, DfSMod.MOD_ID);


    public static final RegistryObject<Structure<NoFeatureConfig>> GIANT_TREE = FEATURES.register("giant_tree_structures", () -> new GiantTreeStructure(NoFeatureConfig.field_236558_a_));
    public static final RegistryObject<Structure<NoFeatureConfig>> MOUNTAIN_CAVE = FEATURES.register("mountain_structures", () -> new MountainStructure(NoFeatureConfig.field_236558_a_));
    public static final RegistryObject<Structure<NoFeatureConfig>> DESERT_STRUCTURES = FEATURES.register("desert_structures", () -> new DesertStructure(NoFeatureConfig.field_236558_a_));
    public static final RegistryObject<Structure<NoFeatureConfig>> PLAIN_TRAPS = FEATURES.register("plains_structures", () -> new PlainsStructure(NoFeatureConfig.field_236558_a_));
}
