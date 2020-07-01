package de.noisruker.dfs.blocks;

import de.noisruker.dfs.DfSMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nonnull;

public class DungeonPortal extends NetherPortalBlock{

    public DungeonPortal() {
        super(Properties.create(Material.GLASS));
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isPassenger() && !entityIn.isBeingRidden() && entityIn.isNonBoss() && !worldIn.isRemote && DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE) != null) {
            entityIn.changeDimension(worldIn.dimension.getType() == DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE) ? DimensionType.OVERWORLD : DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE));

        }

    }

}
