package net.sourceforge.fenixedu.renderers.model;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

public interface MetaObject extends Serializable {
    //
    // Meta factory responsability, not settable in a general way
    //
    
    public Object getObject();
    public Class getType();
    public String getSchema();
    public MetaObjectKey getKey();

    public List<MetaSlot> getSlots();
    public void addSlot(MetaSlot slot);
    public boolean removeSlot(MetaSlot slot);

    //
    // Interaction with framework
    //
    
    public UserIdentity getUser();
    public void setUser(UserIdentity user);
    
    public List<MetaSlot> getHiddenSlots();
    public void addHiddenSlot(MetaSlot slot);
    
    public Properties getProperties();
    public void setProperties(Properties properties);
    
    //
    // Transactional environment
    // TODO: cfgi, how to allow two commits to be transactional
    //
    
    public void commit();
}
