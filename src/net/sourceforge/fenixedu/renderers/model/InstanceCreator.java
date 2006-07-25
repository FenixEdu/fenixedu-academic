package net.sourceforge.fenixedu.renderers.model;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class InstanceCreator implements Serializable {

    private Class type;

    private List<MetaSlot> slots;
    private List<Class> argumentTypes;
    
    public InstanceCreator(Class type) {
        super();
    
        this.type = type;
        this.slots = new ArrayList<MetaSlot>();
        this.argumentTypes = new ArrayList<Class>();
    }
    
    public void addArgument(MetaSlot slot, Class type) {
        this.slots.add(slot);
        this.argumentTypes.add(type);
        slot.setSetterIgnored(true);
    }
    
    public Object createInstance() {
        try {
            Constructor constructor = getConstructor();
            Object[] values = getArgumentValues();
            
            return constructor.newInstance(values);
        } catch (Exception e) {
            throw new RuntimeException("failed to create instance of " + this.type.getName() + " with arguments " + getArgumentTypes(), e);
        }
    }
    
    public Class[] getArgumentTypes() {
        return this.argumentTypes.toArray(new Class[0]);
    }
    
    public Constructor getConstructor() throws SecurityException, NoSuchMethodException {
        return this.type.getConstructor(getArgumentTypes());
    }

    public Object[] getArgumentValues() {
        Object[] values = new Object[this.slots.size()];
        
        for (int i = 0; i < values.length; i++) {
            values[i] = this.slots.get(i).getObject();
        }
        
        return values;
    }
}
