package de.noisruker.dfs.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.noisruker.dfs.species.PlayerSpecies;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import java.util.Map;
import java.util.UUID;

public class CommandGetAllSpecies implements Command<CommandSource> {

    public static final CommandGetAllSpecies CGS = new CommandGetAllSpecies();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("all_species").requires(cs -> cs.hasPermissionLevel(1)).executes(CGS);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(context.getSource().asPlayer());

        if(species == null)
            context.getSource().sendFeedback(new StringTextComponent("No species found for you!"), false);
        else
            context.getSource().sendFeedback(new StringTextComponent("Your Species: " + species.getSpecies().toString()), false);

        for(Map.Entry<UUID, PlayerSpecies> entry: PlayerSpecies.getMap().entrySet()) {
            context.getSource().sendFeedback(new StringTextComponent(entry.getValue().getPlayer().getName().getFormattedText() + ": " + entry.getValue().getSpecies().toString()), false);
            //context.getSource().sendFeedback(entry.getValue().getPlayer().getDisplayNameAndUUID(), false);
        }

        return 0;
    }
}
