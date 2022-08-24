/*
 * Copyright (C) 2019. SakuraKooi(sakurakoi993519867@gmail.com) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package sakura.kooi.VirtualGraphicTablets.server.bootstrap.logger;

import java.util.regex.Pattern;

@SuppressWarnings("SpellCheckingInspection")
class MinecraftColorFormatter {
	private static final String ANSI_RESET = "\u001B[39;0m";
	private static final char COLOR_CHAR = '§';
	private static final String LOOKUP = "0123456789abcdefklmnor";
	private static final String[] ansiCodes = new String[] {
			"\u001B[0;30;22m", // Black §0
			"\u001B[0;34;22m", // Dark Blue §1
			"\u001B[0;32;22m", // Dark Green §2
			"\u001B[0;36;22m", // Dark Aqua §3
			"\u001B[0;31;22m", // Dark Red §4
			"\u001B[0;35;22m", // Dark Purple §5
			"\u001B[0;33;22m", // Gold §6
			"\u001B[0;37;22m", // Gray §7
			"\u001B[0;30;1m", // Dark Gray §8
			"\u001B[0;34;1m", // Blue §9
			"\u001B[0;32;1m", // Green §a
			"\u001B[0;36;1m", // Aqua §b
			"\u001B[0;31;1m", // Red §c
			"\u001B[0;35;1m", // Light Purple §d
			"\u001B[0;33;1m", // Yellow §e
			"\u001B[0;37;1m", // White §f
			"\u001B[5m", // Obfuscated §k
			"\u001B[21m", // Bold §l
			"\u001B[9m", // Strikethrough §m
			"\u001B[4m", // Underline §n
			"\u001B[3m", // Italic §o
			ANSI_RESET, // Reset §r
	};
	private static final Pattern colorPattern = Pattern.compile("§[0-z]");

	private MinecraftColorFormatter() {
	}

	public static String format(final String s) {
		final StringBuilder result = new StringBuilder();
		int next = s.indexOf(COLOR_CHAR);
		final int last = s.length() - 1;
		if (next == -1 || next == last)
			return s;
		int pos = next;
		int format;
		do {
			if (pos != next) {
				result.append(s, pos, next);
			}

			format = LOOKUP.indexOf(s.charAt(next + 1));
			if (format != -1) {
				result.append(ansiCodes[format]);
				pos = next += 2;
			} else {
				next++;
			}
			next = s.indexOf(COLOR_CHAR, next);
		} while (next != -1 && next < last);
		result.append(s, pos, s.length());
		result.append(ANSI_RESET);
		return result.toString();
	}

	public static String strip(final String msg) {
		return colorPattern.matcher(msg).replaceAll("");
	}
}
