/*
 * JavaEstadoCivil2SqlEstadoCivilFieldConversion.java
 *
 * Created on 14 de Novembro de 2002, 10:12
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.EstadoCivil;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author David Santos
 */
public class JavaEstadoCivil2SqlEstadoCivilFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof EstadoCivil) {
            EstadoCivil s = (EstadoCivil) source;
            return s.getEstadoCivil();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new EstadoCivil(src);
        }

        return source;

    }
}