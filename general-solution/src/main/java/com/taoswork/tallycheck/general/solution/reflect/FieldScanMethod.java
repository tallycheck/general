package com.taoswork.tallycheck.general.solution.reflect;

import java.lang.reflect.Field;

/**
 * Created by Gao Yuan on 2016/2/15.
 */
public abstract class FieldScanMethod {
    public boolean includeId = false;
    public boolean includeStatic = false;
    public boolean includeTransient = false;
    public boolean scanSuper = true;

    public FieldScanMethod setIncludeId(boolean includeId) {
        this.includeId = includeId;
        return this;
    }

    public FieldScanMethod setIncludeStatic(boolean includeStatic) {
        this.includeStatic = includeStatic;
        return this;
    }

    public FieldScanMethod setIncludeTransient(boolean includeTransient) {
        this.includeTransient = includeTransient;
        return this;
    }

    public FieldScanMethod setScanSuper(boolean scanSuper) {
        this.scanSuper = scanSuper;
        return this;
    }

    public Class<?> getTheSuper(Class<?> clz) {
        if (!this.scanSuper) {
            return null;
        } else {
            return clz.getSuperclass();
        }
    }

    public abstract boolean isIdField(Field field);

    public abstract boolean isTransientField(Field field);
}
