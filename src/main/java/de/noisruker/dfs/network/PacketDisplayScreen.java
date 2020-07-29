package de.noisruker.dfs.network;

import de.noisruker.dfs.objects.screens.SpeciesScreen;
import de.noisruker.dfs.registries.ModSpecies;
import de.noisruker.dfs.species.Species;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class PacketDisplayScreen {

    public PacketDisplayScreen(PacketBuffer buffer) {

    }

    public void toBytes(PacketBuffer buffer) {

    }

    public PacketDisplayScreen() {

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Map.Entry<String, Species>[] speciesToChoose = new Map.Entry[Species.SPECIES.size() - 1];

            int i = 0;

            for(Map.Entry<String, Species> e: Species.SPECIES.entrySet())
                if(!e.getValue().equals(ModSpecies.HUMAN))
                    speciesToChoose[i++] = e;

            Minecraft.getInstance().displayGuiScreen(new SpeciesScreen(speciesToChoose));
        });
        ctx.get().setPacketHandled(true);
    }

}
