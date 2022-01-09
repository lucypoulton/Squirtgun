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

package net.lucypoulton.squirtgun.bukkit;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.context.StringContext;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Wraps a {@link CommandNode} so it can be executed by a Bukkit server.
 */
public class BukkitNodeExecutor implements TabExecutor {

    private final CommandNode<?> node;
    private final FormatProvider formatter;
    private final BukkitPlatform platform;

    /**
     * @param node      the root command node to execute
     * @param formatter the command sender to pass to the context
     * @param platform  the platform that this node belongs to
     */
    public BukkitNodeExecutor(CommandNode<?> node, FormatProvider formatter, BukkitPlatform platform) {
        this.node = node;
        this.formatter = formatter;
        this.platform = platform;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender bukkitSender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        SquirtgunUser sender = platform.getUser(bukkitSender);
        Component ret = new StringContext(formatter, sender, node, String.join(" ", args)).execute();
        if (ret != null) {
            sender.sendMessage(ret);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] stringArgs) {
        CommandContext context = new StringContext(
                formatter, platform.getUser(sender), node, String.join(" ", stringArgs));
        List<String> ret = context.tabComplete();
        return ret == null ? ImmutableList.of() : ret;
    }
}
