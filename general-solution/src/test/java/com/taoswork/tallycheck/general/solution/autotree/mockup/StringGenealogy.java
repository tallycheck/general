package com.taoswork.tallycheck.general.solution.autotree.mockup;

import com.taoswork.tallycheck.general.solution.autotree.AutoTreeGenealogy;
import com.taoswork.tallycheck.general.solution.threading.annotations.ThreadSafe;

/**
 * Created by Gao Yuan on 2015/5/22.
 */
@ThreadSafe
public class StringGenealogy extends AutoTreeGenealogy<String> {

    @Override
    public String calcDirectSuper(String a, String referenceSuper) {
        if (a != null && a.indexOf(referenceSuper) == 0 && a.length() > referenceSuper.length()) {
            return new String(a.substring(0, a.length() - 1));
        }
        return null;
    }

    @Override
    public String calcDirectSuperRegardBranch(String a, String referenceBranch) {
        if (a != null && a.indexOf(referenceBranch) == 0 && a.length() > referenceBranch.length()) {
            return null;
        }
        return new String(a.substring(0, a.length() - 1));
    }

    @Override
    public boolean isSuperOf(String a, String b) {
        if (b.indexOf(a) == 0) {
            if (b.length() > a.length()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkEqual(String a, String b) {
        return a.equals(b);
    }
}
