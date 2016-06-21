package com.taoswork.tallycheck.general.solution.reflect;

import com.taoswork.tallycheck.general.extension.collections.ListBuilder;

import java.lang.reflect.Field;
import java.util.*;

public class ClassUtility {
    public static Class getSuperClassWithout(Class clz, String fragment){
        if(!clz.getName().toLowerCase().contains(fragment)){
            return clz;
        }
        Class inputClz = clz;
        while (clz.getSimpleName().toLowerCase().contains(fragment)){
            clz = clz.getSuperclass();
            if(clz == null){
                return inputClz;
            }
        }
        return clz;
    }

    private static boolean nullableIsAncestorOf(Class<?> ancestor, Class<?> descendant) {
        if(null == ancestor){
            return true;
        }
        if(ancestor.equals(descendant)){
            return false;
        }
        return ancestor.isAssignableFrom(descendant);
    }

    public static boolean isAncestorOf(Class<?> ancestor, Class<?> descendant) {
        if(ancestor.equals(descendant)){
            return false;
        }
        return ancestor.isAssignableFrom(descendant);
    }

    public static Collection<Class> getAllSupers(Class root,
                                                 Class target,
                                                 boolean _interface,
                                                 boolean _class){
        Collection<Class> result = new HashSet<Class>();
        getAllSupers(root, target, _interface, _class, result);
        return result;
    }

    public static void getAllSupers(Class root,
                                    Class target,
                                    boolean _interface,
                                    boolean _class,
                                    Collection<Class> result){
        if(!nullableIsAncestorOf(root, target)){
            return;
        }
        if(target.isInterface() && _interface){
            result.add(target);
        }
        boolean notInterface = !target.isInterface();
        Class<?> clzSup = target.getSuperclass();

        if(notInterface && clzSup!= null && nullableIsAncestorOf(root, clzSup)){
            if(isAncestorOf(Object.class, clzSup) && _class){
                result.add(clzSup);
            }
            getAllSupers(root, clzSup,
                _interface, _class,
                result);
        }

        for (Class itfType : target.getInterfaces()){
            getAllSupers(root, itfType,
                _interface, _class,
                result);
        }
    }

    public static Collection<Class> getAllSupers(Class root,
                                                 Class[] targets,
                                                 boolean _interface,
                                                 boolean _class){
        List<Class> targetList = new ArrayList<Class>();
        new ListBuilder<Class>(targetList).append(targets);

        return getAllSupers(root, targetList, _interface, _class);
    }

    public static Collection<Class> getAllSupers(Class root,
                                                 Collection<Class> targets,
                                                 boolean _interface,
                                                 boolean _class){
        Set<Class> classes = new HashSet<Class>();
        for(Class cls : targets){
            getAllSupers(root, cls, _interface, _class, classes);
        }
        return classes;
    }

    public static Class[] getSuperClasses(Class clz, boolean reverseOrder) {
        return getSuperClasses(clz, reverseOrder, false);
    }

    public static Class[] getSuperClasses(Class clz, boolean reverseOrder, boolean includeObject) {
        boolean skipObject = !includeObject;
        List<Class> classes = new ArrayList<Class>();
        if (clz != null) {
            do {
                clz = clz.getSuperclass();
                if(Object.class.equals(clz) && skipObject)
                    continue;
                if (clz != null) {
                    if (reverseOrder) {
                        classes.add(0, clz);
                    } else {
                        classes.add(clz);
                    }
                } else {
                    break;
                }
            } while (true);
        }
        return classes.toArray(new Class[classes.size()]);
    }

    public static Field getFieldOfName(Class clz, String fieldName, boolean checkSuper) {
        do {
            try {
                Field field = clz.getDeclaredField(fieldName);
                if (field != null) {
                    return field;
                }
            } catch (NoSuchFieldException e) {
            }
            if (checkSuper) {
                clz = clz.getSuperclass();
            } else {
                return null;
            }
        } while (clz != null);
        return null;
    }

}
