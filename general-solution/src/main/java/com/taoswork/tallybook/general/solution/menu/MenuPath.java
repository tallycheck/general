package com.taoswork.tallybook.general.solution.menu;

import com.taoswork.tallybook.general.extension.collections.StringChain;
import com.taoswork.tallybook.general.extension.utils.CloneUtility;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuPath implements Serializable{
    private List<String> path = new ArrayList<String>();

    public MenuPath() {
    }

    public MenuPath(List<String> path) {
        this.path.addAll(path);
    }

    public MenuPath(String... path) {
        for(String p : path){
            this.path.add(p);
        }
    }

    public MenuPath pushPath(String path){
        this.path.add(path);
        return this;
    }

    public Collection<String> getPath() {
        return path;
    }

    public MenuPath makeSubPath(String path){
        MenuPath copy = CloneUtility.makeClone(this);
        copy.pushPath(path);
        return copy;
    }

    @Override
    public String toString() {
        StringChain stringChain = new StringChain("", "/", "");
        stringChain.addAll(path);
        return stringChain.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MenuPath)) return false;

        MenuPath path1 = (MenuPath) o;

        return new EqualsBuilder()
            .append(path, path1.path)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(path)
            .toHashCode();
    }
}
