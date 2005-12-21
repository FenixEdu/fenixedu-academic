package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.GradeScale;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaGradeScale2SqlGradeScaleFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof GradeScale) {
        	GradeScale s = (GradeScale) source;
            return s.toString();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return GradeScale.valueOf(src);
        }
        return source;

    }

}
