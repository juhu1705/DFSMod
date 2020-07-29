package de.noisruker.dfs.objects.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import de.noisruker.dfs.DfSMod;
import de.noisruker.dfs.network.PacketSetSpecies;
import de.noisruker.dfs.network.SpeciesMessages;
import de.noisruker.dfs.species.Species;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SpeciesScreen extends Screen {

    public static final ResourceLocation SCREEN_TEXTURE = new ResourceLocation(DfSMod.MOD_ID, "textures/screen/choose_species_window.png");

    public int speciesPosition = 0;

    public final Map.Entry<String, Species>[] species;

    public SpeciesScreen(Map.Entry<String, Species>... species) {
        super(new TranslationTextComponent(""));

        this.species = species;
    }

    @Override
    protected void init() {
        super.addButton(new Button(this.width / 2 + 40, this.height / 6 + 70, 100, 20, I18n.format("screen.dfssul.species.continue"), (event) -> {
            speciesPosition++;
            if(speciesPosition >= species.length)
                speciesPosition = 0;
        }));
        super.addButton(new Button(this.width / 2 - 50, this.height / 6 + 150, 100, 20, I18n.format("screen.dfssul.species.finish"), (event) -> {
            SpeciesMessages.INSTANCE.sendToServer(new PacketSetSpecies(species[speciesPosition].getKey()));

            this.onClose();
        }));
        super.addButton(new Button(this.width / 2 - 140, this.height / 6 + 70, 100, 20, I18n.format("screen.dfssul.species.back"), (event) -> {
            speciesPosition--;
            if(speciesPosition < 0)
                speciesPosition = species.length - 1;
        }));
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);

        this.drawCenteredString(this.font, I18n.format("screen.dfssul.species." + species[speciesPosition].getKey()), super.width / 2, super.height / 2 - 75, 0);
    }

    @Override
    public void renderBackground() {
        super.renderBackground();



        Color color = new Color(species[speciesPosition].getValue().getColor());

        RenderSystem.color4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f);

        this.minecraft.getTextureManager().bindTexture(SCREEN_TEXTURE);

        this.blit((super.width - 256) / 2, (super.height - 258) / 2, 0, 0, 256, 256);

        RenderSystem.clearCurrentColor();

        this.minecraft.getTextureManager().bindTexture(new ResourceLocation(DfSMod.MOD_ID, "textures/screen/species/" + species[speciesPosition].getKey() + ".png"));

        this.blit((super.width - 128) / 2, (super.height - 128) / 2, 0, 0, 128, 128);

    }
}
