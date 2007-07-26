package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.personnelSection.payrollSection.bonus.util.BonusType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class BonusType2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof BonusType) {
	    BonusType bonusType = (BonusType) source;
	    return bonusType.toString();
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source instanceof String) {
	    return BonusType.valueOf((String) source);
	}
	return source;
    }

}
