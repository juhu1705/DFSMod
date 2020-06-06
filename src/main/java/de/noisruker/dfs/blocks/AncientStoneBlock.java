package de.noisruker.dfs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class AncientStoneBlock extends OreBlock {

    public AncientStoneBlock() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(1.0f, 1.9f)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
        );
    }

    @Override
    protected int getExperience(Random rand) {
        return MathHelper.nextInt(rand, 5, 20);
    }
}
