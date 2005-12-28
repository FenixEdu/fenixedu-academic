package net.sourceforge.fenixedu.renderers.model;

import net.sourceforge.fenixedu.renderers.schemas.Schema;

public abstract class MetaObjectFactory {
    public static MetaObjectFactory DEFAULT_FACTORY = new DefaultMetaObjectFactory();

    private static MetaObjectFactory currentFactory = DEFAULT_FACTORY;

    public static void setCurrentFactory(MetaObjectFactory factory) {
        currentFactory = factory;
    }

    public static MetaObjectFactory getCurrentFactory() {
        return currentFactory;
    }
    
    public static MetaObject createObject(Object object, Schema schema) {
        Schema usedSchema = schema;
        
        if (usedSchema == null) {
            usedSchema = SchemaFactory.create(object);
        }
        
        return currentFactory.createMetaObject(object, usedSchema);
    }

    public abstract MetaObject createMetaObject(Object object, Schema schema);
}
