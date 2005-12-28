package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.ObjectKey;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.UpdateObjects.ObjectChange;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;

public class DomainMetaObject implements MetaObject {

    private Class type;
    private int oid;
    private List<MetaSlot> slots;
    private Properties properties;

    private transient IDomainObject object;
    private transient UserIdentity userIdentity;
    private String schema;

    public DomainMetaObject(IDomainObject object) {
        this.type = object.getClass();
        this.oid = object.getIdInternal().intValue();
        this.properties = new Properties();
        this.slots = new ArrayList<MetaSlot>();
        this.object = object;
    }
    
    public void setUser(UserIdentity user) {
        this.userIdentity = user;
    }

    public IDomainObject getObject() {
        if (this.object == null) {
            this.object = getPersistentObject();
        }
        
        return this.object;
    }

    private IDomainObject getPersistentObject() {
        try {
            IUserView userView = getUserView();

            return (IDomainObject) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] { getType(), getOid() });
        } catch (FenixFilterException e) {
            e.printStackTrace();
        } catch (FenixServiceException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private IUserView getUserView() {
        return ((FenixUserIdentity) this.userIdentity).getUserView();
    }

    public int getOid() {
        return oid;
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
        return new DomainMetaObjectKey(getOid(), getType());
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
                ObjectKey key = new ObjectKey(getOid(), getType());
                ObjectChange objectChange = new ObjectChange(key, cachedSlot.getName(), change);
                changes.add(objectChange);
            }
        }
 
        // TODO: do something with the exception
        try {
            ServiceUtils.executeService(getUserView(), "UpdateObjects", new Object[] { changes });
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
