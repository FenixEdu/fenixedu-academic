package net.sourceforge.fenixedu.persistenceTier.Conversores.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhaseStatus;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ValuationPhaseStatus2SqlValuationPhaseStatusConversion implements FieldConversion {

	public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ValuationPhaseStatus) {
        	ValuationPhaseStatus s = (ValuationPhaseStatus) source;
            return s.name();
        }
        return source;
	}

	public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ValuationPhaseStatus.valueOf(src);
        }
        return source;        
	}

}
