/*
 * Created on 25/Nov/2003
 *
 */
package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public class AreaType
{

    public static final int SPECIALIZATION = 1;
    public static final int SECONDARY = 2;
    public static final int BASE = 3;

    public static final String SPECIALIZATION_STRING = "Specialization";
    public static final String SECONDARY_STRING = "Secundary";
    public static final String BASE_STRING = "Base";
    public static final String DEFAULT = "[Choose one specialization]";

    public static final AreaType SPECIALIZATION_OBJ = new AreaType(AreaType.SPECIALIZATION);
    public static final AreaType SECONDARY_OBJ = new AreaType(AreaType.SECONDARY);
    public static final AreaType BASE_OBJ = new AreaType(AreaType.BASE);

    private Integer areaType;

    public AreaType()
    {
    }

    public AreaType(int areaType)
    {
        this.areaType = new Integer(areaType);
    }

    public AreaType(Integer areaType)
    {
        this.areaType = areaType;
    }

    public AreaType(String areaType)
    {
        if (areaType.equals(AreaType.SPECIALIZATION_STRING))
            this.areaType = new Integer(AreaType.SPECIALIZATION);
        if (areaType.equals(AreaType.SECONDARY_STRING))
            this.areaType = new Integer(AreaType.SECONDARY);
        if (areaType.equals(AreaType.BASE_STRING))
            this.areaType = new Integer(AreaType.BASE);
    }

    public static ArrayList toArrayList()
    {
        ArrayList result = new ArrayList();
        result.add(new LabelValueBean(AreaType.DEFAULT, null));
        result.add(new LabelValueBean(AreaType.SPECIALIZATION_STRING, AreaType.SPECIALIZATION_STRING));
        result.add(new LabelValueBean(AreaType.SECONDARY_STRING, AreaType.SECONDARY_STRING));
        result.add(new LabelValueBean(AreaType.BASE_STRING, AreaType.BASE_STRING));
        return result;
    }

    public static ArrayList toLabelValueBeanList()
    {
        ArrayList result = new ArrayList();
        result.add(
            new LabelValueBean(AreaType.SPECIALIZATION_STRING, String.valueOf(AreaType.SPECIALIZATION)));
        result.add(new LabelValueBean(AreaType.SECONDARY_STRING, String.valueOf(AreaType.SECONDARY)));
        result.add(new LabelValueBean(AreaType.BASE_STRING, String.valueOf(AreaType.BASE)));
        return result;
    }

    public Integer getAreaType()
    {
        return this.areaType;
    }

    public void setAreaType(Integer areaType)
    {
        this.areaType = areaType;
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof AreaType)
        {
            AreaType type = (AreaType) obj;
            resultado = (getAreaType() != null) && (getAreaType().equals(type.getAreaType()));
        }
        return resultado;
    }

    public String toString()
    {
        int value = this.areaType.intValue();
        switch (value)
        {
            case SPECIALIZATION :
                return SPECIALIZATION_STRING;
            case SECONDARY :
                return SECONDARY_STRING;
            case BASE :
                return BASE_STRING;
        }
        return "Error: Invalid specialization type";
    }

}
