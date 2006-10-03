package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.tests.TestGroupStatus;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class TestGroupStatus2SqlTestGroupStatus implements FieldConversion {

	public Object javaToSql(Object source) throws ConversionException {

		if (source instanceof TestGroupStatus) {
			return ((TestGroupStatus) source).name();
		}
		return source;
	}

	public Object sqlToJava(Object source) throws ConversionException {
		if (source == null || source.equals("")) {
			return null;
		} else if (source instanceof String) {
			return TestGroupStatus.valueOf((String) source);
		}
		return source;
	}
}
