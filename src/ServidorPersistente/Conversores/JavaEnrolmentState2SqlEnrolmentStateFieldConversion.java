/*
 * JavaStudentState2SqlStudentStateFieldConversion.java
 *
 * Created on 21 de Novembro de 2002, 15:57
 */

package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.EnrolmentState;

public class JavaEnrolmentState2SqlEnrolmentStateFieldConversion implements FieldConversion {
    
    /*
     * @see FieldConversion#javaToSql(Object)
     */
    
	public Object javaToSql(Object obj) throws ConversionException {
		if (obj instanceof EnrolmentState) {
			EnrolmentState enrolmentState = (EnrolmentState) obj;
			return new Integer(enrolmentState.getValue());
		}
		return obj;
	}
       

   /*
     * @see FieldConversion#sqlToJava(Object)
     */

	public Object sqlToJava(Object obj) throws ConversionException {
		EnrolmentState enrolmentState = null;
		if (obj instanceof Integer) {
			Integer enrolmentStateId = (Integer) obj;
			
			enrolmentState = EnrolmentState.getEnum(enrolmentStateId.intValue());
			if (enrolmentState == null) {
				throw new IllegalArgumentException(this.getClass().getName() + ": Illegal EnrolmentState type!(" + obj + ")");
			}
		} else {
			throw new IllegalArgumentException("Illegal EnrolmentState type!(" + obj + ")");
		}
		return enrolmentState;

	}
    
}
