package de.noisruker.dfs.objects.tileentities;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.noisruker.dfs.objects.containers.AncientFurnaceContainer;
import de.noisruker.dfs.registries.ModRecipeTypes;
import de.noisruker.dfs.registries.ModSpecies;
import de.noisruker.dfs.registries.ModTileEntityTypes;
import de.noisruker.dfs.species.PlayerSpecies;
import de.noisruker.dfs.species.PlayerSpeciesEvents;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class BlockAncientFurnaceTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity, IMagicTileEntity {
    private static final int[] SLOTS_UP = new int[]{0};
    private static final int[] SLOTS_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_HORIZONTAL = new int[]{1};
    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    private float power = 0;
    private float maxPower = 100;

    private int burnTime;
    private int recipesUsed;
    private int cookTime;
    private int cookTimeTotal;
    public final IIntArray furnaceData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return BlockAncientFurnaceTileEntity.this.burnTime;
                case 1:
                    return BlockAncientFurnaceTileEntity.this.recipesUsed;
                case 2:
                    return BlockAncientFurnaceTileEntity.this.cookTime;
                case 3:
                    return BlockAncientFurnaceTileEntity.this.cookTimeTotal;
                case 4:
                    return (int)(BlockAncientFurnaceTileEntity.this.power * 100f);
                case 5:
                    return (int)(BlockAncientFurnaceTileEntity.this.maxPower * 100f);
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    BlockAncientFurnaceTileEntity.this.burnTime = value;
                    break;
                case 1:
                    BlockAncientFurnaceTileEntity.this.recipesUsed = value;
                    break;
                case 2:
                    BlockAncientFurnaceTileEntity.this.cookTime = value;
                    break;
                case 3:
                    BlockAncientFurnaceTileEntity.this.cookTimeTotal = value;
                    break;
                case 4:
                    BlockAncientFurnaceTileEntity.this.power = value;
                    break;
                case 5:
                    BlockAncientFurnaceTileEntity.this.maxPower = value;
                    break;

            }

        }

        public int size() {
            return 6;
        }
    };
    private final Map<ResourceLocation, Integer> field_214022_n = Maps.newHashMap();
    protected final IRecipeType<? extends AbstractCookingRecipe> recipeType;
    protected final IRecipeType<? extends AbstractCookingRecipe> magicFurnaceRecipeType;

    protected BlockAncientFurnaceTileEntity(TileEntityType<?> tileTypeIn, IRecipeType<? extends AbstractCookingRecipe> recipeTypeIn) {
        super(tileTypeIn);
        this.recipeType = recipeTypeIn;
        this.magicFurnaceRecipeType = ModRecipeTypes.ANCIENT_FURNACE_RECIPE;
    }

    public BlockAncientFurnaceTileEntity() {
        this(ModTileEntityTypes.ANCIENT_FURNACE.get(), IRecipeType.SMELTING);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void read(CompoundNBT compound) {
        super.read(compound);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.items);
        this.burnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.recipesUsed = this.getBurnTime(this.items.get(1));
        this.maxPower = compound.getFloat("MaxPower");
        this.power = compound.getFloat("Power");
        int i = compound.getShort("RecipesUsedSize");

        for(int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = new ResourceLocation(compound.getString("RecipeLocation" + j));
            int k = compound.getInt("RecipeAmount" + j);
            this.field_214022_n.put(resourcelocation, k);
        }

    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.putFloat("Power", this.power);
        compound.putFloat("MaxPower", this.maxPower);
        ItemStackHelper.saveAllItems(compound, this.items);
        compound.putShort("RecipesUsedSize", (short)this.field_214022_n.size());
        int i = 0;

        for(Map.Entry<ResourceLocation, Integer> entry : this.field_214022_n.entrySet()) {
            compound.putString("RecipeLocation" + i, entry.getKey().toString());
            compound.putInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }

        return compound;
    }

    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!this.world.isRemote) {
            ItemStack itemstack = this.items.get(1);
            if (this.isBurning() || this.hasPower() && !this.items.get(0).isEmpty()) {
                IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>)this.recipeType, this, this.world).orElse(this.world.getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>)this.magicFurnaceRecipeType, this, this.world).orElse(null));
                if (!this.isBurning() && this.canSmelt(irecipe)) {
                    if(this.usePower(10))
                        this.burnTime = 100;
                    else {
                        this.burnTime = (int) (this.getPower() * 10f);
                        this.setPower(0f);
                    }
                    this.recipesUsed = this.burnTime;
                }

                if (this.isBurning() && this.canSmelt(irecipe)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.smelt(irecipe);
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 1, 0, this.cookTimeTotal);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
            }
        }

        if (flag1) {
            this.markDirty();
        }

    }

    protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
        if (!this.items.get(0).isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.getRecipeOutput();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.items.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    private void smelt(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = this.items.get(2);
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (!this.world.isRemote) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
                this.items.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    protected int getBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(fuel);
        }
    }

    protected int getCookTime() {
        if(this.world != null)
            return this.world.getRecipeManager()
                    .getRecipe((IRecipeType<AbstractCookingRecipe>)this.recipeType, this, this.world)
                    .map(AbstractCookingRecipe::getCookTime).orElse(this.world.getRecipeManager()
                            .getRecipe((IRecipeType<AbstractCookingRecipe>)this.magicFurnaceRecipeType, this, this.world)
                            .map(AbstractCookingRecipe::getCookTime).orElse(200));
        return 200 / 2;
    }

    public static boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            Item item = stack.getItem();
            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.markDirty();
        }

    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
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

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 2) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            ItemStack itemstack = this.items.get(1);
            return isFuel(stack) || stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET;
        }
    }

    public void clear() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            this.field_214022_n.compute(recipe.getId(), (p_214004_0_, p_214004_1_) -> {
                return 1 + (p_214004_1_ == null ? 0 : p_214004_1_);
            });
        }

    }

    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void onCrafting(PlayerEntity player) {
    }

    public void func_213995_d(PlayerEntity player) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for(Map.Entry<ResourceLocation, Integer> entry : this.field_214022_n.entrySet()) {
            player.world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((p_213993_3_) -> {
                list.add(p_213993_3_);
                spawnExpOrbs(player, entry.getValue(), ((FurnaceRecipe)p_213993_3_).getExperience());
            });
        }

        player.unlockRecipes(list);
        this.field_214022_n.clear();
    }

    private static void spawnExpOrbs(PlayerEntity player, int p_214003_1_, float experience) {
        if (experience == 0.0F) {
            p_214003_1_ = 0;
        } else if (experience < 1.0F) {
            int i = MathHelper.floor((float)p_214003_1_ * experience);
            if (i < MathHelper.ceil((float)p_214003_1_ * experience) && Math.random() < (double)((float)p_214003_1_ * experience - (float)i)) {
                ++i;
            }

            p_214003_1_ = i;
        }

        while(p_214003_1_ > 0) {
            int j = ExperienceOrbEntity.getXPSplit(p_214003_1_);
            p_214003_1_ -= j;
            player.world.addEntity(new ExperienceOrbEntity(player.world, player.getPosX(), player.getPosY() + 0.5D, player.getPosZ() + 0.5D, j));
        }

    }

    public void fillStackedContents(RecipeItemHelper helper) {
        for(ItemStack itemstack : this.items) {
            helper.accountStack(itemstack);
        }

    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
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

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.ancient_furnace");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new AncientFurnaceContainer(id, player, this);
    }

    @Override
    public void setPower(float power) {
        if(power < this.maxPower)
            this.power = power;
        else
            this.power = this.maxPower;

        if(this.power < 0)
            this.power = 0;
    }

    @Override
    public float getPower() {
        return this.power;
    }

    @Override
    public void setMaxPower(float power) {
        if(this.maxPower < 0)
            return;
        this.maxPower = power;
        if(this.power > this.maxPower)
            this.power = this.maxPower;
    }

    @Override
    public float getMaxPower() {
        return this.maxPower;
    }

    @Override
    public float addPower(float power) {
        if(this.power + power > this.maxPower) {
            float dif = this.power + power - this.maxPower;
            this.setPower(this.maxPower);
            return dif;
        }
        this.setPower(this.getPower() + power);
        return 0;
    }

    @Override
    public boolean usePower(float power) {
        if(this.power >= power) {
            this.setPower(this.getPower() - power);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPower() {
        return this.getPower() > 0;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }
}
