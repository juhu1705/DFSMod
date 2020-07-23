package de.noisruker.dfs.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.noisruker.dfs.DfSMod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = DfSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands {

    @SubscribeEvent
    public static void onLoad(FMLServerStartingEvent event) {
        ModCommands.register(event.getCommandDispatcher());
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> cmds = dispatcher.register(Commands.literal(DfSMod.MOD_ID).then(CommandGetSpecies.register(dispatcher)).then(CommandGetAllSpecies.register(dispatcher)).then(CommandSetPower.register(dispatcher)));
    }
}
