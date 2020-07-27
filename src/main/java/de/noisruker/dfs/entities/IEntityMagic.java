package de.noisruker.dfs.entities;

public interface IEntityMagic {

    public float getPower();

    public float getMaxPower();

    public IEntityMagic setPower(float power);

    public IEntityMagic setMaxPower(float maxPower);

    public IEntityMagic usePower(float amount);

    public IEntityMagic setPowerRegenerationAmount(float powerRegeneration);

    public IEntityMagic regeneratePower();

    public IEntityMagic addPower(float power);

}
