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
        ModContainerTypes.CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS TileEntities are registered");

        ModEntityTypes.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("DfS Entities are registered");

        ModDimensions.MOD_DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModRecipeTypes.RECIPE_SERIALIZER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        DfSMod.LOGGER.debug("Dfs Dimensions are registered");
    }




    //Loot Tables
    public static final ResourceLocation DESERT_CHEST_LOOT = new ResourceLocation(DfSMod.MOD_ID, "chests/desert_structures");
    public static final ResourceLocation ARMOR_CHEST_LOOT = new ResourceLocation(DfSMod.MOD_ID, "chests/chest_armor");
    public static final ResourceLocation TREASURE_CHEST_LOOT = new ResourceLocation(DfSMod.MOD_ID, "chests/chest_treasure");
    public static final ResourceLocation MOUNTAIN_TRESURE_CHEST_LOOT = new ResourceLocation(DfSMod.MOD_ID, "chests/chest_mountain_treasure");
    public static final ResourceLocation BARREL_MOUNTAIN_LOOT = new ResourceLocation(DfSMod.MOD_ID, "chests/barrel_mountain_loot");

}
