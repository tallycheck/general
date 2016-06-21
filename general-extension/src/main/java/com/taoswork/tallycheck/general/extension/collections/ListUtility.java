package com.taoswork.tallycheck.general.extension.collections;

import java.util.List;

/**
 * Created by Gao Yuan on 2015/4/21.
 */
public class ListUtility {
    public static <T> T getTheSingleElement(List<T> list){
        if(list == null || list.size() != 1){
            return null;
        }
        return list.get(0);
    }

    public static <T> void addAll(List<T> list, T... elements){
        for(T ele : elements){
            list.add(ele);
        }
    }

    public static <T> void reversedCopy(List<T> src, List<T> dest){
        dest.clear();
        for(T e : src){
            dest.add(0, e);
        }
    }
}
