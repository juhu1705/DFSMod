package de.noisruker.dfs.objects.containers;

import com.google.common.collect.Lists;
import de.noisruker.dfs.objects.tileentities.BlockAncientFurnaceTileEntity;
import de.noisruker.dfs.registries.ModContainerTypes;
import de.noisruker.dfs.registries.ModRecipeTypes;
import de.noisruker.dfs.registries.ModTileEntityTypes;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Objects;

public class AncientFurnaceContainer extends RecipeBookContainer<IInventory> {
    private final IInventory furnaceInventory;
    private final IIntArray furnaceData;
    protected final World world;
    private final IRecipeType<? extends AbstractCookingRecipe> recipeType;
    private final IRecipeType<? extends AbstractCookingRecipe> magicFurnaceRecipeType;

    protected AncientFurnaceContainer(int p_i50098_1_, PlayerInventory p_i50098_2_, IInventory p_i50098_3_, IIntArray p_i50098_4_) {
        this(ModContainerTypes.ANCIENT_FURNACE_CONTAINER.get(), IRecipeType.SMELTING, ModRecipeTypes.ANCIENT_FURNACE_RECIPE, p_i50098_1_, p_i50098_2_, p_i50098_3_, p_i50098_4_);
    }

    protected AncientFurnaceContainer(ContainerType<?> containerTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn, IRecipeType<? extends AbstractCookingRecipe> magicFurnaceRecipeTypeIn, int id, PlayerInventory playerInventoryIn, IInventory furnaceInventoryIn, IIntArray furnaceDataIn) {
        super(containerTypeIn, id);
        this.recipeType = recipeTypeIn;
        this.magicFurnaceRecipeType = magicFurnaceRecipeTypeIn;
        assertInventorySize(furnaceInventoryIn, 3);
        assertIntArraySize(furnaceDataIn, 4);
        this.furnaceInventory = furnaceInventoryIn;
        this.furnaceData = furnaceDataIn;
        this.world = playerInventoryIn.player.world;
        this.addSlot(new Slot(furnaceInventoryIn, 0, 56, 17));
        this.addSlot(new Slot(furnaceInventoryIn, 1, -1000, -1000));

        this.addSlot(new FurnaceResultSlot(playerInventoryIn.player, furnaceInventoryIn, 2, 116, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
        }

        this.trackIntArray(furnaceDataIn);
    }

    public static BlockAncientFurnaceTileEntity getTileEntity(final PlayerInventory inv, final PacketBuffer data) {
        Objects.requireNonNull(inv, "inv cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tile = inv.player.world.getTileEntity(data.readBlockPos());
        if(tile instanceof BlockAncientFurnaceTileEntity)
            return (BlockAncientFurnaceTileEntity) tile;
        throw new IllegalStateException("Tile entity is not correct! " + tile);
    }

    public AncientFurnaceContainer(final int p_i50097_1_, final PlayerInventory p_i50097_2_, final BlockAncientFurnaceTileEntity p_i50097_3_) {
        this(p_i50097_1_, p_i50097_2_, p_i50097_3_, p_i50097_3_.furnaceData);
    }

    public AncientFurnaceContainer(int p_i50097_1_, PlayerInventory p_i50097_2_, PacketBuffer p_i50097_3_) {
        this(p_i50097_1_, p_i50097_2_, ModTileEntityTypes.ANCIENT_FURNACE.get().create());
    }

    public void fillStackedContents(RecipeItemHelper itemHelperIn) {
        if (this.furnaceInventory instanceof IRecipeHelperPopulator) {
            ((IRecipeHelperPopulator)this.furnaceInventory).fillStackedContents(itemHelperIn);
        }

    }

    public void clear() {
        this.furnaceInventory.clear();
    }

    public void func_217056_a(boolean p_217056_1_, IRecipe<?> p_217056_2_, ServerPlayerEntity p_217056_3_) {
        (new ServerRecipePlacerFurnace<>(this)).place(p_217056_3_, (IRecipe<IInventory>)p_217056_2_, p_217056_1_);
    }

    public boolean matches(IRecipe<? super IInventory> recipeIn) {
        return recipeIn.matches(this.furnaceInventory, this.world);
    }

    public int getOutputSlot() {
        return 2;
    }

    public int getWidth() {
        return 1;
    }

    public int getHeight() {
        return 1;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 3;
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.furnaceInventory.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (this.hasRecipe(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    protected boolean hasRecipe(ItemStack stack) {
        return this.world.getRecipeManager().getRecipe((IRecipeType)this.recipeType, new Inventory(stack), this.world).isPresent() || this.world.getRecipeManager().getRecipe((IRecipeType)this.magicFurnaceRecipeType, new Inventory(stack), this.world).isPresent();
    }

    protected boolean isFuel(ItemStack stack) {
        return AbstractFurnaceTileEntity.isFuel(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.furnaceData.get(2);
        int j = this.furnaceData.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int i = this.furnaceData.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.furnaceData.get(0) * 13 / i;
    }

    @OnlyIn(Dist.CLIENT)
    public int getPowerLeftScaled() {
        int i = this.furnaceData.get(5);
        if (i == 0) {
            i = 200;
        }

        return this.furnaceData.get(4) * 18 / i;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.furnaceData.get(0) > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasPower() {
        return this.furnaceData.get(4) > 0;
    }

    @Override
    public List<RecipeBookCategories> getRecipeBookCategories() {
        return Lists.newArrayList(RecipeBookCategories.FURNACE_SEARCH, RecipeBookCategories.FURNACE_FOOD, RecipeBookCategories.FURNACE_BLOCKS, RecipeBookCategories.FURNACE_MISC);
    }
}
