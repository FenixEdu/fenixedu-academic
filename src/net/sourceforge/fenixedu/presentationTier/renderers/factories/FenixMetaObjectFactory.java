package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.domain.DomainObject;
import pt.ist.fenixWebFramework.renderers.model.DefaultMetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectCollection;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.schemas.SchemaSlotDescription;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;

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
	} else {
	    // standard object
	    return super.createOneMetaObject(object, schema);
	}
    }

    @Override
    public MetaObject createMetaObject(Class type, Schema schema) {
	if (DomainObject.class.isAssignableFrom(type)) {
	    return createCreationMetaObject(type, schema);
	} else {
	    return super.createMetaObject(type, schema);
	}
    }

    private MetaObject createDomainMetaObject(Object object, Schema schema) {
	DomainMetaObject metaObject = new DomainMetaObject((DomainObject) object);

	metaObject.setSchema(schema);

	addSlotDescriptions(schema, metaObject);
	addCompositeSlotSetters(schema, metaObject);

	return metaObject;
    }

    private MetaObject createCreationMetaObject(Class type, Schema schema) {
	CreationDomainMetaObject metaObject = new CreationDomainMetaObject(type);

	metaObject.setSchema(schema);

	addSlotDescriptions(schema, metaObject);
	setInstanceCreator(schema.getType(), schema, metaObject);
	addCompositeSlotSetters(schema, metaObject);

	return metaObject;
    }

    @Override
    public MetaSlot createMetaSlot(MetaObject metaObject, SchemaSlotDescription slotDescription) {
	MetaSlot metaSlot;

	if (metaObject instanceof CreationDomainMetaObject) { // CreationMetaObject
	    // extends
	    // DomainMetaObject
	    metaSlot = new MetaSlotWithDefault(metaObject, slotDescription.getSlotName());
	} else if (metaObject instanceof DomainMetaObject) {
	    metaSlot = new MetaSlot(metaObject, slotDescription.getSlotName());
	} else {
	    metaSlot = super.createMetaSlot(metaObject, slotDescription);
	}

	metaSlot.setLabelKey(slotDescription.getKey());
	metaSlot.setBundle(slotDescription.getBundle());
	metaSlot.setSchema(RenderKit.getInstance().findSchema(slotDescription.getSchema()));
	metaSlot.setLayout(slotDescription.getLayout());
	metaSlot.setValidators(slotDescription.getValidators());
	metaSlot.setDefaultValue(slotDescription.getDefaultValue());
	metaSlot.setProperties(slotDescription.getProperties());
	metaSlot.setConverter(slotDescription.getConverter());
	metaSlot.setReadOnly(slotDescription.isReadOnly());
	// metaSlot.setSetterIgnored(slotDescription.isSetterIgnored());

	return metaSlot;
    }
}
