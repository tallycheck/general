package com.taoswork.tallybook.general.solution.threading;

import com.taoswork.tallybook.general.solution.quickinterface.IValueMaker;

/**
 * Created by Gao Yuan on 2015/8/16.
 */
public class ThreadLocalHelper {
    public static <T> ThreadLocal<T> createThreadLocal(final Class<T> type, final IValueMaker<T> valueMaker) {
        ThreadLocal<T> result = new ThreadLocal<T>() {
            @Override
            protected T initialValue() {
                if(valueMaker != null){
                    return valueMaker.make();
                }
                return super.initialValue();
            }
        };
        return result;
    }

    public static <T> ThreadLocal<T> createThreadLocal(final Class<T> type, final boolean createInitialValue) {
        IValueMaker<T> valueMaker = createInitialValue ? new IValueMaker<T>() {
            @Override
            public T make() {
                T obj = null;
                try {
                    obj = type.newInstance();
                } catch (InstantiationException exp) {
                    throw new RuntimeException(exp);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return obj;
            }
        } : null;
        return createThreadLocal(type, valueMaker);
    }

    public static <T> ThreadLocal<T> createThreadLocal(final Class<T> type) {
        return createThreadLocal(type, true);
    }

}
