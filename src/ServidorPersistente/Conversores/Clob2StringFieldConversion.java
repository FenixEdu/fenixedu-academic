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

public class Clob2StringFieldConversion implements FieldConversion {

	public Object javaToSql(Object source) {
		String str = (String) source;
		Clob clob = null;
		if (str != null) {

			clob = new com.mysql.jdbc.Clob(str);
			try {
				clob.setAsciiStream(str.length());
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}
		return clob;
	}

	public Object sqlToJava(Object source) {
		if (source instanceof Clob) {
			Clob c = (Clob) source;

			Long l;
			String str = null;
			try {
				l = new Long(c.length());
				str = c.getSubString(1, l.intValue());
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
			return str;
		} else {
			return source;
		}
	}

}