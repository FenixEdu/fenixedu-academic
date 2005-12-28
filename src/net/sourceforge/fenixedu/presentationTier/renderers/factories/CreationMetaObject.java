package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dml.DomainClass;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.ObjectKey;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.UpdateObjects.ObjectChange;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;

public class CreationMetaObject implements MetaObject {

    private List<MetaSlot> slots;
    private Class type;
    private Properties properties;
    private transient UserIdentity userIdentity;
    private String schema;
    
    public CreationMetaObject(DomainClass domainClass) {
        this.slots = new ArrayList<MetaSlot>();
        this.properties = new Properties();
        
        try {
            this.type = Class.forName(domainClass.getFullName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("could not find concrete class for domain class "+ domainClass);
        }
    }

    public void setUser(UserIdentity user) {
        this.userIdentity = user;
    }
    
    private IUserView getUserView() {
        return ((FenixUserIdentity) this.userIdentity).getUserView();
    }

    public Object getObject() {
        return MetadataManager.getDomainModel().findClass(getType().getName());
    }

    public Class getType() {
        return type;
    }

    public void addSlot(MetaSlot metaSlot) {
        this.slots.add(metaSlot);
    }

    public List<MetaSlot> getSlots() {
        return slots;
    }

    public MetaObjectKey getKey() {
        return new CreationMetaObjectKey(getType());
    }

    public Properties getProperties() {
        return properties;
    }

    public void commit() {
        List<ObjectChange> changes = new ArrayList<ObjectChange>();
        
        for (MetaSlot slot : getSlots()) {
            CachedMetaSlot cachedSlot = (CachedMetaSlot) slot;
            
            Object change = cachedSlot.getCachedObject();
            if (change != null) {
                ObjectKey key = new ObjectKey(0, getType());
                ObjectChange objectChange = new ObjectChange(key, cachedSlot.getName(), change);
                changes.add(objectChange);
            }
        }
 
        // TODO: do something with the exception
        try {
            ServiceUtils.executeService(getUserView(), "CreateObjects", new Object[] { changes });
        } catch (FenixFilterException e) {
            e.printStackTrace();
        } catch (FenixServiceException e) {
            e.printStackTrace();
        }

    }

    public void setSchema(String name) {
        this.schema = name;
    }

    public String getSchema() {
        return schema;
    }
}
