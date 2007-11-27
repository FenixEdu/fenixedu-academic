package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.math.BigDecimal;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class BigDecimalConverter implements FieldConversion {

    public Object javaToSql(Object source) {
	return source instanceof BigDecimal ? source.toString() : source;
    }

    public Object sqlToJava(Object source) {	
	if(source == null || source.equals("")){
	    return null;
	}	
	return source instanceof String ? new BigDecimal((String) source) : source;
    }

}
