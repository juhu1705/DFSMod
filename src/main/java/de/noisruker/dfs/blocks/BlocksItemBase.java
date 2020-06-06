package de.noisruker.dfs.blocks;

import de.noisruker.dfs.DfSMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlocksItemBase extends BlockItem {

    public BlocksItemBase(Block block) {
        super(block, new Item.Properties().group(DfSMod.TAB_BLOCKS));
    }

}
