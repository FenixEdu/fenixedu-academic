package net.sourceforge.fenixedu.persistenceTier.Conversores.accounting;

import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemptionType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaPenaltyExemptionType2SqlPenaltyExemptionTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof PenaltyExemptionType) ? ((PenaltyExemptionType) source).name() : null;  
    }
    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? PenaltyExemptionType.valueOf((String) source) : null;
    }
}
