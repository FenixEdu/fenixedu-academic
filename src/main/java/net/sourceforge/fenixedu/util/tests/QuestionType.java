/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 8/Apr/2004
 */

package net.sourceforge.fenixedu.util.tests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */

public class QuestionType extends FenixUtil {

    public static final int LID = 1; // Logical identifier(multiple choise)

    public static final int STR = 2; // String

    public static final int NUM = 3; // Numeric

    public static final String LID_ID_STRING = "response_lid";

    public static final String STR_ID_STRING = "response_str";

    public static final String NUM_ID_STRING = "response_num";

    public static final String LID_STRING = "Identificadores Lógicos";

    public static final String STR_STRING = "Perguntas de Texto";

    public static final String NUM_STRING = "Perguntas Numéricas";

    // if questionType == 2 tipo de resposa = String
    // if questionType == 3 tipo de resposa = Integer | Decimal
    // private Integer rows = new Integer(1);

    // private Integer columns = new Integer(1);

    // private Integer maxchars = new Integer(1);

    // prompt (Box | Dashline | Asterisk | Underline ) #IMPLIED

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
        if (typeName.equals(LID_ID_STRING)) {
            return new Integer(LID);
        } else if (typeName.equals(STR_ID_STRING)) {
            return new Integer(STR);
        } else if (typeName.equals(NUM_ID_STRING)) {
            return new Integer(NUM);
        }
        return null;
    }

    public String getTypeString() {
        if (this.type.intValue() == LID) {
            return LID_ID_STRING;
        } else if (this.type.intValue() == STR) {
            return STR_ID_STRING;
        } else if (this.type.intValue() == NUM) {
            return NUM_ID_STRING;
        }
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
        return "<" + getTypeString() + " ident=\"" + code + "\" " + cardinalityType.toXML() + ">\n" + render.toXML(inside) + "</"
                + getTypeString() + ">\n";
    }
}