package com.taoswork.tallycheck.general.solution.reference;

import org.apache.commons.beanutils.PropertyUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by gaoyuan on 7/19/16.
 */
public class EntityReference implements Serializable {
    private final Object holder;
    private final String holdingField;
    private final EntityId entityId;

    public EntityReference(Object holder, String holdingField, EntityId entityId) {
        this.holder = holder;
        this.holdingField = holdingField;
        this.entityId = entityId;
    }

    public EntityId getEntityId() {
        return entityId;
    }

    void setRecord(Object entityRecord) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PropertyUtils.setSimpleProperty(holder, holdingField, entityRecord);
    }
}
