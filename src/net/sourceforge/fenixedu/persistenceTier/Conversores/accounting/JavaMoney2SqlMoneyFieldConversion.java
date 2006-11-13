package net.sourceforge.fenixedu.persistenceTier.Conversores.accounting;

import net.sourceforge.fenixedu.util.Money;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaMoney2SqlMoneyFieldConversion implements FieldConversion {
	
	public Object javaToSql(Object source) {
		return source instanceof Money ? source.toString() : source;
	}
	
	public Object sqlToJava(Object source) {
		return source instanceof String ? new Money((String) source) : source;
	}

}
