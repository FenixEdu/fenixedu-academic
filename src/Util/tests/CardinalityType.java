/*
 * Created on 8/Apr/2004
 */

package Util.tests;

import Util.FenixUtil;

/**
 * @author Susana Fernandes
 */

public class CardinalityType extends FenixUtil
{

    public static final int SINGLE = 1;

    public static final int MULTIPLE = 2;

    //public static final int ORDERED = 3;

    public static final String SINGLE_STRING = "Single";

    public static final String MULTIPLE_STRING = "Multiple";

    //public static final String ORDERED_STRING = "Ordered";

    private Integer type;

    private String typeString;

    public CardinalityType(int type)
    {
        this.type = new Integer(type);
        setTypeString();
    }

    public CardinalityType(Integer type)
    {
        this.type = type;
        setTypeString();
    }

    public CardinalityType(String type)
    {
        this.type = getTypeCode(type);
        setTypeString();
    }

    public Integer getType()
    {
        return type;
    }

    public String getTypeString()
    {
        return typeString;
    }

    public void setType(int type)
    {
        this.type = new Integer(type);
        setTypeString();
    }

    public void setType(Integer type)
    {
        this.type = type;
        setTypeString();
    }

    public void setType(String type)
    {
        this.type = getTypeCode(type);
        this.typeString = type;
    }

    public Integer getTypeCode(String cardinalityName)
    {
        if (cardinalityName.equals(SINGLE_STRING))
            return new Integer(SINGLE);
        else if (cardinalityName.equals(MULTIPLE_STRING))
            return new Integer(MULTIPLE);
        return null;
    }

    public void setTypeString()
    {
        if (type.intValue() == SINGLE)
            typeString = SINGLE_STRING;
        else if (type.intValue() == MULTIPLE)
            typeString = MULTIPLE_STRING;
    }
}