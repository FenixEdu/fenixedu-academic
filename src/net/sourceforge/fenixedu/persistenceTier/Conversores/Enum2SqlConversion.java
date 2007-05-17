package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Enum2SqlConversion<T extends Enum<T>> implements FieldConversion {

	private static final long serialVersionUID = 1L;

	private Class<T> enumType;
	
	public Enum2SqlConversion(Class<T> enumType) {
		super();
		
		this.enumType = enumType;
	}

	public Object javaToSql(Object object) throws ConversionException {
		if (object == null) {
			return null;
		}
		else {
			return ((Enum) object).name();
		}
	}

	public Object sqlToJava(Object object) throws ConversionException {
		if (object == null || object.equals("")) {
			return null;
		}
		else {
			return Enum.valueOf(this.enumType, (String) object);
		}
	}

	public static <T extends Enum<T>> FieldConversion getConversion(Class<T> enumType) {
		return new Enum2SqlConversion<T>(enumType);
	}
}
