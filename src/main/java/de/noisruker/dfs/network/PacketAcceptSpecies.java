package de.noisruker.dfs.network;

import de.noisruker.dfs.species.PlayerSpeciesEvents;
import de.noisruker.dfs.species.Species;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAcceptSpecies {
    private final String species;

    public PacketAcceptSpecies(PacketBuffer buffer) {
        this.species = buffer.readString();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(species);
    }

    public PacketAcceptSpecies(String species) {
        this.species = species;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Species s = Species.SPECIES.get(species);

            if(s == null)
                return;

            PlayerSpeciesEvents.species = s;

            PlayerEntity player = Minecraft.getInstance().player;
        });
        ctx.get().setPacketHandled(true);
    }
}
