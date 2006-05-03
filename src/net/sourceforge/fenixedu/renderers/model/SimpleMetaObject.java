package net.sourceforge.fenixedu.renderers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SimpleMetaObject implements MetaObject {

    private Object object;
    private int code;
    
    private String schema;
    private List<MetaSlot> slots;
    
    private List<MetaSlot> hiddenSlots;
    private Properties properties;

    public SimpleMetaObject(Object object) {
        super();

        this.object = object;
        this.code = object == null ? 0 : object.hashCode();
        
        this.slots = new ArrayList<MetaSlot>();
        this.hiddenSlots = new ArrayList<MetaSlot>();
    }

    public void setUser(UserIdentity user) {
        // no user needed
    }

    public UserIdentity getUser() {
        return null;
    }

    public void setSchema(String name) {
        this.schema = name;
    }

    public String getSchema() {
        return schema;
    }

    public Object getObject() {
        return this.object;
    }

    public Class getType() {
        return this.object.getClass();
    }

    public List<MetaSlot> getSlots() {
        return this.slots;
    }
    
    public void addSlot(MetaSlot slot) {
        this.slots.add(slot);
    }

    public boolean removeSlot(MetaSlot slot) {
        return this.slots.remove(slot);
    }

    public List<MetaSlot> getHiddenSlots() {
        return this.hiddenSlots;
    }
    
    public void addHiddenSlot(MetaSlot slot) {
        this.hiddenSlots.add(slot);
    }

    public SimpleMetaObjectKey getKey() {
        return new SimpleMetaObjectKey(getType(), this.code);
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
 
    public void commit() {
    }
}
