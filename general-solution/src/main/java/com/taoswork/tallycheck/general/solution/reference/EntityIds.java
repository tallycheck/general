package com.taoswork.tallycheck.general.solution.reference;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Multiple Entity references by entityType and ids
 */
public class EntityIds implements Serializable{
    private final String entityType;
    private final Set<Object> entityIds = new HashSet<Object>();

    public EntityIds(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void pushReference(Object id) {
        entityIds.add(id);
    }

    public Collection<Object> getIds() {
        return entityIds;
    }
}
