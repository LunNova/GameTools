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
package me.nallar.test;

import org.getspout.spoutapi.material.Block;
import org.getspout.spoutapi.sound.SoundEffect;

public class TestBlock implements Block {
	public int id = 0, lightLevel = 0;
	public String name = "";

	public TestBlock(String name, int id) {
		this.name = name;
		this.id = id;
	}

	@Override
	public SoundEffect getStepSound() {
		return null;
	}

	@Override
	public Block setStepSound(SoundEffect sound) {
		return null;
	}

	@Override
	public float getFriction() {
		return 0;
	}

	@Override
	public Block setFriction(float slip) {
		return null;
	}

	@Override
	public float getHardness() {
		return 0;
	}

	@Override
	public Block setHardness(float hardness) {
		return null;
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

	@Override
	public Block setOpaque(boolean opaque) {
		return null;
	}

	@Override
	public int getLightLevel() {
		return lightLevel;
	}

	@Override
	public Block setLightLevel(int level) {
		lightLevel = level;
		return this;
	}

	@Override
	public int getRawId() {
		return 0;
	}

	@Override
	public int getRawData() {
		return 0;
	}

	@Override
	public boolean hasSubtypes() {
		return false;
	}

	@Override
	public String getNotchianName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
