package net.sourceforge.fenixedu.renderers.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;

public class DefaultMetaObjectFactory extends MetaObjectFactory {

    @Override
    public MultipleMetaObject createMetaObjectCollection() {
        return new SimpleMetaObjectCollection();
    }

    @Override
    public MetaObject createMetaObject(Object object, Schema schema) {
        if (object instanceof Collection) {
            MultipleMetaObject multipleMetaObject = createMetaObjectCollection();
            
            for (Iterator iter = ((Collection) object).iterator(); iter.hasNext();) {
                Object element = (Object) iter.next();
                
                multipleMetaObject.add(createOneMetaObject(element, schema));
            }
            
            return multipleMetaObject;
        }
        else {
            return createOneMetaObject(object, schema);
        }
    }

    @Override
    public MetaObject createMetaObject(Class type, Schema schema) {
        SimpleCreationMetaObject metaObject;
        
        try {
            metaObject = new SimpleCreationMetaObject(type.newInstance());
        } catch (Exception e) {
            throw new RuntimeException("could not create a new instance of " + type, e);
        } 
        
        metaObject.setSchema(schema.getName());
        
        List<SchemaSlotDescription> slotDescriptions = schema.getSlotDescriptions(); 
        for (SchemaSlotDescription description : slotDescriptions) {
            SimpleMetaSlot metaSlot = (SimpleMetaSlot) createMetaSlot(metaObject, description);
            
            metaObject.addSlot(metaSlot);
        }
        
        return metaObject;
    }

    protected MetaObject createOneMetaObject(Object object, Schema schema) {
        SimpleMetaObject metaObject = new SimpleMetaObject(object);
        metaObject.setSchema(schema.getName());
        
        List<SchemaSlotDescription> slotDescriptions = schema.getSlotDescriptions(); 
        for (SchemaSlotDescription description : slotDescriptions) {
            SimpleMetaSlot metaSlot = (SimpleMetaSlot) createMetaSlot(metaObject, description);
            
            metaObject.addSlot(metaSlot);
        }
        
        return metaObject;
    }

    @Override
    public MetaSlot createMetaSlot(MetaObject metaObject, SchemaSlotDescription slotDescription) {
        SimpleMetaSlot metaSlot;
        
        if (metaObject instanceof CreationMetaObject) {
            metaSlot = new SimpleMetaSlotWithDefault((SimpleMetaObject) metaObject, slotDescription.getSlotName());
        }
        else {
            metaSlot = new SimpleMetaSlot((SimpleMetaObject) metaObject, slotDescription.getSlotName());
        }
        
        metaSlot.setLabelKey(slotDescription.getKey());
        metaSlot.setBundle(slotDescription.getBundle());
        metaSlot.setSchema(slotDescription.getSchema());
        metaSlot.setLayout(slotDescription.getLayout());
        metaSlot.setValidator(slotDescription.getValidator());
        metaSlot.setValidatorProperties(slotDescription.getValidatorProperties());
        metaSlot.setDefaultValue(slotDescription.getDefaultValue());
        metaSlot.setProperties(slotDescription.getProperties());
        metaSlot.setConverter(slotDescription.getConverter());
        metaSlot.setReadOnly(slotDescription.isReadOnly());
        
        return metaSlot;
    }

}
