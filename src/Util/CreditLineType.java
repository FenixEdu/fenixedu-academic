package Util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

import java.util.List;

/**
 * @author Fernanda Quitério
 * 28/Nov/2003
 * 
 */
public class CreditLineType extends ValuedEnum implements Serializable
{
	public static final int MANAGEMENT_POSITION_TYPE = 1;
	public static final int SERVICE_EXEMPTION_TYPE = 2;
	public static final int SABBATICAL_TYPE = 3;
	public static final int OTHER_TYPE = 4;
	
	public static final CreditLineType MANAGEMENT_POSITION = new CreditLineType("creditLineType.managementPosition", CreditLineType.MANAGEMENT_POSITION_TYPE);
	public static final CreditLineType SERVICE_EXEMPTION = new CreditLineType("creditLineType.serviceExemption", CreditLineType.SERVICE_EXEMPTION_TYPE);
	public static final CreditLineType SABBATICAL = new CreditLineType("creditLineType.sabbatical", CreditLineType.SABBATICAL_TYPE);
	public static final CreditLineType OTHER = new CreditLineType("creditLineType.other", CreditLineType.OTHER_TYPE);

    private CreditLineType(String name, int value)
    {
		super(name, value);
	}

    public static CreditLineType getEnum(String creditLineType)
    {
		return (CreditLineType) getEnum(CreditLineType.class, creditLineType);
	}

    public static CreditLineType getEnum(int creditLineType)
    {
		return (CreditLineType) getEnum(CreditLineType.class, creditLineType);
	}

    public static Map getEnumMap()
    {
		return getEnumMap(CreditLineType.class);
	}

    public static List getEnumList()
    {
		return getEnumList(CreditLineType.class);
	}

    public static Iterator iterator()
    {
		return iterator(CreditLineType.class);
	}

    public String toString()
    {
		String result = "Credit Line Type:\n";
		result += "\n  - Credit Line Type : " + this.getName();

		return result;
	}
}