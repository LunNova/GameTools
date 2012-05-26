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

import org.junit.Test;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static org.junit.Assert.assertTrue;

public class CommandHandlerTest {
	@Test
	public void testParameterlessCommand() {
		GameTools gt = new GameTools();
		Command cmd = new Command("gttestcommand") {
			@Override
			public boolean execute(CommandSender s, String ss, String[] sss) {
				return false;
			}
		};
		assertTrue(gt.onCommand(null, cmd, null, null));
	}
}
