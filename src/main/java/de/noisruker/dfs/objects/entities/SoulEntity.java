package de.noisruker.dfs.objects.entities;

import de.noisruker.dfs.registries.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class SoulEntity extends FlyingEntity implements IEntityMagic, IMob {

    private float power, maxPower, regenerationAmount;



    public SoulEntity(EntityType<? extends SoulEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 40;
        this.moveController = new SoulEntity.MoveHelperController(this);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        super.goalSelector.addGoal(3, new RandomFlyGoal(this));
        super.goalSelector.addGoal(5, new LookAroundGoal(this));
        super.goalSelector.addGoal(4, new MagicProjectileAttackGoal(this));
        super.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 15, true, false, (p_213812_1_) -> {
            if(p_213812_1_ instanceof PlayerEntity) {
                for(ItemStack stack: ((PlayerEntity) p_213812_1_).getArmorInventoryList()) {
                    if(stack.getItem() instanceof ArmorItem){
                        if(((ArmorItem) stack.getItem()).getEquipmentSlot().equals(EquipmentSlotType.HEAD))
                            return false;
                    }
                }
            }

            return Math.abs(p_213812_1_.getPosY() - this.getPosY()) <= 6.0D;
        }));
        super.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CreeperEntity.class, 15, true, false, (p_213812_1_) -> Math.abs(p_213812_1_.getPosY() - this.getPosY()) <= 6.0D));
    }

    @Override
    public double getPosYEye() {
        return super.getPosY() + 0.25D;
    }

    @Override
    protected boolean isDespawnPeaceful() {
        return true;
    }

    @Override
    public AttributeModifierManager getAttributeManager() {
        return super.getAttributeManager();
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 40.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 2.0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 120.0D);
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
        if(maxPower >= 0)
            this.maxPower = maxPower;
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

    @Override
    public IEntityMagic addPower(float power) {
        this.setPower(this.getPower() + power);
        return this;
    }

    static class MagicProjectileAttackGoal extends Goal {
        private final SoulEntity parentEntity;
        public int attackTimer;

        public MagicProjectileAttackGoal(SoulEntity soul) {
            this.parentEntity = soul;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return this.parentEntity.getAttackTarget() != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.attackTimer = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {

        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.parentEntity.getAttackTarget();

            if(livingentity instanceof PlayerEntity) {
                for(ItemStack stack: ((PlayerEntity) livingentity).getArmorInventoryList()) {
                    if(stack.getItem() instanceof ArmorItem){
                        if(((ArmorItem) stack.getItem()).getEquipmentSlot().equals(EquipmentSlotType.HEAD)) {
                            this.parentEntity.setAttackTarget(null);
                            return;
                        }
                    }
                }
            }

            if (livingentity != null && livingentity.getDistanceSq(this.parentEntity) < 4096.0D
                    && this.parentEntity.canEntityBeSeen(livingentity)) {
                World world = this.parentEntity.world;
                ++this.attackTimer;
                if (this.attackTimer == 10) {
                    //world.playEvent((PlayerEntity)null, 1015, new BlockPos(this.parentEntity), 0);
                }

                if (this.attackTimer == 40) {

                    MagicProjectileEntity magicProjectileEntity = new MagicProjectileEntity(world, this.parentEntity);

                    magicProjectileEntity.setItem(new ItemStack(ModItems.MAGIC_PROJECTILE.get()));

                    double d0 = livingentity.getPosX() - this.parentEntity.getPosX();
                    double d1 = livingentity.getPosYHeight(0.3333333333333333D) - magicProjectileEntity.getPosY();
                    double d2 = livingentity.getPosZ() - this.parentEntity.getPosZ();
                    double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);

                    magicProjectileEntity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.parentEntity.world.getDifficulty().getId() * 4));

                    magicProjectileEntity.setPower(100f);

                    world.addEntity(magicProjectileEntity);

                    this.attackTimer = -40;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
        }
    }

    static class LookAroundGoal extends Goal {
        private final SoulEntity parentEntity;

        public LookAroundGoal(SoulEntity soul) {
            this.parentEntity = soul;
            this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.parentEntity.getAttackTarget() == null) {
                Vector3d vec3d = this.parentEntity.getMotion();
                this.parentEntity.rotationYaw = -((float) MathHelper.atan2(vec3d.x, vec3d.z)) * (180F / (float)Math.PI);
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            } else {
                LivingEntity livingentity = this.parentEntity.getAttackTarget();
                double d0 = 64.0D;
                if (livingentity.getDistanceSq(this.parentEntity) < 4096.0D) {
                    double d1 = livingentity.getPosX() - this.parentEntity.getPosX();
                    double d2 = livingentity.getPosZ() - this.parentEntity.getPosZ();
                    this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }

        }
    }

    static class MoveHelperController extends MovementController {
        private final SoulEntity parentEntity;
        private int courseChangeCooldown;

        public MoveHelperController(SoulEntity soul) {
            super(soul);
            this.parentEntity = soul;
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    Vector3d vec3d = new Vector3d(this.posX - this.parentEntity.getPosX(), this.posY - this.parentEntity.getPosY(), this.posZ - this.parentEntity.getPosZ());
                    double d0 = vec3d.length();
                    vec3d.normalize();
                    if (this.func_220673_a(vec3d, MathHelper.ceil(d0))) {
                        this.parentEntity.setMotion(this.parentEntity.getMotion().add(vec3d.scale(0.01D)));
                    } else {
                        this.action = MovementController.Action.WAIT;
                    }
                }

            }
        }

        private boolean func_220673_a(Vector3d p_220673_1_, int p_220673_2_) {
            AxisAlignedBB axisalignedbb = this.parentEntity.getBoundingBox();

            for(int i = 1; i < p_220673_2_; ++i) {
                axisalignedbb = axisalignedbb.offset(p_220673_1_);
                if (!this.parentEntity.world.hasNoCollisions(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }

            return true;
        }
    }



    static class RandomFlyGoal extends Goal {
        private final SoulEntity parentEntity;

        public RandomFlyGoal(SoulEntity soul) {
            this.parentEntity = soul;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            MovementController movementcontroller = this.parentEntity.getMoveHelper();
            if (!movementcontroller.isUpdating()) {
                return true;
            } else {
                double d0 = movementcontroller.getX() - this.parentEntity.getPosX();
                double d1 = movementcontroller.getY() - this.parentEntity.getPosY();
                double d2 = movementcontroller.getZ() - this.parentEntity.getPosZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.getPosX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.parentEntity.getPosY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.getPosZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
        }
    }

}
