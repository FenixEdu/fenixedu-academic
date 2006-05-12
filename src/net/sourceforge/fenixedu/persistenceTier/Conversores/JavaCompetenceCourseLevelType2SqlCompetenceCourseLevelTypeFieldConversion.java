package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaCompetenceCourseLevelType2SqlCompetenceCourseLevelTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof CompetenceCourseLevel) {
            CompetenceCourseLevel ccl = (CompetenceCourseLevel) source;
            return ccl.name();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return CompetenceCourseLevel.valueOf(src);
        }
        return source;
    }

}