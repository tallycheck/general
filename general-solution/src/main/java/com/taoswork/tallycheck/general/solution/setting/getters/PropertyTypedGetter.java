package com.taoswork.tallycheck.general.solution.setting.getters;

import com.taoswork.tallycheck.general.solution.setting.ValueGetter;

/**
 * Created by Gao Yuan on 2015/4/24.
 */
public abstract class PropertyTypedGetter<T> implements ValueGetter<T> {

    private PropertyGetter propertyGetter;
    private T defaultValueObject = null;

    protected PropertyTypedGetter(String propertyName, T defaultValue) {
        propertyGetter = new PropertyGetter(propertyName, null);
        this.defaultValueObject = defaultValue;
    }

    @Override
    public T get() {
        String valueInString = propertyGetter.get();
        if (null == valueInString) {
            return defaultValueObject;
        } else {
            return convert(valueInString);
        }
    }

    protected abstract T convert(String valueString);
}
