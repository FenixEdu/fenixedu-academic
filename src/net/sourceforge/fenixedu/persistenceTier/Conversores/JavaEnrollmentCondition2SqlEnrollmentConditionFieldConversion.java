package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.enrollment.EnrollmentCondition;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author David Santos in Jun 15, 2004
 */

public class JavaEnrollmentCondition2SqlEnrollmentConditionFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof EnrollmentCondition) {
            EnrollmentCondition condition = (EnrollmentCondition) source;
            return condition.getName();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return EnrollmentCondition.getEnum(src);
        }
        return source;

    }

}