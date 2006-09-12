package net.sourceforge.fenixedu.renderers.model;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;

public abstract class MetaObjectFactory {
    public static MetaObjectFactory DEFAULT_FACTORY = new DefaultMetaObjectFactory();

    private static MetaObjectFactory currentFactory = DEFAULT_FACTORY;

    public static void setCurrentFactory(MetaObjectFactory factory) {
        currentFactory = factory;
    }

    public static MetaObjectFactory getCurrentFactory() {
        return currentFactory;
    }
    
    public static MetaObjectCollection createObjectCollection() {
        return currentFactory.createMetaObjectCollection();
    }

    public static MetaObject createObject(Object object, Schema schema) {
        Schema usedSchema = schema;
        
        if (usedSchema == null && !(object instanceof Collection)) {
            usedSchema = SchemaFactory.create(object);
        }
        
        if (usedSchema == null && object instanceof Collection) {
            Collection collection = (Collection) object;
            
            if (! collection.isEmpty()) {
                usedSchema = SchemaFactory.create(collection.iterator().next());    
            }
        }
        
        return currentFactory.createMetaObject(object, usedSchema);
    }
    
    public static MetaObject createObject(Class type, Schema schema) {
        Schema usedSchema = schema;
        
        if (usedSchema == null) {
            usedSchema = SchemaFactory.create(type);
        }
        
        return currentFactory.createMetaObject(type, usedSchema);
    }
    
    public static MetaSlot createSlot(MetaObject metaObject, SchemaSlotDescription slotDescription) {
        return currentFactory.createMetaSlot(metaObject, slotDescription);
    }

    public abstract MetaObjectCollection createMetaObjectCollection();
    
    public abstract MetaObject createMetaObject(Object object, Schema schema);
    
    public abstract MetaObject createMetaObject(Class type, Schema schema);

    public abstract MetaSlot createMetaSlot(MetaObject metaObject, SchemaSlotDescription slotDescription);
}
