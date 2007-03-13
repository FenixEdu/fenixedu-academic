package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.CompetenceCourseType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class CompetenceCourseType2SqlConversion implements FieldConversion {

    private static final long serialVersionUID = 1L;

    public Object javaToSql(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return ((CompetenceCourseType) source).name();
        }
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null && source.equals("")) {
            return null;
        }
        else {
            return CompetenceCourseType.valueOf((String) source);
        }
    }

}
