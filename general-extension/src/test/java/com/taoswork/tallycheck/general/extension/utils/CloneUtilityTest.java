package com.taoswork.tallycheck.general.extension.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

/**
 * Created by Gao Yuan on 2015/6/24.
 */
public class CloneUtilityTest {
    private static class A implements Cloneable {
        public static int cloned = 0;
        private String name;

        public A(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public A setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            cloned ++;
            return super.clone();
        }
    }

    private static class B implements Serializable {
        private String name;

        public B(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public B setName(String name) {
            this.name = name;
            return this;
        }
    }


    private static class C {
        private String name;

        public C(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public C setName(String name) {
            this.name = name;
            return this;
        }
    }

    @Test
    public void cloneableClone() {
        A a = new A("xxx");
        Assert.assertEquals(A.cloned, 0);
        A b = CloneUtility.makeClone(a);
        Assert.assertTrue(a != b);
        Assert.assertEquals(a.name, b.name);
        Assert.assertEquals(A.cloned, 1);
    }

    @Test
    public void serializableClone() {
        B a = new B("xxx");
        B b = CloneUtility.makeClone(a);
        Assert.assertTrue(a != b);
        Assert.assertEquals(a.name, b.name);
    }


    @Test
    public void notSupportedClone() {
        C a = new C("xxx");
        try {
            C b = CloneUtility.makeClone(a);
            Assert.fail();
        } catch (Exception e) {
            //failed as expected
        }
    }
}
