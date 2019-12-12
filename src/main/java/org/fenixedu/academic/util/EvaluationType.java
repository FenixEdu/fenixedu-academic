/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util;

import java.io.Serializable;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class EvaluationType implements Serializable {

    public static final int EXAM = 1;

    public static final int FINAL = 2;

    @Deprecated
    public static final int ONLINE_TEST = 3;

    public static final int TEST = 4;

    public static final int PROJECT = 5;

    public static final int AD_HOC = 6;

    public static final EvaluationType EXAM_TYPE = new EvaluationType(EXAM);

    public static final EvaluationType FINAL_TYPE = new EvaluationType(FINAL);

    @Deprecated
    public static final EvaluationType ONLINE_TEST_TYPE = new EvaluationType(ONLINE_TEST);

    public static final EvaluationType TEST_TYPE = new EvaluationType(TEST);

    public static final EvaluationType PROJECT_TYPE = new EvaluationType(PROJECT);

    public static final EvaluationType AD_HOC_TYPE = new EvaluationType(AD_HOC);

    public static final String EXAM_STRING = "Exame";

    public static final String FINAL_STRING = "Final";

    @Deprecated
    public static final String ONLINE_TEST_STRING = "Ficha de Trabalho";

    public static final String TEST_STRING = "Teste";

    public static final String PROJECT_STRING = "Projecto";

    public static final String AD_HOC_STRING = "Ad Hoc";

    private Integer type;

    public EvaluationType() {
    }

    public EvaluationType(int type) {
        this.type = new Integer(type);
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof EvaluationType) {
            EvaluationType ds = (EvaluationType) obj;
            resultado = this.getType().equals(ds.getType());
        }
        return resultado;
    }

    @Override
    public String toString() {
        if (type.intValue() == EvaluationType.EXAM) {
            return EvaluationType.EXAM_STRING;
        }
        if (type.intValue() == EvaluationType.FINAL) {
            return EvaluationType.FINAL_STRING;
        }
        if (type.intValue() == EvaluationType.ONLINE_TEST) {
            return EvaluationType.ONLINE_TEST_STRING;
        }
        if (type.intValue() == EvaluationType.TEST) {
            return EvaluationType.TEST_STRING;
        }
        if (type.intValue() == EvaluationType.PROJECT) {
            return EvaluationType.PROJECT_STRING;
        }
        if (type.intValue() == EvaluationType.AD_HOC) {
            return EvaluationType.AD_HOC_STRING;
        }

        return "ERRO!";
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer integer) {
        type = integer;
    }
}