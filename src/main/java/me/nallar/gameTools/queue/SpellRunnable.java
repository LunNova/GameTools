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
package me.nallar.gameTools.queue;

import java.util.List;

import me.nallar.gameTools.spells.Effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpellRunnable implements Runnable {
	public Location current;
	public Location end;
	public double xStep;
	public double yStep;
	public double zStep;
	public String spoutEffect;
	public String hitSpoutEffect;
	public int fallbackEffect;
	public int hitFallbackEffect;
	public Player hitCommandPlayer;
	public String hitCommand;
	public int id;
	public Vector effectVector = new Vector();

	public void setHitCommand(String endCommand, Player endCommandPlayer) {
		this.hitCommand = endCommand;
		this.hitCommandPlayer = endCommandPlayer;
	}

	public void setHitEffects(String spoutEffect, int fallbackEffect) {
		this.hitSpoutEffect = spoutEffect;
		this.hitFallbackEffect = fallbackEffect;
	}

	public void setEffects(String spoutEffect, int fallbackEffect) {
		this.spoutEffect = spoutEffect;
		this.fallbackEffect = fallbackEffect;
	}

	public SpellRunnable(Location current, Location end) {
		xStep = Math.max(((end.getX() - current.getX()) / 6), 5);
		yStep = Math.max(((end.getY() - current.getY()) / 6), 5);
		zStep = Math.max(((end.getZ() - current.getZ()) / 6), 5);
		this.current = current.clone();
		this.end = end.clone();
		if (xStep < 0.1 && xStep > -0.1) {
			this.current.setX(end.getX());
			xStep = 0;
		}
		if (yStep < 0.1 && yStep > -0.1) {
			this.current.setY(end.getY());
			yStep = 0;
		}
		if (zStep < 0.1 && zStep > -0.1) {
			this.current.setZ(end.getZ());
			zStep = 0;
		}
		System.out.println("c: " + current + "\te:" + end);
	}

	public void hit() {
		Effects.spoutEffect(end, hitSpoutEffect, 2f, 4, Effects.randomVector());
		Effects.createEffect(end, hitFallbackEffect, 0, 128);
		if (hitCommand != null) {
			Bukkit.getServer().dispatchCommand(hitCommandPlayer, hitCommand);
		}
		Bukkit.getScheduler().cancelTask(id);
	}

	public void playEffect() {
		List<CraftPlayer> players = Effects.nearbyPlayers(current, 256);
		Effects.spoutEffect(players, current, spoutEffect, 0.5f, 1, Effects.randomizeVector(effectVector, 0.1f));
		Effects.createEffect(players, current, fallbackEffect, 0);
	}

	@Override
	public void run() {
		double cx = current.getX();
		double cy = current.getY();
		double cz = current.getZ();
		double ex = end.getX();
		double ey = end.getY();
		double ez = end.getZ();
		boolean y = false, x = false, z = false;
		System.out.println("Run! " + cx + ", " + cy + ", " + cz);
		if ((xStep > 0 && cx < ex) || (xStep < 0 && cx > ex)) {
			current.setX(cx + xStep);
		} else {
			x = true;
		}
		if ((yStep > 0 && cy < ey) || (yStep < 0 && cy > ey)) {
			current.setY(cy + yStep);
		} else {
			y = true;
		}
		if ((zStep > 0 && cz < ez) || (zStep < 0 && cz > ez)) {
			current.setZ(cz + zStep);
		} else {
			z = true;
		}
		playEffect();
		if (x && y && z) {
			hit();
		}
	}
}
