package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.renderers.model.DefaultMetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.MultipleMetaObject;
import net.sourceforge.fenixedu.renderers.model.SimpleMetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;

public class FenixMetaObjectFactory extends DefaultMetaObjectFactory {
    
    @Override
    public MultipleMetaObject createMetaObjectCollection() {
        return new DomainCollectionMetaObject();
    }

    @Override
    protected MetaObject createOneMetaObject(Object object, Schema schema) {
        if (object instanceof DomainObject) {
            // persistent object
            return createDomainMetaObject(object, schema);
        }
        else {
            // standard object
            return super.createOneMetaObject(object, schema);
        }
    }

    @Override
    public MetaObject createMetaObject(Class type, Schema schema) {
        if (DomainObject.class.isAssignableFrom(type)) {
            return createCreationMetaObject(type, schema);
        }
        else {
            return super.createMetaObject(type, schema);
        }
    }

    private MetaObject createDomainMetaObject(Object object, Schema schema) {
        DomainMetaObject metaObject = new DomainMetaObject((DomainObject) object);
        metaObject.setSchema(schema.getName());
        
        List<SchemaSlotDescription> slotDescriptions = schema.getSlotDescriptions(); 
        for (SchemaSlotDescription description : slotDescriptions) {
            MetaSlot metaSlot = (MetaSlot) createMetaSlot(metaObject, description);
            
            metaObject.addSlot(metaSlot);
        }
        
        return metaObject;
    }

    private MetaObject createCreationMetaObject(Class type, Schema schema) {
        CreationDomainMetaObject metaObject = new CreationDomainMetaObject(type);
        metaObject.setSchema(schema.getName());
        
        List<SchemaSlotDescription> slotDescriptions = schema.getSlotDescriptions(); 
        for (SchemaSlotDescription description : slotDescriptions) {
            MetaSlot metaSlot = (MetaSlot) createMetaSlot(metaObject, description);
            
            metaObject.addSlot(metaSlot);
        }
        
        return metaObject;
    }

    @Override
    public MetaSlot createMetaSlot(MetaObject metaObject, SchemaSlotDescription slotDescription) {
        SimpleMetaSlot metaSlot;
        
        if (metaObject instanceof CreationDomainMetaObject) { // CreationMetaObject extends DomainMetaObject
            metaSlot = new CachedMetaSlotWithDefault(metaObject, slotDescription.getSlotName());
        }
        else if (metaObject instanceof DomainMetaObject) {
            metaSlot = new CachedMetaSlot(metaObject, slotDescription.getSlotName());
        }
        else {
            metaSlot = (SimpleMetaSlot) super.createMetaSlot(metaObject, slotDescription);
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
