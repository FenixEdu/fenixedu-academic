/*
 * 
 * Created on Apr 3, 2003
 */

package ServidorPersistente.Conversores;

/**
 * @author jpvl
 */
import java.util.Date;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaDate2SqlDateConversion implements FieldConversion
{

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source)
    {
        if (source instanceof Date)
        {
            Date date = (Date) source;
            return new java.sql.Date(date.getTime());
        } 
            return source;
        
    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source)
    {
        if (source instanceof java.sql.Date)
        {
            java.sql.Date sqlDate = (java.sql.Date) source;
            Date date = new Date(sqlDate.getTime());
            return date;
        } 
            return source;
        
    }

}