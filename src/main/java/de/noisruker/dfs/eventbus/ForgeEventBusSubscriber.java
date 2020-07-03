package de.noisruker.dfs.eventbus;

import Dimensions.DungeonDimension;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.registries.ModDimensions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusSubscriber {

    @SubscribeEvent
    public static void registerDimension(final RegisterDimensionsEvent event){
        if (DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE) == null){
            DimensionManager.registerDimension(DfSMod.DUNGEON_DIM_TYPE, ModDimensions.DUNGEON.get(), null, true);
        }
        DfSMod.LOGGER.info("Dimension registered " + (DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE) == null));
    }
}
