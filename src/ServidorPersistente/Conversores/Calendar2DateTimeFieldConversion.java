/*
 *
 * Created on Apr 1, 2004
 */

package ServidorPersistente.Conversores;

/**
 * 
 * @author Tânia Pousão
 */
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Calendar2DateTimeFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof Calendar) {
            return new Timestamp(((Calendar) source).getTimeInMillis());
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Timestamp) {
            Calendar res = Calendar.getInstance();
            res.setTimeInMillis(((Timestamp) source).getTime());
            return res;
        }

        return source;

    }

}