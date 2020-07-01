package de.noisruker.dfs.registries;

import Dimensions.DungeonModDimension;
import de.noisruker.dfs.DfSMod;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModDimensions {

    public static final DeferredRegister<ModDimension> MOD_DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, DfSMod.MOD_ID);

    public static final RegistryObject<ModDimension> DUNGEON = MOD_DIMENSIONS.register("dungeon", DungeonModDimension::new);
}
