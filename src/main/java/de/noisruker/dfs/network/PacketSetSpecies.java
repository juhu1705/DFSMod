package de.noisruker.dfs.network;

import de.noisruker.dfs.species.PlayerSpecies;
import de.noisruker.dfs.species.Species;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSetSpecies {

    private final String species;

    public PacketSetSpecies(PacketBuffer buffer) {
        this.species = buffer.readString();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(species);
    }

    public PacketSetSpecies(String species) {
        this.species = species;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if(player == null)
                return;
            PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(player);
            Species s = Species.SPECIES.get(species);

            if(s == null)
                return;

            playerSpecies.setNewSpecies(s);
        });
        ctx.get().setPacketHandled(true);
    }

}
