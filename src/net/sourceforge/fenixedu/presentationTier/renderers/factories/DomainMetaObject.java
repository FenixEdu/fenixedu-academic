package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.ObjectChange;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.ObjectKey;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.model.CompositeSlotSetter;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.SimpleMetaObject;

public class DomainMetaObject extends SimpleMetaObject {

    private Class type;
    private int oid;
    
    private transient DomainObject object;

    private String service;

    protected DomainMetaObject() {
        super(null);
        
        setService("UpdateObjects");
    }
    
    public DomainMetaObject(DomainObject object) {
        this();
        
        setObject(object);
    }
    
    @Override
    public Object getObject() {
        if (this.object == null) {
            this.object = getPersistentObject();
        }
        
        return this.object;
    }

    @Override
    protected void setObject(Object object) {
        this.object = (DomainObject) object;
        
        if (this.object != null) {
            this.type = this.object.getClass();
            this.oid = this.object.getIdInternal().intValue();
        }
    }

    protected DomainObject getPersistentObject() {
        return RootDomainObject.getInstance().readDomainObjectByOID(getType(), getOid());
    }

    protected IUserView getUserView() {
        return ((FenixUserIdentity) getUser()).getUserView();
    }
    
    public int getOid() {
        return oid;
    }
    
    protected void setOid(int oid) {
        this.oid = oid;
    }

    @Override
    public Class getType() {
        return type;
    }

    protected void setType(Class type) {
        this.type = type;
    }

    @Override
    public MetaObjectKey getKey() {
        return new MetaObjectKey(getType(), getOid());
    }

    @Override
    public void commit() {
        List<ObjectChange> changes = new ArrayList<ObjectChange>();
        
        ObjectKey key = new ObjectKey(getOid(), getType());

        for (MetaSlot slot : getAllSlots()) {
            if (slot.isSetterIgnored()) {
                continue;
            }
            
            if (slot.isCached()) {
                Object change = slot.getObject();
                
                ObjectChange objectChange = new ObjectChange(key, slot.getName(), change);
                changes.add(objectChange);
            }
        }
 
        for (CompositeSlotSetter compositeSetter : getCompositeSetters()) {
            try {
                changes.add(new ObjectChange(key, compositeSetter.getSetter(getType()), compositeSetter.getArgumentValues()));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("could not find specialized setter", e);
            }
        }
        
        callService(changes);
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    protected Object callService(List<ObjectChange> changes) {
        try {
            return ServiceUtils.executeService(getUserView(), getService(), getServiceArguments(changes));
        } 
        catch (RuntimeException e) {
            throw e;
        } catch (FenixServiceException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                if (e.getCause().getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause().getCause();
                }
            }
            
            throw new DomainException("domain.metaobject.service.failed", e);
        }
        catch (Exception e) {
            throw new DomainException("domain.metaobject.service.failed", e);
        }
    }

    protected Object[] getServiceArguments(List<ObjectChange> changes) {
        return new Object[] { changes };
    }

}
