package de.noisruker.dfs.objects.blocks;

import de.noisruker.dfs.objects.tileentities.BlockAncientFurnaceTileEntity;
import de.noisruker.dfs.registries.ModTileEntityTypes;
import de.noisruker.dfs.species.PlayerSpecies;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockAncientFurnace extends AbstractFurnaceBlock implements IMagicBlock {

    public BlockAncientFurnace() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13));
    }

    /**
     * Interface for handling interaction with blocks that implement AbstractFurnaceBlock. Called in onBlockActivated
     * inside AbstractFurnaceBlock.
     */
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof BlockAncientFurnaceTileEntity) {
            player.openContainer((INamedContainerProvider)tileentity);
            player.addStat(Stats.INTERACT_WITH_BLAST_FURNACE);
        }

    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof BlockAncientFurnaceTileEntity) {
                InventoryHelper.dropItems(worldIn, pos, ((BlockAncientFurnaceTileEntity)te).getItems());
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(player.isSneaking()) {
            PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(player);

            if(species != null && species.getMaxPower() != 0) {

                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if(tileEntity instanceof BlockAncientFurnaceTileEntity &&
                        ((BlockAncientFurnaceTileEntity) tileEntity).getMaxPower() > ((BlockAncientFurnaceTileEntity) tileEntity).getPower()) {
                    species.usePower(10);
                    ((BlockAncientFurnaceTileEntity) tileEntity).addPower(10);
                }
                return ActionResultType.CONSUME;
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = stateIn.get(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getXOffset() * 0.52D : d4;
            double d6 = rand.nextDouble() * 9.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getZOffset() * 0.52D : d4;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return ModTileEntityTypes.ANCIENT_FURNACE.get().create();
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
