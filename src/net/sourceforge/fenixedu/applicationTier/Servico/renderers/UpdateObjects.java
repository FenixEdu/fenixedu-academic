package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadDomainObject;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.PropertyUtils;

import net.sourceforge.fenixedu.applicationTier.Service;

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

    public void run(List<ObjectChange> changes) throws ExcepcaoPersistencia, ClassNotFoundException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Hashtable<ObjectKey, Object> objects = new Hashtable<ObjectKey, Object>();

        for (ObjectChange change : changes) {
            Object object = getObject(objects, change);

            setProperty(object, change.slot, change.value);
        }
    }

    private void setProperty(Object object, String slot, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class type = PropertyUtils.getPropertyType(object, slot);
        
        if (type.isAssignableFrom(List.class)) {
            setRelation(object, slot, (List) value);
        }
        else {
            PropertyUtils.setProperty(object, slot, value);
        }
    }

    private void setRelation(Object object, String slot, List list) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List relationList = (List) PropertyUtils.getProperty(object, slot);
        
        // TODO: cfgi, I hope this is ok but must check
        relationList.retainAll(list);
        relationList.addAll(list);
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
