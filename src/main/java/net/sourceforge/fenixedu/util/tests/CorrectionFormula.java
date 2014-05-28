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
public class CorrectionFormula extends FenixUtil {

    public static final int FENIX = 1;

    public static final int IMS = 2;

    public static final String FENIX_STRING = "Utilizar fórmula do sistema.";

    public static final String LID_MULTIPLE_FORMULA =
            "<br/>cotação * 2*(num de respostas certas escolhidas + num de respostas erradas escolhidas)/num de opções -1";

    public static final String LID_SIMPLE_FORMULA = "<br/>cotação * 1 / (num de opções -1)";

    public static final String IMS_STRING = "Utilizar cotações indicadas no ficheiro.";

    private final Integer formula;

    public CorrectionFormula(int formula) {
        this.formula = new Integer(formula);
    }

    public CorrectionFormula(Integer formula) {
        this.formula = formula;
    }

    public Integer getFormula() {
        return formula;
    }

    public static List<LabelValueBean> getFormulas() {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        result.add(new LabelValueBean(IMS_STRING, new Integer(IMS).toString()));
        // + LID_SIMPLE_FORMULA + LID_MULTIPLE_FORMULA,
        result.add(new LabelValueBean(FENIX_STRING, new Integer(FENIX).toString()));
        return result;
    }
}