package de.noisruker.dfs.objects.blocks;

import de.noisruker.dfs.objects.tileentities.BlockAncientFurnaceTileEntity;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAncientFurnace extends AbstractFurnaceBlock implements IMagicBlock {

    public BlockAncientFurnace() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13));
    }

    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new BlockAncientFurnaceTileEntity();
    }

    @Override
    public void setPower(float power) {

    }

    @Override
    public float getPower() {
        return 0;
    }

    @Override
    public void setMaxPower(float power) {

    }

    @Override
    public float getMaxPower() {
        return 0;
    }

    @Override
    public float addPower(float power) {
        return 0;
    }

    @Override
    public boolean usePower(float power) {
        return false;
    }
}
