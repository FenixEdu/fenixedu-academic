/*
 * Created on 19/Ago/2003
 *
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author Susana Fernandes
 */
public class Int2BooleanFieldConversion implements FieldConversion {

    private static Integer I_TRUE = new Integer(1);

    private static Integer I_FALSE = new Integer(0);

    private static Boolean B_TRUE = new Boolean(true);

    private static Boolean B_FALSE = new Boolean(false);

    public Object javaToSql(Object arg0) throws ConversionException {
        if (arg0 instanceof Boolean) {
            if (arg0.equals(B_TRUE)) {
                return I_TRUE;
            }
            return I_FALSE;

        }
        return arg0;

    }

    public Object sqlToJava(Object arg0) throws ConversionException {
        if (arg0 instanceof Integer) {
            if (arg0.equals(I_TRUE)) {
                return B_TRUE;
            }
            return B_FALSE;

        }
        return arg0;

    }
}