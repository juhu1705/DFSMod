package de.noisruker.dfs.species;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.registries.ModSpecies;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpeciesPassiveAbilities {

    private static final HashMap<PlayerEntity, Integer> ELVES_ABILITY_COOLDOWN = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(player);

            if(species == null)
                return;

            if(species.getSpecies().equals(ModSpecies.DRAGONTH)) {
                if(event.getSource().isFireDamage()) {
                    event.setAmount(0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {

        PlayerEntity player = event.getPlayer();

        PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(player);

        if(species == null)
            return;

        try {
            if (species.getSpecies().equals(ModSpecies.DRAGONTH)) {
                if (player.world.rand.nextInt(10) == 1) {
                    event.getTarget().setFire(player.world.rand.nextInt(10000));
                }
            } else if (species.getSpecies().equals(ModSpecies.NITHRILN)) {
                if (player.world.rand.nextInt(5) == 1) {
                    if (event.getTarget() instanceof LivingEntity)
                        ((LivingEntity) event.getTarget()).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 1000, 1));
                }
            }
        } catch (NullPointerException ignored) {

        }

    }

    @SubscribeEvent
    public static void onPlayerDie(LivingDeathEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(player);

            if(species == null)
                return;

            if(species.getSpecies().equals(ModSpecies.ELVES) && !ELVES_ABILITY_COOLDOWN.containsKey(player)) {
                player.setHealth(6);
                event.setCanceled(true);
                SpeciesPassiveAbilities.ELVES_ABILITY_COOLDOWN.put(player, 60 * 20 * 1000);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(event.player);

        if(species == null)
            return;

        try {
            if (species.getSpecies().equals(ModSpecies.ELVES) && event.player.world.rand.nextInt(50) == 1) {
                BlockPos pos1 = event.player.getPosition().add(-5, -5, -5);
                BlockPos pos2 = event.player.getPosition().add(5, 5, 5);
                for (BlockPos pos : BlockPos.getAllInBoxMutable(pos1, pos2)) {
                    BlockState blockState = event.player.world.getBlockState(pos);
                    if (blockState.getBlock() instanceof IGrowable && !(blockState.getBlock() instanceof GrassBlock) &&
                            !(blockState.getBlock() instanceof TallGrassBlock)) {
                        IGrowable igrowable = (IGrowable) blockState.getBlock();
                        if (igrowable.canGrow(event.player.world, pos, blockState, event.player.world.isRemote)) {
                            if (event.player.world.rand.nextInt(50) == 1) {
                                if (event.player.world instanceof ServerWorld) {
                                    if (igrowable.canUseBonemeal(event.player.world, event.player.world.rand, pos, blockState)) {
                                        igrowable.grow((ServerWorld) event.player.world, event.player.world.rand, pos, blockState);
                                    }
                                }
                            }
                        }
                    }

                }
            } else if (species.getSpecies().equals(ModSpecies.ENFALI)) {
                if(event.player.isInWater())
                    if (event.player.getAir() < event.player.getMaxAir())
                        event.player.setAir(Math.min(event.player.getAir() + 4, event.player.getMaxAir()));
            }
        } catch (NullPointerException ignored) {

        }
    }

    @SubscribeEvent
    public static void onPlayerHarvest(PlayerEvent.BreakSpeed event) {
        PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(event.getPlayer());

        if(species == null)
            return;

        try {
            if (species.getSpecies().equals(ModSpecies.ENFALI)) {
                if(event.getPlayer().isInWaterOrBubbleColumn()) {
                    if(event.getPlayer().areEyesInFluid(FluidTags.WATER))
                        event.setNewSpeed(event.getNewSpeed() * 6f);

                    if(!event.getPlayer().isOnGround()) {
                        event.setNewSpeed(event.getNewSpeed() * 6f);
                    }
                    event.setCanceled(false);

                }
            } else if(species.getSpecies().equals(ModSpecies.DWARFS)) {
                event.setNewSpeed(event.getNewSpeed() * 6f);

            }
        } catch (NullPointerException ignored) {

        }
    }

    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(event.getPlayer());

        if(species == null)
            return;

        try {
            if(species.getSpecies().equals(ModSpecies.DWARFS)) {
                if(event.getTargetBlock().getMaterial().equals(Material.ROCK))
                    event.setCanHarvest(true);
            }
        } catch (NullPointerException ignored) {

        }
    }

    @SubscribeEvent
    public static void reduceCooldown(TickEvent event) {
        ArrayList<PlayerEntity> toRemove = new ArrayList<>();
        for(Map.Entry<PlayerEntity, Integer> entry: SpeciesPassiveAbilities.ELVES_ABILITY_COOLDOWN.entrySet()) {
            entry.setValue(entry.getValue() - 1);
            if(entry.getValue() <= 0)
                toRemove.add(entry.getKey());
        }
        for(PlayerEntity e: toRemove) {
            SpeciesPassiveAbilities.ELVES_ABILITY_COOLDOWN.remove(e);
        }


    }

}
