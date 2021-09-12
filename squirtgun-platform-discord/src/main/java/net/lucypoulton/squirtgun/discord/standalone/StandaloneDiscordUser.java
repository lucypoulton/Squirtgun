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
package net.lucypoulton.squirtgun.discord.standalone;

import net.dv8tion.jda.api.entities.User;
import net.lucypoulton.squirtgun.discord.DiscordUser;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StandaloneDiscordUser extends DiscordUser {

    private final User user;

    public StandaloneDiscordUser(User user) {
        this.user = user;
    }

    /**
     * @return a generated UUID, derived from the user's Discord account ID
     */
    @Override
    public UUID getUuid() {
        return UUID.nameUUIDFromBytes(("DiscordUser" + user.getId()).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean hasPermission(String permission) {
        return false; // TODO
    }

    @Override
    public User discordUser() {
        return user;
    }
}