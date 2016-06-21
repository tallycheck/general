package com.taoswork.tallycheck.general.extension.collections;

import java.util.*;

/**
 * Created by Gao Yuan on 2015/4/24.
 */
public class PropertiesUtility {
    /**
     * Get the sub collection of the properties with a specified prefix
     *
     * @param props  collection of source properties set
     * @param prefix
     * @return the sub collection of the properties, (the key's prefix is removed)
     */
    public static Properties listPropertiesWithPrefix(Properties props, String prefix, String replacement) {
        Properties persistenceProps = new Properties();
        for (String key : props.stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                String newKey = replacement + key.substring(prefix.length());
                String value = props.getProperty(key);
                persistenceProps.setProperty(newKey, value);
            }
        }
        return persistenceProps;
    }
//    Properties getSubCollectionProperties(String prefix, String newPrefix);

    public static Properties getSubProperties(Properties src, String prefix){
        Properties persistenceProps = new Properties();
        for (String key : src.stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                String value = src.getProperty(key);
                persistenceProps.setProperty(key, value);
            }
        }
        return persistenceProps;
    }

    public static Properties updateKeyPrefix(Properties src, Properties des,
                                             String prefix, String newPrefix,
                                             boolean removeExisting){
        Map<String, String> persistenceProps = new HashMap<String, String>();
        List<String> removeList = new ArrayList<String>();
        for (String key : src.stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                String newKey = newPrefix + key.substring(prefix.length());
                String value = src.getProperty(key);
                persistenceProps.put(newKey, value);
                if(removeExisting){
                    removeList.add(key);
                }
            }
        }
        if(removeExisting){
            for (String key : removeList){
                src.remove(key);
            }
        }
        if(des == null){
            des = new Properties();
        }
        des.putAll(persistenceProps);
        return des;
    }
}
