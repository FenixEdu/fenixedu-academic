/*
 * JavaStudentState2SqlStudentStateFieldConversion.java
 *
 * Created on 21 de Novembro de 2002, 15:57
 */

package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.EnrollmentState;

public class JavaEnrollmentState2SqlEnrollmentStateFieldConversion implements FieldConversion {
    
    /*
     * @see FieldConversion#javaToSql(Object)
     */
    
	public Object javaToSql(Object obj) throws ConversionException {
		if (obj instanceof EnrollmentState) {
			EnrollmentState enrollmentState = (EnrollmentState) obj;
			return new Integer(enrollmentState.getValue());
		}
		return obj;
	}
       

   /*
     * @see FieldConversion#sqlToJava(Object)
     */

	public Object sqlToJava(Object obj) throws ConversionException {
		EnrollmentState enrollmentState = null;
		if (obj instanceof Integer) {
			Integer enrollmentStateId = (Integer) obj;
			
			enrollmentState = EnrollmentState.getEnum(enrollmentStateId.intValue());
			if (enrollmentState == null) {
				throw new IllegalArgumentException(this.getClass().getName() + ": Illegal EnrolmentState type!(" + obj + ")");
			}
		} else {
			throw new IllegalArgumentException("Illegal EnrolmentState type!(" + obj + ")");
		}
		return enrollmentState;

	}
    
}
