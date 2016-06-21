package com.taoswork.tallycheck.general.solution.setting.getters;

/**
 * Created by Gao Yuan on 2015/4/24.
 */
public class PropertyGetter implements StringGetter {

    protected final String propertyName;
    protected String defaultValue;

    public PropertyGetter(String propertyName, String defaultValue) {
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    @Override
    public String get() {
        return System.getProperty(propertyName, defaultValue);
    }
}
