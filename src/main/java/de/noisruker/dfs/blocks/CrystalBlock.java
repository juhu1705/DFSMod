package de.noisruker.dfs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class CrystalBlock extends Block {

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(6, 4, 7, 7, 7, 8),
            Block.makeCuboidShape(7, 1, 7, 8, 4, 8),
            Block.makeCuboidShape(8, 4, 7, 9, 7, 8),
            Block.makeCuboidShape(8, 3, 6, 9, 6, 7),
            Block.makeCuboidShape(8, 6, 8, 9, 8, 9),
            Block.makeCuboidShape(7, 4, 8, 8, 6, 9),
            Block.makeCuboidShape(6, 3, 6, 7, 5, 7),
            Block.makeCuboidShape(7, 7, 7, 8, 10, 8),
            Block.makeCuboidShape(8, 3, 8, 9, 5, 9),
            Block.makeCuboidShape(7, 5, 6, 8, 7, 7)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    public CrystalBlock() {
        super(Properties.create(Material.GLASS)
                .hardnessAndResistance(3.0f, 10.0f)
                .sound(SoundType.GLASS)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE));

    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE_N;
    }
}
