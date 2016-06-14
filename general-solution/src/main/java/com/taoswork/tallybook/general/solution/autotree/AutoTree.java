package com.taoswork.tallybook.general.solution.autotree;

import com.taoswork.tallybook.general.extension.collections.CollectionUtility;
import com.taoswork.tallybook.general.extension.utils.TPredicate;
import com.taoswork.tallybook.general.solution.quickinterface.ICallback;
import com.taoswork.tallybook.general.solution.quickinterface.ICallback2;
import com.taoswork.tallybook.general.solution.quickinterface.IToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gao Yuan on 2015/7/3.
 */
public class AutoTree <D> implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoTree.class);

    protected final D data;
    protected AutoTree<D> parent;
    protected final List<AutoTree<D>> kids = new ArrayList<AutoTree<D>>();

    public AutoTree(D data){
        this.data = data;
    }

    public D getData(){
        return data;
    }

    public AutoTree<D> getParent(){
        return parent;
    }

    public boolean isRoot(){
        return parent == null;
    }
    public <T extends AutoTree<D>> T getRoot(){
        AutoTree<D> node = this;
        while (node.parent != null){
            node = node.parent;
        }
        return (T)node;
    }

    public Collection<AutoTree<D>> getReadonlyChildren(){
        return Collections.unmodifiableCollection(this.kids);
    }

    <N extends AutoTree<D>> AddResult<N> safeDirectAddKid(AutoTree<D> candidate, AutoTreeGenealogy<D> genealogy){
        final AutoTree<D> node = candidate;
        D expectedSameAsThis = genealogy.calcDirectSuper(node.data, this.data);
        if(genealogy.checkEqual(expectedSameAsThis, this.data)){
            AutoTree<D> shouldNoMatch = CollectionUtility.find(kids, new TPredicate<AutoTree<D>>() {
                @Override
                public boolean evaluate(AutoTree<D> notNullObj) {
                    return notNullObj.data.equals(node.data);
                }
            });
            if(shouldNoMatch == null){
                node.parent = this;
                this.kids.add(node);
                return new AddResult<N>(AddResult.Type.Success, (N)node);
            }else {
                return new AddResult<N>(AddResult.Type.Exist, (N)shouldNoMatch);
            }
        }
        return new AddResult<N>(AddResult.Type.Fail, null);
    }

    <N extends AutoTree<D>> AddResult<N> safeDirectAddParent(AutoTree<D> candidate, AutoTreeGenealogy<D> genealogy){
        final AutoTree<D> node = candidate;
        if(this.parent != null){
            if(genealogy.checkEqual(this.parent.data, candidate.data)){
                return new AddResult<N>(AddResult.Type.Exist, (N)this.parent);
            }else {
                return new AddResult<N>(AddResult.Type.Fail, null);
            }
        }
        D expected = genealogy.calcDirectSuper(this.data, node.data);
        if(genealogy.checkEqual(expected, node.data)){
            AddResult<N> tempResult = candidate.safeDirectAddKid(this, genealogy);
            if(tempResult.isOk()){
                return new AddResult<N>(tempResult.type, (N)candidate);
            }else {
                return tempResult;
            }
        }
        return new AddResult<N>(AddResult.Type.Fail, null);
    }

    public <N extends AutoTree<D>> N findKid(D data, AutoTreeGenealogy genealogy) {
        if (genealogy.checkEqual(this.data, data)) {
            return (N) this;
        }
        if (genealogy.isSuperOf(this.data, data)) {
            for (AutoTree<D> kid : this.kids) {
                N tt = kid.findKid(data, genealogy);
                if (tt != null) {
                    return tt;
                }
            }
        }
        return null;
    }

    public <N extends AutoTree<D>> N find(D data, AutoTreeGenealogy genealogy, boolean searchSuper){
        if(genealogy.checkEqual(this.data, data)){
            return (N)this;
        }
        if(genealogy.isSuperOf(this.data, data)) {
            return findKid(data, genealogy);
        }
        if(searchSuper){
            return this.getRoot().findKid(data, genealogy);
        }else {
            return null;
        }
    }
    public String buildTreeString(boolean useIndent, String delimiter) {
        return this.buildTreeString(useIndent, delimiter, new IToString<D>() {
            @Override
            public String makeString(D data) {
                return data.toString();
            }
        });
    }

    public String buildTreeString(boolean useIndent, String delimiter, IToString<D> toString){
        StringBuilder sb = new StringBuilder();
        printTree(sb, useIndent, 0, delimiter, toString);
        return sb.toString();
    }

    private void printTree(StringBuilder sb, boolean useIndent, int indent, String delimiter, IToString<D> toString){
        sb.append("(" + indent + ")");
        if(useIndent) {
            for (int i = 0; i < indent; i++) {
                sb.append(".");
            }
        }
        sb.append(toString.makeString(data));
        sb.append(delimiter);
        if(null == kids){
            return;
        }
        for (AutoTree<D> kid : kids){
            kid.printTree(sb, useIndent, indent + 1, delimiter, toString);
        }
    }
    
    public static class TraverseControl{
        public boolean shouldBreak = false;
    }

    public void traverse(
            boolean rootToLeaves,
            ICallback<Void, D, AutoTreeException> elementCallback,
            boolean skipRoot){
        final int callableLvl = skipRoot ? 1 : 0;
        this.traverse(rootToLeaves, elementCallback, new ICallableChecker<Integer>() {
            @Override
            public boolean callable(Integer parameter) {
                return parameter.intValue() >= callableLvl;
            }
        });
    }

    public void traverse(
            boolean rootToLeaves,
            ICallback<Void, D, AutoTreeException> elementCallback,
            ICallableChecker<Integer> callable){
        this.traverse(rootToLeaves, elementCallback, callable, 0);
    }

    private void traverse(
            boolean rootToLeaves,
            ICallback<Void, D, AutoTreeException> elementCallback,
            ICallableChecker<Integer> callable,
            int depth) throws AutoTreeException{
        if(rootToLeaves){
            if(callable.callable(depth)){
                elementCallback.callback(data);
            }
        }
        if (kids != null && kids.size() > 0){
            for (AutoTree<D> treeNode : kids){
                treeNode.traverse(rootToLeaves, elementCallback, callable, depth + 1);
            }
        }
        if(!rootToLeaves) {
            if (callable.callable(depth)) {
                elementCallback.callback(data);
            }
        }
    }

    public void traverse(
            boolean rootToLeaves,
            ICallback2<Void, D, TraverseControl, AutoTreeException> elementCallback,
            final boolean skipRoot){
        this.traverse(rootToLeaves, elementCallback, new ICallableChecker<Integer>() {
            @Override
            public boolean callable(Integer parameter) {
                if(skipRoot){
                    return parameter.intValue() > 0;
                }else {
                    return true;
                }
            }
        });
    }

    public void traverse(
            boolean rootToLeaves,
            ICallback2<Void, D, TraverseControl, AutoTreeException> elementCallback,
            ICallableChecker<Integer> canCallChecker){
        this.traverse(rootToLeaves, elementCallback, canCallChecker, new TraverseControl(), 0);
    }

    private void traverse(
            boolean rootToLeaves,
            ICallback2<Void, D, TraverseControl, AutoTreeException> elementCallback,
            ICallableChecker<Integer> canCallChecker,
            TraverseControl traverseControl,
            int depth) throws AutoTreeException{
        if(rootToLeaves){
            if(canCallChecker.callable(depth)){
                elementCallback.callback(data, traverseControl);
                if(traverseControl.shouldBreak){
                    return;
                }
            }
        }
        if (kids != null && kids.size() > 0){
            for (AutoTree<D> treeNode : kids){
                treeNode.traverse(rootToLeaves, elementCallback, canCallChecker, traverseControl, depth + 1);
                if(traverseControl.shouldBreak){
                    return;
                }
            }
        }
        if(!rootToLeaves) {
            if (canCallChecker.callable(depth)) {
                elementCallback.callback(data, traverseControl);
                if(traverseControl.shouldBreak){
                    return;
                }
            }
        }
    }
}
