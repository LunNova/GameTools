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

import java.util.ArrayList;
import java.util.List;

import me.nallar.gameTools.GameTools;
import me.nallar.gameTools.queue.SpellRunnable;
import net.minecraft.server.Packet61WorldEvent;
import org.getspout.spoutapi.packet.PacketParticle;
import org.getspout.spoutapi.particle.Particle;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Effects {
	private Effects() {
	}

	public static final Vector randomVector() {
		return randomizeVector(new Vector());
	}

	public static final Vector randomVector(float f) {
		return randomizeVector(new Vector(), f);
	}

	public static final Vector randomizeVector(Vector v) {
		return randomizeVector(v, 5f);
	}

	public static final Vector randomizeVector(Vector v, float f) {
		v.setX((Math.random() - 0.5) * f);
		v.setY((Math.random() - 0.5) * f);
		v.setZ((Math.random() - 0.5) * f);
		return v;
	}

	public static final SpellRunnable spellTravel(Player p) {
		return spellTravel(p.getEyeLocation(), p.getTargetBlock(null, 100).getLocation());
	}

	public static final SpellRunnable spellTravel(Location l, Location l2) {
		if (l == null || l2 == null) {
			throw new NullPointerException();
		}

		SpellRunnable r = new SpellRunnable(l, l2);
		r.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameTools.instance, r, 1, 3);

		List<CraftPlayer> players = nearbyPlayers(l, 256);

		createEffect(players, l, 1004, 0, 16);//Fizz sound.
		createEffect(players, l, 1004, 0, 16);//Fizz sound.
		l.setY(l.getY() + 3);
		l2.setY(l2.getY() + 4);
		spoutEffect(players, l, "bubble", 2f, 4, randomVector());
		spoutEffect(players, l, "depthsuspend", 2f, 4, randomVector());
		spoutEffect(players, l, "townaura", 2f, 4, randomVector());
		spoutEffect(players, l, "suspend", 2f, 4, randomVector());
		spoutEffect(players, l, "mobSpell", 2f, 4, randomVector());
		createEffect(players, l, Effect.ENDER_SIGNAL.getId(), 0, 200);
		createEffect(players, l2, Effect.ENDER_SIGNAL.getId(), 0, 200);
		l2.setY(l2.getY() + 10);
		createEffect(players, l2, Effect.ENDER_SIGNAL.getId(), 0, 200);
		spoutEffect(players, l2, "dripLava", 2f, 4, randomVector());
		spoutEffect(players, l2, "tilecrack_1", 2f, 2, randomVector());

		return r;
	}

	public static final void spoutEffect(Location l, String type, float scale, int time, Vector v) {
		spoutEffect(nearbyPlayers(l, 128), l, type, scale, time, v);
	}

	public static final void spoutEffect(List<CraftPlayer> players, Location l, String type, float scale, int time, Vector v) {
		Particle particle = new Particle(type, l, v);
		particle.setScale(scale);
		particle.setGravity(0);
		particle.setMaxAge(time * 20);
		if (l != null && type != null) {
			for (Player p : players) {
				if ((p instanceof SpoutPlayer) && ((SpoutPlayer) p).isSpoutCraftEnabled()) {
					((SpoutPlayer) p).sendPacket(new PacketParticle(particle));
				}
			}
		}
	}

	public static final void createExplosionEffect(Location l) {
		l.getWorld().createExplosion(l, 0, false);
	}

	public static final void createLightningExplosion(Location l) {
		World w = l.getWorld();
		w.createExplosion(l, 0, true);
		w.playEffect(l, Effect.SMOKE, 3);
		w.strikeLightningEffect(l);
	}

	public static final void createExplosion(Location l, float s) {
		l.getWorld().createExplosion(l, s);
	}

	public static final void createEffect(Player p, int e, int d, int r) {
		createEffect(p.getLocation(), e, d, r);
	}

	public static final List<CraftPlayer> nearbyPlayers(Location l, int r){
		r *= r;
		List<CraftPlayer> players = new ArrayList<CraftPlayer>();
		for (Player player : l.getWorld().getPlayers()) {
			CraftPlayer cp = (CraftPlayer)player;
			if (cp.getHandle().netServerHandler != null && ((int) player.getLocation().distanceSquared(l)) <= r) {
				players.add((CraftPlayer)player);
			}
		}
		return players;
	}

	public static final void createEffect(Location l, int e, int d, int r) {
		createEffect(nearbyPlayers(l, r), l, e, d);
	}

	public static final void createEffect(List<CraftPlayer> players, Location l, int e, int d){
		Packet61WorldEvent packet = new Packet61WorldEvent(e, l.getBlockX(), l.getBlockY(), l.getBlockZ(), d, false);
		for(CraftPlayer p : players){
			p.getHandle().netServerHandler.sendPacket(packet);
		}
	}

	public static final void createEffect(List<CraftPlayer> players, Location l, int e, int d, int r){
		Packet61WorldEvent packet = new Packet61WorldEvent(e, l.getBlockX(), l.getBlockY(), l.getBlockZ(), d, false);
		for(CraftPlayer p : players){
			if(p.getLocation().distance(l) <= r){
				p.getHandle().netServerHandler.sendPacket(packet);
			}
		}
	}

	public static void createEffect(Player p, Effect e) {
		p.getWorld().playEffect(p.getLocation(), e, 0, 5);
	}

	public class EffectTask {
	}

	public class SpoutEffectTask {
	}
}
