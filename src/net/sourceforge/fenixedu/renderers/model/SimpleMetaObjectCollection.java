package net.sourceforge.fenixedu.renderers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SimpleMetaObjectCollection implements MultipleMetaObject {

    private List<MetaObject> metaObjects;
    private String schema;
    
    private transient UserIdentity user;

    public SimpleMetaObjectCollection() {
        super();
        
        this.metaObjects = new ArrayList<MetaObject>();
    }

    public List<MetaObject> getAllMetaObjects() {
        return this.metaObjects;
    }

    public void addMetaObject(MetaObject metaObject) {
        this.metaObjects.add(metaObject);
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

    public List<MetaSlot> getSlots() {
        return new ArrayList<MetaSlot>();
    }

    public MetaObjectKey getKey() {
        return null;
    }

    public Properties getProperties() {
        return new Properties();
    }

    public void setProperties(Properties properties) {
    }

    public void setSchema(String name) {
        this.schema = name;
    }
    
    public String getSchema() {
        return this.schema;
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
