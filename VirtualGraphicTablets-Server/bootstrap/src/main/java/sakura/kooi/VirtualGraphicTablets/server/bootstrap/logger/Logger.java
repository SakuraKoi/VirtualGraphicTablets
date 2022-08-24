package sakura.kooi.VirtualGraphicTablets.server.bootstrap.logger;

import lombok.Setter;
import sakura.kooi.VirtualGraphicTablets.server.bootstrap.Constants;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private final String module;

	private Logger(final String module) {
		this.module = module;
	}

	private static final String TAG_INFO = "§f [INFO] \t§r";
	private static final String TAG_DEBUG = "§8 [DEBUG] \t§r";
	private static final String TAG_SUCC = "§a [SUCC] \t";
	private static final String TAG_WARN = "§e [WARN] \t";
	private static final String TAG_ERROR = "§c [ERROR] \t";
	private static final String TAG_CRITICAL = "§4 [CRIT] \t";
	@Setter
	private static PrintStream out = null;
	@Setter
	private static boolean supportColor = true;
	
	private static final Object lock = new Object();

	public static void println(final String msg) {
		synchronized (lock) {
			if (out != null) {
				if (supportColor)
					out.println(MinecraftColorFormatter.format(msg));
				else
					out.println(MinecraftColorFormatter.strip(msg));
			}
			System.out.println(MinecraftColorFormatter.format(msg));
		}
	}

	public static void print(final String msg) {
		synchronized (lock) {
			if (out != null) {
				if (supportColor)
					out.print(MinecraftColorFormatter.format(msg));
				else
					out.print(MinecraftColorFormatter.strip(msg));
			}
			System.out.print(MinecraftColorFormatter.format(msg));
		}

	}

	private static void printFormat(final String level, final String msg) {
		println("§7" + currentTime() + level + msg);
	}

	private static final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

	private static String currentTime() {
		return df.format(new Date());
	}

	public static void infoMulti(final String... messages) {
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_INFO, m);
			}
		}
	}

	public static void warnMulti(final String... messages) {
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_WARN, m);
			}
		}
	}

	public static void errorMulti(final String... messages) {
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_ERROR, m);
			}
		}
	}

	public static void critMulti(final String... messages) {
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_CRITICAL, m);
			}
		}
	}

	private static long timer = -1L;

	public static void infoPeriod(final long period, final String format, final Object... args) {
		if (System.currentTimeMillis() - timer > period) {
			timer = System.currentTimeMillis();
			info(format, args);
		}
	}

	public static void resetPeriod() {
		timer = System.currentTimeMillis();
	}

	public static void debug(final String format, final Object... args) {
		if (Constants.RUNNING_IN_CONSOLE) return;
		synchronized (lock) {
			printFormat(TAG_DEBUG, MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public static void info(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_INFO, MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public static void succ(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_SUCC, MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public static void warn(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_WARN, MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public static void error(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_ERROR, MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public static void crit(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_CRITICAL, MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public static void warn(final String msg, final Throwable ex) {
		final String[] messages = (msg + "\n" + printStackTrace(ex)).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_WARN, m);
			}
		}
	}

	public static void error(final String msg, final Throwable ex) {
		final String[] messages = (msg + "\n" + printStackTrace(ex)).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_ERROR, m);
			}
		}
	}

	public static void crit(final String msg, final Throwable ex) {
		final String[] messages = (msg + "\n" + printStackTrace(ex)).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_CRITICAL, m);
			}
		}
	}

	public static void warn(final Throwable ex) {
		final String[] messages = printStackTrace(ex).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_WARN, m);
			}
		}
	}

	public static void error(final Throwable ex) {
		final String[] messages = printStackTrace(ex).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_ERROR, m);
			}
		}
	}

	public static void crit(final Throwable ex) {
		final String[] messages = printStackTrace(ex).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_CRITICAL, m);
			}
		}
	}

	private static String printStackTrace(final Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}

	public static Logger module(final String module) {
		return new Logger(module);
	}
	public static Logger module(final Class<?> module) {
		return new Logger(module.getSimpleName());
	}

	private long t;

	public void iPeriod(final long period, final String format, final Object... args) {
		if (System.currentTimeMillis() - t > period) {
			t = System.currentTimeMillis();
			i(format, args);
		}
	}

	public void rstPeriod() {
		t = System.currentTimeMillis();
	}

	public void d(final String format, final Object... args) {
		if (Constants.RUNNING_IN_CONSOLE) return;
		synchronized (lock) {
			printFormat(TAG_DEBUG, "§b[" + module + "] §r" + MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public void s(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_SUCC, "§b[" + module + "] §r" + MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public void i(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_INFO, "§b[" + module + "] §r" + MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public void w(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_WARN, "§b[" + module + "] §e" + MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public void e(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_ERROR, "§b[" + module + "] §c" + MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public void c(final String format, final Object... args) {
		synchronized (lock) {
			printFormat(TAG_CRITICAL,
					"§b[" + module + "] §4" + MessageFormatter.arrayFormat(format, args).getMessage());
		}
	}

	public void w(final String msg, final Throwable ex) {
		final String[] messages = (msg + "\n" + printStackTrace(ex)).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_WARN, "§b[" + module + "] §e" + m);
			}
		}
	}

	public void e(final String msg, final Throwable ex) {
		final String[] messages = (msg + "\n" + printStackTrace(ex)).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_ERROR, "§b[" + module + "] §c" + m);
			}
		}
	}

	public void c(final String msg, final Throwable ex) {
		final String[] messages = (msg + "\n" + printStackTrace(ex)).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_CRITICAL, "§b[" + module + "] §4" + m);
			}
		}
	}

	public void w(final Throwable ex) {
		final String[] messages = printStackTrace(ex).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_WARN, "§b[" + module + "] §e" + m);
			}
		}
	}

	public void e(final Throwable ex) {
		final String[] messages = printStackTrace(ex).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_ERROR, "§b[" + module + "] §c" + m);
			}
		}
	}

	public void c(final Throwable ex) {
		final String[] messages = printStackTrace(ex).split("\n");
		synchronized (lock) {
			for (final String m : messages) {
				printFormat(TAG_CRITICAL, "§b[" + module + "] §4" + m);
			}
		}
	}

}
