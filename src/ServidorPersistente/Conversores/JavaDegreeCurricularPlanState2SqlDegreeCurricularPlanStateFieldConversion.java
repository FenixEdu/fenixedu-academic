package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class JavaDegreeCurricularPlanState2SqlDegreeCurricularPlanStateFieldConversion implements FieldConversion {

	public Object javaToSql(Object source) {
		if (source instanceof DegreeCurricularPlanState) {
			DegreeCurricularPlanState s = (DegreeCurricularPlanState) source;
			return s.getDegreeState();
		} 
			return source;
		
	}

	public Object sqlToJava(Object source) {
		if (source instanceof Integer) {
			Integer src = (Integer) source;
			return new DegreeCurricularPlanState(src);
		} 
			return source;
		
	}

}