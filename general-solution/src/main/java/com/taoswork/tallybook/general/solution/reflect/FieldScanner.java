package com.taoswork.tallybook.general.solution.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Gao Yuan on 2016/2/15.
 */
public class FieldScanner {
    public static List<Field> getFields(Class<?> clz,
                                        FieldScanMethod scanMethod) {
        List<Field> fieldList = new ArrayList<Field>();
        getFields(clz, scanMethod, fieldList);

        return fieldList;
    }
    public static void getFields(Class<?> clz,
                                        FieldScanMethod scanMethod, Collection<Field> fieldList) {
        scanHierarchySuperFirst(clz, scanMethod, fieldList);
    }

    private static void scanHierarchySuperFirst(Class<?> clz, FieldScanMethod scanMethod, Collection<Field> fieldList) {
        Class<?> superClz = scanMethod.getTheSuper(clz);

        if (superClz != null) {
            scanHierarchySuperFirst(superClz, scanMethod, fieldList);
        }

        fetchFileds(clz, scanMethod, fieldList);
    }

    private static void scanHierarchyChildFirst(Class<?> clz, FieldScanMethod scanMethod, Collection<Field> fieldList) {
        Class<?> superClz = scanMethod.getTheSuper(clz);

        fetchFileds(clz, scanMethod, fieldList);

        if (superClz != null) {
            scanHierarchyChildFirst(superClz, scanMethod, fieldList);
        }
    }

    private static void fetchFileds(Class<?> clz, FieldScanMethod scanMethod, Collection<Field> fieldList) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            boolean abandon = false;
            int modifiers = field.getModifiers();
            if (scanMethod.isIdField(field)) {
                if (!scanMethod.includeId) {
                    abandon = true;
                }
            }
            if (Modifier.isStatic(modifiers)) {
                if (!scanMethod.includeStatic) {
                    abandon = true;
                }
            }
            if (Modifier.isTransient(modifiers) ||
                    scanMethod.isTransientField(field)) {
                if (!scanMethod.includeTransient) {
                    abandon = true;
                }
            }
            //Version still needed when doing value copy
//            if (field.isAnnotationPresent(Version.class)) {
//                abandon = true;
//            }
            if (!abandon) {
                fieldList.add(field);
            }
        }
    }
}
