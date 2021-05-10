/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of LucyCommonLib.
 *
 * LucyCommonLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LucyCommonLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LucyCommonLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucyy.squirtgun.update;

import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.scheduler.Task;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Update checking mechanism base class.
 */
public abstract class UpdateChecker {
	private boolean updateAvailable;
	private final Platform plugin;
	private final Component updateMessage;
	private final String listenerPermission;
	private final String url;
	private final Task listenerTask;

	/**
	 * Creates a new update checker, and schedule update checking every 3 hours.
	 *
	 * @param plugin             the plugin to check against
	 * @param url                a URL (such as the spigot api) that will return a plaintext version string when retrieved
	 * @param updateMessage      the message to show in console and to players with the listener permission on join
	 * @param listenerPermission if a player holds this permission and an update is available, they will be sent the update message in chat
	 */
	protected UpdateChecker(Platform plugin, String url, Component updateMessage, String listenerPermission) {
		this.plugin = plugin;
		this.url = url;
		this.updateMessage = updateMessage;
		this.listenerPermission = listenerPermission;

		plugin.registerEventListener(new UpdateListener(this, plugin));

		if (plugin.getPluginVersion().contains("-")) {
			plugin.getLogger().warning("Development version detected, skipping update check.");
			listenerTask = null;
			return;
		}

		listenerTask = Task.builder()
				.async()
				.interval(216000)   // every 3 hours
				.action(x -> checkForUpdate())
				.build();

		plugin.getTaskScheduler().start(listenerTask);
	}

	/**
	 * Check if the result of a request indicates an update is available.
	 *
	 * @param input the string result of a HTTP GET request from the URL provided to the constructor
	 * @return whether the data provided shows an update available. In the event of an error, a warning
	 * should be printed to the console and this should return false.
	 */
	protected abstract boolean checkDataForUpdate(String input);

	/**
	 * Gets the platform that this checker is associated with.
	 */
	protected Platform getPlatform() {
		return plugin;
	}

	/**
	 * Executes a blocking request to check for an update. In the event of failure, a warning
	 * will be printed to the console.
	 *
	 * @return whether an update is available. Should the check fail, returns false
	 */
	@SuppressWarnings("UnusedReturnValue")
	public boolean checkForUpdate() {
		try {
			plugin.getLogger().info("Checking for updates...");

			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			if (con.getResponseCode() != 200) throw new Exception();

			String text = new BufferedReader(
				new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)
			).lines().collect(Collectors.joining("\n"));

			if (checkDataForUpdate(text)) {
				updateAvailable = true;
				plugin.getLogger().warning(LegacyComponentSerializer.legacySection().serialize(getUpdateMessage()));
				plugin.getTaskScheduler().cancel(listenerTask);
				return true;
			} else plugin.getLogger().info("No update available.");

		} catch (Exception ignored) {
			plugin.getLogger().warning("Unable to check for updates!");
		}
		return false;
	}

	/**
	 * @return the update message as set in the constructor
	 */
	public Component getUpdateMessage() {
		return updateMessage;
	}

	/**
	 * @return whether there is an update available. Note that this will not check for updates,
	 * only return a cached result.
	 */
	public boolean checkDataForUpdate() {
		return updateAvailable;
	}

	/**
	 * @return the listener permission as set in the constructor
	 */
	public String getListenerPermission() {
		return listenerPermission;
	}
}