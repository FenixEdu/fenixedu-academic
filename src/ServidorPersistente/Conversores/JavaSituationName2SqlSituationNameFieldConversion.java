/*
 * JavaSituationName2SqlSituationNameFieldConversion.java
 *
 * Created on 19 de Novembro de 2002, 17:30
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.SituationName;

public class JavaSituationName2SqlSituationNameFieldConversion implements
        FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof SituationName) {
            SituationName s = (SituationName) source;
            return s.getSituationName();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new SituationName(src);
        }

        return source;

    }
}