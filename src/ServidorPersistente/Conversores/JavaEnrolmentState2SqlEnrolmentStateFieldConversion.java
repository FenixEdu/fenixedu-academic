/*
 * JavaStudentState2SqlStudentStateFieldConversion.java
 *
 * Created on 21 de Novembro de 2002, 15:57
 */

package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.EnrollmentState;

public class JavaEnrolmentState2SqlEnrolmentStateFieldConversion implements FieldConversion {
    
    /*
     * @see FieldConversion#javaToSql(Object)
     */
    
	public Object javaToSql(Object obj) throws ConversionException {
		if (obj instanceof EnrollmentState) {
			EnrollmentState enrolmentState = (EnrollmentState) obj;
			return new Integer(enrolmentState.getValue());
		}
		return obj;
	}
       

   /*
     * @see FieldConversion#sqlToJava(Object)
     */

	public Object sqlToJava(Object obj) throws ConversionException {
		EnrollmentState enrolmentState = null;
		if (obj instanceof Integer) {
			Integer enrolmentStateId = (Integer) obj;
			
			enrolmentState = EnrollmentState.getEnum(enrolmentStateId.intValue());
			if (enrolmentState == null) {
				throw new IllegalArgumentException(this.getClass().getName() + ": Illegal EnrolmentState type!(" + obj + ")");
			}
		} else {
			throw new IllegalArgumentException("Illegal EnrolmentState type!(" + obj + ")");
		}
		return enrolmentState;

	}
    
}
