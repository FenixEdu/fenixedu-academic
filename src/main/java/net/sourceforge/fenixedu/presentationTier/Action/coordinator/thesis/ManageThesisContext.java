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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;

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

    public Scheduleing getScheduleing() {
        final ExecutionDegree executionDegree = getPreviousExecutionDegree();
        return executionDegree == null ? null : executionDegree.getScheduling();
    }

}
