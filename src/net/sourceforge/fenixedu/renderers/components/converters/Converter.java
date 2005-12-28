package net.sourceforge.fenixedu.renderers.components.converters;

import java.io.Serializable;

public abstract class Converter implements Serializable {

    public abstract Object convert(Class type, Object value);
    
}
