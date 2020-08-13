package de.noisruker.dfs.objects.blocks;

import de.noisruker.dfs.objects.tileentities.LevitatorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

public class LevitatorBlock extends Block {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public LevitatorBlock() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(2.0f, 3f)
                .sound(SoundType.LANTERN)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE));
        super.setDefaultState(this.getStateContainer().getBaseState().with(LIT, Boolean.FALSE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LevitatorTileEntity();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        super.fillStateContainer(builder);
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + 0.5D;
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.BLOCKS, 0.01F, 0.5F, false);
            }

            Direction direction = Direction.UP;
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getXOffset() * 0.52D : d4;
            double d6 = rand.nextDouble() * 9.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getZOffset() * 0.52D : d4;

            double ySpeed = ((double) worldIn.getRedstonePowerFromNeighbors(pos)) / 16d;

            worldIn.addParticle(ParticleTypes.CLOUD, d0 + d5, d1 + d6, d2 + d7, 0.0D, ySpeed, 0.0D);
        }
    }
}
