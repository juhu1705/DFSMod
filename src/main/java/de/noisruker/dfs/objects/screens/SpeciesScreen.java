package de.noisruker.dfs.objects.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.network.PacketSetSpecies;
import de.noisruker.dfs.network.SpeciesMessages;
import de.noisruker.dfs.species.Species;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SpeciesScreen extends Screen {

    public static final ResourceLocation SCREEN_TEXTURE = new ResourceLocation(DfSMod.MOD_ID, "textures/screen/choose_species_window.png");

    public final IteratableOption species_optioned;

    public int speciesPosition = 0;

    public final Map.Entry<String, Species>[] species;

    public SpeciesScreen(Map.Entry<String, Species>... species) {
        super(new TranslationTextComponent(""));

        this.species = species;

        species_optioned = new IteratableOption("options.particles", (p_216622_0_, p_216622_1_) -> {
            p_216622_0_.particles = ParticleStatus.byId(p_216622_0_.particles.getId() + p_216622_1_);
        }, (p_216616_0_, p_216616_1_) -> {
            return ITextComponent.getTextComponentOrEmpty(p_216616_1_.getName(Minecraft.getInstance().gameSettings).getString() + I18n.format(p_216616_0_.particles.getResourceKey()));
        });
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        super.addButton(new Button(this.width / 2 + 80, this.height / 6 + 70, 20, 20, ITextComponent.getTextComponentOrEmpty(">"), (event) -> {
            speciesPosition++;
            if(speciesPosition >= species.length)
                speciesPosition = 0;
        }));
        super.addButton(new Button(this.width / 2 - 50, this.height / 6 + 150, 100, 20, ITextComponent.getTextComponentOrEmpty(I18n.format("screen.dfssul.species.finish")), (event) -> {
            SpeciesMessages.INSTANCE.sendToServer(new PacketSetSpecies(species[speciesPosition].getKey()));

            this.closeScreen();
        }));
        super.addButton(new Button(this.width / 2 - 100, this.height / 6 + 70, 20, 20, ITextComponent.getTextComponentOrEmpty("<"), (event) -> {
            speciesPosition--;
            if(speciesPosition < 0)
                speciesPosition = species.length - 1;
        }));
    }

    @Override
    public void render(MatrixStack stack, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground(stack);
        super.render(stack, p_render_1_, p_render_2_, p_render_3_);

        String name = I18n.format("screen.dfssul.species." + species[speciesPosition].getKey());
        String description = I18n.format("screen.dfssul.species." + species[speciesPosition].getKey() + ".description");

        this.font.drawString(stack, name, (float)(super.width / 2 - this.font.getStringWidth(name) / 2), (float)super.height / 2 - 75, 0);
        this.font.func_238418_a_(ITextComponent.getTextComponentOrEmpty(description), super.width / 2 - 60, super.height / 2 - 60, 120, 0);
    }

    @Override
    public void renderBackground(MatrixStack stack) {
        super.renderBackground(stack);



        Color color = new Color(species[speciesPosition].getValue().getColor());

        RenderSystem.color4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f);

        this.minecraft.getTextureManager().bindTexture(SCREEN_TEXTURE);

        this.blit(stack, (super.width - 256) / 2, (super.height - 258) / 2, 0, 0, 256, 256);

        RenderSystem.clearCurrentColor();

        //this.minecraft.getTextureManager().bindTexture(new ResourceLocation(DfSMod.MOD_ID, "textures/screen/species/" + species[speciesPosition].getKey() + ".png"));

        //this.blit((super.width - 128) / 2, (super.height - 128) / 2, 0, 0, 128, 128);

    }
}
