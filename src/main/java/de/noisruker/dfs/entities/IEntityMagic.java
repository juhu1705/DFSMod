package de.noisruker.dfs.entities;

public interface IEntityMagic {

    public long getPower();

    public long getMaxPower();

    public IEntityMagic setPower(long power);

    public IEntityMagic setMaxPower(long maxPower);

    public IEntityMagic usePower(long amount);

    public IEntityMagic setPowerRegenerationAmount(long powerRegeneration);

    public IEntityMagic regeneratePower();

}
