package net.sourceforge.fenixedu.renderers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SimpleMetaObject implements MetaObject {

    private Object object;
    private Properties properties;
    private List<MetaSlot> slots;
    private String schema;
    
    public SimpleMetaObject(Object object) {
        super();

        this.object = object;
        this.slots = new ArrayList<MetaSlot>();
    }

    public void setUser(UserIdentity user) {
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

    public SimpleMetaObjectKey getKey() {
        return new SimpleMetaObjectKey(getObject(), getType());
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void commit() {
    }

    public void setSchema(String name) {
        this.schema = name;
    }

    public String getSchema() {
        return schema;
    }
}
