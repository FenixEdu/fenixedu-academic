package ServidorPersistente.Conversores.classProperties;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.classProperties.GeneralClassPropertyName;

/**
 * @author David Santos in Apr 7, 2004
 */

public class GeneralClassPropertyNameJava2SqlConversion implements FieldConversion
{
	public Object javaToSql(Object source)
	{
		if (source instanceof GeneralClassPropertyName)
		{
			GeneralClassPropertyName src = (GeneralClassPropertyName) source;
			return src.getName();
		} 
		
			return source;
		
	}

	public Object sqlToJava(Object source)
	{
		if (source instanceof String)
		{
			String src = (String) source;
			return new GeneralClassPropertyName(src);
		} 
			return source;
		
	}
}