package de.noisruker.dfs.objects.tileentities;

import de.noisruker.dfs.registries.ModTileEntityTypes;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

public class LevitatorTileEntity extends TileEntity implements ITickableTileEntity {

    private final boolean push;
    private final Direction direction;
    private int strength;

    public LevitatorTileEntity() {
        this(Direction.UP, 0, true);
    }

    public LevitatorTileEntity(int strength, boolean push) {
        this(Direction.UP, strength, push);
    }

    public LevitatorTileEntity(Direction direction, boolean push) {
        this(direction, 0, push);
    }

    public LevitatorTileEntity(Direction direction, int strength, boolean push) {
        super(ModTileEntityTypes.LEVITATOR.get());
        this.direction = direction;
        this.push = push;
        this.strength = strength;
    }

    @Override
    public void tick() {

        if(super.world == null)
            return;

        this.setStrength(super.world.getRedstonePowerFromNeighbors(super.pos));

        if(this.strength == 0) {
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AbstractFurnaceBlock.LIT, Boolean.FALSE), 3);
            return;
        }
        this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AbstractFurnaceBlock.LIT, Boolean.TRUE), 3);

        AxisAlignedBB aabb = new AxisAlignedBB(super.pos.offset(this.direction)).expand(
                new Vector3d(this.direction.getDirectionVec().getX(),
                        this.direction.getDirectionVec().getY(),
                        this.direction.getDirectionVec().getZ())
                        .scale(this.strength));

        for(Entity e: super.world.getEntitiesWithinAABBExcludingEntity(null, aabb)) {
            Vector3d vec3d = e.getMotion();
            double d0;

            if(this.push)
                d0 = Math.min(0.7D, vec3d.y + 0.12D);
            else
                d0 = Math.min(-0.1, vec3d.y + 0.1);

            if(e.isSneaking()) {
                if (vec3d.y > -0.3)
                    d0 = Math.max(-0.3, vec3d.y - 0.1);
                else
                    d0 = Math.min(-0.3, vec3d.y + 0.1);
            }

            e.setMotion(vec3d.x, d0, vec3d.z);
            e.fallDistance = 0.0F;

            if(e instanceof ServerPlayerEntity)
                this.resetFlightTime((ServerPlayerEntity) e);
        }
    }

    public void resetFlightTime(ServerPlayerEntity player) {
        player.connection.floatingTickCount = 0;
    }

    public void setStrength(int strength) {
        this.strength = Math.max(strength, 0);
        this.markDirty();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("strength", this.strength);
        return super.write(compound);
    }


    @Override
    public void read(BlockState state, CompoundNBT compound) {
        this.strength = compound.getInt("strength");
        super.read(state, compound);
    }

    public Direction getDirection() {
        return this.direction;
    }
}
