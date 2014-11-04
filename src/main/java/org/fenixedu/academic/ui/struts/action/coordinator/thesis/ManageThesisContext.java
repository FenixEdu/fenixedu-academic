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
package org.fenixedu.academic.ui.struts.action.coordinator.thesis;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;

public class ManageThesisContext implements Serializable {

    private ExecutionDegree executionDegree = null;

    public ManageThesisContext(final ExecutionDegree executionDegree) {
        setExecutionDegree(executionDegree);
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public ExecutionDegree getPreviousExecutionDegree() {
        final ExecutionDegree executionDegree = getExecutionDegree();
        if (executionDegree != null) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            final ExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();
            if (previousExecutionYear != null) {
                final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                for (final ExecutionDegree otherExecutionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                    if (otherExecutionDegree.getExecutionYear() == previousExecutionYear) {
                        return otherExecutionDegree;
                    }
                }
            }
        }
        return null;
    }

    public SortedSet<ExecutionDegree> getAvailableExecutionDegrees() {
        final SortedSet<ExecutionDegree> executionDegrees =
                new TreeSet<ExecutionDegree>(ExecutionDegree.REVERSE_EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        final ExecutionDegree executionDegree = getExecutionDegree();
        if (executionDegree != null) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            executionDegrees.addAll(degreeCurricularPlan.getExecutionDegreesSet());
        }
        return executionDegrees;
    }

}
