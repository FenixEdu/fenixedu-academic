package net.sourceforge.fenixedu.persistenceTier.Conversores.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class TSDValueType2SqlTSDValueTypeConversion implements FieldConversion {

	public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof TSDValueType) {
        	TSDValueType s = (TSDValueType) source;
            return s.name();
        }
        return source;
	}

	public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return TSDValueType.valueOf(src);
        }
        return source;        
	}

}
