package net.sourceforge.fenixedu.renderers.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CompositeSlotSetter implements Serializable {
    private MetaObject metaObject;
    
    private String setterName;
    private List<MetaSlot> slots;
    private List<Class> types;
    
    public CompositeSlotSetter(MetaObject metaObject, String setterName) {
        super();
    
        this.metaObject = metaObject;
        this.setterName = setterName;
        
        this.slots = new ArrayList<MetaSlot>();
        this.types = new ArrayList<Class>();
    }
    
    public MetaObject getMetaObject() {
        return this.metaObject;
    }

    public String getSetterName() {
        return this.setterName;
    }

    public void setMetaObject(MetaObject metaObject) {
        this.metaObject = metaObject;
    }

    public void addArgument(MetaSlot slot, Class type) {
        this.slots.add(slot);
        this.types.add(type);
        slot.setSetterIgnored(true);
    }
    
    public void executeSetter() {
        Object object = this.metaObject.getObject();
        
        try {
            Method method = getSetter(object.getClass());
            Object[] values = getArgumentValues();
            
            method.invoke(object, values);
        } 
        catch (RuntimeException e) {
            throw e;
        }
        catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            else {
                throw new RuntimeException(e);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }

    public Method getSetter(Class type) throws NoSuchMethodException {
        return type.getMethod(getSetterName(), getArgumentTypes());
    }

    public Object[] getArgumentValues() {
        Object[] values = new Object[this.slots.size()];
        
        int i = 0;
        for (MetaSlot slot : this.slots) {
            values[i++] = slot.getObject();
        }
        
        return values;
    }

    public Class[] getArgumentTypes() {
        return this.types.toArray(new Class[0]);
    }
}
