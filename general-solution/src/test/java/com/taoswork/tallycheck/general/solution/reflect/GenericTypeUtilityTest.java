package com.taoswork.tallycheck.general.solution.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Gao Yuan on 2015/6/18.
 */
public class GenericTypeUtilityTest {
    @Test
    public void genericTypeTest() {
        Assert.assertFalse(GenericTypeUtility.isGenericType(Integer.class));
        Assert.assertFalse(GenericTypeUtility.isGenericType(String.class));

        Assert.assertTrue(GenericTypeUtility.isGenericType(List.class));
        Assert.assertTrue(GenericTypeUtility.isGenericType(ArrayList.class));

        Assert.assertTrue(GenericTypeUtility.isGenericType(Set.class));
        Assert.assertTrue(GenericTypeUtility.isGenericType(HashSet.class));

        Assert.assertTrue(GenericTypeUtility.isGenericType(Map.class));
        Assert.assertTrue(GenericTypeUtility.isGenericType(HashMap.class));
    }

    @Test
    public void genericTypeArgumentTest() {
        Assert.assertFalse(
                GenericTypeUtility.isTypeArgumentMissing(
                        Integer.class));

        checkField("_List", false);
        checkField("_List_6Integer9", true);
        checkField("_ArrayList_6Integer9", true);
        checkField("_List_6List_6Integer9_9", true);
        checkField("_List_6List9", false);

        checkField("_Map", false);
        checkField("_Map_6String0String9", true);
        checkField("_HashMap_6String0String9", true);
        checkField("_Map_6String0List9", false);
        checkField("_Map_6List0String9", false);
        checkField("_Map_6List0List9", false);
        checkField("_Map_6String0List_6String9_9", true);
        checkField("_Map_6List_6String9_0String9", true);
        checkField("_Map_6List0List_6String9_9", false);
        checkField("_Map_6List_6String9_0List9", false);
        checkField("_Map_6List_6String9_0List_6String9_9", true);

    }

    private void checkField(String fieldName, boolean isFullfilled){
        try {
            Field field = XXX.class.getField(fieldName);
            boolean hasMissing = GenericTypeUtility.isTypeArgumentMissing(field);
            Assert.assertNotEquals("Check Field Failed: " + fieldName, isFullfilled, hasMissing);
        } catch(Exception e){
            Assert.fail("Check Field Failed with Exception: " + fieldName + " " + e);
        }
    }

    static class XXX{
        public List _List;
        public List<Integer> _List_6Integer9;
        public ArrayList<Integer> _ArrayList_6Integer9;
        public List<List<Integer>> _List_6List_6Integer9_9;
        public List<List> _List_6List9;

        public Set _Set;
        public Set<Integer> _Set_6Integer9;
        public HashSet<Integer> _HashSet_6Integer9;

        public Map _Map;
        public Map<String, String> _Map_6String0String9;
        public HashMap<String, String> _HashMap_6String0String9;

        public Map<String, List> _Map_6String0List9;
        public Map<List, String> _Map_6List0String9;
        public Map<List, List> _Map_6List0List9;
        public Map<String, List<String>> _Map_6String0List_6String9_9;
        public Map<List<String>, String> _Map_6List_6String9_0String9;
        public Map<List, List<String>> _Map_6List0List_6String9_9;
        public Map<List<String>, List> _Map_6List_6String9_0List9;
        public Map<List<String>, List<String>> _Map_6List_6String9_0List_6String9_9;

    }
}