package net.sourceforge.fenixedu.renderers.model;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

public interface MetaObject extends Serializable {
    public void setUser(UserIdentity user);
    public Object getObject();
    public Class getType();
    public List<MetaSlot> getSlots();
    public String getSchema();
    public MetaObjectKey getKey();
    public Properties getProperties();
    public void commit();
}
