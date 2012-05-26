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
package me.nallar.gameTools.spells;

import me.nallar.gameTools.GameTools;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MagicSpells {
	private MagicSpells() {
	}

	public static void runSpell(String name, Player asPlayer, boolean withPermissions) {
		runSpell(name, "", asPlayer, withPermissions);
	}

	public static void runSpell(String name, String parameters, Player asPlayer, boolean withPermissions) {
		if (name.contains(" ")) {
			throw new IllegalArgumentException("Spell name can not contain a space.");
		}
		Bukkit.getServer().dispatchCommand(asPlayer, "/cast " + name + " " + parameters);
	}

	public static void cancelRite(Player p, boolean couldMove) {
		if (!couldMove) {

		}
	}

	public static void startRite(final SpoutPlayer p, int timeout, final boolean canMove) {
		if (!canMove) {
			p.setAirSpeedMultiplier(0);
			p.setGravityMultiplier(0);
			p.setJumpingMultiplier(0);
			p.setWalkingMultiplier(0);
			p.setSwimmingMultiplier(0);
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(GameTools.instance, new Runnable() {
			@Override
			public void run() {
				cancelRite(p, canMove);
			}
		}, 20 * timeout);
	}
}
