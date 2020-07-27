package de.noisruker.dfs.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class StonePillarBlock extends RotatedPillarBlock {

    public StonePillarBlock() {
        super(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(1.0f, 1.9f)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE));
    }

}
