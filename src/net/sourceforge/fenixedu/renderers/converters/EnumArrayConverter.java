/**
 * 
 */
package net.sourceforge.fenixedu.renderers.converters;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class EnumArrayConverter extends Converter{

    private EnumConverter concreteConverter;
    
    public EnumArrayConverter() {
        this.concreteConverter = new EnumConverter();
    }
    
    public EnumArrayConverter(Class enumClass) {
        this.concreteConverter = new EnumConverter(enumClass);
    }
    
    @Override
    public Object convert(Class type, Object value) {
        List enumValues = new ArrayList();
     
        String[] values = (String[]) value;
        for (int i = 0; i < values.length; i++) {
            String enumString = values[i];
            
            enumValues.add(this.concreteConverter.convert(type, enumString));
        }
        
        return enumValues;
    }

}