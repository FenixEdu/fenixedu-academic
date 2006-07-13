package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.PropertyUtils;

public class UpdateObjects extends Service {
    
    public Collection run(List<ObjectChange> changes) throws ExcepcaoPersistencia, ClassNotFoundException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        beforeRun(changes);
        
        Hashtable<ObjectKey, Object> objects = new Hashtable<ObjectKey, Object>();

        for (ObjectChange change : changes) {
            Object object = getObject(objects, change);

            processChange(change, object);
        }
        
        afterRun(objects.values());
        
        return objects.values();
    }

    protected void processChange(ObjectChange change, Object object) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        if (change.slot != null) {
            setProperty(object, change.slot, change.value);
        }
        else if (change.setter != null) {
            invokeSetter(object, change.setter, change.values);
        }
    }

    private void invokeSetter(Object object, Method setter, Object[] values) {
        try {
            setter.invoke(object, values);
        } catch (InvocationTargetException e) {
            if (e.getCause() != null && e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            else {
                throw new RuntimeException("error while invoking specialized setter", e.getTargetException());
            }
        } catch (Exception e) {
            throw new RuntimeException("error while invoking specialized setter", e);
        }
    }

    /**
     * Executed before any change is made to the domain.
     * 
     * @param changes the list of changes planed for the domain
     */
    protected void beforeRun(List<ObjectChange> changes) {
        // nothing
    }

    /**
     * Executed after all changes are made to the domain. This includes the creation of new objects.
     * 
     * @param touchedObjects the objects that were edited in the interface or created
     */
    protected void afterRun(Collection<Object> touchedObjects) {
        // nothing
    }

    protected void setProperty(Object object, String slot, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Class type = getSlotType(object, slot);
        
        if (type == null) {
            throw new RuntimeException("could not find type of property " + slot + " in object " + object);
        }
        
        if (type.isAssignableFrom(Collection.class)) {
            setCollectionProperty(object, slot, (List) value);
        }
        else {
            try {
                setSlotProperty(object, slot, value);
            }
            catch (IllegalArgumentException e) {
                throw new RuntimeException("the value '" + value + "' given for slot '" + slot + "' does not match slot's type '" + type + "'");
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException("could not access to the slot '" + slot + "' of object '" + object + "', probably is not public");
            }
        }
    }

    protected Class getSlotType(Object object, String slot) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return PropertyUtils.getPropertyType(object, slot);
    }

    protected void setSlotProperty(Object object, String slot, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        PropertyUtils.setProperty(object, slot, value);
    }

    protected void setCollectionProperty(Object object, String slot, List list) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Collection relation = (Collection) getSlotProperty(object, slot);
        
        if (relation == null || isWriteableSlot(object, slot)) { 
            relation = new ArrayList();
            relation.addAll(list);
            
            // ASSUMPTION: if collection is null then there is a setter that allows the value to be changed
            setSlotProperty(object, slot, relation);
        }
        else {
            // ASSUMPTION: changing the list affects the relation
            // TODO: cfgi, I hope this is ok but must check
            relation.clear();
            relation.addAll(list);
        }
    }

    protected boolean isWriteableSlot(Object object, String slot) {
        return PropertyUtils.isWriteable(object, slot);
    }

    protected Object getSlotProperty(Object object, String slot) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        return PropertyUtils.getProperty(object, slot);
    }

    private Object getObject(Hashtable<ObjectKey, Object> objects, ObjectChange change) throws ExcepcaoPersistencia,
            ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object object = objects.get(change.key);

        if (object == null) {
            object = getNewObject(change);
            objects.put(change.key, object);
        }
        
        return object;
    }

    protected DomainObject getNewObject(ObjectChange change) throws InstantiationException, IllegalAccessException, ExcepcaoPersistencia, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        return RootDomainObject.readDomainObjectByOID(change.key.getType(), change.key.getOid());
    }
}
