package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class JavaCurricularCourseType2SqlCurricularCourseTypeFieldConversion implements FieldConversion {

	public Object javaToSql(Object source) {
		if (source instanceof CurricularCourseType) {
			CurricularCourseType src = (CurricularCourseType) source;
			return src.getCurricularCourseType();
		} else {
			return source;
		}
	}

	public Object sqlToJava(Object source) {
		if (source instanceof Integer) {
			Integer src = (Integer) source;
			return new CurricularCourseType(src);
		} else {
			return source;
		}
	}

}