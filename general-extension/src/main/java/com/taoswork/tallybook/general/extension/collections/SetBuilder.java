package com.taoswork.tallybook.general.extension.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Gao Yuan on 2015/4/21.
 */
public class SetBuilder<E> implements Set<E>{
    private final Set<E> innerSet;

    public SetBuilder(){
        this(new HashSet<E>());
    }

    public SetBuilder(Set<E> set){
        innerSet = set;
    }

    @Override
    public int size() {
        return innerSet.size();
    }

    @Override
    public boolean isEmpty() {
        return innerSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return innerSet.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return innerSet.iterator();
    }

    @Override
    public Object[] toArray() {
        return innerSet.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return innerSet.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return innerSet.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return innerSet.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return innerSet.removeAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return innerSet.addAll(c);
    }

    public void addAll(E... cs) {
        for(E e : cs){
            innerSet.add(e);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return innerSet.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return innerSet.retainAll(c);
    }

    @Override
    public void clear() {
        innerSet.clear();
    }

    public SetBuilder<E> append(E element) {
        this.add(element);
        return this;
    }
}
