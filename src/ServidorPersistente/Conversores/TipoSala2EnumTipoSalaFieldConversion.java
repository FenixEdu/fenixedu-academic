/*
 * TipoSala2EnumTipoSalaFieldConversion.java
 *
 * Created on October 12, 2002, 23:37 PM
 */

package ServidorPersistente.Conversores;

/**
 * 
 * @author tfc130
 */
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.TipoSala;

public class TipoSala2EnumTipoSalaFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {

        if (source instanceof TipoSala) {
            TipoSala s = (TipoSala) source;
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
            return new TipoSala(src);
        }

        return source;

    }

}