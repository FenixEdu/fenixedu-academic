package ServidorPersistente.Conversores.classProperties;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.classProperties.GeneralClassPropertyValue;

/**
 * @author David Santos in Apr 7, 2004
 */

public class GeneralClassPropertyValueJava2SqlConversion implements FieldConversion
{
	public Object javaToSql(Object source)
	{
		if (source instanceof GeneralClassPropertyValue)
		{
			GeneralClassPropertyValue src = (GeneralClassPropertyValue) source;
			return src.getValue();
		} 
			return source;
		
	}

	public Object sqlToJava(Object source)
	{
		if (source instanceof String)
		{
			String src = (String) source;
			return new GeneralClassPropertyValue(src);
		} 
			return source;
		
	}
}