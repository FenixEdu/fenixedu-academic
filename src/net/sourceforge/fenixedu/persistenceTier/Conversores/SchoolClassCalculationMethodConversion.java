package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularLoad.SchoolClassCalculationMethod;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class SchoolClassCalculationMethodConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	if (source instanceof SchoolClassCalculationMethod) {
	    SchoolClassCalculationMethod s = (SchoolClassCalculationMethod) source;
	    return s.name();
	}
	return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	if (source == null || source.equals("")) {
	    return null;
	} else if (source instanceof String) {
	    String src = (String) source;
	    return SchoolClassCalculationMethod.valueOf(src);
	}
	return source;
    }

}
