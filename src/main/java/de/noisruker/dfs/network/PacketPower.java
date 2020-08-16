package de.noisruker.dfs.network;

import de.noisruker.dfs.species.PlayerSpeciesEvents;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPower {

    private final float maxPower, power;

    public PacketPower(PacketBuffer buffer) {
        this.power = buffer.readFloat();
        this.maxPower = buffer.readFloat();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeFloat(this.power);
        buffer.writeFloat(this.maxPower);
    }

    public PacketPower(final float power, final float maxPower) {
        this.power = power;
        this.maxPower = maxPower;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerSpeciesEvents.powerAmount = this.power;
            PlayerSpeciesEvents.maxPowerAmount = this.maxPower;
        });
        ctx.get().setPacketHandled(true);
    }

}
