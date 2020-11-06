package de.noisruker.dfs.objects.items;

import de.noisruker.dfs.registries.ModStructures;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class LocateGiantTreeItem extends ItemBase {
    public LocateGiantTreeItem() {
        super();
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

        BlockPos structurePos = worldIn.func_241117_a_(ModStructures.GIANT_TREE.get(), entityIn.getPosition(), 3000, false);
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
                    return new BlockPos(p_12345_2_.getWorldInfo().getSpawnX(),
                            p_12345_2_.getWorldInfo().getSpawnY(),
                            p_12345_2_.getWorldInfo().getSpawnZ());
                }
            } else {
                return new BlockPos(p_12345_2_.getWorldInfo().getSpawnX(),
                        p_12345_2_.getWorldInfo().getSpawnY(),
                        p_12345_2_.getWorldInfo().getSpawnZ());
            }
        } else {
            return new BlockPos(p_12345_2_.getWorldInfo().getSpawnX(),
                    p_12345_2_.getWorldInfo().getSpawnY(),
                    p_12345_2_.getWorldInfo().getSpawnZ());
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
                tooltip.add(new TranslationTextComponent("dfssul.giant_tree_compass.found.tooltip").mergeStyle(TextFormatting.GREEN));
            } else {
                tooltip.add(new TranslationTextComponent("dfssul.giant_tree_compass.failed.tooltip").mergeStyle(TextFormatting.RED));
            }
        } else {
            tooltip.add(new TranslationTextComponent("dfssul.giant_tree_compass.unset.tooltip").mergeStyle(TextFormatting.GOLD));
        }
    }
}
