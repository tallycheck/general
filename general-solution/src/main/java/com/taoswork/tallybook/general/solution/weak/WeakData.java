package com.taoswork.tallybook.general.solution.weak;

import java.lang.ref.WeakReference;

/**
 * Created by Gao Yuan on 2015/5/6.
 */
public abstract class WeakData<T> {
    private WeakReference<T> dataHolder = new WeakReference<T>(null);

    public final T get() {
        T data = dataHolder.get();
        if (null != data) {
            return data;
        } else {
            data = createData();
            if(null == data){
                throw new NullPointerException();
            }
            dataHolder = new WeakReference<T>(data);
            return get();
        }
    }

    protected abstract T createData();
}
