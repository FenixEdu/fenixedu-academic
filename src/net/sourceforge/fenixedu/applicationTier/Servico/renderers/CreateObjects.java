package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.renderers.utils.RendererPropertyUtils;

public class CreateObjects extends UpdateObjects {

    public CreateObjects() {
        super();
    }

    @Override
    protected DomainObject getNewObject(ObjectChange change) throws ExcepcaoPersistencia, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class objectClass = change.key.getType();
        
        if (change.constructor != null) {
            return (DomainObject) change.constructor.newInstance(change.values);
        }
        else {
            return (DomainObject) objectClass.newInstance();
        }
    }
    
    @Override
    protected Class getSlotType(Object object, String slot) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return RendererPropertyUtils.getPropertyType(object.getClass(), slot);
    }

    @Override
    protected Object getSlotProperty(Object object, String slot) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        return RendererPropertyUtils.getProperty(object, slot, true);
    }

    @Override
    protected void setSlotProperty(Object object, String slot, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        RendererPropertyUtils.setProperty(object, slot, value, true);
    }
}
