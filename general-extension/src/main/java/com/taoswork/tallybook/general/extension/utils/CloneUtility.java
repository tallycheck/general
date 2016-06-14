package com.taoswork.tallybook.general.extension.utils;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by Gao Yuan on 2015/6/24.
 */
public class CloneUtility {
    public static <T> T makeClone(T source) {
        try {
            if(source == null){
                return null;
            } else if (source instanceof Cloneable) {
                Method cloneMethod = source.getClass().getDeclaredMethod("clone");
                return (T) cloneMethod.invoke(source);
            } else if(source instanceof Serializable){
                return (T) SerializationUtils.clone((Serializable) source);
            }
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }

        throw new RuntimeException("Clone not supported: " + source.getClass() + ". (extends Cloneable or Serializable) required.");
    }
}
