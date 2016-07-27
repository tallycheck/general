package com.taoswork.tallycheck.general.solution.reference;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;

public class ExternalReference implements Serializable {

    /**
     * Entity references grouped by entity-types
     * Map key is entity-type
     */
    private final Map<Class, EntityIds> referencesByType = new HashMap();
    /**
     * Slots referencing other entities
     */
    private final List<EntityReference> referencingSlots = new ArrayList<EntityReference>();

    public void publishReference(Object holder, String holdingField, Class entityType, String id){
        String entityTypeName = entityType.getName();
        referencesByType.putIfAbsent(entityType, new EntityIds(entityTypeName));
        EntityIds references = referencesByType.get(entityType);
        references.pushReference(id);

        EntityId pr = new EntityId(entityTypeName, id);
        referencingSlots.add(new EntityReference(holder, holdingField, pr));
    }

    public boolean hasReference(){
        return !referencingSlots.isEmpty();
    }

    public Map<String, EntityRecords> calcReferenceValue(IEntityRecordsFetcher fetcher) throws EntityFetchException {
        Map<String, EntityRecords> result = new HashMap<String, EntityRecords>();
        for(Map.Entry<Class, EntityIds> entry : referencesByType.entrySet()){
            Class entityType = entry.getKey();
            String entityTypeName = entityType.getName();
            EntityIds entityIds = entry.getValue();
            final EntityRecords entityRecords = new EntityRecords(entityTypeName);
            Collection<Object> ids = entityIds.getIds();
            Map<Object, Object> records = fetcher.fetch(entityType, ids);

            records.forEach(new BiConsumer<Object, Object>() {
                @Override
                public void accept(Object k, Object v) {
                    entityRecords.setRecord(k.toString(), v);
                }
            });

            if(!entityRecords.isEmpty()){
                result.put(entityTypeName, entityRecords);
            }
        }
        return result;
    }

    public void fillReferencingSlots(Map<String, EntityRecords> records) throws EntityFetchException{
        try {
            for (EntityReference slot : referencingSlots) {
                EntityId reference = slot.getEntityId();
                String entityType = reference.getEntityType();
                String entityId = reference.getEntityId();
                Object entityRecord = null;
                EntityRecords typedRecords = records.get(entityType);
                if (typedRecords != null) {
                    entityRecord = typedRecords.getRecord(entityId);
                }
                if (entityRecord != null) {
                    slot.setRecord(entityRecord);
                }
            }
        } catch (IllegalAccessException e) {
            throw new EntityFetchException(e);
        } catch (NoSuchMethodException e) {
            throw new EntityFetchException(e);
        } catch (InvocationTargetException e) {
            throw new EntityFetchException(e);
        }
    }

}
