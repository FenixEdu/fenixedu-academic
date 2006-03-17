package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.Duration;

public class Duration2SqlBigIntConverter implements FieldConversion {
	
	public Object javaToSql(Object source) {
		if (source instanceof Duration) {
			Duration duration = (Duration) source;
			return duration.getMillis();
		}
		return source;
	  }
	  
	public Object sqlToJava(Object source) {
		if (source instanceof Long) {
			Long sqlBigInt = (Long) source;
			return new Duration(sqlBigInt.longValue());
		}
		return source;
	}

}
