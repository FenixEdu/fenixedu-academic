package net.sourceforge.fenixedu.renderers.model;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;
import net.sourceforge.fenixedu.renderers.schemas.Signature;
import net.sourceforge.fenixedu.renderers.schemas.SignatureParameter;

public class DefaultMetaObjectFactory extends MetaObjectFactory {

    @Override
    public MetaObjectCollection createMetaObjectCollection() {
        return new MetaObjectCollection();
    }

    @Override
    public MetaObject createMetaObject(Object object, Schema schema) {
        if (object instanceof Collection) {
            MetaObjectCollection multipleMetaObject = createMetaObjectCollection();
            
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
        CreationMetaObject metaObject;
        
        try {
            metaObject = new CreationMetaObject(type);
        } catch (Exception e) {
            throw new RuntimeException("could not create a new instance of " + type, e);
        } 
        
        addSlotDescriptions(schema, metaObject);
        setInstanceCreator(type, schema, metaObject);
        addCompositeSlotSetters(schema, metaObject);
        
        return metaObject;
    }

    protected void setInstanceCreator(Class type, Schema schema, MetaObject metaObject) {
        Signature signature = schema.getConstructor();
        
        if (signature != null) {
            InstanceCreator creator = new InstanceCreator(type);
            
            for (SignatureParameter parameter : signature.getParameters()) {
                SchemaSlotDescription description = parameter.getSlotDescription();
                
                for (MetaSlot slot : metaObject.getAllSlots()) {
                    if (slot.getName().equals(description.getSlotName())) {
                        creator.addArgument(slot, parameter.getType());
                    }
                }
            }
            
            metaObject.setInstanceCreator(creator);
        }
    }

    protected void addSlotDescriptions(Schema schema, SimpleMetaObject metaObject) {
        List<SchemaSlotDescription> slotDescriptions = schema.getSlotDescriptions(); 
        for (SchemaSlotDescription description : slotDescriptions) {
            MetaSlot metaSlot = (MetaSlot) createMetaSlot(metaObject, description);
            
            if (! description.isHidden()) {
                metaObject.addSlot(metaSlot);
            }
            else {
                metaObject.addHiddenSlot(metaSlot);
            }
        }
    }

    protected void addCompositeSlotSetters(Schema schema, SimpleMetaObject metaObject) {
        for (Signature setterSignature : schema.getSpecialSetters()) {
            CompositeSlotSetter compositeSlotSetter = new CompositeSlotSetter(metaObject, setterSignature.getName());
            
            for (SignatureParameter parameter : setterSignature.getParameters()) {
                SchemaSlotDescription description = parameter.getSlotDescription();
                
                for (MetaSlot slot : metaObject.getAllSlots()) {
                    if (slot.getName().equals(description.getSlotName())) {
                       compositeSlotSetter.addArgument(slot, parameter.getType());
                    }
                }
            }
            
            metaObject.addCompositeSetter(compositeSlotSetter);
        }
    }

    protected MetaObject createOneMetaObject(Object object, Schema schema) {
        if (isPrimitiveObject(object)) {
            return new PrimitiveMetaObject(object);
        }
        else {
            SimpleMetaObject metaObject = new SimpleMetaObject(object);

            addSlotDescriptions(schema, metaObject);
            addCompositeSlotSetters(schema, metaObject);
            
            return metaObject;
        }
    }

    private boolean isPrimitiveObject(Object object) {
        Class[] primitiveTypes = new Class[] {
                String.class,
                Number.class,
                Integer.TYPE,
                Long.TYPE,
                Short.TYPE,
                Character.TYPE,
                Float.TYPE,
                Double.TYPE,
                Date.class,
                Enum.class
        };
        
        if (object == null) {
            return true;
        }
        
        for (int i = 0; i < primitiveTypes.length; i++) {
            Class type = primitiveTypes[i];
            
            if (type.isAssignableFrom(object.getClass())) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public MetaSlot createMetaSlot(MetaObject metaObject, SchemaSlotDescription slotDescription) {
        MetaSlot metaSlot;
        
        if (metaObject instanceof CreationMetaObject) {
            metaSlot = new MetaSlotWithDefault((SimpleMetaObject) metaObject, slotDescription.getSlotName());
        }
        else {
            metaSlot = new MetaSlot((SimpleMetaObject) metaObject, slotDescription.getSlotName());
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
