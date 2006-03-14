package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadDomainObject;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.PropertyUtils;

public class UpdateObjects extends Service {
    
    public static class ObjectChange {
        public final ObjectKey key;

        public final String slot;

        public final Object value;

        public ObjectChange(ObjectKey key, String slot, Object value) {
            this.key = key;
            this.slot = slot;
            this.value = value;
        }
    }

    public Collection run(List<ObjectChange> changes) throws ExcepcaoPersistencia, ClassNotFoundException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Hashtable<ObjectKey, Object> objects = new Hashtable<ObjectKey, Object>();

        for (ObjectChange change : changes) {
            Object object = getObject(objects, change);

            setProperty(object, change.slot, change.value);
        }
        
        return objects.values();
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
            setSlotProperty(object, slot, value);
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
        
        if (relation == null) { 
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

    protected Object getSlotProperty(Object object, String slot) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        return PropertyUtils.getProperty(object, slot);
    }

    private Object getObject(Hashtable<ObjectKey, Object> objects, ObjectChange change) throws ExcepcaoPersistencia,
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        Object object = objects.get(change.key);

        if (object == null) {
            object = getNewObject(change);
            objects.put(change.key, object);
        }
        
        return object;
    }

    protected DomainObject getNewObject(ObjectChange change) throws ExcepcaoPersistencia, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ReadDomainObject<DomainObject> readService = new ReadDomainObject<DomainObject>();
        
        return readService.run(change.key.getType(), change.key.getOid());
    }
}
