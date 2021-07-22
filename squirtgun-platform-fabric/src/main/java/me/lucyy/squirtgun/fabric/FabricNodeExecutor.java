/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lucyy.squirtgun.fabric;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.lucyy.squirtgun.command.context.StringContext;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.audience.SquirtgunUser;
import net.kyori.adventure.text.Component;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Command adapter between Fabric and Squirtgun command execution.
 */
public class FabricNodeExecutor implements Command<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {

	private final CommandNode<SquirtgunUser> commandNode;
	private final FormatProvider formatProvider;
	// Command registration happens <i>before</i> server initialization,
	// however, commands can't be dispatched until after the server has initialized
	private final Supplier<FabricPlatform> platformSupplier;

	public FabricNodeExecutor(final @NotNull CommandNode<SquirtgunUser> commandNode,
														final @NotNull FormatProvider formatProvider,
														final @NotNull Supplier<FabricPlatform> platformSupplier) {
		this.commandNode = Objects.requireNonNull(commandNode, "commandNode");
		this.formatProvider = Objects.requireNonNull(formatProvider, "formatProvider");
		this.platformSupplier = Objects.requireNonNull(platformSupplier, "platformSupplier");
	}

	@Override
	public int run(final CommandContext<ServerCommandSource> context) {
		String input = context.getInput();
		final int i = input.indexOf(' ');
		input = i > -1 ? input.substring(i + 1) : "";

		final SquirtgunUser source = this.platformSupplier.get().fromCommandSource(context.getSource());
		final Component result = new StringContext<>(this.formatProvider, source, this.commandNode, input).execute();
		if (result != null) {
			source.sendMessage(result);
		}

		return Command.SINGLE_SUCCESS;
	}

	@Override
	public CompletableFuture<Suggestions> getSuggestions(final CommandContext<ServerCommandSource> context, final SuggestionsBuilder builder) {
		String input = context.getInput();
		final int i = input.indexOf(' ');
		input = i > -1 ? input.substring(i + 1) : "";

		final SquirtgunUser source = this.platformSupplier.get().fromCommandSource(context.getSource());
		final List<String> suggestions = new StringContext<>(this.formatProvider, source, this.commandNode, input).tabComplete();
		if (suggestions == null) {
			return Suggestions.empty();
		} else {
			suggestions.forEach(builder::suggest);
			return builder.buildFuture();
		}
	}
}
