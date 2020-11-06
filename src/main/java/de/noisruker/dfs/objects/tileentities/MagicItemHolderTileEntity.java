package de.noisruker.dfs.objects.tileentities;

import de.noisruker.dfs.registries.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class MagicItemHolderTileEntity extends TileEntity implements ITickableTileEntity, ISidedInventory {

    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    public final float hoverStart = (float)(Math.random() * Math.PI * 2.0D);
    private int age = 0;

    public MagicItemHolderTileEntity() {
        super(ModTileEntityTypes.MAGIC_ITEM_HOLDER.get());
    }

    /**
     * Only for classes extends of this one
     * @param tileEntityType Your tile entity type
     */
    public MagicItemHolderTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public boolean canAddItem(ItemStack item) {
        return items.get(0).isEmpty() || (items.get(0).isItemEqual(item) && (items.get(0).getCount() + item.getCount() <= item.getMaxStackSize()) && (items.get(0).getCount() + item.getCount() <= this.getSizeInventory()));
    }

    public boolean addItem(PlayerEntity player) {
        if(this.canAddItem(player.getHeldItemMainhand())) {
            this.addItemToSlot(0,
                    player.isCreative() ?
                            new ItemStack(player.inventory.getCurrentItem().getItem(), 1) :
                            player.inventory.decrStackSize(player.inventory.currentItem, 1));

            this.markDirty();
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);

            return true;
        }
        return false;
    }

    private void addItemToSlot(int slot, ItemStack item) {
        if(items.get(slot).isEmpty())
            items.set(slot, item);
        else if(items.get(slot).isItemEqual(item))
            items.get(slot).grow(item.getCount());
    }

    public boolean hasItem() {
        return !items.get(0).isEmpty();
    }

    public ItemStack getItem() {
        return items.get(0);
    }

    public ItemStack useItem() {
        return this.useItem(1);
    }

    public ItemStack useItem(int count) {
        ItemStack stack;
        ItemStack toReturn;

        stack = items.get(0);
        toReturn = stack.split(count);

        if(stack.isEmpty())
            items.set(0, ItemStack.EMPTY);
         else
            items.set(0, stack);

        this.markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
        return toReturn;
    }

    public void playerRemoveItem(PlayerEntity player) {
        if(this.hasItem()) {
            player.inventory.addItemStackToInventory(this.useItem());

            this.markDirty();
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
        }
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        items = NonNullList.withSize(1, ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(compound, items);
        super.read(state, compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, items);

        return super.write(compound);
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
        items = NonNullList.withSize(1, ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(pkt.getNbtCompound(), items);

        this.markDirty();
        this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
        super.handleUpdateTag(state, tag);
    }

    public int getAge() {
        return age;
    }

    @Override
    public void tick() {
        this.age++;
    }

    public static final int[] SLOTS_UP_DOWN = new int[]{0};

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS_UP_DOWN;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return canAddItem(itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return this.hasItem() && stack.isItemEqual(items.get(0));
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.hasItem();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.useItem(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return this.items.remove(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.items.set(index, stack);
        this.markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.items.set(0, ItemStack.EMPTY);
    }
}
