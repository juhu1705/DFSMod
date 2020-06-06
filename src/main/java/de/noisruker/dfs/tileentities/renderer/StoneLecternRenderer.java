package de.noisruker.dfs.tileentities.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.blocks.StoneLectern;
import de.noisruker.dfs.tileentities.StoneLecternTileEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

public class StoneLecternRenderer extends TileEntityRenderer<StoneLecternTileEntity> {

    public StoneLecternRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(StoneLecternTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        Direction direction = tileEntityIn.getBlockState().get(StoneLectern.FACING);
        NonNullList<ItemStack> nonnulllist = tileEntityIn.getItems();

        for(int i = 0; i < 3; ++i) {
            ItemStack itemstack = nonnulllist.get(i);

            if (itemstack != ItemStack.EMPTY) {

                if(i == 0) {
                    matrixStackIn.push();
                    matrixStackIn.translate(0.5D, 0.44921875D, 0.5D);

                    Direction direction1 = Direction.byHorizontalIndex((i + direction.getHorizontalIndex()) % 4);
                    float f = -direction1.getHorizontalAngle();

                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
                    matrixStackIn.translate(0D, 0.125D, -0.55D);
                    matrixStackIn.rotate(Vector3f.ZN.rotationDegrees(180.0F));
                    matrixStackIn.rotate(Vector3f.XN.rotationDegrees(20.0F));
                    matrixStackIn.scale(0.75F, 0.75F, 0.75F);

                    Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                    matrixStackIn.pop();
                } else if(i == 1) {
                    matrixStackIn.push();
                    matrixStackIn.translate(0.5D, 0.44921875D, 0.5D);

                    float y_height = MathHelper.sin(Minecraft.getInstance().world.getGameTime());

                    Direction direction1 = Direction.byHorizontalIndex((i + direction.getHorizontalIndex()) % 4);
                    float f = -direction1.getHorizontalAngle();



                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees((Minecraft.getInstance().world.getGameTime() % 360) + 180));

                    matrixStackIn.translate(-0.40D, -0.125D + y_height, 0.25D);

                    matrixStackIn.scale(0.5F, 0.5F, 0.5F);

                    matrixStackIn.rotate(Vector3f.YN.rotationDegrees( Minecraft.getInstance().world.getGameTime() % 90 * 4));

                    Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);

                    matrixStackIn.pop();
                } else if(i == 2) {
                    matrixStackIn.push();
                    matrixStackIn.translate(0.5D, 0.44921875D, 0.5D);

                    float y_height = MathHelper.sin(Minecraft.getInstance().world.getGameTime());

                    Direction direction1 = Direction.byHorizontalIndex((i + direction.getHorizontalIndex()) % 4);
                    float f = -direction1.getHorizontalAngle();



                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(Minecraft.getInstance().world.getGameTime() % 360));

                    matrixStackIn.translate(-0.4D, -0.125D + y_height, 0.25D);

                    matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                    matrixStackIn.rotate(Vector3f.YN.rotationDegrees( Minecraft.getInstance().world.getGameTime() % 90 * 4));

                    Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                    matrixStackIn.pop();
                }

            }
        }
    }


}
