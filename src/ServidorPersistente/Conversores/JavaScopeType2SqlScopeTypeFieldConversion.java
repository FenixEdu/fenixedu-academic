package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.ScopeType;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class JavaScopeType2SqlScopeTypeFieldConversion implements FieldConversion {

	public Object javaToSql(Object source) {
		if (source instanceof ScopeType) {
			ScopeType src = (ScopeType) source;
			return src.getType();
		} else {
			return source;
		}
	}

	public Object sqlToJava(Object source) {
		if (source instanceof Integer) {
			Integer src = (Integer) source;
			return new ScopeType(src);
		} else {
			return source;
		}
	}

}