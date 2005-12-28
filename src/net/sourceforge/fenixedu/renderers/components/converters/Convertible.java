package net.sourceforge.fenixedu.renderers.components.converters;

public interface Convertible {
    public boolean hasConverter();
    public Converter getConverter();
    public Object getConvertedValue(Class type);
}
