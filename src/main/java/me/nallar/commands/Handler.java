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
package me.nallar.commands;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.nallar.commands.annotations.ChatCommand;

public class Handler {
	protected static final Map<String, Object> handlerCache = new HashMap<String, Object>();
	protected static final Object invalidMethod = new Object();

	private Handler() {
	}

	public static String arg(String[] args, int index, String def) {
		if (index >= args.length) {
			return def;
		}
		return args[index];
	}

	public static int arg(String[] args, int index, int def) {
		if (index >= args.length) {
			return def;
		}
		return Integer.parseInt(args[index]);
	}

	public static boolean arg(String[] args, int index, boolean def) {
		if (index >= args.length) {
			return def;
		}
		return stringToBool(args[index]);
	}

	public static boolean stringToBool(String s) {
		if (s == null) {
			return false;
		}
		return s.equalsIgnoreCase("true") || s.equals("1") || s.equalsIgnoreCase("on");
	}

	public static boolean canHandle(String commandName) {
		return handlerCache.get(commandName) != invalidMethod;
	}

	public static boolean handle(String commandName, Class<?> commandHandler, Map<Class<?>, Object[]> fullParameterMapping) {
		Method mm = (Method) handlerCache.get(commandName);
		if (mm != null) {
			fullParameterMapping = new HashMap<Class<?>, Object[]>(fullParameterMapping);

			Class<?>[] parameterTypes = mm.getParameterTypes();
			Object[] parameters = new Object[parameterTypes.length];

			for (int i = 0; i < parameterTypes.length; i++) {
				Object[] parameterValue = fullParameterMapping.get(parameterTypes[i]);
				parameters[i] = parameterValue[0];
				fullParameterMapping.put(parameterTypes[i], Arrays.copyOfRange(parameterValue, 1, parameterValue.length));
			}

			try {
				return (Boolean) mm.invoke((Class<?>) null, parameters);
			} catch (Exception ignored) { }

			throw new IllegalStateException("Cached method for " + commandName + " was invalid.");
		}
		System.out.println(commandName);
		Map<Class<?>, Object[]> parameterMapping;
		MethodLoop:
		for (Method m : commandHandler.getMethods()) {
			if (m.getAnnotation(ChatCommand.class) == null || !m.getName().equalsIgnoreCase(commandName)) {continue;}

			parameterMapping = new HashMap<Class<?>, Object[]>(fullParameterMapping);

			Class<?>[] parameterTypes = m.getParameterTypes();
			Object[] parameters = new Object[parameterTypes.length];

			for (int i = 0; i < parameterTypes.length; i++) {
				Object[] parameterValue = parameterMapping.get(parameterTypes[i]);

				if (parameterValue == null || parameterValue.length == 0) {
					continue MethodLoop;
				}

				parameters[i] = parameterValue[0];
				parameterMapping.put(parameterTypes[i], Arrays.copyOfRange(parameterValue, 1, parameterValue.length));
			}

			try {
				boolean ret = (Boolean) m.invoke((Class<?>) null, parameters);
				handlerCache.put(commandName, m);
				return ret;
			} catch (Exception ignored) { }
		}

		handlerCache.put(commandName, invalidMethod);

		return false;
	}
}
