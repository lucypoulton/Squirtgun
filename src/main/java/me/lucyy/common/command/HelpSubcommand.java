package me.lucyy.common.command;

import me.lucyy.common.format.TextFormatter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A builtin subcommand that will print the plugin name, version, author, and a
 * list of subcommands with their descriptions.
 */
public class HelpSubcommand implements Subcommand {

    private final Command cmd;
    private final FormatProvider provider;
    private final JavaPlugin plugin;
    private final String commandName;

    public HelpSubcommand(Command cmd, FormatProvider provider, JavaPlugin plugin, String commandName) {
        this.cmd = cmd;
        this.provider = provider;
        this.plugin = plugin;
        this.commandName = commandName;
    }


    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "List all subcommands for the plugin.";
    }

    @Override
    public String getUsage() {
        return "help";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean execute(CommandSender sender, CommandSender target, String[] args) {
        StringBuilder output = new StringBuilder();

        output.append(TextFormatter.formatTitle("Commands:", provider)).append("\n");
        cmd.getUserSubcommands(sender).forEach(cmd -> {
                    if (cmd.getPermission() == null || sender.hasPermission(cmd.getPermission()))
                        output.append(provider.formatMain("/" + commandName + " "))
                                .append(provider.formatAccent(cmd.getName()))
                                .append(provider.formatMain(" - " + cmd.getDescription()))
								.append("\n");
                }
        );
		output.append("\n").append(TextFormatter.formatTitle(plugin.getName() + " v"
                + plugin.getDescription().getVersion() + " by "
                + plugin.getDescription().getAuthors().get(0), provider));
		sender.sendMessage(output.toString());
        return true;
    }
}
