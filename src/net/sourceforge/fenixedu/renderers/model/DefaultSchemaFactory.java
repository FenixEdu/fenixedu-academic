package net.sourceforge.fenixedu.renderers.model;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;

import org.apache.commons.beanutils.PropertyUtils;

public class DefaultSchemaFactory extends SchemaFactory {

    @Override
    public Schema createSchema(Object object) {
        Schema schema = new Schema(object == null ? Object.class : object.getClass());
        
        if (object == null) {
            return schema;
        }
        
        if (object instanceof Collection) {
            return schema;
        }
        
        List<String> filteredSlots = Arrays.asList(new String[] { "class" });
        List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>(Arrays
                .asList(PropertyUtils.getPropertyDescriptors(object)));
    
        for (PropertyDescriptor descriptor : descriptors) {
            if (!filteredSlots.contains(descriptor.getName())) {
                schema.addSlotDescription(new SchemaSlotDescription(descriptor.getName()));
            }
        }
    
        return schema;
    }

}
