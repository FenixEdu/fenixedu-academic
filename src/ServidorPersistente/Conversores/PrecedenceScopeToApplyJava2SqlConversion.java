/*
 * Created on 9/Mai/2003 by jpvl
 *
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 */
public class PrecedenceScopeToApplyJava2SqlConversion implements FieldConversion{

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
	 */
	public Object javaToSql(Object source) throws ConversionException {
		if (source instanceof PrecedenceScopeToApply){
			PrecedenceScopeToApply precedenceScopeToApply = (PrecedenceScopeToApply) source;
			switch (precedenceScopeToApply.getScope()){
				case PrecedenceScopeToApply.TO_APLLY_DURING_ENROLMENT_INT:
					return PrecedenceScopeToApply.TO_APLLY_DURING_ENROLMENT_STR; 
				case PrecedenceScopeToApply.TO_APPLY_TO_SPAN_INT:
					return PrecedenceScopeToApply.TO_APPLY_TO_SPAN_STR;
				default:
					throw new IllegalArgumentException(this.getClass().getName()+": Unkown PrecedenceScopeToApply!");
			}
		}
		return source;
	}

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
	 */
	public Object sqlToJava(Object source) throws ConversionException {
		if (source instanceof String){
			String sourceStr = (String) source;

			if (PrecedenceScopeToApply.TO_APPLY_TO_SPAN_STR.equals(sourceStr)){
				return PrecedenceScopeToApply.TO_APPLY_TO_SPAN;				
			}else if (PrecedenceScopeToApply.TO_APLLY_DURING_ENROLMENT_STR.equals(sourceStr)){
				return PrecedenceScopeToApply.TO_APPLY_DURING_ENROLMENT;
			}else{
				throw new IllegalArgumentException(this.getClass().getName()+": Unkown type!("+source+")");
			}
		}
		return source;

	}

}
