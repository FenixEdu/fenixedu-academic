package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public final class GratuityState extends FenixValuedEnum
{

    public static final int PAYED_TYPE = 1;

    public static final int NOT_PAYED_TYPE = 2;

    public static final int PAYED_PARTIALLY_TYPE = 3;

    public static final int EXEMPT_SON_OF_MILITARY_TYPE = 4;

    public static final int EXEMPT_EMPLOYEE_TYPE = 5;

    public static final int EXEMPT_POLITECNIC_TEACHER_TYPE = 6;

    public static final int OTHER_TYPE = 7;

    public static final GratuityState PAYED = new GratuityState("payed",
            GratuityState.PAYED_TYPE);

    public static final GratuityState NOT_PAYED = new GratuityState(
            "not.payed", GratuityState.NOT_PAYED_TYPE);

    public static final GratuityState PAYED_PARTIALLY = new GratuityState(
            "payed.partially", GratuityState.PAYED_PARTIALLY_TYPE);

    public static final GratuityState EXEMPT_SON_OF_MILITARY = new GratuityState(
            "exempt.son.of.military", GratuityState.EXEMPT_SON_OF_MILITARY_TYPE);

    public static final GratuityState EXEMPT_EMPLOYEE = new GratuityState(
            "exempt.employee", GratuityState.EXEMPT_EMPLOYEE_TYPE);

    public static final GratuityState EXEMPT_POLITECNIC_TEACHER = new GratuityState(
            "exempt.politecnic.teacher",
            GratuityState.EXEMPT_POLITECNIC_TEACHER_TYPE);

    public static final GratuityState OTHER = new GratuityState("other",
            GratuityState.OTHER_TYPE);

    /**
     * @param name
     * @param value
     */
    private GratuityState(String name, int value)
    {
        super(name, value);
    }

    public static GratuityState getEnum(String name)
    {
        return (GratuityState) getEnum(GratuityState.class, name);
    }

    public static GratuityState getEnum(int value)
    {
        return (GratuityState) getEnum(GratuityState.class, value);
    }

    public static Map getEnumMap()
    {
        return getEnumMap(GratuityState.class);
    }

    public static List getEnumList()
    {
        return getEnumList(GratuityState.class);
    }

    public static Iterator iterator()
    {
        return iterator(GratuityState.class);
    }

    public String toString()
    {
        String result = "Gratuity State:\n";
        result += "\n  - Gratuity State : " + this.getName();

        return result;
    }

}
