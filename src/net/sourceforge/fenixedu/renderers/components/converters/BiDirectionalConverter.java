package net.sourceforge.fenixedu.renderers.components.converters;


public abstract class BiDirectionalConverter extends Converter {

    public abstract Object convert(Class type, Object value);

    public abstract String deserialize(Object object);
}
