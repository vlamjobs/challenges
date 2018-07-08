package com.jpm.interview.vlam.util;


import java.text.SimpleDateFormat;


/**
 * Avoid having external dependencies like log4j and always log to System.err
 *
 */
public class Logger {

    public enum Level {
        DEBUG,
        INFO,
        ERROR,
    }

    private static final Level sCurrentLevel = Level.INFO;
    private static final String sPattern = "yyyy-MM-dd HH:mm:ss.SSS zzz";
    private static final ThreadLocal<SimpleDateFormat> sFmt = ThreadLocal.withInitial(() -> new SimpleDateFormat(sPattern));

    private static void log(Level level, String msg, Throwable t) {
        if (level.ordinal() >= sCurrentLevel.ordinal()) {
            String s = String.format("[%s] %s: %s %s", level, sFmt.get().format(new java.util.Date()), msg, (t == null ? "" : t.getClass().getSimpleName()));
            System.err.println(s);
            if (t != null)
                t.printStackTrace();
        }
    }

    public static void debug(String msg) {
        log(Level.DEBUG, msg, null);
    }
    public static void info(String msg)  {
        log(Level.INFO, msg, null);
    }
    public static void error(String msg, Throwable t) {
        log(Level.ERROR, msg, t);
    }

}
