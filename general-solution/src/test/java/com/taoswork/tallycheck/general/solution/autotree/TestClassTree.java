package com.taoswork.tallycheck.general.solution.autotree;

import com.taoswork.tallycheck.general.solution.autotree.mockup.StringTree;
import com.taoswork.tallycheck.general.solution.autotree.mockup.StringTreeAccessor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Gao Yuan on 2015/5/22.
 */
public class TestClassTree {
    private static final String delimiter = "*";

    @Test
    public void addKidBySteps() {
        StringTree a = new StringTree("A");
        StringTreeAccessor accessor = new StringTreeAccessor();
        accessor.allowAll();

        StringTree aa = accessor.add(a, "AA");
        StringTree aaa = accessor.add(a, "AAA");
        StringTree aaaa = accessor.add(a, "AAAA");
        StringTree aab = accessor.add(a, "AAB");
        StringTree bb = accessor.add(a, "BB");

        String xx = a.buildTreeString(false, delimiter);
        String xxx = a.getRoot().buildTreeString(false, delimiter);

        Assert.assertEquals(xx, "(0)A*(1)AA*(2)AAA*(3)AAAA*(2)AAB*");
        Assert.assertEquals(xxx, "(0)*(1)A*(2)AA*(3)AAA*(4)AAAA*(3)AAB*(1)B*(2)BB*");

        Assert.assertSame(aa, accessor.find(a, "AA", false));
        Assert.assertSame(aa, accessor.find(a, "AA", true));
        Assert.assertSame(aaa, accessor.find(a, "AAA", false));
        Assert.assertSame(aaa, accessor.find(a, "AAA", true));
        Assert.assertSame(aaaa, accessor.find(a, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(a, "AAAA", true));
        Assert.assertSame(aab, accessor.find(a, "AAB", false));
        Assert.assertSame(aab, accessor.find(a, "AAB", true));
        Assert.assertNotSame(bb, accessor.find(a, "BB", false));
        Assert.assertSame(bb, accessor.find(a, "BB", true));

        Assert.assertSame(aa, accessor.find(aa, "AA", false));
        Assert.assertSame(aa, accessor.find(aa, "AA", true));
        Assert.assertSame(aaa, accessor.find(aa, "AAA", false));
        Assert.assertSame(aaa, accessor.find(aa, "AAA", true));
        Assert.assertSame(aaaa, accessor.find(aa, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aa, "AAAA", true));
        Assert.assertSame(aab, accessor.find(aa, "AAB", false));
        Assert.assertSame(aab, accessor.find(aa, "AAB", true));
        Assert.assertNotSame(bb, accessor.find(aa, "BB", false));
        Assert.assertSame(bb, accessor.find(aa, "BB", true));

        Assert.assertNotSame(aa, accessor.find(aaa, "AA", false));
        Assert.assertSame(aa, accessor.find(aaa, "AA", true));
        Assert.assertSame(aaa, accessor.find(aaa, "AAA", false));
        Assert.assertSame(aaa, accessor.find(aaa, "AAA", true));
        Assert.assertSame(aaaa, accessor.find(aaa, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aaa, "AAAA", true));
        Assert.assertNotSame(aab, accessor.find(aaa, "AAB", false));
        Assert.assertSame(aab, accessor.find(aaa, "AAB", true));
        Assert.assertNotSame(bb, accessor.find(aaa, "BB", false));
        Assert.assertSame(bb, accessor.find(aaa, "BB", true));

        Assert.assertNotSame(aa, accessor.find(aab, "AA", false));
        Assert.assertSame(aa, accessor.find(aab, "AA", true));
        Assert.assertNotSame(aaa, accessor.find(aab, "AAA", false));
        Assert.assertSame(aaa, accessor.find(aab, "AAA", true));
        Assert.assertNotSame(aaaa, accessor.find(aab, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aab, "AAAA", true));
        Assert.assertSame(aab, accessor.find(aab, "AAB", false));
        Assert.assertSame(aab, accessor.find(aab, "AAB", true));
        Assert.assertNotSame(bb, accessor.find(aab, "BB", false));
        Assert.assertSame(bb, accessor.find(aab, "BB", true));

        Assert.assertNotSame(aa, accessor.find(bb, "AA", false));
        Assert.assertSame(aa, accessor.find(bb, "AA", true));
        Assert.assertNotSame(aaa, accessor.find(bb, "AAA", false));
        Assert.assertSame(aaa, accessor.find(bb, "AAA", true));
        Assert.assertNotSame(aaaa, accessor.find(bb, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(bb, "AAAA", true));
        Assert.assertNotSame(aab, accessor.find(bb, "AAB", false));
        Assert.assertSame(aab, accessor.find(bb, "AAB", true));
        Assert.assertSame(bb, accessor.find(bb, "BB", false));
        Assert.assertSame(bb, accessor.find(bb, "BB", true));
    }

    @Test
    public void addKidJumpSteps() {
        StringTree a = new StringTree("A");
        StringTreeAccessor accessor = new StringTreeAccessor();
        accessor.allowAll();

        StringTree aaaa = accessor.add(a, "AAAA");
        StringTree aaa = accessor.add(a, "AAA");
        StringTree aa = accessor.add(a, "AA");
        StringTree aab = accessor.add(a, "AAB");
        StringTree bbc = accessor.add(a, "BBC");

        String xx = a.buildTreeString(false, delimiter);
        String xxx = a.getRoot().buildTreeString(false, delimiter);

        Assert.assertEquals(xx, "(0)A*(1)AA*(2)AAA*(3)AAAA*(2)AAB*");
        Assert.assertEquals(xxx, "(0)*(1)A*(2)AA*(3)AAA*(4)AAAA*(3)AAB*(1)B*(2)BB*(3)BBC*");

        Assert.assertSame(aaaa, accessor.find(a, "AAAA", false));
        Assert.assertSame(aaa, accessor.find(a, "AAA", false));
        Assert.assertSame(aa, accessor.find(a, "AA", false));
        Assert.assertSame(aab, accessor.find(a, "AAB", false));

        Assert.assertSame(bbc, accessor.find(bbc, "BBC", false));
        Assert.assertSame(bbc, accessor.find(bbc, "BBC", true));
    }

    @Test
    public void addKidDenyParentBranch() {
        StringTree aa = new StringTree("AA");
        StringTreeAccessor accessor = new StringTreeAccessor();
        accessor.denyAll().setAllowKid(true);

        StringTree a = accessor.add(aa, "A");
        Assert.assertNull(a);
        StringTree aaaa = accessor.add(aa, "AAAA");
        StringTree bbc = accessor.add(aa, "BBC");
        Assert.assertNull(bbc);

        String xx = aa.buildTreeString(false, delimiter);
        String xxx = aa.getRoot().buildTreeString(false, delimiter);

        Assert.assertEquals(xx, "(0)AA*(1)AAA*(2)AAAA*");
        Assert.assertEquals(xxx, xx);

        Assert.assertNull(accessor.find(aa, "BBC", false));
        Assert.assertNull(accessor.find(aa, "BBC", true));

        Assert.assertSame(aa, accessor.find(aa, "AA", false));
        Assert.assertSame(aa, accessor.find(aa, "AA", true));
        Assert.assertSame(aaaa, accessor.find(aa, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aa, "AAAA", true));

        Assert.assertNotSame(aa, accessor.find(aaaa, "AA", false));
        Assert.assertSame(aa, accessor.find(aaaa, "AA", true));
        Assert.assertSame(aaaa, accessor.find(aaaa, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aaaa, "AAAA", true));

    }

    @Test
    public void addKidDenyParentBranch2() {
        StringTree aa = new StringTree("AA");
        StringTreeAccessor accessor = new StringTreeAccessor();
        accessor.denyAll().setAllowKid(true);

        StringTree aaaa = accessor.add(aa, "AAAA");
        StringTree a = accessor.add(aa, "A");
        StringTree bbc = accessor.add(aa, "BBC");

        String xx = aa.buildTreeString(false, delimiter);
        String xxx = aa.getRoot().buildTreeString(false, delimiter);

        Assert.assertEquals(xx, "(0)AA*(1)AAA*(2)AAAA*");
        Assert.assertEquals(xxx, xx);


        Assert.assertSame(aaaa, accessor.find(aa, "AAAA", false));
        Assert.assertSame(null, accessor.find(aa, "BBC", false));

    }

    @Test
    public void addDirectParent() {
        StringTree aaaa = new StringTree("AAAA");
        StringTreeAccessor accessor = new StringTreeAccessor();
        accessor.denyAll().setAllowKid(false).setAllowParent(true);

        StringTree aaa =accessor.add(aaaa, "AAA");
        StringTree aa =accessor.add(aaaa, "AA");
        StringTree a =accessor.add(aaaa, "A");
        StringTree aaaaa =accessor.add(aaaa, "AAAAA");
        Assert.assertNull(aaaaa);

        String xx = aaaa.buildTreeString(false, delimiter);
        String xxx = aaaa.getRoot().buildTreeString(false, delimiter);

        Assert.assertEquals(xx, "(0)AAAA*");
        Assert.assertEquals(xxx, "(0)A*(1)AA*(2)AAA*(3)AAAA*");

        Assert.assertSame(a, accessor.find(a, "A", false));
        Assert.assertSame(a, accessor.find(a, "A", true));
        Assert.assertSame(aa, accessor.find(a, "AA", false));
        Assert.assertSame(aa, accessor.find(a, "AA", true));
        Assert.assertSame(aaa, accessor.find(a, "AAA", false));
        Assert.assertSame(aaa, accessor.find(a, "AAA", true));
        Assert.assertSame(aaaa, accessor.find(a, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(a, "AAAA", true));

        Assert.assertNotSame(a, accessor.find(aa, "A", false));
        Assert.assertSame(a, accessor.find(aa, "A", true));
        Assert.assertSame(aa, accessor.find(aa, "AA", false));
        Assert.assertSame(aa, accessor.find(aa, "AA", true));
        Assert.assertSame(aaa, accessor.find(aa, "AAA", false));
        Assert.assertSame(aaa, accessor.find(aa, "AAA", true));
        Assert.assertSame(aaaa, accessor.find(aa, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aa, "AAAA", true));

        Assert.assertNotSame(a, accessor.find(aaa, "A", false));
        Assert.assertSame(a, accessor.find(aaa, "A", true));
        Assert.assertNotSame(aa, accessor.find(aaa, "AA", false));
        Assert.assertSame(aa, accessor.find(aaa, "AA", true));
        Assert.assertSame(aaa, accessor.find(aaa, "AAA", false));
        Assert.assertSame(aaa, accessor.find(aaa, "AAA", true));
        Assert.assertSame(aaaa, accessor.find(aaa, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aaa, "AAAA", true));

        Assert.assertNotSame(a, accessor.find(aaaa, "A", false));
        Assert.assertSame(a, accessor.find(aaaa, "A", true));
        Assert.assertNotSame(aa, accessor.find(aaaa, "AA", false));
        Assert.assertSame(aa, accessor.find(aaaa, "AA", true));
        Assert.assertNotSame(aaa, accessor.find(aaaa, "AAA", false));
        Assert.assertSame(aaa, accessor.find(aaaa, "AAA", true));
        Assert.assertSame(aaaa, accessor.find(aaaa, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aaaa, "AAAA", true));

    }

    @Test
    public void addParent() {
        StringTree aa = new StringTree("AA");
        StringTreeAccessor accessor = new StringTreeAccessor();
        accessor.denyAll().setAllowParent(true);

        StringTree aaaa = accessor.add(aa, "AAAA");
        Assert.assertNull(aaaa);
        StringTree a = accessor.add(aa, "A");
        StringTree bbc = accessor.add(aa, "BBC");
        Assert.assertNull(bbc);

        String xx = aa.buildTreeString(false, delimiter);
        String xxx = aa.getRoot().buildTreeString(false, delimiter);

        Assert.assertEquals(xx, "(0)AA*");
        Assert.assertEquals(xxx, "(0)A*(1)AA*");

        Assert.assertSame(null, accessor.find(aa, "AAAA", false));
        Assert.assertNotSame(a, accessor.find(aa, "A", false));
        Assert.assertSame(a, accessor.find(aa, "A", true));
    }

    @Test
    public void addJumpParent() {
        StringTree aaa = new StringTree("AAA");
        StringTreeAccessor accessor = new StringTreeAccessor();
        accessor.denyAll().setAllowKid(true).setAllowParent(true);

        StringTree a = accessor.add(aaa, "A");
        StringTree aaaa = accessor.add(aaa, "AAAA");
        StringTree bbc = accessor.add(aaa, "BBC");

        String xx = aaa.buildTreeString(false, delimiter);
        String xxx = aaa.getRoot().buildTreeString(false, delimiter);

        Assert.assertEquals(xx, "(0)AAA*(1)AAAA*");
        Assert.assertEquals(xxx, "(0)A*(1)AA*(2)AAA*(3)AAAA*");

        Assert.assertNotSame(a, accessor.find(aaa, "A", false));
        Assert.assertSame(a, accessor.find(aaa, "A", true));
        Assert.assertNotSame(aaa, accessor.find(aaaa, "AAA", false));
        Assert.assertSame(aaa, accessor.find(aaaa, "AAA", true));
        Assert.assertNotSame(a, accessor.find(aaaa, "A", false));
        Assert.assertSame(a, accessor.find(aaaa, "A", true));
    }

    @Test
    public void addBranch()
    {
        StringTree aa = new StringTree("AA");
        StringTreeAccessor accessor = new StringTreeAccessor();
        accessor.denyAll().allowAll();

        StringTree aaaa =  accessor.add(aa, "AAAA");
        StringTree a = accessor.add(aa, "A");
        StringTree bbc = accessor.add(aa, "BBC");

        String xx = aa.buildTreeString(false, delimiter);
        String xxx = aa.getRoot().buildTreeString(false, delimiter);

        Assert.assertEquals(xx, "(0)AA*(1)AAA*(2)AAAA*");
        Assert.assertEquals(xxx, "(0)*(1)A*(2)AA*(3)AAA*(4)AAAA*(1)B*(2)BB*(3)BBC*");

        Assert.assertSame(a, accessor.find(a, "A", false));
        Assert.assertSame(a, accessor.find(a, "A", true));
        Assert.assertSame(aa, accessor.find(a, "AA", false));
        Assert.assertSame(aa, accessor.find(a, "AA", true));
        Assert.assertSame(aaaa, accessor.find(a, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(a, "AAAA", true));
        Assert.assertNotSame(bbc, accessor.find(a, "BBC", false));
        Assert.assertSame(bbc, accessor.find(a, "BBC", true));

        Assert.assertNotSame(a, accessor.find(aa, "A", false));
        Assert.assertSame(a, accessor.find(aa, "A", true));
        Assert.assertSame(aa, accessor.find(aa, "AA", false));
        Assert.assertSame(aa, accessor.find(aa, "AA", true));
        Assert.assertSame(aaaa, accessor.find(aa, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aa, "AAAA", true));
        Assert.assertNotSame(bbc, accessor.find(aa, "BBC", false));
        Assert.assertSame(bbc, accessor.find(aa, "BBC", true));

        Assert.assertNotSame(a, accessor.find(aaaa, "A", false));
        Assert.assertSame(a, accessor.find(aaaa, "A", true));
        Assert.assertNotSame(aa, accessor.find(aaaa, "AA", false));
        Assert.assertSame(aa, accessor.find(aaaa, "AA", true));
        Assert.assertSame(aaaa, accessor.find(aaaa, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(aaaa, "AAAA", true));
        Assert.assertNotSame(bbc, accessor.find(aaaa, "BBC", false));
        Assert.assertSame(bbc, accessor.find(aaaa, "BBC", true));

        Assert.assertNotSame(a, accessor.find(bbc, "A", false));
        Assert.assertSame(a, accessor.find(bbc, "A", true));
        Assert.assertNotSame(aa, accessor.find(bbc, "AA", false));
        Assert.assertSame(aa, accessor.find(bbc, "AA", true));
        Assert.assertNotSame(aaaa, accessor.find(bbc, "AAAA", false));
        Assert.assertSame(aaaa, accessor.find(bbc, "AAAA", true));
        Assert.assertSame(bbc, accessor.find(bbc, "BBC", false));
        Assert.assertSame(bbc, accessor.find(bbc, "BBC", true));
    }
}
