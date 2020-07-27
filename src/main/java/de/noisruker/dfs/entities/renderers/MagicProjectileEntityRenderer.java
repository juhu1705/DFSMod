package de.noisruker.dfs.entities.renderers;

import de.noisruker.dfs.entities.MagicProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class MagicProjectileEntityRenderer extends SpriteRenderer<MagicProjectileEntity> {

    public MagicProjectileEntityRenderer(EntityRendererManager renderManagerIn, ItemRenderer itemRenderer) {
        super(renderManagerIn, itemRenderer);
    }

    public static class Factory implements IRenderFactory<MagicProjectileEntity> {

        @Override
        public EntityRenderer<? super MagicProjectileEntity> createRenderFor(EntityRendererManager manager) {
            return new MagicProjectileEntityRenderer(manager, Minecraft.getInstance().getItemRenderer());
        }

    }
}
