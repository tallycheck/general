package com.taoswork.tallycheck.general.solution.reference;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Single Entity reference by entityType and id
 */
public class EntityId implements Serializable {
    private final String entityType;
    private final String entityId;

    public EntityId(String entityType, String entityId) {
        this.entityType = entityType;
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EntityId)) return false;

        EntityId that = (EntityId) o;

        return new EqualsBuilder()
            .append(entityType, that.entityType)
            .append(entityId, that.entityId)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(entityType)
            .append(entityId)
            .toHashCode();
    }

    @Override
    public String toString() {
        return entityType + ':' + entityId;
    }
}
