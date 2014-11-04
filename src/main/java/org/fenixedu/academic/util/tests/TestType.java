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
public class TestType extends FenixUtil {
    public static final int EVALUATION = 1;

    public static final int STUDY = 2;

    public static final int INQUIRY = 3;

    public static final String EVALUATION_STRING = "Ficha de Avaliação (guardar primeira resposta)";

    public static final String STUDY_STRING = "Ficha de Estudo (guardar última resposta)";

    public static final String INQUIRY_STRING = "Inquérito";

    private final Integer type;

    public TestType(int type) {
        this.type = new Integer(type);
    }

    public TestType(Integer type) {
        this.type = type;
    }

    public TestType(String typeString) {
        this.type = getTypeCode(typeString);
    }

    public Integer getType() {
        return type;
    }

    public static List<LabelValueBean> getAllTypes() {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        result.add(new LabelValueBean(EVALUATION_STRING, new Integer(EVALUATION).toString()));
        result.add(new LabelValueBean(STUDY_STRING, new Integer(STUDY).toString()));
        result.add(new LabelValueBean(INQUIRY_STRING, new Integer(INQUIRY).toString()));
        return result;
    }

    public Integer getTypeCode(String typeName) {
        if (typeName.equals(EVALUATION_STRING)) {
            return new Integer(EVALUATION);
        } else if (typeName.equals(STUDY_STRING)) {
            return new Integer(STUDY);
        } else if (typeName.equals(INQUIRY_STRING)) {
            return new Integer(INQUIRY);
        }
        return null;
    }

    public String getTypeString() {
        if (type.intValue() == EVALUATION) {
            return new String(EVALUATION_STRING);
        } else if (type.intValue() == STUDY) {
            return new String(STUDY_STRING);
        } else if (type.intValue() == INQUIRY) {
            return new String(INQUIRY_STRING);
        }

        return null;
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