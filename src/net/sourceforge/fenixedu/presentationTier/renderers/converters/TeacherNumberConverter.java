package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.lang.StringUtils;

public class TeacherNumberConverter extends Converter{

    @Override
    public Object convert(Class type, Object value) {
	
	String numberText = ((String) value).trim();        
        
        if (numberText.length() == 0 || !StringUtils.isNumeric(numberText)) {
            return null;
        }
        
        return Teacher.readByNumber(Integer.valueOf(numberText));        
    }

}
