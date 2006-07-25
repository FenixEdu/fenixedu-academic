package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.renderers.model.DefaultMetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectCollection;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;

public class FenixMetaObjectFactory extends DefaultMetaObjectFactory {
    
    @Override
    public MetaObjectCollection createMetaObjectCollection() {
        return new DomainMetaObjectCollection();
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
        
        addSlotDescriptions(schema, metaObject);
        addCompositeSlotSetters(schema, metaObject);
        
        return metaObject;
    }

    private MetaObject createCreationMetaObject(Class type, Schema schema) {
        CreationDomainMetaObject metaObject = new CreationDomainMetaObject(type);
        
        addSlotDescriptions(schema, metaObject);
        setInstanceCreator(schema.getType(), schema, metaObject);
        addCompositeSlotSetters(schema, metaObject);
        
        return metaObject;
    }

    @Override
    public MetaSlot createMetaSlot(MetaObject metaObject, SchemaSlotDescription slotDescription) {
        MetaSlot metaSlot;
        
        if (metaObject instanceof CreationDomainMetaObject) { // CreationMetaObject extends DomainMetaObject
            metaSlot = new MetaSlotWithDefault(metaObject, slotDescription.getSlotName());
        }
        else if (metaObject instanceof DomainMetaObject) {
            metaSlot = new MetaSlot(metaObject, slotDescription.getSlotName());
        }
        else {
            metaSlot = super.createMetaSlot(metaObject, slotDescription);
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
        //metaSlot.setSetterIgnored(slotDescription.isSetterIgnored());
        
        return metaSlot;
    }
}
