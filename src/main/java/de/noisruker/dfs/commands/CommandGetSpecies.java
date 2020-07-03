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

public class CommandGetSpecies implements Command<CommandSource> {

    public static final CommandGetSpecies CGS = new CommandGetSpecies();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("species").requires(cs -> cs.hasPermissionLevel(0)).executes(CGS);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(new StringTextComponent("Your Species: " + PlayerSpecies.getOrCreatePlayer(context.getSource().asPlayer()).getSpecies().toString()), false);

        return 0;
    }
}
