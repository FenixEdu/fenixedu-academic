/*
 * Created on 8/Apr/2004
 */

package Util.tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import Util.FenixUtil;

/**
 * @author Susana Fernandes
 */

public class QuestionType extends FenixUtil {

    public static final int LID = 1; //Logical identifier(multiple choise)

    public static final int STR = 2; //String

    public static final int NUM = 3; //Numeric

    public static final String LID_ID_STRING = "response_lid";

    public static final String STR_ID_STRING = "response_str";

    public static final String NUM_ID_STRING = "response_num";

    public static final String LID_STRING = "Identificadores Lógicos";

    public static final String STR_STRING = "Perguntas de Texto";

    public static final String NUM_STRING = "Perguntas Numéricas";

    //if questionType == 2 tipo de resposa = String
    //if questionType == 3 tipo de resposa = Integer | Decimal
    //  private Integer rows = new Integer(1);

    //  private Integer columns = new Integer(1);

    //private Integer maxchars = new Integer(1);

    //  prompt (Box | Dashline | Asterisk | Underline ) #IMPLIED

    private Integer type;

    private CardinalityType cardinalityType;

    private Render render;

    public QuestionType(int type) {
        this.type = new Integer(type);
    }

    public QuestionType(Integer type) {
        this.type = type;
    }

    public QuestionType(String type) {
        this.type = getTypeCode(type);
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = new Integer(type);
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = getTypeCode(type);
    }

    public CardinalityType getCardinalityType() {
        return cardinalityType;
    }

    public void setCardinalityType(CardinalityType cardinalityType) {
        this.cardinalityType = cardinalityType;
    }

    public Render getRender() {
        return render;
    }

    public void setRender(Render render) {
        this.render = render;
    }

    public Integer getTypeCode(String typeName) {
        if (typeName.equals(LID_ID_STRING))
            return new Integer(LID);
        else if (typeName.equals(STR_ID_STRING))
            return new Integer(STR);
        else if (typeName.equals(NUM_ID_STRING)) return new Integer(NUM);
        return null;
    }

    public String getTypeString() {
        if (this.type.intValue() == LID)
            return LID_ID_STRING;
        else if (this.type.intValue() == STR)
            return STR_ID_STRING;
        else if (this.type.intValue() == NUM) return NUM_ID_STRING;
        return null;
    }

    public static List getAllTypes() {
        List result = new ArrayList();
        result.add(new LabelValueBean(LID_STRING, new Integer(LID).toString()));
        result.add(new LabelValueBean(STR_STRING, new Integer(STR).toString()));
        result.add(new LabelValueBean(NUM_STRING, new Integer(NUM).toString()));
        return result;
    }

    public String toXML(String inside, int code) {
        return new String("<" + getTypeString() + " ident=\"" + code + "\" "
                + cardinalityType.toXML() + ">\n" + render.toXML(inside)
                + "</" + getTypeString() + ">\n");
    }
}