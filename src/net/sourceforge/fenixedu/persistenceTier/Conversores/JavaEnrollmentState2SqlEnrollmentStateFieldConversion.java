/*
 * JavaStudentState2SqlStudentStateFieldConversion.java
 * 
 * Created on 21 de Novembro de 2002, 15:57
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaEnrollmentState2SqlEnrollmentStateFieldConversion implements FieldConversion {

    public Object javaToSql(final Object obj) throws ConversionException {
        if (obj instanceof EnrollmentState) {
            final EnrollmentState enrollmentState = (EnrollmentState) obj;
            return enrollmentState.toString();
        }
        return obj;
    }

    public Object sqlToJava(final Object obj) throws ConversionException {
        EnrollmentState enrollmentState = null;
        if (obj instanceof String) {
            final String enrollmentStateString = (String) obj;
			return EnrollmentState.valueOf(enrollmentStateString);
        }
        return enrollmentState;

    }

}