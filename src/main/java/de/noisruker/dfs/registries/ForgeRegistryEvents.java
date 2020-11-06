package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeRegistryEvents {

    private static ForgeRegistryEvents DimensionManager;

    /*@SubscribeEvent
    public static <RegisterDimensionsEvent> void registerDimension(final RegisterDimensionsEvent event){
        if (DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE) == null){
            DimensionManager.registerDimension(DfSMod.DUNGEON_DIM_TYPE, ModDimensions.DUNGEON.get(), null, true);
        }
        DfSMod.LOGGER.info("Dimension registered " + (DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE) == null));
    }*/
}
