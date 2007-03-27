package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.contacts.PartyContactType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class PartyContactType2SqlConversion implements FieldConversion {

    public Object javaToSql(final Object source) throws ConversionException {
	return (source instanceof PartyContactType) ? ((PartyContactType) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	return (source instanceof String) ? PartyContactType.valueOf((String) source) : null;
    }

}
