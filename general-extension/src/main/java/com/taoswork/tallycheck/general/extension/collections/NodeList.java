package com.taoswork.tallycheck.general.extension.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gao Yuan on 2015/5/28.
 */

public class NodeList<T>{
    private final List<T> mNodes;
    protected final boolean mSkipNull;
    protected final boolean mSkipSame;

    public NodeList(boolean skipNull, boolean skipSame) {
        mSkipNull = skipNull;
        mSkipSame = skipSame;
        mNodes = new ArrayList<T>();
    }

    public NodeList() {
        this(true, true);
    }

    public NodeList(boolean skipNull, boolean skipSame, Iterable<T> nodes) {
        this(skipNull, skipSame);
        this.addAll(nodes);
    }

    public NodeList(Iterable<T> nodes) {
        this(true, true);
        this.addAll(nodes);
    }

    public void preAdd(T node){
    }

    public void postRemove(T node){
    }

    public NodeList<T> add(T node) {
        if (mSkipNull && node == null)
            return this;
        if (mSkipSame) {
            if (mNodes.contains(node))
                return this;
        }
        preAdd(node);
        mNodes.add(node);
        return this;
    }

    public NodeList<T> addAll(Iterable<T> nodes) {
        for(T node : nodes){
            mNodes.add(node);
        }
        return this;
    }

    public NodeList<T> remove(T node) {
        if (mSkipNull && node == null)
            return this;
        mNodes.remove(node);
        postRemove(node);
        return this;
    }

    public NodeList<T> clear() {
        while(!mNodes.isEmpty()){
            T node = mNodes.get(0);
            this.remove(node);
        }
        return this;
    }

    public boolean isEmpty(){
        return mNodes.isEmpty();
    }

    public int count() {
        return mNodes.size();
    }

    public T getAt(int idx){
        return mNodes.get(idx);
    }

    public Iterable<T> iterator() {
        return mNodes;
    }

    @Override
    public String toString() {
        StringChain<String> sc = new StringChain<String>();
        for(T node : mNodes){
            String ns = node.toString();
            sc.add(ns);
        }
        return sc.toString();
    }

}
