package de.noisruker.dfs.tileentities;

import de.noisruker.dfs.registries.ModItems;
import de.noisruker.dfs.registries.ModTileEntityTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class StoneLecternTileEntity extends TileEntity {

    public ItemStack item_1 = ItemStack.EMPTY;
    public ItemStack item_2 = ItemStack.EMPTY;
    public ItemStack item_3 = ItemStack.EMPTY;

    public StoneLecternTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public StoneLecternTileEntity() {
        super(ModTileEntityTypes.STONE_LECTERN.get());
    }



    public boolean onAddItem(PlayerEntity playerEntity) {
        ItemStack itemToAdd = playerEntity.inventory.getCurrentItem();

        if(itemToAdd.isItemEqual(ModItems.CRYSTAL_ITEM.get().getDefaultInstance())) {
            if(item_2.isEmpty())
                item_2 = playerEntity.inventory.decrStackSize(playerEntity.inventory.currentItem, 1);
            else if(item_3.isEmpty())
                item_3 = playerEntity.inventory.decrStackSize(playerEntity.inventory.currentItem, 1);

            this.markDirty();
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
            return true;
        } else if(itemToAdd.isItemEqual(ModItems.ANCIENT_THING.get().getDefaultInstance())) {
            if(item_1.isEmpty())
                item_1 = playerEntity.inventory.decrStackSize(playerEntity.inventory.currentItem, 1);
            this.markDirty();
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
            return true;
        }
        this.markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
        return false;
    }

    public boolean isFilledCompletely() {
        return !item_1.isEmpty() && !item_2.isEmpty() && !item_3.isEmpty();
    }

    public NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

        items.set(0, item_1);
        items.set(1, item_2);
        items.set(2, item_3);


        return items;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {

        ItemStackHelper.saveAllItems(compound, this.getItems());

        return super.write(compound);

    }

    @Override
    public void read(CompoundNBT compound) {
        if(compound == null)
            return;

        super.read(compound);

        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(compound, items);

        this.item_1 = items.get(0);
        this.item_2 = items.get(1);
        this.item_3 = items.get(2);
    }

    public void clearItems() {
        item_1 = ItemStack.EMPTY;
        item_2 = ItemStack.EMPTY;
        item_3 = ItemStack.EMPTY;

        this.markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, ItemStackHelper.saveAllItems(new CompoundNBT(), this.getItems()));
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return ItemStackHelper.saveAllItems(super.getUpdateTag(), this.getItems());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(pkt.getNbtCompound(), items);

        this.item_1 = items.get(0);
        this.item_2 = items.get(1);
        this.item_3 = items.get(2);

        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.read(tag);
        super.handleUpdateTag(tag);
    }
}
