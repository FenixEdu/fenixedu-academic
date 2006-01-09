package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.renderers.model.DefaultMetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.SimpleMetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;
import dml.DomainClass;

public class FenixMetaObjectFactory extends DefaultMetaObjectFactory {
    
    @Override
    protected MetaObject createOneMetaObject(Object object, Schema schema) {
        if (object instanceof DomainObject) {
            // persistent object
            return createDomainMetaObject(object, schema);
        }
        else if (object instanceof DomainClass) {
            // domain class, used for object creation
            return createCreationMetaObject(object, schema);
        }
        else {
            // standard object
            return super.createOneMetaObject(object, schema);
        }
    }

    private MetaObject createDomainMetaObject(Object object, Schema schema) {
        DomainMetaObject metaObject = new DomainMetaObject((DomainObject) object);
        metaObject.setSchema(schema.getName());
        
        List<SchemaSlotDescription> slotDescriptions = schema.getSlotDescriptions(); 
        for (SchemaSlotDescription description : slotDescriptions) {
            MetaSlot metaSlot = (MetaSlot) createMetaSlot(metaObject, object, description);
            
            metaObject.addSlot(metaSlot);
        }
        
        return metaObject;
    }

    private MetaObject createCreationMetaObject(Object object, Schema schema) {
        CreationMetaObject metaObject = new CreationMetaObject((DomainClass) object);
        metaObject.setSchema(schema.getName());
        
        List<SchemaSlotDescription> slotDescriptions = schema.getSlotDescriptions(); 
        for (SchemaSlotDescription description : slotDescriptions) {
            MetaSlot metaSlot = (MetaSlot) createMetaSlot(metaObject, object, description);
            
            metaObject.addSlot(metaSlot);
        }
        
        return metaObject;
    }

    @Override
    protected MetaSlot createMetaSlot(MetaObject metaObject, Object object, SchemaSlotDescription slotDescription) {
        SimpleMetaSlot metaSlot;
        
        if (metaObject instanceof DomainMetaObject) {
            metaSlot = new CachedMetaSlot(metaObject, slotDescription.getSlotName());
        }
        else if (metaObject instanceof CreationMetaObject) {
            metaSlot = new CachedMetaSlotWithDefault(metaObject, slotDescription.getSlotName());
        }
        else {
            metaSlot = (SimpleMetaSlot) super.createMetaSlot(metaObject, object, slotDescription);
        }
        
        metaSlot.setLabelKey(slotDescription.getKey());
        metaSlot.setSchema(slotDescription.getSchema());
        metaSlot.setLayout(slotDescription.getLayout());
        metaSlot.setValidator(slotDescription.getValidator());
        metaSlot.setValidatorProperties(slotDescription.getValidatorProperties());
        metaSlot.setDefaultValue(slotDescription.getDefaultValue());
        metaSlot.setProperties(slotDescription.getProperties());
        
        return metaSlot;
    }
    
}
