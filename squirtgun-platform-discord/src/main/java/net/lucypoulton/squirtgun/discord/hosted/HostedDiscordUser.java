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
package net.lucypoulton.squirtgun.discord.hosted;

import net.dv8tion.jda.api.entities.User;
import net.lucypoulton.squirtgun.discord.DiscordUser;
import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunPlayer;

import java.util.UUID;

/**
 * A SquirtgunUser that's linked to a parent user.
 */
public class HostedDiscordUser extends DiscordUser {

    private final SquirtgunPlayer parent;
    private final User user;

    public HostedDiscordUser(SquirtgunPlayer parent, User user) {
        this.parent = parent;
        this.user = user;
    }

    /**
     * Gets the user's Minecraft username
     */
    @Override
    public String getUsername() {
        return parent.getUsername();
    }

    /**
     * Gets the user's Minecraft UUID
     */
    @Override
    public UUID getUuid() {
        return parent.getUuid();
    }

    @Override
    public boolean hasPermission(String permission) {
        return parent.hasPermission(permission);
    }

    @Override
    public User discordUser() {
        return user;
    }
}
