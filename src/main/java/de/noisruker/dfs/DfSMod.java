package de.noisruker.dfs;

import de.noisruker.dfs.commands.ModCommands;
import de.noisruker.dfs.items.ItemSpawnEggSoul;
import de.noisruker.dfs.network.SpeciesMessages;
import de.noisruker.dfs.registries.*;
import de.noisruker.dfs.species.PlayerSpecies;
import de.noisruker.dfs.tickrateHandling.TickrateReducer;
import de.noisruker.dfs.world.gen.DfSGenerator;
import de.noisruker.dfs.world.gen.structures.DesertStructure;
import de.noisruker.dfs.world.gen.structures.DesertStructuresPiece;
import de.noisruker.dfs.world.gen.structures.PlainsStructure;
import de.noisruker.dfs.world.gen.structures.PlainsStructuresPiece;
import de.noisruker.dfs.world.gen.structures.giant_tree.GiantTreeStructure;
import de.noisruker.dfs.world.gen.structures.giant_tree.GiantTreeStructuresPiece;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("dfssul")
@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DfSMod {

    public static final String MOD_ID = "dfssul";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final ResourceLocation DUNGEON_DIM_TYPE = new ResourceLocation("dungeon_dimension");

    public DfSMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        RegistryHandler.init();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(TickrateReducer.getInstance());
        MinecraftForge.EVENT_BUS.register(PlayerSpecies.class);
        MinecraftForge.EVENT_BUS.register(ModCommands.class);
        MinecraftForge.EVENT_BUS.register(ForgeRegistryEvents.class);

        SpeciesMessages.registerMessages();
    }

    private void setup(final FMLCommonSetupEvent event) {
        DfSGenerator.generate();

        LOGGER.debug("Loaded Generator");
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.CHAIN_BLOCK.get(), RenderType.getTranslucent());

        ModEntityTypes.bindRenderers();

        ModTileEntityTypes.bindSpecialRenderers();
    }

    @SubscribeEvent
    public static void onRenderItem(ColorHandlerEvent.Item event) {
        event.getItemColors().register(((stack, i) -> ((ItemSpawnEggSoul)stack.getItem()).getColor(i)), ModItems.SPAWN_SOUL.get());
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

        DfSGenerator.GIANT_TREE_STRUCTURE_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE,
                DfSMod.MOD_ID + ":giant_tree_structures", GiantTreeStructuresPiece.GiantTree::new);
        args.getRegistry().register(new GiantTreeStructure(NoFeatureConfig::deserialize)
                .setRegistryName(DfSMod.MOD_ID + ":giant_tree_structures"));



        ModEntityTypes.registerEntityWorldSpawns();
    }


}
