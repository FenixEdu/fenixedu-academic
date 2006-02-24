package net.sourceforge.fenixedu.renderers.model;

import net.sourceforge.fenixedu.renderers.schemas.Schema;

public abstract class SchemaFactory {
    public static SchemaFactory DEFAULT_FACTORY = new DefaultSchemaFactory();

    private static SchemaFactory currentFactory = DEFAULT_FACTORY;

    public static void setCurrentFactory(SchemaFactory factory) {
        currentFactory = factory;
    }

    public static SchemaFactory getCurrentFactory() {
        return currentFactory;
    }
    
    public static Schema create(Object object) {
        return currentFactory.createSchema(object);
    }

    public static Schema create(Class type) {
        return currentFactory.createSchema(type);
    }

    public abstract Schema createSchema(Object object);

    public abstract Schema createSchema(Class type);
}
