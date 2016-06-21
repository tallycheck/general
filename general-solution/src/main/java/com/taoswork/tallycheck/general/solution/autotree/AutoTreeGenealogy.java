package com.taoswork.tallycheck.general.solution.autotree;

import com.taoswork.tallycheck.general.solution.threading.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gao Yuan on 2015/7/3.
 */
@ThreadSafe
public abstract class AutoTreeGenealogy<D> {
    public abstract D calcDirectSuper(D a, D referenceSuper);

    public abstract D calcDirectSuperRegardBranch(D a, D referenceBranch);

    public abstract boolean isSuperOf(D a, D b);

    public abstract boolean checkEqual(D a, D b);

    //returns a list: (a, b]
    public List<D> superChain(final D a, final D b) {
        List<D> list = new ArrayList<D>();
        if (checkEqual(a, b)) {
            return list;
        }
        D d = b;
        do {
            list.add(0, d);
            d = calcDirectSuper(d, a);
            if (null == d) {
                break;
            }
        } while (!d.equals(a));
        return list;
    }
}
