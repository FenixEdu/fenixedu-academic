package net.sourceforge.fenixedu.persistenceTier.Conversores.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ValuationValueType2SqlValuationValueTypeConversion implements FieldConversion {

	public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ValuationValueType) {
        	ValuationValueType s = (ValuationValueType) source;
            return s.name();
        }
        return source;
	}

	public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ValuationValueType.valueOf(src);
        }
        return source;        
	}

}
