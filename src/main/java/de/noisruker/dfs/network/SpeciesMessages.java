package de.noisruker.dfs.network;

import de.noisruker.dfs.DfSMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class SpeciesMessages {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(DfSMod.MOD_ID, "species"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private static int ID;

    public static int getNextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(getNextID(), PacketSetSpecies.class, PacketSetSpecies::toBytes, PacketSetSpecies::new, PacketSetSpecies::handle);
    }

}
