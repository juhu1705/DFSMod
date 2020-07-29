package de.noisruker.dfs.network;

import de.noisruker.dfs.registries.ModActiveSpeciesAbility;
import de.noisruker.dfs.registries.ModSpecies;
import de.noisruker.dfs.species.PlayerSpecies;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketActiveAbility {

    public PacketActiveAbility(PacketBuffer buffer) {

    }

    public void toBytes(PacketBuffer buffer) {

    }

    public PacketActiveAbility() {

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if(player == null)
                return;
            PlayerSpecies playerSpecies = PlayerSpecies.getOrCreatePlayer(player);

            if(playerSpecies == null || playerSpecies.getSpecies().equals(ModSpecies.HUMAN))
                return;

            ModActiveSpeciesAbility.ABILITIES.get(playerSpecies.getSpecies()).process(playerSpecies);

        });
        ctx.get().setPacketHandled(true);
    }

}
