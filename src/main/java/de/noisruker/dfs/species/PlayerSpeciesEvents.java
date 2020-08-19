package de.noisruker.dfs.species;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.network.PacketActiveAbility;
import de.noisruker.dfs.network.SpeciesMessages;
import de.noisruker.dfs.registries.ModKeyBindings;
import de.noisruker.dfs.registries.ModSpecies;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerSpeciesEvents {

    public static final ResourceLocation POWER_BAR = new ResourceLocation(DfSMod.MOD_ID, "textures/gui/power_bar.png");

    @OnlyIn(Dist.CLIENT)
    public static float powerAmount = 0, maxPowerAmount = 0, shownPower = 0;
    public static Species species = ModSpecies.HUMAN;

    @SubscribeEvent
    public static void onPlayerEyeHeightEvent(EntityEvent.EyeHeight event) {
        if(event.getEntity() instanceof PlayerEntity) {

            PlayerSpecies player = PlayerSpecies.getOrCreatePlayer((PlayerEntity) event.getEntity());

            if(player == null)
                return;

            try {
                switch (event.getPose()) {
                    case SPIN_ATTACK:
                    case FALL_FLYING:
                    case SWIMMING:
                        //event.setNewHeight(player.species.getEyeHeight() - 1.22f);
                        event.setNewHeight(0.4f);
                        break;
                    case CROUCHING:
                        event.setNewHeight(player.species.getEyeHeight() - 0.35f);
                        break;
                    default:
                        event.setNewHeight(player.species.getEyeHeight());
                }
            } catch (NullPointerException exception) {
                DfSMod.LOGGER.debug("Can't load eye height!");
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerSpecies.registerDataParameters(event.getPlayer(), ModSpecies.HUMAN);

        PlayerEntity player = (PlayerEntity) event.getEntity();

        //registerDataParameters(player, ModSpecies.HUMAN);


        PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(player);

        if (playerSpecies == null)
            return;

        playerSpecies.onLoad();
        playerSpecies.species.setHearts(player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone event) {
        PlayerSpecies.registerDataParameters(event.getPlayer(), Species.SPECIES.get(event.getOriginal().getDataManager().get(PlayerSpecies.SPECIES_VALUE)));

        PlayerSpecies.changePlayerBoundig(event.getOriginal(), event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerResize(RenderPlayerEvent event) {
        if(event instanceof RenderPlayerEvent.Pre) {
            PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(event.getPlayer());

            event.getMatrixStack().push();

            if(playerSpecies == null)
                return;


            float width = playerSpecies.species.getWidth() / 0.6f,
                    height = playerSpecies.species.getHeight() / 1.8f;
            //DfSMod.LOGGER.debug("Width: " + width + "; Height: " + height);

            if(playerSpecies.player.getPose().equals(Pose.SWIMMING) ||playerSpecies.player.getPose().equals(Pose.FALL_FLYING) ||playerSpecies.player.getPose().equals(Pose.SPIN_ATTACK)) {
                event.getMatrixStack().scale(height, height, height);
            } else
                event.getMatrixStack().scale(height, height, height);
        } else if(event instanceof RenderPlayerEvent.Post) {
            event.getMatrixStack().pop();
        }
    }

    @SubscribeEvent
    public static void onPlayerSave(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(event.getPlayer());

        if (playerSpecies == null)
            return;

        playerSpecies.onSave();
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(player);

            if(species != null)
                species.regeneratePower(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTickLast(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity player = playerTickEvent.player;
        if(playerTickEvent.phase == TickEvent.Phase.START /*&& player.getPose().equals(Pose.STANDING) || player.getPose().equals(Pose.CROUCHING)*/) {
            Species species;
            if(!player.world.isRemote) {
                PlayerSpecies pSpecies = PlayerSpecies.getOrCreatePlayer(player);

                if (pSpecies == null)
                    return;

                species = pSpecies.species;
            } else
                species = PlayerSpeciesEvents.species;

            // Edit bounding box
            try {
                species.loadSizeForPlayer();
                /*player.boundingBox = new AxisAlignedBB(player.boundingBox.minX,
                        player.boundingBox.minY,
                        player.boundingBox.minZ,
                        player.boundingBox.minX + species.getWidth(),
                        player.boundingBox.minY + species.getHeight() - (player.getPose().equals(Pose.STANDING) ? 0f : 0.3f),
                        player.boundingBox.minZ + species.getWidth());*/
            } catch (NullPointerException ignored) {

            }
        }
    }

    @SubscribeEvent
    public static void onRenderPower(RenderGameOverlayEvent event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            Minecraft minecraft = Minecraft.getInstance();
            if(!minecraft.player.isCreative() && !minecraft.player.isSpectator()) {
                minecraft.getTextureManager().bindTexture(PlayerSpeciesEvents.POWER_BAR);

                if(PlayerSpeciesEvents.shownPower < PlayerSpeciesEvents.powerAmount) {
                    if(PlayerSpeciesEvents.shownPower + 0.1f >= PlayerSpeciesEvents.powerAmount)
                        PlayerSpeciesEvents.shownPower = PlayerSpeciesEvents.powerAmount;
                    else
                        PlayerSpeciesEvents.shownPower += 0.1f;
                } else if(PlayerSpeciesEvents.shownPower > PlayerSpeciesEvents.powerAmount) {
                    if(PlayerSpeciesEvents.shownPower - 0.1f <= PlayerSpeciesEvents.powerAmount)
                        PlayerSpeciesEvents.shownPower = PlayerSpeciesEvents.powerAmount;
                    else
                        PlayerSpeciesEvents.shownPower -= 0.1f;
                }

                float i = PlayerSpeciesEvents.maxPowerAmount * 10f;
                int x = event.getWindow().getScaledWidth() / 2 - 91;
                if (i > 0) {
                    int j = 182;
                    int k = (int)(((PlayerSpeciesEvents.shownPower * 10f) / i) * 183f);
                    int l = event.getWindow().getScaledHeight() - 32 + 3;
                    if (k > 0) {
                        minecraft.ingameGUI.blit(x, l, 0, 0, k, 5);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        if(ModKeyBindings.USE_ACTIVE_ABILITY.isPressed()) {
            SpeciesMessages.INSTANCE.sendToServer(new PacketActiveAbility());
        }
    }

}
