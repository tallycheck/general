package com.taoswork.tallycheck.general.solution.logging;

/**
 * Created by Gao Yuan on 2015/4/24.
 */
public interface SupportLoggerAdapter {

    public String getName();

    public void setName(String name);

    /**
     * emit a SUPPORT level message
     * @param message
     */
    public void support(String message);

    /**
     * emit a SUPPORT level message with throwable
     * @param message
     * @param t
     */
    public void support(String message, Throwable t);

    /**
     * emit a SUPPORT lifecycle message
     * @param lifeCycleEvent
     * @param message
     */
    public void lifecycle(LifeCycleEvent lifeCycleEvent, String message);

    /**
     * In order to be backwards compatible. The support logger should also support
     * the debug, error, fatal, info, and warn levels as well.
     * @param message
     */

    public void debug(String message);

    public void debug(String message, Throwable t);

    public void error(String message);

    public void error(String message, Throwable t);

    public void fatal(String message);

    public void fatal(String message, Throwable t);

    public void info(String message);

    public void info(String message, Throwable t);

    public void warn(String message);

    public void warn(String message, Throwable t);
}
