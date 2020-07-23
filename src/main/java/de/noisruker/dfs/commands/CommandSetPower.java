package de.noisruker.dfs.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.noisruker.dfs.species.PlayerSpecies;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class CommandSetPower implements Command<CommandSource> {

    public static final CommandSetPower CGS = new CommandSetPower();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("set_power").requires(cs -> cs.hasPermissionLevel(2)).then(Commands.argument("power", FloatArgumentType.floatArg(1f, 1000000000f)).executes(CGS));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Float f = context.getArgument("power", Float.class);
        PlayerSpecies species = PlayerSpecies.getOrCreatePlayer(context.getSource().asPlayer());
        if(species == null) {
            context.getSource().sendFeedback(new StringTextComponent("Unable to set power to " + f.toString()), true);
            return 0;
        }
        species.setPower(f);
        context.getSource().sendFeedback(new StringTextComponent("Successfully set power to " + species.getPower()), true);

        return 0;
    }
}
