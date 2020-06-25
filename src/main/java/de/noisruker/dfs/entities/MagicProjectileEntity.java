package de.noisruker.dfs.entities;

import com.google.common.collect.Lists;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.blocks.AncientStoneBlock;
import de.noisruker.dfs.registries.ModEntityTypes;
import de.noisruker.dfs.registries.ModItems;
import javafx.geometry.Side;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MagicProjectileEntity extends ProjectileItemEntity implements IEntityMagic {

    private float power = 0, maxPower = 100, regenerationAmount = 0;
    private static final DataParameter<Float> POWER = EntityDataManager.createKey(MagicProjectileEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<BlockPos> POSITION = EntityDataManager.createKey(MagicProjectileEntity.class, DataSerializers.BLOCK_POS);

    public MagicProjectileEntity(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public MagicProjectileEntity(double x, double y, double z, World worldIn) {
        super(ModEntityTypes.ENTITY_MAGIC_PROJECTILE.get(), x, y, z, worldIn);


    }

    public MagicProjectileEntity(LivingEntity livingEntityIn, World worldIn) {
        super(ModEntityTypes.ENTITY_MAGIC_PROJECTILE.get(), livingEntityIn, worldIn);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MAGIC_PROJECTILE.get();
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult)result).getEntity();
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), power / 10f);
        } else if(result.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos block = ((BlockRayTraceResult)result).getPos();
            if(world.getBlockState(block).getBlock() instanceof AncientStoneBlock)
                ((AncientStoneBlock)world.getBlockState(block).getBlock()).asItem();
            else
                world.sendBlockBreakProgress(block.hashCode(), block, (int)(power / 10f));
            

        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        BlockPos pos = super.dataManager.get(POSITION);
        super.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    protected void registerData() {
        super.registerData();
        super.dataManager.register(POSITION, super.getPosition());
        super.dataManager.register(POWER, this.power);
    }

    @Override
    public float getPower() {
        return this.power;
    }



    @Override
    public float getMaxPower() {
        return this.maxPower;
    }

    @Override
    public IEntityMagic setPower(float power) {
        if(power <= this.maxPower && power >= 0)
            this.power = power;
        return this;
    }

    @Override
    public IEntityMagic setMaxPower(float maxPower) {

        return this;
    }

    @Override
    public IEntityMagic usePower(float amount) {
        float checkPower = this.power - amount;
        if(checkPower >= 0 && checkPower <= this.maxPower)
            this.power = checkPower;
        return this;
    }

    @Override
    public IEntityMagic setPowerRegenerationAmount(float powerRegeneration) {
        this.regenerationAmount = powerRegeneration;
        return this;
    }

    @Override
    public IEntityMagic regeneratePower() {
        this.usePower(-this.regenerationAmount);
        return this;
    }
}
