/*
 * JavaTipoDocId2SqlTipoDocIdFieldConversion.java
 *
 * Created on 12 de Novembro de 2002, 17:31
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.TipoDocumentoIdentificacao;

/**
 * 
 * @author dcs-rjao
 */

public class JavaTipoDocId2SqlTipoDocIdFieldConversion implements
        FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof TipoDocumentoIdentificacao) {
            TipoDocumentoIdentificacao s = (TipoDocumentoIdentificacao) source;
            return s.getTipo();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new TipoDocumentoIdentificacao(src);
        }

        return source;

    }
}