/*
 * Created on 22/Nov/2003
 *  
 */
package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class OldPublicationType extends ValuedEnum
{
	public static final int DIDACTIC_TYPE = 1;
	public static final int CIENTIFIC_TYPE = 2;

	public static OldPublicationType DIDACTIC = new OldPublicationType("Didactic", DIDACTIC_TYPE);
	public static OldPublicationType CIENTIFIC = new OldPublicationType("Cientific", CIENTIFIC_TYPE);

	public OldPublicationType(String name, int value)
	{
	    super(name, value);
	}

	public static OldPublicationType getEnum(String oldPublicationType)
	{
		return (OldPublicationType) getEnum(OldPublicationType.class, oldPublicationType);
	}

	public static OldPublicationType getEnum(int oldPublicationType)
	{
		return (OldPublicationType) getEnum(OldPublicationType.class, oldPublicationType);
	}

	public static Map getEnumMap()
	{
		return getEnumMap(OldPublicationType.class);
	}

	public static List getEnumList()
	{
		return getEnumList(OldPublicationType.class);
	}

	public static Iterator iterator()
	{
		return iterator(OldPublicationType.class);
	}

	public String toString()
	{
		String result = "Old Publication Type Type :\n";
		result += "\n  - OldPublication Type : " + this.getName();

		return result;
	}

}
