package net.sourceforge.fenixedu.renderers.components.converters;

import java.io.Serializable;

public abstract class Converter implements Serializable {

    public static final Converter IDENTITY_CONVERTER = new Converter() {

        @Override
        public Object convert(Class type, Object value) {
            return value;
        }
        
    };
    
    public abstract Object convert(Class type, Object value);
    
}
