/*
 * DiaSemana2EnumDiaSemanaFieldConversion.java
 *
 * Created on October 13, 2002, 00:11 AM
 */

package ServidorPersistente.Conversores;

/**
 * 
 * @author tfc130
 */
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.DiaSemana;

public class DiaSemana2EnumDiaSemanaFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {

        if (source instanceof DiaSemana) {
            DiaSemana dia = (DiaSemana) source;
            return dia.getDiaSemana();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new DiaSemana(src);
        }

        return source;

    }

}