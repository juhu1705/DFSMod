package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.objects.potions.effects.EffectCompleteSlowness;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPotions {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, DfSMod.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, DfSMod.MOD_ID);

    public static final RegistryObject<Effect> COMPLETE_SLOWNESS_EFFECT = EFFECTS.register("complete_slowness", EffectCompleteSlowness::new);

    public static final RegistryObject<Potion> COMPLETE_SLOWNESS_POTION = POTIONS.register("complete_slowness", () -> new Potion(new EffectInstance(COMPLETE_SLOWNESS_EFFECT.get(), 2400)));



}
