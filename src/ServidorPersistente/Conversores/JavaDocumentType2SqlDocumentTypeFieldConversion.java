package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.DocumentType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaDocumentType2SqlDocumentTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof DocumentType) {
            DocumentType s = (DocumentType) source;
            return s.getType();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new DocumentType(src);
        }
        return source;

    }

}