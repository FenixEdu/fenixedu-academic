/*
 * Created on 27/Nov/2003
 *
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class JavaAreaType2SqlAreaTypeFieldConversion implements FieldConversion
{

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source)
    {
        if (source instanceof AreaType)
        {
            AreaType a = (AreaType) source;
            return a.getAreaType();
        }
       
            return source;
        
    }

    /*
    	 * @see FieldConversion#sqlToJava(Object)
    	 */
    public Object sqlToJava(Object source)
    {
        if (source instanceof Integer)
        {
            Integer src = (Integer) source;
            return new AreaType(src);
        }
        
            return source;
        
    }

}
