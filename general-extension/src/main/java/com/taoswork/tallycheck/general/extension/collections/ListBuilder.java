package com.taoswork.tallycheck.general.extension.collections;

import java.util.*;

/**
 * Created by Gao Yuan on 2015/4/21.
 */
public class ListBuilder<E> implements List<E>{
    private final List<E> innerList;

    public ListBuilder(){
        innerList = new ArrayList<E>();
    }

    public ListBuilder(List<E> list){
        innerList = list;
    }


    @Override
    public int size() {
        return innerList.size();
    }

    @Override
    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return innerList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return innerList.iterator();
    }

    @Override
    public Object[] toArray() {
        return innerList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return innerList.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return innerList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return innerList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return innerList.containsAll(c);
    }

    public void addAll(E ... cs) {
        for(E c : cs){
            innerList.add(c);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return innerList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return innerList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return innerList.retainAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return innerList.retainAll(c);
    }

    @Override
    public void clear() {
        innerList.clear();
    }

    @Override
    public E get(int index) {
        return innerList.get(index);
    }

    @Override
    public E set(int index, E element) {
        return innerList.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        innerList.add(index, element);
    }

    @Override
    public E remove(int index) {
        return innerList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return innerList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return innerList.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return innerList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return innerList.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return innerList.subList(fromIndex, toIndex);
    }

    public ListBuilder<E> append(E e){
        innerList.add(e);
        return this;
    }

    public ListBuilder<E> append(E... elements){
        for(E e : elements) {
            innerList.add(e);
        }
        return this;
    }
}
