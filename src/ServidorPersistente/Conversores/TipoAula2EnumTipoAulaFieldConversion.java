/*
 * TipoAula2EnumTipoAulaFieldConversion.java
 *
 * Created on October 12, 2002, 9:51 PM
 */

package ServidorPersistente.Conversores;

/**
 * 
 * @author tfc130
 */
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.TipoAula;

public class TipoAula2EnumTipoAulaFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {

        if (source instanceof TipoAula) {
            TipoAula s = (TipoAula) source;
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
            return new TipoAula(src);
        }

        return source;

    }

}