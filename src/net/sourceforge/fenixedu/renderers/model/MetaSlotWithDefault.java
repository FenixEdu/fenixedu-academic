package net.sourceforge.fenixedu.renderers.model;

import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

public class MetaSlotWithDefault extends MetaSlot {

    private boolean createValue;
    
    public MetaSlotWithDefault(MetaObject metaObject, String name) {
        super(metaObject, name);
        
        this.createValue = true;
    }

    @Override
    public Object getObject() {
        if (this.createValue) {
            this.createValue = false;
            
            setObject(createDefault(getType(), getDefaultValue()));
        }
        
        return super.getObject();
    }

    @Override
    public void setObject(Object object) {
        super.setObject(object);
        this.createValue = false;
    }

    @Override
    public Class getType() {
        Class type = getMetaObject().getType();
        
        return RendererPropertyUtils.getPropertyType(type, getName());
    }

    protected Object createDefault(Class type, String defaultValue) {
        DefaultValues instance = DefaultValues.getInstance();
        return instance.createValue(type, defaultValue);
    }
    
}
