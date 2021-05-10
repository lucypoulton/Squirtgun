package me.lucyy.squirtgun.command.node;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.argument.ListArgument;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A node that tab completes and acts as a splitter for other nodes.
 * TODO
 */
public class SubcommandNode<T> implements CommandNode<T> {

	private final Set<? extends CommandNode<T>> childNodes;
	private final String name;
	private final @Nullable String permission;
	private final CommandArgument<String> argument;

	@SafeVarargs
	public SubcommandNode(@NotNull String name, @Nullable String permission, CommandNode<T>... childNodes) {
		Preconditions.checkNotNull(childNodes, "Child nodes must not be null");
		Preconditions.checkNotNull(name, "Name must not be null");

		this.childNodes = ImmutableSet.copyOf(childNodes);
		this.name = name;
		this.permission = permission;
		argument = new ListArgument("subcommand",
				"The subcommand to execute",
				this.childNodes.stream().map(CommandNode::getName).collect(Collectors.toList()));
	}

	private @Nullable CommandNode<T> getNode(String name) {
		return childNodes.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
	}

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return ImmutableList.of(argument);
	}

	@Override
	public @Nullable CommandNode<T> next(CommandContext<T> context) {
		String name = context.getArgumentValue(argument);
		return name == null ? null : getNode(name);
	}

	private Component helpMessage() {
		return Component.text("help message"); // TODO
	}

	@Override
	public @Nullable Component execute(CommandContext<T> context) {
		String subcommand = context.getArgumentValue(argument);
		Objects.requireNonNull(subcommand);
		CommandNode<T> node = getNode(subcommand);

		if (node == null) return helpMessage();

		return node.execute(context);
	}

	@Override
	public @NotNull String getName() {
		return name;
	}

	@Override
	public @Nullable String getPermission() {
		return permission;
	}
}