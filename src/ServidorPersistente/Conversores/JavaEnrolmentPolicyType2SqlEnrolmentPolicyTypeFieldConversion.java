/*
 * Created on 23/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.EnrolmentPolicyType;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JavaEnrolmentPolicyType2SqlEnrolmentPolicyTypeFieldConversion implements FieldConversion {
    
/*
 * @see FieldConversion#javaToSql(Object)
 */
	public Object javaToSql(Object source)
	{
		if (source instanceof EnrolmentPolicyType)
		{
			EnrolmentPolicyType s = (EnrolmentPolicyType) source;
			return s.getType();
		}
		else 			
			return source;
	
	}    

	/*
	* @see FieldConversion#sqlToJava(Object)
	*/
		public Object sqlToJava(Object source)
		{
			if (source instanceof Integer)
			{
				Integer src = (Integer) source;
				return new EnrolmentPolicyType(src);
			}
			else
			{
				return source;
			}
		}


}