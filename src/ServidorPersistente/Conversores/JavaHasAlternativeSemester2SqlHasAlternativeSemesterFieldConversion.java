package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.HasAlternativeSemester;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class JavaHasAlternativeSemester2SqlHasAlternativeSemesterFieldConversion implements FieldConversion {

	public Object javaToSql(Object source) {
		if (source instanceof HasAlternativeSemester) {
			HasAlternativeSemester src = (HasAlternativeSemester) source;
			return src.getState();
		} else {
			return source;
		}
	}

	public Object sqlToJava(Object source) {
		if (source instanceof Integer) {
			Integer src = (Integer) source;
			return new HasAlternativeSemester(src);
		} else {
			return source;
		}
	}

}