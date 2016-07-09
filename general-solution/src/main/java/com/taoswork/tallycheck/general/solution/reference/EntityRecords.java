package com.taoswork.tallycheck.general.solution.reference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity records for a particular entity type
 * Having id-value pairs
 */
public class EntityRecords implements Serializable {
    /**
     * The entities' type
     */
    private final String entityType;
    /**
     * Records with id as key
     */
    private final Map<String, Object> records = new HashMap<String, Object>();

    public EntityRecords(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setRecord(String id, Object record) {
        records.put(id, record);
    }

    public Object getRecord(String id) {
        return records.get(id);
    }

    public boolean isEmpty(){
        return records.isEmpty();
    }
}
