package com.taoswork.tallybook.general.solution.autotree;

import com.taoswork.tallybook.general.solution.threading.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Gao Yuan on 2015/5/22.
 */
public abstract class AutoTreeAccessor<D, N extends AutoTree<D>> extends AutoTreeAccessorSetting {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoTreeAccessor.class);

    private final AutoTreeGenealogy<D> genealogy;

    public abstract N createNode(D d);

    public AutoTreeAccessor(AutoTreeGenealogy<D> genealogy) {
        this.genealogy = genealogy;
    }

    public AutoTreeGenealogy<D> getGenealogy() {
        return genealogy;
    }

    @NotThreadSafe(reason = "This method will update nodes' relationship")
    public N add(N existingNode, D newNodeData) {
        D existingNodeData = existingNode.data;
        if (genealogy.isSuperOf(existingNodeData, newNodeData)) {
            if (allowKid) {
                List<D> superChain = genealogy.superChain(existingNodeData, newNodeData);
                N currentNode = existingNode;
                for (D d : superChain) {
                    D currentD = currentNode.data;
                    D expectedP = genealogy.calcDirectSuper(d, currentD);
                    if (genealogy.checkEqual(expectedP, currentD)) {
                        N newNode = createNode(d);
                        AddResult<N> result = currentNode.safeDirectAddKid(newNode, genealogy);
                        if (result.isOk()) {
                            currentNode = result.node;
                        } else {
                            return null;
                        }
                    }
                }
                return currentNode;
            }
        } else if (genealogy.isSuperOf(newNodeData, existingNodeData)) {
            if (allowParent) {
                List<D> superChain = genealogy.superChain(newNodeData, existingNodeData);
                superChain.add(0, newNodeData);
                superChain.remove(superChain.size() - 1);
                N currentNode = existingNode;
                for (int i = superChain.size() - 1; i >= 0; --i) {
                    D candParent = superChain.get(i);
                    N candNode = createNode(candParent);
                    AddResult<N> result = currentNode.safeDirectAddParent(candNode, genealogy);
                    if (result.isOk()) {
                        currentNode = result.node;
                    } else {
                        return null;
                    }
                }
                return currentNode;
            }
        } else if (allowBranch) {
            D p = genealogy.calcDirectSuperRegardBranch(existingNodeData, newNodeData);
            N newNode = this.add(existingNode, p);
            if (newNode != null) {
                return this.add(newNode, newNodeData);
            }
        }
        return null;
    }

    public N find(N existingNode, D data, boolean searchSuper){
        return existingNode.find(data, this.genealogy, searchSuper);
    }
}