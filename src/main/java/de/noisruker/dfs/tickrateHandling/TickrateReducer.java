package de.noisruker.dfs.tickrateHandling;


import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.registries.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickrateReducer {

    public static long SERVER_TICKRATE = 50L;
    public static float CLIENT_TICKRATE;

    public static TickrateReducer INSTANCE = new TickrateReducer();

    public static MinecraftServer server = null;

    public static TickrateReducer getInstance() {
        return INSTANCE;
    }

    private static boolean tickrateHalfed = false;
    private static boolean tickrateQuatered = false;

    private static ArrayList<Entity> halfRequestsEntities = new ArrayList<>();
    private static ArrayList<Entity> quaterRequestsEntities = new ArrayList<>();

    public static void halfTickrate() {
        if(!tickrateQuatered) {
            setClientTickrate(10f);
            setServerTickrate(100L);
            tickrateHalfed = true;
        }
    }

    public static void quaterTickrate() {
        setClientTickrate(5f);
        setServerTickrate(200L);
        tickrateQuatered = true;
        tickrateHalfed = false;
    }

    public static void setNormalTickrate() {
        setClientTickrate(20F);
        setServerTickrate(50L);
        tickrateQuatered = false;
        tickrateHalfed = false;
        Minecraft.getInstance().gameSettings.smoothCamera = false;
    }

    public static void checkForNormalTickrate(Entity entity, EffectInstance effectInstance) {

        DfSMod.LOGGER.debug("Effect Amount Half: " + halfRequestsEntities.size());
        DfSMod.LOGGER.debug("Effect Amount Quater: " + quaterRequestsEntities.size());

        if(effectInstance.getAmplifier() == 0 && halfRequestsEntities.contains(entity)) {
            halfRequestsEntities.remove(entity);
            if(entity instanceof PlayerEntity)
                ((PlayerEntity) entity).jumpMovementFactor /= 2;
        }
        else if(effectInstance.getAmplifier() == 1 && quaterRequestsEntities.contains(entity)) {
            quaterRequestsEntities.remove(entity);
            if(entity instanceof PlayerEntity)
                ((PlayerEntity) entity).jumpMovementFactor /= 4;
        }

        DfSMod.LOGGER.debug("Effect Amount Half: " + halfRequestsEntities.size());
        DfSMod.LOGGER.debug("Effect Amount Quater: " + quaterRequestsEntities.size());

        if(quaterRequestsEntities.isEmpty()) {
            tickrateQuatered = false;
            if(!halfRequestsEntities.isEmpty())
                halfTickrate();
            else
                setNormalTickrate();
        }
    }

    public static void setClientTickrate(float tickrate) {
        CLIENT_TICKRATE = Minecraft.getInstance().timer.tickLength = 1000.0F / tickrate;
    }

    public static void setServerTickrate(long tickrate) {
        SERVER_TICKRATE = tickrate;
    }

    @SubscribeEvent
    public static void applySlowmotion(PotionEvent.PotionAddedEvent event) {

        DfSMod.LOGGER.debug("Effect added to player: " + event.getPotionEffect().getPotion().getName() + " | " + event.toString());

        if(event.getPotionEffect().getPotion().equals(ModPotions.COMPLETE_SLOWNESS_EFFECT.get())) {
            if (event.getPotionEffect().getAmplifier() == 0 && !halfRequestsEntities.contains(event.getEntity())) {
                halfTickrate();
                halfRequestsEntities.add(event.getEntity());
                if(event.getEntityLiving() instanceof PlayerEntity)
                    ((PlayerEntity) event.getEntityLiving()).jumpMovementFactor *= 2;
            }
            else if (event.getPotionEffect().getAmplifier() == 1 && !quaterRequestsEntities.contains(event.getEntity())) {
                quaterTickrate();
                quaterRequestsEntities.add(event.getEntity());
                if(event.getEntityLiving() instanceof PlayerEntity)
                    ((PlayerEntity) event.getEntityLiving()).jumpMovementFactor *= 4;
            }
        }
    }

    @SubscribeEvent
    public static void slowmotionHaste(PlayerEvent.BreakSpeed event) {
        if(halfRequestsEntities.contains(event.getEntity()))
            event.setNewSpeed(event.getOriginalSpeed() * 2);
        else if(quaterRequestsEntities.contains(event.getEntity()))
            event.setNewSpeed(event.getOriginalSpeed() * 4);
    }

    @SubscribeEvent
    public static void removeSlowmotion(PotionEvent.PotionRemoveEvent event) {
        DfSMod.LOGGER.debug("Effect removed from player: " + event.getPotion().getName() + " to " + event.getPotionEffect());

        if(event.getPotionEffect() == null)
            return;

        if(event.getPotionEffect().getPotion().equals(ModPotions.COMPLETE_SLOWNESS_EFFECT.get())) {
            checkForNormalTickrate(event.getEntity(), event.getPotionEffect());
        }
    }

    @SubscribeEvent
    public static void expireSlowmotion(PotionEvent.PotionExpiryEvent event) {
        DfSMod.LOGGER.debug("Effect removed from player: " + event.getPotionEffect().getPotion().getName() + " to " + event.getPotionEffect());

        if(event.getPotionEffect() == null)
            return;

        if(event.getPotionEffect().getPotion().equals(ModPotions.COMPLETE_SLOWNESS_EFFECT.get())) {
            checkForNormalTickrate(event.getEntity(), event.getPotionEffect());
        }
    }

    static boolean smoothed = false;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if(!halfRequestsEntities.isEmpty() && !quaterRequestsEntities.isEmpty()) {
            if (!halfRequestsEntities.contains(Minecraft.getInstance().player) && !quaterRequestsEntities.contains(Minecraft.getInstance().player)) {
                Minecraft.getInstance().gameSettings.smoothCamera = true;
                smoothed = true;
            }
        } else if (halfRequestsEntities.isEmpty() && quaterRequestsEntities.isEmpty()) {
            if(smoothed) {
                Minecraft.getInstance().gameSettings.smoothCamera = false;
                smoothed = false;
            }
        }

        // DfSMod.LOGGER.debug("Species: " + Minecraft.getInstance().player);
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if(!event.world.isRemote())
            server = event.world.getServer();
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if(event.phase.equals(TickEvent.Phase.START) && server != null) {
            //DfSMod.LOGGER.debug("Ticking : " + SERVER_TICKRATE);

            server.serverTime -= 50;
            server.serverTime += TickrateReducer.SERVER_TICKRATE;
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        setNormalTickrate();
        halfRequestsEntities.clear();
        quaterRequestsEntities.clear();
    }

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Load event) {
        setNormalTickrate();
        halfRequestsEntities.clear();
        quaterRequestsEntities.clear();
    }

}
