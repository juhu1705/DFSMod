package de.noisruker.dfs.species;

import com.google.common.collect.ImmutableMap;
import de.noisruker.dfs.registries.ModSpecies;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class Species {

    public static final HashMap<String, Species> SPECIES = new HashMap<>();


    private final float initMaxPower, eyeHeight, height, width, powerRegeneration, hearts, attackSpeed, attackDamage;
    private final int color;

    public Species(float initMaxPower, float powerRegeneration, float eyeHeight, float height, float width, float hearts, float attackSpeed, float attackDamage, int color) {
        this.initMaxPower = initMaxPower;
        this.eyeHeight = eyeHeight;
        this.height = height;
        this.width = width;
        this.powerRegeneration = powerRegeneration;
        this.hearts = hearts;
        this.color = color;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    public static String getKeyForSpecies(Species species) {
        for(Map.Entry<String, Species> entries: Species.SPECIES.entrySet())
            if(entries.getValue().equals(species))
                return entries.getKey();
        return "human";
    }

    public void applyBaseValues(PlayerEntity playerEntity) {
        this.setAttackSpeed(playerEntity);
        this.setStrength(playerEntity);
        this.setHearts(playerEntity);
        if(this.equals(ModSpecies.ENFALI)) {

        }

    }

    public void setStrength(PlayerEntity playerEntity) {
        if(playerEntity.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
            playerEntity.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.attackDamage);
    }

    public void setAttackSpeed(PlayerEntity playerEntity) {
        if(playerEntity.getAttribute(SharedMonsterAttributes.ATTACK_SPEED) != null)
            playerEntity.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(this.attackSpeed);
    }

    public void setHearts(PlayerEntity playerEntity) {
        if(playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH) != null)
            playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.hearts);
    }

    public void applyBoundings(PlayerEntity playerEntity) {
        EntitySize size = new EntitySize(width, height, playerEntity.size.fixed);
        playerEntity.size = size;
            //PlayerEntity.STANDING_SIZE = EntitySize.flexible(width, height);;

            //PlayerEntity.SIZE_BY_POSE = ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, PlayerEntity.STANDING_SIZE).put(Pose.SLEEPING, EntitySize.fixed(0.2F, 0.2F)).put(Pose.FALL_FLYING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SWIMMING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SPIN_ATTACK, EntitySize.flexible(0.6F, 0.6F)).put(Pose.CROUCHING, EntitySize.flexible(width, height - 0.3f)).put(Pose.DYING, EntitySize.fixed(0.2F, 0.2F)).build();

    }



    public static Species registerSpecies(String name, Species species) {
        if(Species.SPECIES.containsKey(name))
            return null;
        else {
            Species.SPECIES.put(name, species);
            return Species.SPECIES.get(name);
        }
    }

    public float getPowerRegeneration() {
        return powerRegeneration;
    }

    public float getInitMaxPower() { return this.initMaxPower; }

    public float getEyeHeight() {
        return this.eyeHeight;
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }

    public int getColor(){
        return this.color;
    }

    @Override
    public String toString() {
        return Species.getKeyForSpecies(this);
    }

    public void loadSizeForPlayer() {
        PlayerEntity.STANDING_SIZE = EntitySize.flexible(width, height);
        PlayerEntity.SIZE_BY_POSE = ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, PlayerEntity.STANDING_SIZE).put(Pose.SLEEPING, EntitySize.fixed(0.2F, 0.2F)).put(Pose.FALL_FLYING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SWIMMING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SPIN_ATTACK, EntitySize.flexible(0.6F, 0.6F)).put(Pose.CROUCHING, EntitySize.flexible(width, height - 0.3f)).put(Pose.DYING, EntitySize.fixed(0.2F, 0.2F)).build();
    }
}
