/*
 * Created on Feb 18, 2004
 *  
 */
package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public class DelegateType extends ValuedEnum
{
    public static final int DEGREE_TYPE = 1;
    public static final int FIRST_YEAR_TYPE = 2;
    public static final int SECOND_YEAR_TYPE = 2;
    public static final int THIRD_YEAR_TYPE = 2;
    public static final int FOURTH_YEAR_TYPE = 2;
    public static final int FIFTH_YEAR_TYPE = 2;

    public static DelegateType DEGREE = new DelegateType("Degree", DEGREE_TYPE);
    public static DelegateType FIRST_YEAR = new DelegateType("FirstYear", FIRST_YEAR_TYPE);
    public static DelegateType SECOND_YEAR = new DelegateType("SecondYear", SECOND_YEAR_TYPE);
    public static DelegateType THIRD_YEAR = new DelegateType("ThirdYear", THIRD_YEAR_TYPE);
    public static DelegateType FOURTH_YEAR = new DelegateType("FourthYear", FOURTH_YEAR_TYPE);
    public static DelegateType FIFTH_YEAR = new DelegateType("FifthYear", FIFTH_YEAR_TYPE);

    public DelegateType(String name, int value)
    {
        super(name, value);
    }

    public static DelegateType getEnum(String delegateType)
    {
        return (DelegateType) getEnum(DelegateType.class, delegateType);
    }

    public static DelegateType getEnum(int delegateType)
    {
        return (DelegateType) getEnum(DelegateType.class, delegateType);
    }

    public static Map getEnumMap()
    {
        return getEnumMap(DelegateType.class);
    }

    public static List getEnumList()
    {
        return getEnumList(DelegateType.class);
    }

    public static Iterator iterator()
    {
        return iterator(DelegateType.class);
    }

    public String toString()
    {
        String result = "Delegate Type :\n";
        result += "\n  - Delegate Type : " + this.getName();

        return result;
    }
}
