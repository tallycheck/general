package com.taoswork.tallybook.general.solution.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gao Yuan on 2015/4/21.
 */
public class EntityInterfaceManager {

    private Map<String, Class<?>> entityInterface2Class = new HashMap<String, Class<?>>();

    protected EntityInterfaceManager(){
    }

    private static EntityInterfaceManager _instance = new EntityInterfaceManager();
    public static EntityInterfaceManager instance(){
        return _instance;
    }

    public <TI, TC extends TI> EntityInterfaceManager registEntity(Class<TI> entityInterface, Class<TC> entityClass){
        entityInterface2Class.put(entityInterface.getName(), entityClass);
        return  this;
    }

    public <TC> EntityInterfaceManager registEntity(String entityInterfaceName, Class<TC> entityClass){
        entityInterface2Class.put(entityInterfaceName, entityClass);
        return  this;
    }

    public Class<?> lookupEntityClass(String entityInterfaceName){
        return this.entityInterface2Class.get(entityInterfaceName);
    }

    public <T> Class<? extends T> lookupEntityClass(Class<T> entityInterfaceType){
        return (Class<T> )this.entityInterface2Class.get(entityInterfaceType.getName());
    }

}
