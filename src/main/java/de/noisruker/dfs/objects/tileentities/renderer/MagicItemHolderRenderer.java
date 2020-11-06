package de.noisruker.dfs.objects.tileentities.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.noisruker.dfs.objects.tileentities.MagicItemHolderTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

public class MagicItemHolderRenderer extends TileEntityRenderer<MagicItemHolderTileEntity> {

    private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
    private final Random random = new Random();

    public MagicItemHolderRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(MagicItemHolderTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack itemstack = tileEntityIn.getItems().get(0);

        //DfSMod.LOGGER.debug(itemstack);

        if (!itemstack.isEmpty() && itemstack != ItemStack.EMPTY) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5, 1.05, 0.5);
            matrixStackIn.scale(0.5F, 0.5F,0.5F);

            int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getDamage();
            this.random.setSeed((long)i);
            IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, tileEntityIn.getWorld(), (LivingEntity)null);
            boolean flag = ibakedmodel.isGui3d();
            int j = this.getModelCount(itemstack);
            float f = 0.25F;
            float f1 = this.shouldBob() ? MathHelper.sin(((float)tileEntityIn.getAge() + partialTicks) / 10.0F + tileEntityIn.hoverStart) * 0.1F + 0.1F : 0;
            float f2 = ibakedmodel.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.getY();
            matrixStackIn.translate(0.0D, (double)(f1 + 0.25F * f2), 0.0D);
            float f3 = ((float)tileEntityIn.getAge() + partialTicks) / 20.0F + tileEntityIn.hoverStart;
            matrixStackIn.rotate(Vector3f.YP.rotation(f3));
            if (!flag) {
                float f7 = -0.0F * (float)(j - 1) * 0.5F;
                float f8 = -0.0F * (float)(j - 1) * 0.5F;
                float f9 = -0.09375F * (float)(j - 1) * 0.5F;
                matrixStackIn.translate((double)f7, (double)f8, (double)f9);
            }

            for(int k = 0; k < j; ++k) {
                matrixStackIn.push();
                if (k > 0) {
                    if (flag) {
                        float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        matrixStackIn.translate(shouldSpreadItems() ? f11 : 0, shouldSpreadItems() ? f13 : 0, shouldSpreadItems() ? f10 : 0);
                    } else {
                        float f12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        float f14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        matrixStackIn.translate(shouldSpreadItems() ? f12 : 0, shouldSpreadItems() ? f14 : 0, 0.0D);
                    }
                }

                this.itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                matrixStackIn.pop();
                if (!flag) {
                    matrixStackIn.translate(0.0, 0.0, 0.09375F);
                }
            }

            matrixStackIn.pop();
        }
    }

    protected int getModelCount(ItemStack stack) {
        int i = 1;
        if (stack.getCount() > 48) {
            i = 5;
        } else if (stack.getCount() > 32) {
            i = 4;
        } else if (stack.getCount() > 16) {
            i = 3;
        } else if (stack.getCount() > 1) {
            i = 2;
        }

        return i;
    }

    /**
     * @return If items should spread out when rendered in 3D
     */
    public boolean shouldSpreadItems() {
        return true;
    }

    /**
     * @return If items should have a bob effect
     */
    public boolean shouldBob() {
        return true;
    }

}
