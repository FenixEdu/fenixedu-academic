package net.sourceforge.fenixedu.persistenceTier.Conversores.accounting;

import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaGratuityExemptionType2SqlGratuityExemptionTypeFieldConversion implements
	FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	return (source instanceof GratuityExemptionType) ? ((GratuityExemptionType) source).name()
		: null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return (source instanceof String) ? GratuityExemptionType.valueOf((String) source) : null;
    }
}
