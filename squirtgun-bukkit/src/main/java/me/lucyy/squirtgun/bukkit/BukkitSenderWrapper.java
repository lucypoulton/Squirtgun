package me.lucyy.squirtgun.bukkit;

import me.lucyy.squirtgun.platform.PermissionHolder;
import org.bukkit.command.CommandSender;

public class BukkitSenderWrapper implements PermissionHolder {

	private final CommandSender parent;

	public BukkitSenderWrapper(CommandSender parent) {
		this.parent = parent;
	}

	@Override
	public boolean hasPermission(String permission) {
		return parent.hasPermission(permission);
	}
}