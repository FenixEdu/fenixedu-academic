package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.thesis.ThesisState;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ThesisState2SqlConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return ((ThesisState) source).name();
        }
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null && source.equals("")) {
            return null;
        }
        else {
            return ThesisState.valueOf((String) source);
        }
    }

}
