/*
 * Created on 8/Apr/2004
 */

package Util.tests;

import Util.FenixUtil;

/**
 * @author Susana Fernandes
 */

public class QuestionType extends FenixUtil
{
    public static final int LID = 1; //Logical identifier(multiple choise)

    public static final int STR = 2; //String

    public static final int NUM = 3; //Numeric

    public static final String LID_STRING = "response_lid";

    public static final String STR_STRING = "response_str";

    public static final String NUM_STRING = "response_num";

    //if questionType == 2 tipo de resposa = String
    //if questionType == 3 tipo de resposa = Integer | Decimal
    //  private Integer rows = new Integer(1);

    //  private Integer columns = new Integer(1);

    //private Integer maxchars = new Integer(1);

    //  prompt (Box | Dashline | Asterisk | Underline ) #IMPLIED

    private Integer type;

    public QuestionType(int type) {
        this.type = new Integer(type);
    }

    public QuestionType(Integer type) {
        this.type = type;
    }

    public QuestionType(String type) {
        this.type = getTypeCode(type);
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = new Integer(type);
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public void setType(String type)
    {
        this.type = getTypeCode(type);
    }

    public Integer getTypeCode(String typeName)
    {
        if (typeName.equals(LID_STRING))
            return new Integer(LID);
        else
            if (typeName.equals(STR_STRING))
                return new Integer(STR);
            else
                if (typeName.equals(NUM_STRING))
                    return new Integer(NUM);
        return null;
    }

}