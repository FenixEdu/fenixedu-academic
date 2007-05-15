package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunctionType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ResearchFunctionType2SqlConversion implements FieldConversion {
	
	private static final long serialVersionUID = 1L;

	public Object javaToSql(Object source) throws ConversionException {
		if (source == null) {
			return null;
		} else {
			return ((ResearchFunctionType) source).name();
		}
	}

	public Object sqlToJava(Object source) throws ConversionException {
		if (source == null) {
			return null;
		} else {
			return ResearchFunctionType.valueOf((String) source);
		}
	}
}
