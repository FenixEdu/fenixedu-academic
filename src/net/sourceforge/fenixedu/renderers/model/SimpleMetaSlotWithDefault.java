package net.sourceforge.fenixedu.renderers.model;

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
}
