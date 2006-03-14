package net.sourceforge.fenixedu.renderers.model;

import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

public class SimpleMetaSlotWithDefault extends SimpleMetaSlot {

    private boolean createValue;
    
    public SimpleMetaSlotWithDefault(MetaObject metaObject, String name) {
        super(metaObject, name);
        
        this.createValue = true;
    }

    @Override
    public Object getObject() {
        if (this.createValue) {
            this.createValue = false;
            
            setObject(DefaultValues.getInstance().createValue(getType(), getDefaultValue()));
        }
        
        return super.getObject();
    }
    
    @Override
    public void setObject(Object object) {
        setProperty(getMetaObject().getObject(), getName(), object);
    }

    /**
     * Sets property and build parent objects if they are null.
     */
    private void setProperty(Object object, String name, Object value) {
        RendererPropertyUtils.setProperty(object, name, value, true);
    }
}
