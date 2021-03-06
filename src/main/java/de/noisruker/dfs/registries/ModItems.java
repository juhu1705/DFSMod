package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.objects.blocks.BlocksItemBase;
import de.noisruker.dfs.objects.entities.MagicProjectileEntity;
import de.noisruker.dfs.objects.items.ItemBase;
import de.noisruker.dfs.objects.items.ItemSpawnEggSoul;
import de.noisruker.dfs.objects.items.LocateGiantTreeItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DfSMod.MOD_ID);

    // Items
    public static final RegistryObject<Item> ANCIENT_THING = ITEMS.register("ancient_paper", ItemBase::new);
    public static final RegistryObject<Item> ANCIENT_SHARD = ITEMS.register("ancient_shard", ItemBase::new);
    public static final RegistryObject<Item> ANCIENT_STONE_RUNE = ITEMS.register("ancient_stone_rune", ItemBase::new);
    public static final RegistryObject<Item> MAGIC_PROJECTILE = ITEMS.register("magic_projectile", () -> new ItemBase() {
        @Override
        public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            if (!worldIn.isRemote) {
                MagicProjectileEntity magicProjectileEntity = new MagicProjectileEntity(worldIn, playerIn);
                magicProjectileEntity.setItem(itemstack);
                magicProjectileEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 0);

                magicProjectileEntity.setPower(10f);

                worldIn.addEntity(magicProjectileEntity);
            }

            playerIn.addStat(Stats.ITEM_USED.get(this));
            if (!playerIn.abilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            return ActionResult.resultSuccess(itemstack);
        }
    });
    public static final RegistryObject<Item> MAGIC_SHARD = ITEMS.register("magic_shard", () -> new ItemBase() {
        @Override
        public int getBurnTime(ItemStack itemStack) {
            return 5000;
        }
    });
    public static final RegistryObject<Item> GIANT_TREE_COMPASS = ITEMS.register("giant_tree_compass", LocateGiantTreeItem::new);

    //Block Items
    public static final RegistryObject<Item> STONE_PILLAR_ITEM = ITEMS.register("stone_pillar_block", () -> new BlocksItemBase(ModBlocks.STONE_PILLAR_BLOCK.get()));
    public static final RegistryObject<Item> STONE_LECTERN_ITEM = ITEMS.register("stone_lectern_block", () -> new BlocksItemBase(ModBlocks.STONE_LECTERN_BLOCK.get()));
    public static final RegistryObject<Item> ANCIENT_STONE_ITEM = ITEMS.register("ancient_stone_block", () -> new BlocksItemBase(ModBlocks.ANCIENT_STONE_BLOCK.get()));
    public static final RegistryObject<Item> CRYSTAL_ITEM = ITEMS.register("crystal_block", () -> new BlocksItemBase(ModBlocks.CRYSTAL_BLOCK.get()));
    public static final RegistryObject<Item> CHAIN_ITEM = ITEMS.register("chain", () -> new BlocksItemBase(ModBlocks.CHAIN_BLOCK.get()));
    public static final RegistryObject<Item> DUNGEON_PORTAL_ITEM = ITEMS.register("dungeon_portal", () -> new BlocksItemBase(ModBlocks.DUNGEON_PORTAL.get()));
    public static final RegistryObject<Item> MAGIC_ORE_BLOCK_ITEM = ITEMS.register("magic_ore_block", () -> new BlocksItemBase(ModBlocks.MAGIC_ORE_BLOCK.get()));
    public static final RegistryObject<Item> MAGIC_BLOCK_ITEM = ITEMS.register("magic_block", () -> new BlocksItemBase(ModBlocks.MAGIC_BLOCK.get()));
    public static final RegistryObject<Item> ANCIENT_FURNACE_ITEM = ITEMS.register("ancient_furnace", () -> new BlocksItemBase(ModBlocks.ANCIENT_FURNACE.get()));
    public static final RegistryObject<Item> LEVITATOR_ITEM = ITEMS.register("levitator_block", () -> new BlocksItemBase(ModBlocks.LEVITATOR.get()));

    //Spawn Eggs
    public static final RegistryObject<Item> SPAWN_SOUL = ITEMS.register("soul_spawn_egg", ItemSpawnEggSoul::new);
}
