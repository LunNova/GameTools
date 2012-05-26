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

import me.nallar.commands.annotations.ChatCommand;
import me.nallar.gameTools.queue.SpellRunnable;
import me.nallar.gameTools.spells.Effects;
import org.getspout.spoutapi.Spout;
import org.getspout.spoutapi.SpoutWorld;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import static me.nallar.commands.Handler.arg;

public class CommandHandler {
	@ChatCommand
	public static boolean GTTestCommand() {
		return true;
	}

	@ChatCommand
	public static boolean testSwirl(SpoutPlayer p) {
		SpellRunnable r = Effects.spellTravel(p);
		r.setEffects("townaura", Effect.MOBSPAWNER_FLAMES.getId());
		return true;
	}

	@ChatCommand
	public static boolean setPvP(CommandSender s, SpoutPlayer p, String[] a) {
		String worldName;
		if (p != null) {
			worldName = p.getWorld().getName();
		} else {
			worldName = "world";
		}
		SpoutWorld world = Spout.getServer().getWorld(worldName);
		boolean enabled = arg(a, 0, !world.getPVP());

		if (p != null && !p.hasPermission("gametools.setpvp")) {
			s.sendMessage(ChatColor.RED + "[GameTools] You do not have permission to perform this command.");
			return true;
		}

		if (enabled == world.getPVP()) {
			s.sendMessage(ChatColor.RED + "PvP is already set to " + (enabled ? "1" : "0") + "!");
			return true;
		}
		world.setPVP(enabled);

		GameTools.showMessage(ChatColor.RED + "PvP Set!", ChatColor.YELLOW + "PvP " + (enabled ? "enabled" : "disabled"), enabled ? Material.DIAMOND_SWORD : Material.DIAMOND_CHESTPLATE);
		return true;
	}

	@ChatCommand
	public static boolean game(SpoutPlayer p, String[] a) {
		a = Arrays.copyOfRange(a, 1, a.length);
		return false;
	}
}
