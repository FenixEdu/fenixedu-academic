/*
 * Created on 22/Jan/2004
 *
 */
package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Tânia Pousão
 *
 */
public class GratuitySituationType extends FenixValuedEnum
{
	public static final int DEBTOR_TYPE = 1;
	public static final int CREDITOR_TYPE = 2;
	public static final int REGULARIZED_TYPE = 3;
	
	public static final GratuitySituationType DEBTOR = new GratuitySituationType("debtor", GratuitySituationType.DEBTOR_TYPE);
	public static final GratuitySituationType CREDITOR = new GratuitySituationType("creditor", GratuitySituationType.CREDITOR_TYPE);
	public static final GratuitySituationType REGULARIZED = new GratuitySituationType("regularized", GratuitySituationType.REGULARIZED_TYPE);

	public GratuitySituationType(String name, int value)
	{
		super(name, value);
	}
	
	public GratuitySituationType(String name)
	{
		super(name, 0);
	}

	public static GratuitySituationType getEnum(String gratuitySituationType)
	{
		return (GratuitySituationType) getEnum(GratuitySituationType.class, gratuitySituationType);
	}

	public static GratuitySituationType getEnum(int gratuitySituationType)
	{
		return (GratuitySituationType) getEnum(GratuitySituationType.class, gratuitySituationType);
	}

	public static Map getEnumMap()
	{
		return getEnumMap(GratuitySituationType.class);
	}

	public static List getEnumList()
	{
		return getEnumList(GratuitySituationType.class);
	}
	
	public static Iterator iterator()
	{
		return iterator(GratuitySituationType.class);
	}

	public String toString()
	{
		String gratuitySituationString = "\nGratuity Situation String : " + this.getName();
		gratuitySituationString += "\nGratuity Situation String(value): " + this.getValue();
		return gratuitySituationString;
	}
}
