package com.taoswork.tallybook.general.extension.utils;

/**
 * Created by Gao Yuan on 2015/4/28.
 */
public interface TPredicate<T> {
    boolean evaluate(T notNullObj);
}
