package de.noisruker.dfs.tickrateHandling;


import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.registries.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

    private static ArrayList<LivingEntity> halfRequestsEntities = new ArrayList<>();
    private static ArrayList<LivingEntity> quaterRequestsEntities = new ArrayList<>();

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

        if(effectInstance.getAmplifier() == 0 && halfRequestsEntities.contains(entity)) {
            halfRequestsEntities.remove(entity);
        }
        else if(effectInstance.getAmplifier() == 1 && quaterRequestsEntities.contains(entity)) {
            quaterRequestsEntities.remove(entity);
        }

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

        if(event.getPotionEffect().getPotion().equals(ModPotions.COMPLETE_SLOWNESS_EFFECT.get())) {
            if (event.getPotionEffect().getAmplifier() == 0 && !halfRequestsEntities.contains(event.getEntityLiving())) {
                halfTickrate();
                halfRequestsEntities.add(event.getEntityLiving());
            }
            else if (event.getPotionEffect().getAmplifier() == 1 && !quaterRequestsEntities.contains(event.getEntityLiving())) {
                quaterTickrate();
                quaterRequestsEntities.add(event.getEntityLiving());
            }
        }
    }

    public static final ArrayList<ClientPlayerEntity> SMOOTHED_PLAYERS = new ArrayList<>();

    @SubscribeEvent
    public static void playerTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getInstance().player == null)
            return;

        if(Minecraft.getInstance().player.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) == null && (tickrateHalfed || tickrateQuatered)) {
            Minecraft.getInstance().gameSettings.smoothCamera = true;
            if(!SMOOTHED_PLAYERS.contains(Minecraft.getInstance().player))
                SMOOTHED_PLAYERS.add(Minecraft.getInstance().player);
        } else if(!tickrateHalfed && !tickrateQuatered) {
            if(SMOOTHED_PLAYERS.contains(Minecraft.getInstance().player)) {
                SMOOTHED_PLAYERS.remove(Minecraft.getInstance().player);
                Minecraft.getInstance().gameSettings.smoothCamera = false;
            }
        }

    }

    @SubscribeEvent
    public static void slowmotionHaste(PlayerEvent.BreakSpeed event) {
        if(halfRequestsEntities.contains(event.getPlayer()))
            event.setNewSpeed(event.getOriginalSpeed() * 2);
        else if(quaterRequestsEntities.contains(event.getPlayer()))
            event.setNewSpeed(event.getOriginalSpeed() * 4);
    }

    @SubscribeEvent
    public static void removeSlowmotion(PotionEvent.PotionRemoveEvent event) {


        if(event.getPotionEffect() == null)
            return;

        if(event.getPotionEffect().getPotion().equals(ModPotions.COMPLETE_SLOWNESS_EFFECT.get())) {
            checkForNormalTickrate(event.getEntity(), event.getPotionEffect());
        }
    }

    @SubscribeEvent
    public static void expireSlowmotion(PotionEvent.PotionExpiryEvent event) {

        if(event.getPotionEffect() == null)
            return;

        if(event.getPotionEffect().getPotion().equals(ModPotions.COMPLETE_SLOWNESS_EFFECT.get())) {
            checkForNormalTickrate(event.getEntity(), event.getPotionEffect());
        }
    }

    @SubscribeEvent
    public static void playerDied(PlayerEvent.Clone playerEvent) {
        if(playerEvent.getOriginal().getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) != null)
            checkForNormalTickrate(playerEvent.getOriginal(),
                    playerEvent.getOriginal().getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()));
    }

    @SubscribeEvent
    public static void playerLeft(PlayerEvent.PlayerLoggedOutEvent playerEvent) {
        if(playerEvent.getPlayer().getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) != null)
            checkForNormalTickrate(playerEvent.getPlayer(),
                    playerEvent.getPlayer().getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()));
    }

    static boolean smoothed = false;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        for (LivingEntity e: halfRequestsEntities) {
            if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) != null) {
                if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()).getDuration() <= 1)
                    e.removeActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get());
            }

            if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) == null) {
                checkForNormalTickrate(e, new EffectInstance(ModPotions.COMPLETE_SLOWNESS_EFFECT.get(), 0, 0));
                break;
            }
        }

        for (LivingEntity e: quaterRequestsEntities) {
            if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) != null) {
                if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()).getDuration() <= 1)
                    e.removeActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get());
            }

            if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) == null) {
                checkForNormalTickrate(e, new EffectInstance(ModPotions.COMPLETE_SLOWNESS_EFFECT.get(), 0, 1));
                break;
            }
        }

        if(!halfRequestsEntities.isEmpty() && !quaterRequestsEntities.isEmpty()) {
            if (!halfRequestsEntities.contains(Minecraft.getInstance().player) && !quaterRequestsEntities.contains(Minecraft.getInstance().player) && Minecraft.getInstance().player.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) == null) {
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
        for (LivingEntity e: halfRequestsEntities) {
            if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) != null) {
                if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()).getDuration() <= 1)
                    e.removeActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get());
            }
            if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) == null) {
                checkForNormalTickrate(e, new EffectInstance(ModPotions.COMPLETE_SLOWNESS_EFFECT.get(), 0, 0));
                break;
            }
        }

        for (LivingEntity e: quaterRequestsEntities) {
            if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) != null) {
                if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()).getDuration() <= 1)
                    e.removeActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get());
            }
            if(e.getActivePotionEffect(ModPotions.COMPLETE_SLOWNESS_EFFECT.get()) == null) {
                checkForNormalTickrate(e, new EffectInstance(ModPotions.COMPLETE_SLOWNESS_EFFECT.get(), 0, 1));
                break;
            }
        }

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
