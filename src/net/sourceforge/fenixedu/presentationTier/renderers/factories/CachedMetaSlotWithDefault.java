package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

public class CachedMetaSlotWithDefault extends CachedMetaSlot {

    private boolean createValue;
    
    public CachedMetaSlotWithDefault(MetaObject metaObject, String name) {
        super(metaObject, name);
        
        this.createValue = true;
    }

    @Override
    public Object getObject() {
        if (createValue) {
            setObject(FenixDefaultValues.getInstance().createValue(getType(), getDefaultValue()));
            
            createValue = false;
        }
        
        return super.getObject();
    }

    @Override
    public Class getType() {
        Class type = getMetaObject().getType();
        
        return RendererPropertyUtils.getPropertyType(type, getName());
    }
}
