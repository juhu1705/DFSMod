package de.noisruker.dfs.registries;

import de.noisruker.dfs.species.Species;
import de.noisruker.dfs.species.abilities.IAbility;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.HashMap;

public class ModActiveSpeciesAbility {

    public static final HashMap<Species, IAbility> ABILITIES = new HashMap<>();

    public static final IAbility ENFALI_ABILITY = ABILITIES.put(ModSpecies.ENFALI, (playerSpecies -> {
        playerSpecies.usePower(100);
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(ModPotions.COMPLETE_SLOWNESS_EFFECT.get(), 500, 1));
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.SPEED, 500, 5));
    }));

    public static final IAbility ELF_ABILITY = ABILITIES.put(ModSpecies.ELVES, (playerSpecies -> {
        playerSpecies.usePower(75);
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20, 0));
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 1));
    }));

    public static final IAbility DWARF_ABILITY = ABILITIES.put(ModSpecies.DWARFS, (playerSpecies -> {
        playerSpecies.usePower(25);
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.LUCK, 500, 2));
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.HASTE, 1000, 3));
    }));

    public static final IAbility NITHRILN_ABILITY = ABILITIES.put(ModSpecies.NITHRILN, (playerSpecies -> {
        playerSpecies.usePower(75);
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 100, 1));
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.STRENGTH, 1000, 2));
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.SPEED, 100, 1));
    }));

    public static final IAbility DRAGONTH_ABILITY = ABILITIES.put(ModSpecies.DRAGONTH, (playerSpecies -> {
        playerSpecies.usePower(100);
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20, 0));
        //playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 1, 0));
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1000, 2));
    }));

    public static final IAbility HUMAN_ABILITY = ABILITIES.put(ModSpecies.MAGIC_HUMAN, (playerSpecies -> {
        playerSpecies.usePower(10);
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.LUCK, 1000, 2));
        playerSpecies.getPlayer().addPotionEffect(new EffectInstance(Effects.SPEED, 100, 1));
    }));

}
