/*
 *
 * Created on Apr 3, 2003
 */

package ServidorPersistente.Conversores;




/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import java.sql.Clob;
import java.sql.SQLException;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Clob2StringFieldConversion implements FieldConversion
{

    public Object javaToSql(Object source)
    {
		System.out.println("*********************SOURCE = "+ source+ ";className:"+source.getClass().getName());
		String str = (String) source; 
		Clob clob = new com.mysql.jdbc.Clob(str);
		try {
			clob.setAsciiStream(str.length());
		} catch (SQLException e) {
			System.out.println("*************************************************************************");
			e.printStackTrace(System.out);
		}
		
		
    	return clob;
    }

    public Object sqlToJava(Object source)
    {
		System.out.println("==================================SQL 2 JAVA :"+source+";className:"+source.getClass().getName());
        if (source instanceof Clob)
        {
            Clob c = (Clob) source;
           
            Long l;
			String str = null;
			try {
				l = new Long(c.length());
				str = c.getSubString(1, l.intValue());				
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
			System.out.println("*********************************SQL 2 JAVA :"+str);
            return str;
        }
        else
        {
			System.out.println("==================================SQL 2 JAVA :"+source+";className:"+source.getClass().getName());
         	return source;   
        }
    }

}