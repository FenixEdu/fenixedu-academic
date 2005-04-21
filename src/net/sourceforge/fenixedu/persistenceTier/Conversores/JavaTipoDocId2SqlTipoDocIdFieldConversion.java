/*
 * JavaTipoDocId2SqlTipoDocIdFieldConversion.java
 *
 * Created on 12 de Novembro de 2002, 17:31
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.person.IDDocumentType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author dcs-rjao
 */

public class JavaTipoDocId2SqlTipoDocIdFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof IDDocumentType) {
            IDDocumentType s = (IDDocumentType) source;
            return s.toString();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return IDDocumentType.valueOf(src);
        }

        return source;

    }
}