package de.noisruker.dfs.objects.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.objects.containers.AncientFurnaceContainer;
import net.minecraft.client.gui.recipebook.AbstractRecipeBookGui;
import net.minecraft.client.gui.recipebook.FurnaceRecipeGui;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AncientFurnaceScreen extends ContainerScreen<AncientFurnaceContainer> implements IRecipeShownListener {
    private static final ResourceLocation field_214089_l = new ResourceLocation("textures/gui/recipe_button.png");
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(DfSMod.MOD_ID, "textures/gui/container/ancient_furnace.png");
    public final AbstractRecipeBookGui recipeGui;
    private boolean widthTooNarrowIn;
    private final ResourceLocation guiTexture;

    public AncientFurnaceScreen(AncientFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent field_230704_d_In) {
        this(screenContainer, new FurnaceRecipeGui(), inv, field_230704_d_In, GUI_TEXTURE);
    }

    public AncientFurnaceScreen(AncientFurnaceContainer screenContainer, AbstractRecipeBookGui recipeGuiIn, PlayerInventory inv, ITextComponent field_230704_d_In, ResourceLocation guiTextureIn) {
        super(screenContainer, inv, field_230704_d_In);
        this.recipeGui = recipeGuiIn;
        this.guiTexture = guiTextureIn;
    }

    public void init() {
        super.init();
        this.widthTooNarrowIn = this.width < 379;
        this.recipeGui.init(this.width, this.height, this.getMinecraft(), this.widthTooNarrowIn, this.container);
        this.guiLeft = this.recipeGui.updateScreenPosition(this.widthTooNarrowIn, this.width, this.xSize);
        this.addButton((new ImageButton(this.guiLeft + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, field_214089_l, (p_214087_1_) -> {
            this.recipeGui.initSearchBar(this.widthTooNarrowIn);
            this.recipeGui.toggleVisibility();
            this.guiLeft = this.recipeGui.updateScreenPosition(this.widthTooNarrowIn, this.width, this.xSize);
            ((ImageButton)p_214087_1_).setPosition(this.guiLeft + 20, this.height / 2 - 49);
        })));
    }

    public void tick() {
        super.tick();
        this.recipeGui.tick();
    }

    public void render(MatrixStack stack, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground(stack);
        if (this.recipeGui.isVisible() && this.widthTooNarrowIn) {
            this.drawGuiContainerBackgroundLayer(stack, p_render_3_, p_render_1_, p_render_2_);
            this.recipeGui.render(stack, p_render_1_, p_render_2_, p_render_3_);
        } else {
            this.recipeGui.render(stack, p_render_1_, p_render_2_, p_render_3_);
            super.render(stack, p_render_1_, p_render_2_, p_render_3_);
            this.recipeGui.func_230477_a_(stack, this.guiLeft, this.guiTop, true, p_render_3_);
        }

        super.renderHoveredTooltip(stack, p_render_1_, p_render_2_);
        this.recipeGui.func_238924_c_(stack, this.guiLeft, this.guiTop, p_render_1_, p_render_2_);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        String s = getTitle().getString();
        this.font.drawString(stack, s, (float)(this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 4210752);
        this.font.drawString(stack, this.playerInventory.getDisplayName().getString(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.getMinecraft().getTextureManager().bindTexture(this.guiTexture);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(stack, i, j, 0, 0, this.xSize, this.ySize);
        if (((AncientFurnaceContainer)this.container).isBurning()) {
            int k = ((AncientFurnaceContainer)this.container).getBurnLeftScaled();
            this.blit(stack, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = ((AncientFurnaceContainer)this.container).getCookProgressionScaled();
        this.blit(stack, i + 79, j + 34, 176, 14, l + 1, 16);

        if(((AncientFurnaceContainer)this.container).hasPower()) {
            int k = ((AncientFurnaceContainer) this.container).getPowerLeftScaled();
            this.blit(stack, i + 55, j + 52 + 18 - k, 176, 18 + 31 - k, 18, k + 1);
        }
    }

    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if (this.recipeGui.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
            return true;
        } else {
            return this.widthTooNarrowIn && this.recipeGui.isVisible() || super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
        }
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        this.recipeGui.slotClicked(slotIn);
    }

    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return !this.recipeGui.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) && super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.xSize) || mouseY >= (double)(guiTopIn + this.ySize);
        return this.recipeGui.func_195604_a(mouseX, mouseY, this.guiLeft, this.guiTop, this.xSize, this.ySize, mouseButton) && flag;
    }

    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        return this.recipeGui.charTyped(p_charTyped_1_, p_charTyped_2_) || super.charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    public void recipesUpdated() {
        this.recipeGui.recipesUpdated();
    }

    public RecipeBookGui getRecipeGui() {
        return this.recipeGui;
    }

    @Override
    public void closeScreen() {
        this.recipeGui.removed();
        super.closeScreen();
    }
}