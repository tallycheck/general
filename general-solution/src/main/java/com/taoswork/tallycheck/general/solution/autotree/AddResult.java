package com.taoswork.tallycheck.general.solution.autotree;

/**
 * Created by Gao Yuan on 2015/7/3.
 */
class AddResult<N extends AutoTree> {
    public enum Type{
        Fail,
        Exist,
        Success,
    }

    public Type type;
    public N node;

    public AddResult(Type type, N node) {
        this.type = type;
        this.node = node;
    }

    public boolean isOk(){
        return !Type.Fail.equals(type);
    }
}
