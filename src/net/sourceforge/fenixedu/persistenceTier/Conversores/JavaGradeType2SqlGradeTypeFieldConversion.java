package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.curriculum.GradeType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaGradeType2SqlGradeTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof GradeType) {
            GradeType s = (GradeType) source;
            return s.toString();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return GradeType.valueOf(src);
        }
        return source;

    }

}
