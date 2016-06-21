package com.taoswork.tallycheck.general.solution.logging.adapter;


import com.taoswork.tallycheck.general.solution.logging.LifeCycleEvent;
import com.taoswork.tallycheck.general.solution.logging.SupportLoggerAdapter;
import com.taoswork.tallycheck.general.solution.setting.CachedSetting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gao Yuan on 2015/4/24.
 */
public class SystemSupportLoggerAdapter extends AbstractSupportLoggerAdapter implements SupportLoggerAdapter {

    public static final String SHOW_DATE_TIME_KEY = "SystemSupportLoggerAdapter.showDateTime";
    public static final String DATE_TIME_FORMAT_KEY = "SystemSupportLoggerAdapter.dateTimeFormat";
    public static final String SHOW_THREAD_NAME_KEY = "SystemSupportLoggerAdapter.showThreadName";
    public static final String SHOW_LOG_NAME_KEY = "SystemSupportLoggerAdapter.showLogName";
    public static final String LEVEL_IN_BRACKETS_KEY = "SystemSupportLoggerAdapter.levelInBrackets";
    public static final String SHOW_NON_SUPPORT_LEVELS_KEY = "SystemSupportLoggerAdapter.showNonSupportLevels";

    private static class LoggerSetting extends CachedSetting {
        public LoggerSetting() {
            this.registerBoolProperty(SHOW_DATE_TIME_KEY, true, true)
                    .registerDateFormatProperty(DATE_TIME_FORMAT_KEY, new SimpleDateFormat("HH:mm:ss"), false)
                    .registerBoolProperty(SHOW_THREAD_NAME_KEY, false, true)
                    .registerBoolProperty(SHOW_LOG_NAME_KEY, true, true)
                    .registerBoolProperty(LEVEL_IN_BRACKETS_KEY, true, true)
                    .registerBoolProperty(SHOW_NON_SUPPORT_LEVELS_KEY, false, true);
        }

        public boolean getShowLevelInBrackets() {
            return this.getBoolean(LEVEL_IN_BRACKETS_KEY);
        }

        public boolean getShowNonSupportLevels() {
            return this.getBoolean(SHOW_NON_SUPPORT_LEVELS_KEY);
        }

        public boolean getShowThreadName() {
            return this.getBoolean(SHOW_THREAD_NAME_KEY);
        }

        public boolean getShowLogName() {
            return this.getBoolean(SHOW_LOG_NAME_KEY);
        }

        public boolean getShowDateTime() {
            return this.getBoolean(SHOW_DATE_TIME_KEY)
                    && (this.getDateFormat(DATE_TIME_FORMAT_KEY) != null);
        }

        protected String getFormattedDate() {
            Date now = new Date();
            String dateText;
            DateFormat dateFormat = this.getDateFormat(DATE_TIME_FORMAT_KEY);
            dateText = dateFormat.format(now);
            return dateText;
        }
    }

    private String name;
    private LoggerSetting setting = new LoggerSetting();


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Generate a SUPPORT level log message
     *
     * @param message the log message
     */
    @Override
    public void support(String message) {
        log(LOG_LEVEL_SUPPORT, message, null);
    }

    /**
     * Generate a SUPPORT level log message with an accompanying Throwable
     *
     * @param message the log message
     * @param t       the exception to accompany the log message - will result in a stack track in the log
     */
    @Override
    public void support(String message, Throwable t) {
        log(LOG_LEVEL_SUPPORT, message, t);
    }

    /**
     * Generate a specialized SUPPORT level log message that includes a LifeCycleEvent
     * in the message.
     *
     * @param lifeCycleEvent The module life cycle type for this log message
     * @param message        the log message
     */
    @Override
    public void lifecycle(LifeCycleEvent lifeCycleEvent, String message) {
        log(LOG_LEVEL_SUPPORT, message, null);
    }

    @Override
    public void debug(String message) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_DEBUG, message, null);
        }
    }

    @Override
    public void debug(String message, Throwable t) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_DEBUG, message, t);
        }
    }

    @Override
    public void error(String message) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_ERROR, message, null);
        }
    }

    @Override
    public void error(String message, Throwable t) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_ERROR, message, t);
        }
    }

    @Override
    public void fatal(String message) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_FATAL, message, null);
        }
    }

    @Override
    public void fatal(String message, Throwable t) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_FATAL, message, t);
        }
    }

    @Override
    public void info(String message) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_INFO, message, null);
        }
    }

    @Override
    public void info(String message, Throwable t) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_INFO, message, t);
        }
    }

    @Override
    public void warn(String message) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_WARN, message, null);
        }
    }

    @Override
    public void warn(String message, Throwable t) {
        if (setting.getShowNonSupportLevels()) {
            log(LOG_LEVEL_WARN, message, t);
        }
    }

    protected void log(int level, String message, Throwable t) {

        StringBuffer buf = new StringBuffer(32);

        // Append a readable representation of the log level
        buf.append(setting.getShowLevelInBrackets() ? levelNameInBracket(level) : levelName(level));

        // Append date-time if so configured
        if (setting.getShowDateTime()) {
            buf.append(setting.getFormattedDate());
            buf.append(' ');
        }

        // Append current thread name if so configured
        if (setting.getShowThreadName()) {
            buf.append('[');
            buf.append(Thread.currentThread().getName());
            buf.append("] ");
        }

        // Append the name of the log instance if so configured
        if (setting.getShowLogName()) {
            buf.append(String.valueOf(name)).append(" - ");
        }

        // Append the message
        buf.append(message);

        write(buf, t);

    }

    protected void write(StringBuffer buf, Throwable t) {
        System.out.println(buf.toString());
        if (t != null) {
            t.printStackTrace(System.out);
        }
        System.out.flush();
    }
}
