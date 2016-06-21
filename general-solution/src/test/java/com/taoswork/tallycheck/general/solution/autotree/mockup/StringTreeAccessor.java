package com.taoswork.tallycheck.general.solution.autotree.mockup;

import com.taoswork.tallycheck.general.solution.autotree.AutoTreeAccessor;

/**
 * Created by Gao Yuan on 2015/5/23.
 */
public class StringTreeAccessor extends AutoTreeAccessor<String, StringTree> {
    public StringTreeAccessor() {
        super(new StringGenealogy());
    }

    @Override
    public StringTree createNode(String code) {
        return new StringTree(code);
    }

}
