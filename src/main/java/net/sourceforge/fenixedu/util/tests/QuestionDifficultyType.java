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
 * Created on 19/Ago/2003
 */
package net.sourceforge.fenixedu.util.tests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */
public class QuestionDifficultyType extends FenixUtil {
    public static final int NOT_DEFINED = 0;

    public static final int VERY_EASY = 1;

    public static final int EASY = 2;

    public static final int MEDIUM = 3;

    public static final int DIFFICULT = 4;

    public static final int VERY_DIFFICULT = 5;

    public static final String NOT_DEFINED_STRING = "Não definido";

    public static final String VERY_EASY_STRING = "very easy";

    public static final String EASY_STRING = "easy";

    public static final String MEDIUM_STRING = "medium";

    public static final String DIFFICULT_STRING = "difficult";

    public static final String VERY_DIFFICULT_STRING = "very difficult";

    private Integer type;

    public QuestionDifficultyType() {
    }

    public QuestionDifficultyType(int type) {
        this.type = new Integer(type);
    }

    public QuestionDifficultyType(Integer type) {
        this.type = type;
    }

    public QuestionDifficultyType(String typeString) {
        this.type = getTypeCode(typeString);
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

    public List getAllTypes() {
        List result = new ArrayList();
        result.add(new LabelValueBean(NOT_DEFINED_STRING, new Integer(NOT_DEFINED).toString()));
        result.add(new LabelValueBean(VERY_EASY_STRING, new Integer(VERY_EASY).toString()));
        result.add(new LabelValueBean(EASY_STRING, new Integer(EASY).toString()));
        result.add(new LabelValueBean(MEDIUM_STRING, new Integer(MEDIUM).toString()));
        result.add(new LabelValueBean(DIFFICULT_STRING, new Integer(DIFFICULT).toString()));
        result.add(new LabelValueBean(VERY_DIFFICULT_STRING, new Integer(VERY_DIFFICULT).toString()));
        return result;
    }

    public List getAllTypesStrings() {
        List result = new ArrayList();
        result.add(NOT_DEFINED_STRING);
        result.add(VERY_EASY_STRING);
        result.add(EASY_STRING);
        result.add(MEDIUM_STRING);
        result.add(DIFFICULT_STRING);
        result.add(VERY_DIFFICULT_STRING);
        return result;
    }

    public Integer getTypeCode(String typeName) {
        if (typeName.equals(NOT_DEFINED_STRING)) {
            return new Integer(NOT_DEFINED);
        } else if (typeName.equals(VERY_EASY_STRING)) {
            return new Integer(VERY_EASY);
        } else if (typeName.equals(EASY_STRING)) {
            return new Integer(EASY);
        } else if (typeName.equals(MEDIUM_STRING)) {
            return new Integer(MEDIUM);
        } else if (typeName.equals(DIFFICULT_STRING)) {
            return new Integer(DIFFICULT);
        } else if (typeName.equals(VERY_DIFFICULT_STRING)) {
            return new Integer(VERY_DIFFICULT);
        }
        return null;
    }

    public String getTypeString() {
        if (type.intValue() == NOT_DEFINED) {
            return new String(NOT_DEFINED_STRING);
        } else if (type.intValue() == VERY_EASY) {
            return new String(VERY_EASY_STRING);
        } else if (type.intValue() == EASY) {
            return new String(EASY_STRING);
        } else if (type.intValue() == MEDIUM) {
            return new String(MEDIUM_STRING);
        } else if (type.intValue() == DIFFICULT) {
            return new String(DIFFICULT_STRING);
        } else if (type.intValue() == VERY_DIFFICULT) {
            return new String(VERY_DIFFICULT_STRING);
        }

        return null;
    }

    public static boolean isQuestionDifficultyType(int type) {
        if (type >= NOT_DEFINED && type <= VERY_DIFFICULT) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof TestType) {
            TestType tt = (TestType) obj;
            resultado = this.getType().equals(tt.getType());
        }
        return resultado;
    }

}