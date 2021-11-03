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
package net.lucypoulton.squirtgun.discord;

import net.dv8tion.jda.api.JDA;
import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.discord.adventure.DiscordAudiences;
import net.lucypoulton.squirtgun.discord.adventure.DiscordComponentSerializer;
import net.lucypoulton.squirtgun.discord.command.DiscordCommandListener;
import net.lucypoulton.squirtgun.discord.standalone.StandaloneDiscordAudiences;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.AuthMode;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * A Platform implementation for a JDA instance.
 */
public abstract class DiscordPlatform implements Platform {

    private final JDA jda;
    private final DiscordAudiences audiences;
    private final DiscordCommandListener listener;

    public JDA jda() {
        return jda;
    }

    /**
     * @param jda              the JDA instance to use to provide bot functionality
     * @param commandPrefix    the prefix to use for commands
     */
    protected DiscordPlatform(JDA jda, String commandPrefix) {
        this.jda = jda;
        audiences = new StandaloneDiscordAudiences();
        listener = new DiscordCommandListener(this, commandPrefix);
    }

    @Override
    public String name() {
        return "Discord";
    }

    /**
     * Authentication mode is not relevant to Discord so always returns offline
     *
     * @return {@link AuthMode#OFFLINE}
     */
    @Override
    public AuthMode getAuthMode() {
        return AuthMode.OFFLINE;
    }

    /**
     * Gets a DiscordUser from a user's Minecraft UUID.
     *
     * @param uuid the UUID of the player to get
     * @return a SquirtgunPlayer if the user is known and has a linked Discord account, otherwise null
     */
    @Override
    public abstract @Nullable DiscordUser getPlayer(UUID uuid);

    /**
     * Gets a DiscordUser from a user's Minecraft username.
     *
     * @param name the name of the player to get
     * @return a SquirtgunPlayer if the user is known has a linked Discord account, otherwise null
     */
    @Override
    public abstract @Nullable DiscordUser getPlayer(String name);

    /**
     * @return an empty list - this method is not applicable to Discord
     */
    @Override
    public List<SquirtgunPlayer> getOnlinePlayers() {
        return List.of();
    }

    @Override
    public void registerCommand(CommandNode<?> node, FormatProvider provider) {
        listener.registerCommand(node, provider);
    }

    @Override
    public void log(Component component) {
        getLogger().info(DiscordComponentSerializer.INSTANCE.serialize(component));
    }

    public DiscordAudiences audiences() {
        return audiences;
    }
}
