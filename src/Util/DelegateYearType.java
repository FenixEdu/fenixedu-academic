/*
 * Created on Feb 18, 2004
 *  
 */
package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public class DelegateYearType extends FenixValuedEnum
{
    public static final int DEGREE_TYPE = 0;
    public static final int FIRST_YEAR_TYPE = 1;
    public static final int SECOND_YEAR_TYPE = 2;
    public static final int THIRD_YEAR_TYPE = 3;
    public static final int FOURTH_YEAR_TYPE = 4;
    public static final int FIFTH_YEAR_TYPE = 5;

    public static DelegateYearType DEGREE = new DelegateYearType("Degree", DEGREE_TYPE);
    public static DelegateYearType FIRST_YEAR = new DelegateYearType("FirstYear", FIRST_YEAR_TYPE);
    public static DelegateYearType SECOND_YEAR = new DelegateYearType("SecondYear", SECOND_YEAR_TYPE);
    public static DelegateYearType THIRD_YEAR = new DelegateYearType("ThirdYear", THIRD_YEAR_TYPE);
    public static DelegateYearType FOURTH_YEAR = new DelegateYearType("FourthYear", FOURTH_YEAR_TYPE);
    public static DelegateYearType FIFTH_YEAR = new DelegateYearType("FifthYear", FIFTH_YEAR_TYPE);

    public DelegateYearType(String name, int value)
    {
        super(name, value);
    }

    public static DelegateYearType getEnum(String delegateType)
    {
        return (DelegateYearType) getEnum(DelegateYearType.class, delegateType);
    }

    public static DelegateYearType getEnum(int delegateType)
    {
        return (DelegateYearType) getEnum(DelegateYearType.class, delegateType);
    }

    public static Map getEnumMap()
    {
        return getEnumMap(DelegateYearType.class);
    }

    public static List getEnumList()
    {
        return getEnumList(DelegateYearType.class);
    }

    public static Iterator iterator()
    {
        return iterator(DelegateYearType.class);
    }

    public String toString()
    {
        String result = "Delegate Type :\n";
        result += "\n  - Delegate Type : " + this.getName();

        return result;
    }
}
