package net.sourceforge.fenixedu.renderers.model;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

public class SimpleMetaObject extends MetaObject {

    private Object object;
    private int code;
    
    private List<CompositeSlotSetter> compositeSetters;

    public SimpleMetaObject(Object object) {
        super();

        setObject(object);
        this.compositeSetters = new ArrayList<CompositeSlotSetter>();
    }

    protected void setObject(Object object) {
        this.object = object;
        this.code = object == null ? 0 : object.hashCode();
    }
    
    public Object getObject() {
        return this.object;
    }

    public Class getType() {
        return this.object.getClass();
    }

    public MetaObjectKey getKey() {
        return new MetaObjectKey(getType(), this.code);
    }

    public void addCompositeSetter(CompositeSlotSetter compositeSetter) {
        compositeSetter.setMetaObject(this);
        this.compositeSetters.add(compositeSetter);
    }
    
    protected List<CompositeSlotSetter> getCompositeSetters() {
        return this.compositeSetters;
    }
    
    public void commit() {
        for (MetaSlot slot : getAllSlots()) {
            if (slot.isSetterIgnored()) {
                continue;
            }
            
            if (slot.isCached()) {
                Object value = slot.getObject();
                
                try {
                    setProperty(slot, value);
                } catch (Exception e) {
                    throw new RuntimeException("could not write property '" + slot.getName() + "' in object " + getObject());
                } 
            }
        }
        
        for (CompositeSlotSetter compositeSetter : getCompositeSetters()) {
            compositeSetter.executeSetter();
        }
    }

    protected void setProperty(MetaSlot slot, Object value) {
        RendererPropertyUtils.setProperty(getObject(), slot.getName(), value, false);
    }
}
