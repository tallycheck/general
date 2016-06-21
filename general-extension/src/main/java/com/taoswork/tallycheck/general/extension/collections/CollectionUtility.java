package com.taoswork.tallycheck.general.extension.collections;

import com.taoswork.tallycheck.general.extension.utils.TPredicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.Collection;

public class CollectionUtility {
    public static <T> T find(Collection<T> collection, final TPredicate<T> predicate){
        return (T) CollectionUtils.find(collection, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                if (object == null) {
                    return false;
                }
                T tObj = (T) object;
                if (null == tObj) {
                    return false;
                }
                return predicate.evaluate(tObj);
            }
        });
    }

    public static <T> boolean isEmpty(Collection<T> collection){
        if(collection == null){
            return true;
        }
        return collection.isEmpty();
    }
}
