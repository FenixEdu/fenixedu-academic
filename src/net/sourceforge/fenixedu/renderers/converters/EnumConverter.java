/**
 * 
 */
package net.sourceforge.fenixedu.renderers.converters;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class EnumConverter extends Converter {

    private Class enumClass;
    
    public EnumConverter() {
        super();
    }

    public EnumConverter(Class enumClass) {
        super();

        this.enumClass = enumClass;
    }

    @Override
    public Object convert(Class type, Object value) {
        Object[] enums;
        
        if (this.enumClass == null) {
            enums = type.getEnumConstants();
        }
        else {
            enums = this.enumClass.getEnumConstants();
        }
        
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].toString().equals(value)) {
                return enums[i];
            }
        }
        
        return null;
    }
    
}