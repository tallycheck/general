package com.taoswork.tallycheck.general.solution.reference;

import java.lang.reflect.Field;

/**
 * Created by Gao Yuan on 2015/11/5.
 */

/**
 * A field of an object is referencing another entity
 */
class ReferencingSlot {
    private final Object holder;
    private final Field holdingField;
    private final EntityId entityId;

    public ReferencingSlot(Object holder, Field holdingField, EntityId entityId) {
        this.holder = holder;
        this.holdingField = holdingField;
        this.entityId = entityId;
    }

    public EntityId getEntityId() {
        return entityId;
    }

    public void setRecord(Object entityRecord) throws IllegalAccessException {
        holdingField.set(holder, entityRecord);
    }
}
