package com.taoswork.tallycheck.general.solution.property;

import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Created by Gao Yuan on 2015/5/22.
 */
public class RuntimePropertiesPublisher {
    private static final RuntimePropertiesPublisher instance = new RuntimePropertiesPublisher();
    private Properties properties = new Properties();

    private RuntimePropertiesPublisher(){
    }

    public static RuntimePropertiesPublisher instance(){
        return instance;
    }

    public void add(Properties props){
        properties.putAll(props);
    }

    public String getString(String propertyKey, String defaultVal){
        return properties.getProperty(propertyKey, defaultVal);
    }

    public long getLong(String propertyKey, long defaultVal){
        String strVal = properties.getProperty(propertyKey, "");
        if(StringUtils.isEmpty(strVal)){
            return defaultVal;
        }
        try {
            Long val = Long.parseLong(strVal);
            return val.longValue();
        } catch (NumberFormatException exp){
            return defaultVal;
        }
    }

    public int getInt(String propertyKey, int defaultVal){
        String strVal = properties.getProperty(propertyKey, "");
        if(StringUtils.isEmpty(strVal)){
            return defaultVal;
        }
        try {
            int val = Integer.parseInt(strVal);
            return val;
        } catch (NumberFormatException exp){
            return defaultVal;
        }
    }

    public boolean getBoolean(String propertyKey, boolean defaultVal){
        String strVal = properties.getProperty(propertyKey, "");
        if(StringUtils.isEmpty(strVal)){
            return defaultVal;
        }
        try {
            boolean val = Boolean.parseBoolean(strVal);
            return val;
        } catch (NumberFormatException exp){
            return defaultVal;
        }
    }
}
