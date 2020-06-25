package de.noisruker.dfs.registries;

import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.tileentities.StoneLecternTileEntity;
import de.noisruker.dfs.tileentities.renderer.StoneLecternRenderer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, DfSMod.MOD_ID);

    public static final RegistryObject<TileEntityType<StoneLecternTileEntity>> STONE_LECTERN = TILE_ENTITY_TYPES.register("stone_lectern", () -> TileEntityType.Builder.create(StoneLecternTileEntity::new, ModBlocks.STONE_LECTERN_BLOCK.get()).build(null));


    public static void bindSpecialRenderers() {
        ClientRegistry.bindTileEntityRenderer(STONE_LECTERN.get(), StoneLecternRenderer::new);
    }

}
