/*
 * Created on 19/Ago/2003
 *
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public class TestType2EnumTestTypeFieldConversion implements FieldConversion {

	public Object javaToSql(Object arg0) throws ConversionException {
		if (arg0 instanceof TestType) {
			TestType s = (TestType) arg0;
			return s.getType();
		} else {
			return arg0;
		}
	}

	public Object sqlToJava(Object arg0) throws ConversionException {
		if (arg0 instanceof Integer) {
			Integer type = (Integer) arg0;
			return new TestType(type);
		} else {
			return arg0;
		}
	}

}
