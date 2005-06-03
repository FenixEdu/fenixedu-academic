/*
 * TipoAula2EnumTipoAulaFieldConversion.java
 *
 * Created on October 12, 2002, 9:51 PM
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class TipoAula2EnumTipoAulaFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {

        if (source instanceof ShiftType) {
            ShiftType s = (ShiftType) source;
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
            return  ShiftType.valueOf(src);
        }

        return source;

    }

}