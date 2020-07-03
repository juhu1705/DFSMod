package de.noisruker.dfs.potions.effects;


import de.noisruker.dfs.tickrateHandling.TickrateReducer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nullable;


public class EffectCompleteSlowness extends Effect {

    public EffectCompleteSlowness() {
        super(EffectType.BENEFICIAL, 0x404040);
        this.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 1F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributesModifier(SharedMonsterAttributes.ATTACK_SPEED, "55FCED67-E92A-486E-9800-B47F202C4386", 1F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributesModifier(SharedMonsterAttributes.FLYING_SPEED, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 1F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        //this.addAttributesModifier(LivingEntity.ENTITY_GRAVITY, "648D7064-6A60-4F59-8ABE-C2C23A6DD77D", -0.01F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributesModifier(LivingEntity.SWIM_SPEED, "648D7064-6A60-4F59-8ABE-C2C23A6DD77A", 1F, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        super.performEffect(entityLivingBaseIn, amplifier);
    }

    @Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
        this.performEffect(entityLivingBaseIn, amplifier);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        if(duration < 1) {
            TickrateReducer.setClientTickrate(20f);
            TickrateReducer.setServerTickrate(50L);
        }

        return duration >= 10;
    }
}
