package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class MultiLanguageString2SqlMultiLanguageStringConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof MultiLanguageString) {
            return ((MultiLanguageString) source).exportAsString();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null || source.equals("")) {
            return null;
        }
        if (source instanceof String) {
            String src = (String) source;
            MultiLanguageString multiLanguageString = new MultiLanguageString();
            for (int i = 0; i < src.length();) {
                final String language = src.substring(i, i + 2);
                final int pos = src.indexOf(':');
                final int lenght = Integer.parseInt(src.substring(i + 2, pos));
                final String content = src.substring(pos+1, pos + lenght+1);
                multiLanguageString.setContent(Language.valueOf(language), content);
                i = pos + lenght+1;
            }
            return multiLanguageString;
        }
        return null;

    }
}