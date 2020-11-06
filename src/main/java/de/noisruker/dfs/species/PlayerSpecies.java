package de.noisruker.dfs.species;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.network.PacketAcceptSpecies;
import de.noisruker.dfs.network.PacketPower;
import de.noisruker.dfs.network.SpeciesMessages;
import de.noisruker.dfs.objects.entities.IEntityMagic;
import de.noisruker.dfs.registries.ModSpecies;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerSpecies implements IEntityMagic {

    private static final HashMap<UUID, PlayerSpecies> PLAYER_SPECIES = new HashMap<>();


    private static final String POWER = "power", REGENERATION = "power_regeneration", MAX_POWER = "max_power", SPECIES = "species";
    private static final DataParameter<Float> POWER_VALUE = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.FLOAT), MAX_POWER_VALUE = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.FLOAT), REGENERATION_VALUE = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.FLOAT);
    static final DataParameter<String> SPECIES_VALUE = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.STRING);
    static {
        DfSMod.LOGGER.debug("IDs are registered " + POWER_VALUE.getId() + " | " + MAX_POWER_VALUE.getId() + " | " + REGENERATION_VALUE.getId() + " | " + SPECIES_VALUE.getId());
    }

    Species species;
    PlayerEntity player;
    //private boolean dataRegistered = false;

    public static PlayerSpecies getOrCreatePlayer(PlayerEntity player) {
        // DfSMod.LOGGER.debug("Containing Player: " + PlayerSpecies.PLAYER_SPECIES.containsKey(player));

        if (player.getGameProfile() == null)
            return null;

        //DfSMod.LOGGER.debug("Erfolgreich");

        for (Map.Entry<UUID, PlayerSpecies> entry : PlayerSpecies.PLAYER_SPECIES.entrySet()) {
            //DfSMod.LOGGER.debug("Hi " + entry.getKey().equals(player.getGameProfile().getId()) + " " + entry.getValue().updatePlayer(player).getSpecies() == null);
            if (entry.getKey().equals(player.getGameProfile().getId()))
                return entry.getValue().updatePlayer(player);

        }

        if (PlayerSpecies.PLAYER_SPECIES.containsKey(player.getGameProfile().getId()))
            return PlayerSpecies.PLAYER_SPECIES.get(player.getGameProfile().getId());

        return PlayerSpecies.putPlayer(player, new PlayerSpecies(player)) == null ? PlayerSpecies.PLAYER_SPECIES.get(player.getGameProfile().getId()) : PlayerSpecies.PLAYER_SPECIES.get(player.getGameProfile().getId());
    }

    public static void changePlayerBoundig(PlayerEntity old, PlayerEntity _new) {
        PlayerSpecies species = PlayerSpecies.PLAYER_SPECIES.remove(old.getGameProfile().getId());

        species.updatePlayer(_new);

        putPlayer(_new, species);
        species.setPower(0.0f);
        species.setMaxPower(old.getDataManager().get(PlayerSpecies.MAX_POWER_VALUE));
        species.setPowerRegenerationAmount(old.getDataManager().get(PlayerSpecies.REGENERATION_VALUE));
    }

    public static PlayerSpecies putPlayer(PlayerEntity player, PlayerSpecies species) {
        return PlayerSpecies.PLAYER_SPECIES.put(player.getGameProfile().getId(), species);
    }

    protected PlayerSpecies(PlayerEntity player) {
        this.player = player;

        Species s;

        try {
            s = Species.SPECIES.get(this.player.getDataManager().get(PlayerSpecies.SPECIES_VALUE));
        } catch (NullPointerException exception) {
            s = ModSpecies.HUMAN;
        }

        this.species = s;

        try {
            this.setSpecies(s);
        } catch (NullPointerException exception) {
            DfSMod.LOGGER.debug("Can't load species to players data manager!");
        }
        species.applyBaseValues(player);

        //this.registerDataParameters();
    }

    public PlayerSpecies updatePlayer(PlayerEntity player) {
        this.player = player;
        species.applyBaseValues(player);

        //this.registerDataParameters();

        return this;
    }

    public static Map<UUID, PlayerSpecies> getMap() {
        return PlayerSpecies.PLAYER_SPECIES;
    }

    public PlayerSpecies setNewSpecies(Species species) {
        this.species = species;
        this.setSpecies(species);
        species.applyBaseValues(player);

        this.setPowerRegenerationAmount(species.getPowerRegeneration());
        this.setMaxPower(species.getInitMaxPower());
        this.setPower(0.0f);

        this.player.setHealth(this.player.getMaxHealth() - (10 - this.player.getHealth()));

        this.player.setSneaking(false);

        if(!this.player.world.isRemote && this.player instanceof ServerPlayerEntity) {
            ServerWorld world = (ServerWorld) this.player.world;

            world.spawnParticle((ServerPlayerEntity) player, ParticleTypes.LARGE_SMOKE, true, player.getPosX() - 0.5, player.getPosY(), player.getPosZ() - 0.5, 50, 1, 2, 1, 0.1);
            world.spawnParticle((ServerPlayerEntity) player, ParticleTypes.FIREWORK, true, player.getPosX() - 0.5, player.getPosY(), player.getPosZ() - 0.5, 2, 1, 2, 1, 0.1);
            world.spawnParticle((ServerPlayerEntity) player, ParticleTypes.FLAME, true, player.getPosX() - 0.5, player.getPosY(), player.getPosZ() - 0.5, 20, 1, 2, 1, 0.1);

            world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.AMBIENT, 100f, 1f);
        }
        return this;
    }

    public PlayerSpecies setSpeciesCreating(Species species) {
        this.species = species;
        this.setSpecies(species);
        species.applyBaseValues(player);

        this.setPowerRegenerationAmount(species.getPowerRegeneration());
        this.setMaxPower(species.getInitMaxPower());
        this.setPower(0.0f);

        this.player.setHealth(this.player.getMaxHealth() - (10 - this.player.getHealth()));

        this.player.setSneaking(false);

        return this;
    }

    public void update() {
        this.setSpecies(species);
        species.applyBaseValues(player);

        this.player.setHealth(this.player.getMaxHealth());
    }

    private void onRegenerate() {

    }

    static void registerDataParameters(PlayerEntity player, Species species) {
        DfSMod.LOGGER.debug("Data Parameter are registered!");

        player.getDataManager().register(PlayerSpecies.POWER_VALUE, 0.0f);
        player.getDataManager().register(PlayerSpecies.MAX_POWER_VALUE, species.getInitMaxPower());
        player.getDataManager().register(PlayerSpecies.REGENERATION_VALUE, species.getPowerRegeneration());
        player.getDataManager().register(PlayerSpecies.SPECIES_VALUE, Species.getKeyForSpecies(species));


        DfSMod.LOGGER.debug("Data Parameter are registered! ");
    }

    private void registerDataParameters() {
        registerDataParameters(player, this.species);

    }

    public Species getSpecies() {
        return Species.SPECIES.get(this.player.getDataManager().get(PlayerSpecies.SPECIES_VALUE));
    }

    private void setSpecies(Species species) {
        this.player.getDataManager().set(PlayerSpecies.SPECIES_VALUE, Species.getKeyForSpecies(species));

        if(!player.world.isRemote && player instanceof ServerPlayerEntity)
            SpeciesMessages.INSTANCE.sendTo(new PacketAcceptSpecies(Species.getKeyForSpecies(this.getSpecies())), ((ServerPlayerEntity)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    void onSave() {
        DfSMod.LOGGER.debug("Save Species Playerdata: " + Species.getKeyForSpecies(this.getSpecies()));

        this.player.getPersistentData().putFloat(PlayerSpecies.POWER, this.getPower());
        this.player.getPersistentData().putFloat(PlayerSpecies.MAX_POWER, this.getMaxPower());
        this.player.getPersistentData().putFloat(PlayerSpecies.REGENERATION, this.getPowerRegeneration());
        this.player.getPersistentData().putString(PlayerSpecies.SPECIES, Species.getKeyForSpecies(this.getSpecies()));
    }

    void onLoad() {
        DfSMod.LOGGER.debug("Contains species: " + this.player.getPersistentData().contains(PlayerSpecies.SPECIES));
        if (this.player.getPersistentData().contains(PlayerSpecies.SPECIES))
            this.species = Species.SPECIES.get(this.player.getPersistentData().getString(PlayerSpecies.SPECIES));
        else
            this.species = ModSpecies.HUMAN;

        DfSMod.LOGGER.debug("Load Species Playerdata: " + Species.getKeyForSpecies(this.species));

        this.setSpeciesCreating(this.species);

        DfSMod.LOGGER.debug("Loaded Max Power: " + this.player.getPersistentData().getFloat(PlayerSpecies.MAX_POWER));

        this.setMaxPower(this.player.getPersistentData().getFloat(PlayerSpecies.MAX_POWER));
        this.setPower(this.player.getPersistentData().getFloat(PlayerSpecies.POWER));
        this.setPowerRegenerationAmount(this.player.getPersistentData().getFloat(PlayerSpecies.REGENERATION));
        this.species.applyBaseValues(this.player);
    }

    @Override
    public float getPower() {
        try {
            return this.player.getDataManager().get(PlayerSpecies.POWER_VALUE);
        } catch (NullPointerException ignore) {

        }
        return 0;
    }

    @Override
    public float getMaxPower() {
        try {
            return this.player.getDataManager().get(PlayerSpecies.MAX_POWER_VALUE);
        } catch (NullPointerException ignore) {

        }
        return -1;
    }

    public float getPowerRegeneration() {
        try {
            return this.player.getDataManager().get(PlayerSpecies.REGENERATION_VALUE);
        } catch (NullPointerException ignore) {

        }
        return 0;
    }

    @Override
    public IEntityMagic setPower(float power) {
        try {
            if(power <= this.getMaxPower())
                this.player.getDataManager().set(PlayerSpecies.POWER_VALUE, power);
            else
                this.player.getDataManager().set(PlayerSpecies.POWER_VALUE, this.getMaxPower());
        } catch (NullPointerException ignored) {

        }

        if(this.player instanceof ServerPlayerEntity && !this.player.world.isRemote()) {


            ServerPlayerEntity playerEntity = (ServerPlayerEntity) this.player;

            SpeciesMessages.INSTANCE.sendTo(new PacketPower(this.getPower(), this.getMaxPower()), playerEntity.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);

        }

        return this;
    }

    @Override
    public IEntityMagic setMaxPower(float maxPower) {
        try {
        this.player.getDataManager().set(PlayerSpecies.MAX_POWER_VALUE, maxPower);
        } catch (NullPointerException ignore) {

        }
        if(this.getPower() > this.getMaxPower())
            this.setPower(this.getMaxPower());
        return this;
    }

    @Override
    public IEntityMagic usePower(float amount) {
        if(this.player.isCreative())
            return this;
        if(this.getPower() < amount) {
            amount -= this.getPower();
            this.setPower(0);
            amount *= 0.1;
            this.player.attackEntityFrom(DamageSource.MAGIC, amount);
            return this;
        }
        this.setPower(this.getPower() - amount);
        return this;
    }

    @Override
    public IEntityMagic setPowerRegenerationAmount(float powerRegeneration) {
        try {
            this.player.getDataManager().set(PlayerSpecies.REGENERATION_VALUE, powerRegeneration);
        } catch (NullPointerException ignore) {

        }
        return this;
    }

    @Override
    public IEntityMagic regeneratePower() {
        return this.regeneratePower(player);
    }

    public IEntityMagic regeneratePower(PlayerEntity serverPlayer) {
        try {
            if(this.getMaxPower() == 0) {
                return this;
            } else if(this.getMaxPower() == -1) {
                return this;
            }

            if (serverPlayer.world.rand.nextInt(50) == 1 && !serverPlayer.isSprinting() && serverPlayer.getHealth() == serverPlayer.getMaxHealth()) {
                this.setPower(this.getPower() + this.getPowerRegeneration() * serverPlayer.world.rand.nextFloat());
                //DfSMod.LOGGER.debug("Regenerate to " + this.getPower() + " / " + this.getMaxPower());
            }
        } catch (NullPointerException ignored) {

        }
        return this;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    @Override
    public IEntityMagic addPower(float power) {
        this.setPower(this.getPower() + power);
        return this;
    }

    public EntitySize getSize() {
        return species.getSize(this);
    }
}
