/*
 * JavaString2FloatFieldConversion
 * 
 * Created on 12 de Novembro de 2002, 17:31
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 */

public class JavaString2FloatFieldConversion implements FieldConversion
{

    /*
	 * @see FieldConversion#javaToSql(Object)
	 */
    public Object javaToSql(Object source)
    {
        if (source instanceof Double)
        {

            return source.toString();
        }
        else
        {
            return source;
        }
    }

    /*
	 * @see FieldConversion#sqlToJava(Object)
	 */
    public Object sqlToJava(Object source)
    {
        if (source instanceof String)
        {
            String src = (String) source;
            Long rounded =
                new Long(Math.round(Double.valueOf(src.replace(',', '.')).doubleValue() * 10));

            return new Double(rounded.doubleValue() / 10);
        }
        else
        {
            return source;
        }
    }
}
