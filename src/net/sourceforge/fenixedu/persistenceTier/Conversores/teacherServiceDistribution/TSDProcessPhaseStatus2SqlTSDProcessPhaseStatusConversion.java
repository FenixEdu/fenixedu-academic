package net.sourceforge.fenixedu.persistenceTier.Conversores.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhaseStatus;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class TSDProcessPhaseStatus2SqlTSDProcessPhaseStatusConversion implements FieldConversion {

	public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof TSDProcessPhaseStatus) {
        	TSDProcessPhaseStatus s = (TSDProcessPhaseStatus) source;
            return s.name();
        }
        return source;
	}

	public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return TSDProcessPhaseStatus.valueOf(src);
        }
        return source;        
	}

}
