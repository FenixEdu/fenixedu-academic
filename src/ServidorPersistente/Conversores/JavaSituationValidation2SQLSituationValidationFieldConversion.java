/*
 * JavaSituationValidation2SQLSituationValidationFieldConversion.java
 *
 * Created on 08 of February 2003, 11:17
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.CandidateSituationValidation;


/**
 *
 * @author  Nuno Nunes & Joana Mota
 */
public class JavaSituationValidation2SQLSituationValidationFieldConversion implements FieldConversion {
    
	/*
	 * @see FieldConversion#javaToSql(Object)
	 */
	public Object javaToSql(Object source)
	{
		if (source instanceof CandidateSituationValidation)
		{
			CandidateSituationValidation s = (CandidateSituationValidation) source;
			return s.getCandidateSituationValidation();
		}
		else {
			return source;
		}
	}    

   /*
	 * @see FieldConversion#sqlToJava(Object)
	 */
	public Object sqlToJava(Object source)
	{
		if (source instanceof Integer)
		{
			Integer src = (Integer) source;
			return new CandidateSituationValidation(src);
		}
		else
		{
			return source;
		}
	}    
}
