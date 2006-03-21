package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class MultiLanguageString2SqlMultiLanguageStringConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof MultiLanguageString) {
            return ((MultiLanguageString) source).toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null || source.equals("")) {
            return null;
        }
        if (source instanceof String) {
            String src = (String) source;

            final String[] MLSFromSql = src.split("\001");

            if (MLSFromSql != null) {

                MultiLanguageString multiLanguageString = new MultiLanguageString();

                for (int i = 0; i < MLSFromSql.length; i++) {

                    String[] s = MLSFromSql[i].split("\002");

                    final String language = s[0];
                    final String content = s[1];

                    multiLanguageString.addContent(Language.valueOf(language), content);
                }
                return multiLanguageString;
            }
            return null;
        }
        return source;
    }
}