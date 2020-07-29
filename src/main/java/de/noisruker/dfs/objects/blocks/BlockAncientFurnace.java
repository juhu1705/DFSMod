package de.noisruker.dfs.objects.blocks;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAncientFurnace extends AbstractFurnaceBlock {

    public BlockAncientFurnace() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13));
    }

    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return null;
    }
}
