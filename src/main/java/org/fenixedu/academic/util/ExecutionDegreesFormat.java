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
/*
 * Created on 10/Fev/2004
 * 
 */
package org.fenixedu.academic.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;

/**
 * @author Tânia Pousão
 * 
 */
public class ExecutionDegreesFormat {

    public static List<LabelValueBean> buildLabelValueBeansForExecutionDegree(List<ExecutionDegree> executionDegrees) {

        final List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        for (final ExecutionDegree executionDegree : executionDegrees) {

            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            final String degreeName = executionDegree.getDegree().getNameFor(executionYear).getContent();
            final String degreeType = executionDegree.getDegreeCurricularPlan().getDegreeType().getName().getContent();

            String name = degreeType + " em " + degreeName;
            name +=
                    (addDegreeCurricularPlanName(executionDegree, executionDegrees)) ? " - "
                            + executionDegree.getDegreeCurricularPlan().getName() : "";

            result.add(new LabelValueBean(name, executionDegree.getExternalId().toString()));
        }

        return result;
    }

    private static boolean addDegreeCurricularPlanName(final ExecutionDegree selectedExecutionDegree,
            final List<ExecutionDegree> executionDegrees) {

        for (final ExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegree() == selectedExecutionDegree.getDegree() && executionDegree != selectedExecutionDegree) {
                return true;
            }
        }
        return false;
    }
}