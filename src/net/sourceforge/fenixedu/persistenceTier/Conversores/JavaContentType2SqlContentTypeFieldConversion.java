package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.ContentType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class JavaContentType2SqlContentTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof ContentType) {
            ContentType src = (ContentType) source;
            return src.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return ContentType.valueOf(src);
        }
        return source;
    }

}