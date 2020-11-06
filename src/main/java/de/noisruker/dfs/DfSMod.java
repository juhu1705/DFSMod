package de.noisruker.dfs;

import de.noisruker.dfs.commands.ModCommands;
import de.noisruker.dfs.network.SpeciesMessages;
import de.noisruker.dfs.objects.containers.AncientFurnaceContainer;
import de.noisruker.dfs.objects.entities.MagicProjectileEntity;
import de.noisruker.dfs.objects.items.ItemSpawnEggSoul;
import de.noisruker.dfs.objects.screens.AncientFurnaceScreen;
import de.noisruker.dfs.registries.*;
import de.noisruker.dfs.species.PlayerSpeciesEvents;
import de.noisruker.dfs.species.SpeciesPassiveAbilities;
import de.noisruker.dfs.tickrateHandling.TickrateReducer;
import de.noisruker.dfs.world.gen.DfSGenerator;
import de.noisruker.dfs.world.gen.structures.DesertStructuresPiece;
import de.noisruker.dfs.world.gen.structures.MountainStructuresPiece;
import de.noisruker.dfs.world.gen.structures.PlainsStructuresPiece;
import de.noisruker.dfs.world.gen.structures.giant_tree.GiantTreeStructuresPiece;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;


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
        MinecraftForge.EVENT_BUS.register(PlayerSpeciesEvents.class);
        MinecraftForge.EVENT_BUS.register(ModCommands.class);
        MinecraftForge.EVENT_BUS.register(ForgeRegistryEvents.class);
        MinecraftForge.EVENT_BUS.register(SpeciesPassiveAbilities.class);
        MinecraftForge.EVENT_BUS.register(DfSGenerator.class);

        SpeciesMessages.registerMessages();
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModEntityTypes.registerEntityAttributes();

        LOGGER.debug("Loaded Attributes");

        DispenserBlock.registerDispenseBehavior(ModItems.MAGIC_PROJECTILE.get(), new ProjectileDispenseBehavior() {
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return (ProjectileEntity) Util.make(() -> {
                    MagicProjectileEntity projectileEntity = new MagicProjectileEntity(worldIn, position.getX(), position.getY(), position.getZ());
                    projectileEntity.setPower(10f);
                    return projectileEntity;
                });
            }
        });
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ModBlocks.CRYSTAL_BLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.CHAIN_BLOCK.get(), RenderType.getTranslucent());

        ModEntityTypes.bindRenderers();

        ModTileEntityTypes.bindSpecialRenderers();

        DfSMod.LOGGER.debug("Register Keys");
        ClientRegistry.registerKeyBinding(ModKeyBindings.USE_ACTIVE_ABILITY);

        ScreenManager.<AncientFurnaceContainer, AncientFurnaceScreen>registerFactory(ModContainerTypes.ANCIENT_FURNACE_CONTAINER.get(), AncientFurnaceScreen::new);
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

        DfSGenerator.PLAINS_STRUCTURE_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE,
                DfSMod.MOD_ID + ":plains_structures", PlainsStructuresPiece.Piece::new);

        DfSGenerator.GIANT_TREE_STRUCTURE_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE,
                DfSMod.MOD_ID + ":giant_tree_structures", GiantTreeStructuresPiece.GiantTree::new);

        DfSGenerator.MOUNTAIN_STRUCTURE_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE,
                DfSMod.MOD_ID + ":mountain_structures", MountainStructuresPiece.Piece::new);

        ModEntityTypes.registerEntityWorldSpawns();
    }

    @SubscribeEvent
    public static void registerModelProperties(FMLCommonSetupEvent event) {
        DfSGenerator.registerStructureSeperations();

        ItemModelsProperties.registerProperty(ModItems.GIANT_TREE_COMPASS.get(), new ResourceLocation("angle"), new IItemPropertyGetter() {

            @OnlyIn(Dist.CLIENT)
            private double rotation;
            @OnlyIn(Dist.CLIENT)
            private double rota;
            @OnlyIn(Dist.CLIENT)
            private long lastUpdateTick;

            @Override
            public float call(ItemStack p_call_1_, @Nullable ClientWorld p_call_2_, @Nullable LivingEntity p_call_3_) {
                if (p_call_3_ == null && !p_call_1_.isOnItemFrame()) {
                    return 0.0F;
                } else {
                    boolean flag = p_call_3_ != null;
                    Entity entity = (Entity) (flag ? p_call_3_ : p_call_1_.getItemFrame());
                    if (p_call_2_ == null) {
                        p_call_2_ = (ClientWorld) entity.world;
                    }

                    double d0;
                    if (/*p_call_2_.func_234923_W_().isSurfaceWorld()*/ true) {
                        double d1 = flag ? (double) entity.rotationYaw : this.getFrameRotation((ItemFrameEntity) entity);
                        d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
                        double d2 = this.getSpawnToAngle(p_call_2_, entity, p_call_1_) / (double) ((float) Math.PI * 2F);
                        d0 = 0.5D - (d1 - 0.25D - d2);
                    } else {
                        d0 = Math.random();
                    }

                    if (flag) {
                        d0 = this.wobble(p_call_2_, d0);
                    }

                    return MathHelper.positiveModulo((float) d0, 1.0F);
                }
            }

            @OnlyIn(Dist.CLIENT)
            private double wobble(World worldIn, double p_185093_2_) {
                if (worldIn.getGameTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = worldIn.getGameTime();
                    double d0 = p_185093_2_ - this.rotation;
                    d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
                    this.rota += d0 * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }

                return this.rotation;
            }

            @OnlyIn(Dist.CLIENT)
            private double getFrameRotation(ItemFrameEntity p_185094_1_) {
                return (double) MathHelper.wrapDegrees(180 + p_185094_1_.getHorizontalFacing().getHorizontalIndex() * 90);
            }

            @OnlyIn(Dist.CLIENT)
            private double getSpawnToAngle(IWorld p_185092_1_, Entity p_185092_2_, ItemStack p_185092_3_) {
                BlockPos blockpos = getBlockPos(p_185092_3_, p_185092_2_.world);
                return Math.atan2((double) blockpos.getZ() - p_185092_2_.getPosZ(), (double) blockpos.getX() - p_185092_2_.getPosX());
            }
        });
    }

    public static BlockPos getBlockPos(ItemStack p_12345_1_, World p_12345_2_) {
        if(p_12345_1_.hasTag()) {
            CompoundNBT tag = p_12345_1_.getTag();
            if(tag.contains("found")) {
                if(tag.getBoolean("found")) {
                    Long structureLong = tag.getLong("location");
                    return BlockPos.fromLong(structureLong);
                } else {
                    return new BlockPos(p_12345_2_.getWorldInfo().getSpawnX(),
                            p_12345_2_.getWorldInfo().getSpawnY(),
                            p_12345_2_.getWorldInfo().getSpawnZ());
                }
            } else {
                return new BlockPos(p_12345_2_.getWorldInfo().getSpawnX(),
                        p_12345_2_.getWorldInfo().getSpawnY(),
                        p_12345_2_.getWorldInfo().getSpawnZ());
            }
        } else {
            return new BlockPos(p_12345_2_.getWorldInfo().getSpawnX(),
                    p_12345_2_.getWorldInfo().getSpawnY(),
                    p_12345_2_.getWorldInfo().getSpawnZ());
        }
    }


}
