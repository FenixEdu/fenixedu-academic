package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.DegreeState;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class JavaDegreeState2SqlDegreeStateFieldConversion implements FieldConversion {

	public Object javaToSql(Object source) {
		if (source instanceof DegreeState) {
			DegreeState s = (DegreeState) source;
			return s.getDegreeState();
		} else {
			return source;
		}
	}

	public Object sqlToJava(Object source) {
		if (source instanceof Integer) {
			Integer src = (Integer) source;
			return new DegreeState(src);
		} else {
			return source;
		}
	}

}