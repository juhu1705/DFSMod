package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.objects.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
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
    public static final RegistryObject<Block> ANCIENT_FURNACE = BLOCKS.register("ancient_furnace", BlockAncientFurnace::new);
    public static final RegistryObject<Block> MAGIC_ORE_BLOCK = BLOCKS.register("magic_ore_block",
            () -> new Block(Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(6.0f, 10.0f)
                    .sound(SoundType.STONE)
                    .harvestLevel(3)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> MAGIC_BLOCK = BLOCKS.register("magic_block",
            () -> new Block(Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(3.0f, 5.0f)
                    .sound(SoundType.STONE)
                    .harvestLevel(1)
                    .harvestTool(ToolType.PICKAXE)));

}
