/*
 *
 * Created on Apr 3, 2003
 */

package ServidorPersistente.Conversores;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Calendar2DateFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof Calendar) {
            return new java.sql.Date(((Calendar) source).getTime().getTime());
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof java.sql.Date) {
            Calendar res = Calendar.getInstance();
            res.setTime(new java.util.Date(((java.sql.Date) source).getTime()));
            return res;
        }

        return source;

    }

}