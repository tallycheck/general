package com.taoswork.tallybook.general.solution.setting;

import com.taoswork.tallybook.general.solution.setting.getters.PropertyGetter;
import com.taoswork.tallybook.general.solution.setting.getters.PropertyTypedGetter;
import com.taoswork.tallybook.general.solution.setting.getters.StringGetter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gao Yuan on 2015/4/24.
 */
public class CachedSetting {
    private static class CacheableValueGetter<T>{
        boolean cacheable = false;
        T cached = null;
        ValueGetter<T> valueGetter;
        public CacheableValueGetter( ValueGetter valueGetter, boolean cacheable){
            this.valueGetter = valueGetter;
            this.cacheable = cacheable;
        }
        T getValue(){
            if(cacheable){
                if(cached == null){
                    cached = valueGetter.get();
               }
                return cached;
            }else {
                return valueGetter.get();
            }
        }
    }
    private Map<String, CacheableValueGetter> settingGetters = new HashMap<String, CacheableValueGetter>();

    public CachedSetting register(String settingName, StringGetter getter) {
        settingGetters.put(settingName, new CacheableValueGetter(getter, true));
        return this;
    }

    public CachedSetting registerStringProperty(String propertyName, String defaultValue, boolean cacheable){
        settingGetters.put(propertyName,
                new CacheableValueGetter<Boolean>(
                        new PropertyGetter(propertyName, defaultValue),cacheable));
        return this;
    }

    public CachedSetting registerBoolProperty(String propertyName, boolean defaultValue, boolean cacheable) {
        settingGetters.put(propertyName,
                new CacheableValueGetter<Boolean>(
                        new PropertyTypedGetter<Boolean>(propertyName, defaultValue) {
                            @Override
                            protected Boolean convert(String valueString) {
                                return Boolean.parseBoolean(valueString);
                            }
                        }, cacheable));
        return this;
    }

    public CachedSetting registerDateFormatProperty(String propertyName, DateFormat defaultValue, boolean cacheable){
        settingGetters.put(propertyName,
                new CacheableValueGetter<DateFormat>(
                        new PropertyTypedGetter<DateFormat>(propertyName, defaultValue) {
                            @Override
                            protected DateFormat convert(String valueString) {
                                return new SimpleDateFormat(valueString);
                            }
                        }, cacheable));
        return this;
    }

    public boolean getBoolean(String settingName){
        return Boolean.valueOf((Boolean) getValue(settingName));
    }
    public String getString(String settingName){
        return (String)getValue(settingName);
    }
    public DateFormat getDateFormat(String settingName){
        return (DateFormat)getValue(settingName);
    }

    public <T> T getValue(String settingName) {
        CacheableValueGetter vg = settingGetters.get(settingName);
        if (null == vg) {
            return null;
        }
        return (T) vg.getValue();
    }
}
