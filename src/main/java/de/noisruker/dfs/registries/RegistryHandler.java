package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.blocks.*;
import de.noisruker.dfs.items.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {



    public static void init() {
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS Items are registered");

        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS Blocks are registered");

        ModTileEntityTypes.TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS TileEntities are registered");

        ModEntityTypes.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS Entities are registered");
    }

    public static void postInit() {
        ModEntityTypes.SPAWN_EGGS.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("Spawn Eggs are added");
    }


    //Loot Tables
    public static final ResourceLocation DESERT_CHEST_LOOT = new ResourceLocation(DfSMod.MOD_ID, "chests/desert_structures");

}
