package net.sourceforge.fenixedu.renderers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SimpleMetaObjectCollection implements MultipleMetaObject {

    private List<MetaObject> metaObjects;
    
    private transient UserIdentity user;

    public SimpleMetaObjectCollection() {
        super();
        
        this.metaObjects = new ArrayList<MetaObject>();
    }

    public List<MetaObject> getAllMetaObjects() {
        return this.metaObjects;
    }

    public void add(MetaObject metaObject) {
        this.metaObjects.add(metaObject);
    }
    
    public boolean remove(MetaObject metaObject) {
        return this.metaObjects.remove(metaObject);
    }
    
    public UserIdentity getUser() {
        return this.user;
    }

    public void setUser(UserIdentity user) {
        this.user = user;
        
        for (MetaObject metaObject : getAllMetaObjects()) {
            metaObject.setUser(user);
        }
    }

    public Object getObject() {
        List<Object> objects = new ArrayList<Object>();
        
        for (MetaObject metaObject : getAllMetaObjects()) {
            objects.add(metaObject.getObject());
        }
        
        return objects;
    }

    public Class getType() {
        return ArrayList.class;
    }

    @Deprecated
    public List<MetaSlot> getSlots() {
        List<MetaSlot> slots = new ArrayList<MetaSlot>();
        
        for (MetaObject metaObject : getAllMetaObjects()) {
            slots.addAll(metaObject.getSlots());
        }
        
        return slots;
    }

    public void addSlot(MetaSlot slot) {
    }

    public boolean removeSlot(MetaSlot slot) {
        return false;
    }

    public MetaObjectKey getKey() {
        return null;
    }

    public Properties getProperties() {
        return new Properties();
    }

    public void setProperties(Properties properties) {
    }

    public String getSchema() {
        if (getAllMetaObjects().isEmpty()) {
            return null;
        }
        else {
            return this.getAllMetaObjects().get(0).getSchema();
        }
    }

    public List<MetaSlot> getHiddenSlots() {
        return new ArrayList<MetaSlot>();
    }

    public void addHiddenSlot(MetaSlot slot) {
    }

    public void commit() {
        for (MetaObject metaObject : getAllMetaObjects()) {
            metaObject.commit();
        }
    }

}
