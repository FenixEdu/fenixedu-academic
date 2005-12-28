package net.sourceforge.fenixedu.renderers.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;

public class DefaultMetaObjectFactory extends MetaObjectFactory {

    @Override
    public MetaObject createMetaObject(Object object, Schema schema) {
        MetaObject metaObject;
        
        if (object instanceof Collection) {
            SimpleMetaObjectCollection metaObjectCollection = new SimpleMetaObjectCollection();
            metaObjectCollection.setSchema(schema.getName());
            
            for (Iterator iter = ((Collection) object).iterator(); iter.hasNext();) {
                Object element = (Object) iter.next();
                
                metaObjectCollection.addMetaObject(createOneMetaObject(element, schema));
            }
            
            metaObject = metaObjectCollection;
        }
        else {
            metaObject = createOneMetaObject(object, schema);
        }
        
        return metaObject;
    }

    protected MetaObject createOneMetaObject(Object object, Schema schema) {
        SimpleMetaObject metaObject = new SimpleMetaObject(object);
        metaObject.setSchema(schema.getName());
        
        List<SchemaSlotDescription> slotDescriptions = schema.getSlotDescriptions(); 
        for (SchemaSlotDescription description : slotDescriptions) {
            SimpleMetaSlot metaSlot = (SimpleMetaSlot) createMetaSlot(metaObject, object, description);
            
            metaObject.addSlot(metaSlot);
        }
        
        return metaObject;
    }

    protected MetaSlot createMetaSlot(MetaObject metaObject, Object object, SchemaSlotDescription slotDescription) {
        SimpleMetaSlot metaSlot = new SimpleMetaSlot((SimpleMetaObject) metaObject, slotDescription.getSlotName());
        
        metaSlot.setLabelKey(slotDescription.getKey());
        metaSlot.setSchema(slotDescription.getSchema());
        metaSlot.setLayout(slotDescription.getLayout());
        metaSlot.setValidator(slotDescription.getValidator());
        metaSlot.setDefaultValue(slotDescription.getDefaultValue());
        metaSlot.setProperties(slotDescription.getProperties());
        
        return metaSlot;
    }
}
