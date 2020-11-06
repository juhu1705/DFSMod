package de.noisruker.dfs.objects.tileentities;

import de.noisruker.dfs.registries.ModSpecies;
import de.noisruker.dfs.species.PlayerSpecies;
import de.noisruker.dfs.species.PlayerSpeciesEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MagicCraftingTableControllerTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity, IMagicTileEntity {

    public float maxPower;
    public float powerLoose;
    public float progress;
    public float consumption;
    public float power;
    public int gridSize;
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private NonNullList<ItemStack> craftingGrid;
    private static final int[] SLOTS_UP = new int[]{0};
    private static final int[] SLOTS_DOWN = new int[]{1};
    private static final int[] SLOTS_HORIZONTAL = new int[]{0};

    public MagicCraftingTableControllerTileEntity(TileEntityType<?> typeIn, int gridSize, float maxPower, float powerLoose) {
        super(typeIn);
        this.gridSize = gridSize > 0 ? gridSize : 1;
        this.craftingGrid = NonNullList.withSize(this.gridSize, ItemStack.EMPTY);
        this.maxPower = maxPower;
        this.powerLoose = powerLoose;
    }

    @Override
    public void setPower(float power) {
        this.power = power >= 0 ? Math.min(power, this.maxPower) : 0;
    }

    @Override
    public float getPower() {
        return this.power;
    }

    @Override
    public void setMaxPower(float power) {
        this.maxPower = power >= 0 ? power : 0;
    }

    @Override
    public float getMaxPower() {
        return this.maxPower;
    }

    @Override
    public float addPower(float power) {
        float toReturn = (power + this.power) - this.maxPower;
        this.setPower(this.power + power);
        return toReturn > 0 ? toReturn : 0;
    }

    @Override
    public boolean usePower(float power) {
        boolean toReturn = power >= this.power;
        this.setPower(toReturn ? this.power - power : this.getPower());
        return toReturn;
    }

    @Override
    public boolean hasPower() {
        return this.power > 0;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.magic_crafting");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        if(index == 1) {
            if (this.isItemInCraftingGrid(stack))
                this.items.set(index, stack);
        } else
            this.items.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if(world.isRemote) {
            if(PlayerSpeciesEvents.species.equals(ModSpecies.HUMAN))
                return false;
        } else {
            PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(player);
            if(species == null || species.getSpecies().equals(ModSpecies.HUMAN))
                return false;
        }

        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void fillStackedContents(RecipeItemHelper helper) {

    }

    @Override
    public void setRecipeUsed(IRecipe<?> recipe) {

    }

    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void tick() {

    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void remove() {
        super.remove();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    public boolean isItemInCraftingGrid(ItemStack itemStack) {
        for(ItemStack i : craftingGrid) {
            if(i.isItemEqual(itemStack))
                return true;
        }
        return false;
    }
}
