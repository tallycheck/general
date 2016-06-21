package com.taoswork.tallycheck.general.extension.collections;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Gao Yuan on 2015/4/16.
 */
public class SetUtility {
    public static <E> boolean containsAny(Set<E> container, Collection<E> elements){
        for(E element : elements){
            if(container.contains(element)){
                return true;
            }
        }
        return false;
    }

}
