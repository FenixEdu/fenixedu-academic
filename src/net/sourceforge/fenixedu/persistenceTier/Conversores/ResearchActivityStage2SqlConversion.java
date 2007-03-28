package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityStage;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ResearchActivityStage2SqlConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return ((ResearchActivityStage) source).name();
        }
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null && source.equals("")) {
            return null;
        }
        else {
            return ResearchActivityStage.valueOf((String) source);
        }
    }

}
