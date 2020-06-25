package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegistryHandler {



    public static void init() {
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS Items are registered");

        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS Blocks are registered");

        ModPotions.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModPotions.POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS Potions are registered");

        ModTileEntityTypes.TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS TileEntities are registered");

        ModEntityTypes.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS Entities are registered");
    }




    //Loot Tables
    public static final ResourceLocation DESERT_CHEST_LOOT = new ResourceLocation(DfSMod.MOD_ID, "chests/desert_structures");

}
