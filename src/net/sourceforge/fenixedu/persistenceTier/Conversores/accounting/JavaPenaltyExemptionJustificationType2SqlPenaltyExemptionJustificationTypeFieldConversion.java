package net.sourceforge.fenixedu.persistenceTier.Conversores.accounting;

import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaPenaltyExemptionJustificationType2SqlPenaltyExemptionJustificationTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof PenaltyExemptionJustificationType) ? ((PenaltyExemptionJustificationType) source).name() : null;  
    }
    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? PenaltyExemptionJustificationType.valueOf((String) source) : null;
    }
}
