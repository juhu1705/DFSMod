package de.noisruker.dfs.species;

import de.noisruker.dfs.entities.IEntityMagic;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerSpecies implements IEntityMagic {

    private float power, regeneration, maxPower;
    private PlayerEntity player;





    @Override
    public float getPower() {
        return 0;
    }

    @Override
    public float getMaxPower() {
        return 0;
    }

    @Override
    public IEntityMagic setPower(float power) {
        return this;
    }

    @Override
    public IEntityMagic setMaxPower(float maxPower) {
        return this;
    }

    @Override
    public IEntityMagic usePower(float amount) {
        return this;
    }

    @Override
    public IEntityMagic setPowerRegenerationAmount(float powerRegeneration) {
        return this;
    }

    @Override
    public IEntityMagic regeneratePower() {
        return this;
    }
}
