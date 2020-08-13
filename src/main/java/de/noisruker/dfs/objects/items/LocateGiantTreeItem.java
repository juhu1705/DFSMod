package de.noisruker.dfs.objects.items;

import de.noisruker.dfs.DfSMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class LocateGiantTreeItem extends ItemBase {
    public LocateGiantTreeItem() {
        super();
        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            private double rotation;
            @OnlyIn(Dist.CLIENT)
            private double rota;
            @OnlyIn(Dist.CLIENT)
            private long lastUpdateTick;

            @OnlyIn(Dist.CLIENT)
            public float call(ItemStack p_call_1_, @Nullable World p_call_2_, @Nullable LivingEntity p_call_3_) {
                if (p_call_3_ == null && !p_call_1_.isOnItemFrame()) {
                    return 0.0F;
                } else {
                    boolean flag = p_call_3_ != null;
                    Entity entity = (Entity)(flag ? p_call_3_ : p_call_1_.getItemFrame());
                    if (p_call_2_ == null) {
                        p_call_2_ = entity.world;
                    }

                    double d0;
                    if (p_call_2_.dimension.isSurfaceWorld()) {
                        double d1 = flag ? (double)entity.rotationYaw : this.getFrameRotation((ItemFrameEntity)entity);
                        d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
                        double d2 = this.getSpawnToAngle(p_call_2_, entity, p_call_1_) / (double)((float)Math.PI * 2F);
                        d0 = 0.5D - (d1 - 0.25D - d2);
                    } else {
                        d0 = Math.random();
                    }

                    if (flag) {
                        d0 = this.wobble(p_call_2_, d0);
                    }

                    return MathHelper.positiveModulo((float)d0, 1.0F);
                }
            }

            @OnlyIn(Dist.CLIENT)
            private double wobble(World worldIn, double p_185093_2_) {
                if (worldIn.getGameTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = worldIn.getGameTime();
                    double d0 = p_185093_2_ - this.rotation;
                    d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
                    this.rota += d0 * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }

                return this.rotation;
            }

            @OnlyIn(Dist.CLIENT)
            private double getFrameRotation(ItemFrameEntity p_185094_1_) {
                return (double)MathHelper.wrapDegrees(180 + p_185094_1_.getHorizontalFacing().getHorizontalIndex() * 90);
            }

            @OnlyIn(Dist.CLIENT)
            private double getSpawnToAngle(IWorld p_185092_1_, Entity p_185092_2_, ItemStack p_185092_3_) {
                BlockPos blockpos = getBlockPos(p_185092_3_, p_185092_1_.getWorld());
                return Math.atan2((double)blockpos.getZ() - p_185092_2_.getPosZ(), (double)blockpos.getX() - p_185092_2_.getPosX());
            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote) {
            locateStructure(playerIn, (ServerWorld) worldIn, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void locateStructure(Entity entityIn, ServerWorld worldIn, ItemStack stack) {
        CompoundNBT tag = new CompoundNBT();

        BlockPos structurePos = worldIn.findNearestStructure(DfSMod.MOD_ID + ":giant_tree_structure", entityIn.getPosition(), 3000, false);
        if (structurePos == null) {
            BlockPos spawnPos = worldIn.getSpawnPoint();
            tag.putBoolean("found", false);
            tag.putLong("location", spawnPos.toLong());
        } else {
            tag.putBoolean("found", true);
            tag.putLong("location", structurePos.toLong());
        }

        stack.setTag(tag);
    }

    public BlockPos getBlockPos(ItemStack p_12345_1_, World p_12345_2_) {
        if(p_12345_1_.hasTag()) {
            CompoundNBT tag = p_12345_1_.getTag();
            if(tag.contains("found")) {
                if(tag.getBoolean("found")) {
                    Long structureLong = tag.getLong("location");
                    return BlockPos.fromLong(structureLong);
                } else {
                    return p_12345_2_.getSpawnPoint();
                }
            } else {
                return p_12345_2_.getSpawnPoint();
            }
        } else {
            return p_12345_2_.getSpawnPoint();
        }
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            boolean structureFound = tag.getBoolean("found");
            if(structureFound) {
                tooltip.add(new TranslationTextComponent("dfssul.giant_tree_compass.found.tooltip").applyTextStyle(TextFormatting.GREEN));
            } else {
                tooltip.add(new TranslationTextComponent("dfssul.giant_tree_compass.failed.tooltip").applyTextStyle(TextFormatting.RED));
            }
        } else {
            tooltip.add(new TranslationTextComponent("dfssul.giant_tree_compass.unset.tooltip").applyTextStyle(TextFormatting.GOLD));
        }
    }
}
