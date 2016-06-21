package com.taoswork.tallycheck.general.solution.autotree;

/**
 * Created by Gao Yuan on 2015/7/3.
 */
public class AutoTreeAccessorSetting {
    boolean allowKid = true;
    boolean allowParent = false;
    boolean allowBranch = false;

    public boolean isAllowKid() {
        return allowKid;
    }

    public AutoTreeAccessorSetting setAllowKid(boolean allowKid) {
        this.allowKid = allowKid;
        return this;
    }

    public boolean isAllowParent() {
        return allowParent;
    }

    public AutoTreeAccessorSetting setAllowParent(boolean allowParent) {
        this.allowParent = allowParent;
        return this;
    }

    public boolean isAllowBranch() {
        return allowBranch;
    }

    public AutoTreeAccessorSetting setAllowBranch(boolean allowBranch) {
        this.allowBranch = allowBranch;
        if(allowBranch){
            allowAll();
        }
        return this;
    }

    public AutoTreeAccessorSetting allowAll(){
        allowKid = true;
        allowParent = true;
        allowBranch = true;
        return this;
    }

    public AutoTreeAccessorSetting denyAll(){
        allowKid = false;
        allowParent = false;
        allowBranch = false;
        return this;
    }
}
