package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.EquivalenceType;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class JavaEquivalenceType2SqlEquivalenceTypeFieldConversion implements FieldConversion {

	public Object javaToSql(Object source) {
		if (source instanceof EquivalenceType) {
			EquivalenceType src = (EquivalenceType) source;
			return src.getEquivalenceType();
		} else {
			return source;
		}
	}

	public Object sqlToJava(Object source) {
		if (source instanceof Integer) {
			Integer src = (Integer) source;
			return new EquivalenceType(src);
		} else {
			return source;
		}
	}

}