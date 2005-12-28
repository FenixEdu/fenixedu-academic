package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;

import net.sourceforge.fenixedu.renderers.model.MetaObject;

public class CachedMetaSlotWithDefault extends CachedMetaSlot {

    private boolean createValue;
    
    public CachedMetaSlotWithDefault(MetaObject metaObject, String name) {
        super(metaObject, name);
        
        this.createValue = true;
    }

    @Override
    public Object getObject() {
        if (createValue) {
            setObject(DefaultValues.createValue(getType(), getDefaultValue()));
            
            createValue = false;
        }
        
        return super.getObject();
    }

    @Override
    public Class getType() {
        Class type = getMetaObject().getType();
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(type);
        
        for (int i = 0; i < descriptors.length; i++) {
            PropertyDescriptor descriptor = descriptors[i];
            
            if (descriptor.getName().equals(getName())) {
                return descriptor.getPropertyType();
            }
        }
        
        return super.getType();
    }
}
