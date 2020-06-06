package de.noisruker.dfs;

import de.noisruker.dfs.registries.*;
import de.noisruker.dfs.world.gen.*;
import de.noisruker.dfs.world.gen.structures.DesertStructure;
import de.noisruker.dfs.world.gen.structures.DesertStructuresPiece;
import de.noisruker.dfs.world.gen.structures.PlainsStructure;
import de.noisruker.dfs.world.gen.structures.PlainsStructuresPiece;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("dfssul")
@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DfSMod {

    public static final String MOD_ID = "dfssul";

    public static final Logger LOGGER = LogManager.getLogger();

    public DfSMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        RegistryHandler.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DfSGenerator.generate();

        LOGGER.debug("Loaded Generator");

        ModEntityTypes.SPAWN_EGGS.register(FMLJavaModLoadingContext.get().getModEventBus());

        //for(RegistryObject<Item> egg: ModEntityTypes.SPAWN_EGGS.getEntries()) {
          //  egg.get().setRegistryName(egg.getId());

            //ForgeRegistries.ITEMS.register(egg.get());
        //}



        DfSMod.LOGGER.debug("Spawn Eggs are added");
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.CHAIN_BLOCK.get(), RenderType.getTranslucent());

        

        ModEntityTypes.bindRenderers();

        ModTileEntityTypes.bindSpecialRenderers();

    }

    public static final ItemGroup TAB_ITEMS = new ItemGroup("dfssultabitems") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.ANCIENT_THING.get());
        }
    };

    public static final ItemGroup TAB_BLOCKS = new ItemGroup("dfssultabblocks") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.STONE_PILLAR_ITEM.get());
        }
    };

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> args) {
        DfSGenerator.DESERT_STRUCTURE_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE,
                DfSMod.MOD_ID + ":desert_structures", DesertStructuresPiece.Piece::new);
        args.getRegistry().register(new DesertStructure(NoFeatureConfig::deserialize)
                .setRegistryName(DfSMod.MOD_ID + ":desert_structures"));

        DfSGenerator.PLAINS_STRUCTURE_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE,
                DfSMod.MOD_ID + ":plains_structures", PlainsStructuresPiece.Piece::new);
        args.getRegistry().register(new PlainsStructure(NoFeatureConfig::deserialize)
                .setRegistryName(DfSMod.MOD_ID + ":plains_structures"));

        ModEntityTypes.registerEntityWorldSpawns();
    }
}
