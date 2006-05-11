package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.ObjectKey;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.UpdateObjects.ObjectChange;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;

public class DomainMetaObject implements MetaObject {

    private String schema;
    private Class type;
    private int oid;
    private List<MetaSlot> slots;

    private List<MetaSlot> hiddenSlots;
    private Properties properties;
    
    private transient DomainObject object;
    private transient UserIdentity userIdentity;

    private String service;

    protected DomainMetaObject() {
        this.slots = new ArrayList<MetaSlot>();
        this.hiddenSlots = new ArrayList<MetaSlot>();

        this.properties = new Properties();
        this.service = "UpdateObjects";
    }
    
    public DomainMetaObject(DomainObject object) {
        this();
        
        this.type = object.getClass();
        this.oid = object.getIdInternal().intValue();
        this.object = object;
    }
    
    public Object getObject() {
        if (this.object == null) {
            this.object = getPersistentObject();
        }
        
        return this.object;
    }

    protected DomainObject getPersistentObject() {
        try {
            IUserView userView = getUserView();

            return (DomainObject) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] { getType(), getOid() });
        } catch (FenixFilterException e) {
            e.printStackTrace();
        } catch (FenixServiceException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public void setUser(UserIdentity user) {
        this.userIdentity = user;
        
        List<MetaSlot> allSlots = new ArrayList<MetaSlot>();
        allSlots.addAll(getSlots());
        allSlots.addAll(getHiddenSlots());
        
        for (MetaSlot slot : allSlots) {
            slot.setUser(user);
        }
    }
    
    public UserIdentity getUser() {
        return this.userIdentity;
    }

    protected IUserView getUserView() {
        return ((FenixUserIdentity) getUser()).getUserView();
    }
    
    public int getOid() {
        return oid;
    }
    
    public Class getType() {
        return type;
    }

    public List<MetaSlot> getSlots() {
        return slots;
    }
    
    public void addSlot(MetaSlot metaSlot) {
        this.slots.add(metaSlot);
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

    public MetaObjectKey getKey() {
        return new DomainMetaObjectKey(getOid(), getType());
    }

    public Properties getProperties() {
        return properties;
    }
    
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void commit() {
        List<ObjectChange> changes = new ArrayList<ObjectChange>();
        
        List<MetaSlot> allSlots = new ArrayList<MetaSlot>();
        allSlots.addAll(getSlots());
        allSlots.addAll(getHiddenSlots());
        
        for (MetaSlot slot : allSlots) {
            CachedMetaSlot cachedSlot = (CachedMetaSlot) slot;
            
            Object change = cachedSlot.getCachedObject();
            if (change != null) {
                ObjectKey key = new ObjectKey(getOid(), getType());
                ObjectChange objectChange = new ObjectChange(key, cachedSlot.getName(), change);
                changes.add(objectChange);
            }
        }
 
        callService(changes);
    }

    protected Object callService(List<ObjectChange> changes) {
        try {
            return ServiceUtils.executeService(getUserView(), getService(), new Object[] { changes });
        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            throw new DomainException("domain.metaobject.service.failed", e);
        }
    }

    public void setSchema(String name) {
        this.schema = name;
    }

    public String getSchema() {
        return schema;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

}
