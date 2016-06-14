package com.taoswork.tallybook.general.solution.logging.adapter;

/**
 * Created by Gao Yuan on 2015/4/24.
 */
public abstract class AbstractSupportLoggerAdapter {

    public static final String TRACE = "TRACE";
    public static final String DEBUG = "DEBUG";
    public static final String INFO = "INFO";
    public static final String WARN = "WARN";
    public static final String ERROR = "ERROR";
    public static final String FATAL = "FATAL";
    public static final String SUPPORT = "SUPPORT";

    public static final int LOG_LEVEL_TRACE = 0;
    public static final int LOG_LEVEL_DEBUG = 10;
    public static final int LOG_LEVEL_INFO = 20;
    public static final int LOG_LEVEL_WARN = 30;
    public static final int LOG_LEVEL_ERROR = 40;
    public static final int LOG_LEVEL_FATAL = 50;
    public static final int LOG_LEVEL_SUPPORT = 60;

    public static String levelName(int level) {
        switch (level) {
            case LOG_LEVEL_TRACE:
                return (TRACE);
            case LOG_LEVEL_DEBUG:
                return (DEBUG);
            case LOG_LEVEL_INFO:
                return (INFO);
            case LOG_LEVEL_WARN:
                return (WARN);
            case LOG_LEVEL_ERROR:
                return (ERROR);
            case LOG_LEVEL_FATAL:
                return (FATAL);
            default:
                return (SUPPORT);
        }
    }

    public static String levelNameInBracket(int level) {
        return "[" + levelName(level) + "] ";
    }
}

