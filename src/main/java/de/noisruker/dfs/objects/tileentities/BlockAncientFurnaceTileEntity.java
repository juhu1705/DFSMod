package de.noisruker.dfs.objects.tileentities;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.BlastFurnaceContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BlockAncientFurnaceTileEntity extends AbstractFurnaceTileEntity {

    public BlockAncientFurnaceTileEntity() {
        super(TileEntityType.BLAST_FURNACE, IRecipeType.SMELTING);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.ancient_furnace");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new BlastFurnaceContainer(id, player, this, this.furnaceData);
    }
}
