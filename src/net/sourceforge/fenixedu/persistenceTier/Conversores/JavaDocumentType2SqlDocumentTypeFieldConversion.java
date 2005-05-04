package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import net.sourceforge.fenixedu.domain.DocumentType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaDocumentType2SqlDocumentTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof DocumentType) {
            DocumentType s = (DocumentType) source;
            return s.name();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return DocumentType.valueOf(src);
        }
        return source;

    }

}