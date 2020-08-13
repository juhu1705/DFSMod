package de.noisruker.dfs.objects.entities;

import de.noisruker.dfs.objects.tileentities.IMagicTileEntity;
import de.noisruker.dfs.registries.ModEntityTypes;
import de.noisruker.dfs.registries.ModItems;
import de.noisruker.dfs.species.PlayerSpecies;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class MagicProjectileEntity extends ProjectileItemEntity implements IEntityMagic {

    private static final DataParameter<Float> POWER = EntityDataManager.createKey(MagicProjectileEntity.class, DataSerializers.FLOAT);

    private int ticker = 200;

    public MagicProjectileEntity(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
        super((EntityType<? extends ProjectileItemEntity>) type, worldIn);
    }

    public MagicProjectileEntity(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.ENTITY_MAGIC_PROJECTILE.get(), x, y, z, worldIn);
    }

    public MagicProjectileEntity(World worldIn, LivingEntity livingEntityIn) {
        super(ModEntityTypes.ENTITY_MAGIC_PROJECTILE.get(), livingEntityIn, worldIn);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MAGIC_PROJECTILE.get();
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult)result).getEntity();
            if(entity instanceof PlayerEntity) {
                PlayerSpecies species = PlayerSpecies.getOrCreatePlayer((PlayerEntity) entity);

                if(species != null) {
                    if(species.getMaxPower() > 0) {
                        float powerLeft = this.getPower() + species.getPower() - species.getMaxPower();

                        species.addPower(this.getPower());

                        entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), powerLeft / 10f);
                    } else
                        entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.getPower() / 10f);
                } else
                    entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.getPower() / 10f);
            } else if(entity instanceof IEntityMagic) {
                IEntityMagic entityMagic = (IEntityMagic) entity;

                float powerLeft = this.getPower() + entityMagic.getPower() - entityMagic.getMaxPower();

                entityMagic.addPower(this.getPower());

                entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), powerLeft / 10f);
            } else
                entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.getPower() / 10f);
        } else if(result.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos block = ((BlockRayTraceResult)result).getPos();
            if(world.getTileEntity(block) instanceof IMagicTileEntity && world.getBlockState(block).hasTileEntity()) {
                ((IMagicTileEntity)world.getTileEntity(block)).addPower(this.getPower());
            } else {
                Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
                if(explosion$mode.equals(Explosion.Mode.DESTROY)) {
                    if(!world.isRemote) {
                        //BlockState state = world.getBlockState(block);
                        //state.getBlock().onBlockHarvested(world, block, state, null);
                    }
                }
            }
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        return new RedstoneParticleData(0.25f, 0.5f, 0.5f, 1.0f);
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for(int i = 0; i < 16; ++i) {
                this.world.addParticle(iparticledata, this.getPosX(), this.getPosY() + 0.1f, this.getPosZ(), 0.15f * (rand.nextFloat() - 0.5f), 0.15f * (rand.nextFloat() - 0.5f), 0.15f * (rand.nextFloat() - 0.5f));
            }
        } else {
            this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.func_213882_k()), this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
        }

    }

    @Override
    public void tick() {
        super.tick();

        if(this.getMotion().length() < 0.25f)
            this.setMotion(this.getMotion().scale(1.2d));


        if(this.world.isRemote) {
            this.world.addParticle(new RedstoneParticleData(0.25f, 0.5f, 0.5f, 1.0f), this.getPosX() + 0.01f, this.getPosY() + 0.1f, this.getPosZ() + 0.01f, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f));
            this.world.addParticle(new RedstoneParticleData(0.25f, 0.5f, 0.5f, 1.0f), this.getPosX() + 0.01f, this.getPosY() + 0.2f, this.getPosZ() + 0.01f, 0.25f * (rand.nextFloat() - 0.5f), 0.25f * (rand.nextFloat() - 0.5f), 0.25f * (rand.nextFloat() - 0.5f));
            this.world.addParticle(new RedstoneParticleData(0.25f, 0.5f, 0.5f, 1.0f), this.getPosX() - 0.01f, this.getPosY() + 0.1f, this.getPosZ() - 0.01f, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f));
            this.world.addParticle(new RedstoneParticleData(0.25f, 0.5f, 0.5f, 1.0f), this.getPosX() - 0.01f, this.getPosY() + 0.2f, this.getPosZ() - 0.01f, 0.25f * (rand.nextFloat() - 0.5f), 0.25f * (rand.nextFloat() - 0.5f), 0.25f * (rand.nextFloat() - 0.5f));
        } else if(--ticker <= 0) {
            this.setPower(this.getPower() - 4f);
            ticker = 20;
        }

    }

    @Override
    protected void registerData() {
        this.dataManager.register(MagicProjectileEntity.POWER, 0f);

        super.registerData();
    }

    @Override
    public float getPower() {
        return this.dataManager.get(MagicProjectileEntity.POWER);
    }

    @Override
    public float getMaxPower() {
        return 0;
    }

    @Override
    public IEntityMagic setPower(float power) {
        if(power >= 0)
            this.dataManager.set(MagicProjectileEntity.POWER, power);
        else if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
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

    @Override
    public IEntityMagic addPower(float power) {
        this.setPower(this.getPower() + power);
        return this;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
