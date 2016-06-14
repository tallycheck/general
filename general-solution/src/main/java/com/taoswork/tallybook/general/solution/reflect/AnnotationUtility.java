package com.taoswork.tallybook.general.solution.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by Gao Yuan on 2016/2/20.
 */
public abstract class AnnotationUtility {
    public static boolean isFieldAnnotated(Field f, String annotationSymbol) {
        Annotation[] annotations = f.getDeclaredAnnotations();
        return isAnnotated(annotationSymbol, annotations);
    }

    public static boolean isClassAnnotated(Class c, String annotationSymbol) {
        Annotation[] annotations = c.getDeclaredAnnotations();
        return isAnnotated(annotationSymbol, annotations);
    }

    private static boolean isAnnotated(String annotationSymbol, Annotation[] annotations) {
        annotationSymbol = annotationSymbol.toLowerCase();
        if (annotations != null) {
            for (Annotation ann : annotations) {
                if (ann.annotationType().getSimpleName().toLowerCase().equals(annotationSymbol)) {
                    return true;
                }
            }
        }
        return false;
    }

}
