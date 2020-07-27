package de.noisruker.dfs.objects.blocks;

import de.noisruker.dfs.DfSMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DungeonPortal extends NetherPortalBlock {

    public DungeonPortal() {
        super(Properties.create(Material.GLASS));
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isPassenger() && !entityIn.isBeingRidden() && entityIn.isNonBoss() && !worldIn.isRemote && DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE) != null) {
            entityIn.changeDimension(worldIn.dimension.getType() == DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE) ? DimensionType.OVERWORLD : DimensionType.byName(DfSMod.DUNGEON_DIM_TYPE));
        }

    }

    @Override
    public boolean trySpawnPortal(IWorld worldIn, BlockPos pos) {
        NetherPortalBlock.Size netherportalblock$size = this.isPortal(worldIn, pos);
        if (netherportalblock$size != null && !net.minecraftforge.event.ForgeEventFactory.onTrySpawnPortal(worldIn, pos, netherportalblock$size)) {
            
            return true;
        } else {
            return false;
        }
    }


}
