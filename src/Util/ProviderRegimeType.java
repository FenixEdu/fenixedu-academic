/*
 * Created on Nov 13, 2003
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
public class ProviderRegimeType extends ValuedEnum
{

    public static final int EXCLUSIVE_TYPE = 1;
    public static final int CUMULATIVE_TYPE = 2;
    public static final int COMPLEMENT_TYPE = 3;
    public static final int EXCLUSIVE_CUMULATIVE_TYPE = 4;

    public static final ProviderRegimeType EXCLUSIVE =
        new ProviderRegimeType("regime.exclusive", ProviderRegimeType.EXCLUSIVE_TYPE);
    public static final ProviderRegimeType CUMULATIVE =
        new ProviderRegimeType("regime.cumulative", ProviderRegimeType.CUMULATIVE_TYPE);
    public static final ProviderRegimeType COMPLEMENT =
        new ProviderRegimeType("regime.complement", ProviderRegimeType.COMPLEMENT_TYPE);
    public static final ProviderRegimeType EXCLUSIVE_CUMULATIVE =
        new ProviderRegimeType("regime.exclusiveCumulative", ProviderRegimeType.EXCLUSIVE_CUMULATIVE_TYPE);

    public ProviderRegimeType(String name, int value)
    {
        super(name, value);
    }

    public static ProviderRegimeType getEnum(String providerRegimeType)
    {
        return (ProviderRegimeType) getEnum(ProviderRegimeType.class, providerRegimeType);
    }

    public static ProviderRegimeType getEnum(int providerRegimeType)
    {
        return (ProviderRegimeType) getEnum(ProviderRegimeType.class, providerRegimeType);
    }

    public static Map getEnumMap()
    {
        return getEnumMap(ProviderRegimeType.class);
    }

    public static List getEnumList()
    {
        return getEnumList(ProviderRegimeType.class);
    }

    public static Iterator iterator()
    {
        return iterator(ProviderRegimeType.class);
    }

    public String toString()
    {
        String result = "Provider Regime Type:\n";
        result += "\n  - Provider Regime Type : " + this.getName();

        return result;
    }
}
