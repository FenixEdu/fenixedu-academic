package net.sourceforge.fenixedu.persistenceTier.Conversores.accounting;

import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionJustificationType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaGratuityExemptionJustificationType2SqlGratuityExemptionJustificationTypeFieldConversion implements
	FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	return (source instanceof GratuityExemptionJustificationType) ? ((GratuityExemptionJustificationType) source).name()
		: null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return (source instanceof String) ? GratuityExemptionJustificationType.valueOf((String) source) : null;
    }
}
