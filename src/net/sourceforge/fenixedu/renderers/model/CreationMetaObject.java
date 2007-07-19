package net.sourceforge.fenixedu.renderers.model;

import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

public class CreationMetaObject extends SimpleMetaObject {
    
    private Class type;

    public CreationMetaObject(Class type) {
        super(null);
        
        this.type = type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    @Override
    public Class getType() {
        return this.type;
    }

    @Override
    public void commit() {
        if (getInstanceCreator() != null) {
            setObject(getInstanceCreator().createInstance());
        }
        else {
            try {
                setObject(getType().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("could not create instance of type " + getType().getName() + " using the default constructor");
            }
        }
        
        super.commit();
    }

    @Override
    protected void setProperty(MetaSlot slot, Object value) {
        RendererPropertyUtils.setProperty(getObject(), slot.getName(), value, true);
    }

}
