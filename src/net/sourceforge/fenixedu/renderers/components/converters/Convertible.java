package net.sourceforge.fenixedu.renderers.components.converters;

import net.sourceforge.fenixedu.renderers.model.MetaSlot;

public interface Convertible {
    public boolean hasConverter();
    public Converter getConverter();
    public Object getConvertedValue(MetaSlot slot);
}
