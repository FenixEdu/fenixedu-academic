package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.CurricularCourseExecutionScope;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class JavaCurricularCourseExecutionScope2SqlCurricularCourseExecutionScopeFieldConversion
        implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof CurricularCourseExecutionScope) {
            CurricularCourseExecutionScope src = (CurricularCourseExecutionScope) source;
            return src.getType();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new CurricularCourseExecutionScope(src);
        }
        return source;

    }

}