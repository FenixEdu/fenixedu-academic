package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class CategoryType2SqlConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	if (source instanceof CategoryType) {
	    CategoryType s = (CategoryType) source;
	    return s.name();
	}
	return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	if (source == null || source.equals("")) {
	    return null;
	} else if (source instanceof String) {
	    String src = (String) source;
	    return CategoryType.valueOf(src);
	}
	return source;
    }
}
