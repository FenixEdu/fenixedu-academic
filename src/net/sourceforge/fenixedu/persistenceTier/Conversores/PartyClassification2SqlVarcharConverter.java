package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.PartyClassification;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class PartyClassification2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof PartyClassification) {
            PartyClassification partyClassification = (PartyClassification) source;
	    return partyClassification.toString();
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source instanceof String) {
	    return PartyClassification.valueOf((String) source);
	}
	return source;
    }

}
