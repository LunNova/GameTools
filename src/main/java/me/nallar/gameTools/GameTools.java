/*
 * This file is part of GameTools.
 *
 * Copyright © 2012-2012, nallar <http://nallar.me/minecraft/gameTools/>
 * GameTools is licensed under the N Open License, Version 1.
 *
 * GameTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License Version 3,
 * as published by the Free Software Foundation.
 *
 * In addition, 90 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the FreeBSD license,
 * as described in the N Open License, Version 1.
 *
 * GameTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the FreeBSD license and the N Open License, Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.nallar.me/licenses/n-open-license-v1.txt> for the full license,
 * including the FreeBSD license.
 */
package me.nallar.gameTools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import me.nallar.commands.Handler;
import org.getspout.spoutapi.Spout;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GameTools extends JavaPlugin {
	public static GameTools instance;
	private final static List<String> skinList = Arrays.asList(new String[]{"nallar"});
	private final static Map<String, String> skinMap = new HashMap<String, String>();
	public static Configuration conf;
	public static boolean debug = false;
	public static boolean fixSkins = false;
	public static boolean plus = false;
	public static boolean devLogin = false;
	private final static int spVersion = 1190;

	public GameTools() {
		super();
		if (instance != null) {
			throw new RuntimeException("[GameTools] instantiated without being disabled first - bukkit bug.");
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		instance = null;
		conf = null;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		instance = this;
		conf = getConfig();
		conf.options().copyDefaults(true);
		saveConfig();
		debug = conf.getBoolean("debug", false);
		fixSkins = conf.getBoolean("fixSkins", false);
		devLogin = conf.getBoolean("allowDevLogin", true);
		Bukkit.getPluginManager().registerEvents(new GameToolsSpoutListener(), this);
		plus = !conf.getBoolean("recommendPlusPlus");
		try {
			Class<?> c = Class.forName("org.bukkit.craftbukkit.utils.metrics.TimedThread");
			c.getClassLoader();
			plus = true;
		} catch (Exception ignored) { }
		if (!plus) {
			Bukkit.getLogger().log(Level.WARNING, "[GameTools] It is recommended that you use CraftBukkitPlusPlus to improve performance. This warning can be disabled in the config.");
		}
		try {
			int spVersion = Integer.parseInt(Spout.getServer().getPluginManager().getPlugin("Spout").getDescription().getVersion());
			if (spVersion > 0 && spVersion < GameTools.spVersion) {
				Bukkit.getLogger().log(Level.WARNING, ChatColor.RED + "Your SpoutPlugin is out of date! Recommended: " + GameTools.spVersion + ", you have: " + spVersion);
				Bukkit.getLogger().log(Level.WARNING, ChatColor.RED + "Get the latest version at");
			}
		} catch (Exception ignored) { }
		//GameToolsKeyBinding binding = new GameToolsKeyBinding();
		//SpoutManager.getKeyBindingManager().registerBinding("name", Keyboard.KEY_L, "desc",binding, this);
	}

	public static String getSkinUrl(Player p) {
		String n = p.getName();
		if (skinMap.containsKey(n)) {
			String r = skinMap.get(n);
			return r == null ? "" : r;
		} else if (skinList.contains(n)) {
			return "http://nallar.me/minecraft/skins/" + n + ".png";
		}
		return "http://s3.amazonaws.com/MinecraftSkins/" + n + ".png";
	}

	public static String getSkinUrl(Entity e) {
		throw new UnsupportedOperationException();
	}

	public static void fixSkinForDelayed(final Player player) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameTools.instance, new Runnable() {
			@Override
			public void run() {
				fixSkinFor(player);
			}
		}, 60);
	}

	public static void fixSkinFor(Player player) {
		if (!fixSkins) {
			return;
		}
		SpoutPlayer p;

		if (player instanceof SpoutPlayer) {
			p = (SpoutPlayer) player;
		} else {
			p = SpoutManager.getPlayer(player);
		}
		if (p.isSpoutCraftEnabled()) {
			int rd = p.getRenderDistance().getValue();
			for (Entity e : player.getNearbyEntities(rd, rd, rd)) {
				if (e instanceof Player) {
					SpoutPlayer sp = SpoutManager.getPlayer((Player) e);
					if (sp.isSpoutCraftEnabled()) {
						sp.setSkinFor(p, getSkinUrl((Player) e));
					}
				}
			}
		}
		p.setSkin(getSkinUrl(player));
	}

	public static void showMessage(String title, String msg, Material mat) {
		for (SpoutPlayer p : SpoutManager.getPlayerManager().getOnlinePlayers()) {
			showMessageTo(p, title, msg, mat);
		}
		Bukkit.getLogger().log(Level.INFO, "[GameTools] " + title + ": " + msg);
	}

	public static void showMessageTo(CommandSender s, String title, String msg, Material mat) {
		if (s instanceof SpoutPlayer) {
			SpoutPlayer p = (SpoutPlayer) s;
			if (p.isSpoutCraftEnabled()) {
				p.sendNotification(msg, title, mat);
			} else {
				p.sendRawMessage(title + ": " + ChatColor.WHITE + msg);
			}
		} else {
			s.sendMessage(title + ": " + ChatColor.WHITE + msg);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandText, String[] args) {
		try {
			return onCommand(sender, cmd.getName(), commandText, args);
		} catch (Exception ignored) {
			return false;
		}
	}

	public boolean onCommand(CommandSender sender, String commandName, String commandText, String[] args) {
		commandName = commandName.toLowerCase();
		if (!Handler.canHandle(commandName)) {
			return false;
		}

		boolean isPlayer = sender instanceof Player;
		SpoutPlayer p = null;

		if (isPlayer) {
			p = SpoutManager.getPlayer((Player) sender);
			sender = p;
		}

		Map<Class<?>, Object[]> parameterMapping = new HashMap<Class<?>, Object[]>();

		parameterMapping.put(CommandSender.class, new Object[]{sender});
		parameterMapping.put(Command.class, new Object[]{commandName});
		parameterMapping.put(String[].class, new Object[]{args});
		parameterMapping.put(SpoutPlayer.class, new Object[]{p});

		return Handler.handle(commandName, CommandHandler.class, parameterMapping);
	}

	public void startGame(String[] args) {

	}
}
