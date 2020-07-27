package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.objects.blocks.*;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DfSMod.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> STONE_PILLAR_BLOCK = BLOCKS.register("stone_pillar_block", StonePillarBlock::new);
    public static final RegistryObject<Block> STONE_LECTERN_BLOCK = BLOCKS.register("stone_lectern_block", StoneLectern::new);
    public static final RegistryObject<Block> ANCIENT_STONE_BLOCK = BLOCKS.register("ancient_stone_block", AncientStoneBlock::new);
    public static final RegistryObject<Block> CRYSTAL_BLOCK = BLOCKS.register("crystal_block", CrystalBlock::new);
    public static final RegistryObject<Block> CHAIN_BLOCK = BLOCKS.register("chain", BlockChain::new);
    public static final RegistryObject<Block> DUNGEON_PORTAL = BLOCKS.register("dungeon_portal", DungeonPortal::new);

}
