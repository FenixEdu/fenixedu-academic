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

public class CardinalityType extends FenixUtil {

    public static final int SINGLE = 1;

    public static final int MULTIPLE = 2;

    //public static final int ORDERED = 3;

    public static final String SINGLE_ID_STRING = "Single";

    public static final String MULTIPLE_ID_STRING = "Multiple";

    //public static final String ORDERED_STRING = "Ordered";

    public static final String SINGLE_STRING = "Resposta Simples";

    public static final String MULTIPLE_STRING = "Resposta Múltipla";

    private Integer type;

    private String typeString;

    public CardinalityType(int type) {
        this.type = new Integer(type);
        setTypeString();
    }

    public CardinalityType(Integer type) {
        this.type = type;
        setTypeString();
    }

    public CardinalityType(String type) {
        this.type = getTypeCode(type);
        setTypeString();
    }

    public Integer getType() {
        return type;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setType(int type) {
        this.type = new Integer(type);
        setTypeString();
    }

    public void setType(Integer type) {
        this.type = type;
        setTypeString();
    }

    public void setType(String type) {
        this.type = getTypeCode(type);
        this.typeString = type;
    }

    public Integer getTypeCode(String cardinalityName) {
        if (cardinalityName.equals(SINGLE_ID_STRING))
            return new Integer(SINGLE);
        else if (cardinalityName.equals(MULTIPLE_ID_STRING))
            return new Integer(MULTIPLE);
        return null;
    }

    public void setTypeString() {
        if (type.intValue() == SINGLE)
            typeString = SINGLE_ID_STRING;
        else if (type.intValue() == MULTIPLE)
            typeString = MULTIPLE_ID_STRING;
    }

    public static List getAllTypes() {
        List result = new ArrayList();
        result.add(new LabelValueBean(SINGLE_STRING, new Integer(SINGLE).toString()));
        result.add(new LabelValueBean(MULTIPLE_STRING, new Integer(MULTIPLE).toString()));
        return result;
    }

    public String toXML() {
        return new String("rcardinality=\"" + typeString + "\"");
    }
}