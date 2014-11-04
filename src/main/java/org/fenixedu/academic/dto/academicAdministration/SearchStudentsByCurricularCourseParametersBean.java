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
package net.sourceforge.fenixedu.dataTransferObject.academicAdministration;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class SearchStudentsByCurricularCourseParametersBean implements Serializable {

    private ExecutionYear executionYear;

    private DegreeCurricularPlan degreeCurricularPlan;

    private CurricularCourse curricularCourse;

    private Set<Degree> administratedDegrees;

    public SearchStudentsByCurricularCourseParametersBean() {
    }

    public SearchStudentsByCurricularCourseParametersBean(Set<Degree> administratedDegrees) {
        this.administratedDegrees = administratedDegrees;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public SortedSet<Degree> getAdministratedDegrees() {
        final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        result.addAll(administratedDegrees);
        return result;
    }

    public SortedSet<DegreeCurricularPlan> getAvailableDegreeCurricularPlans() {
        final SortedSet<DegreeCurricularPlan> result =
                new TreeSet<DegreeCurricularPlan>(
                        DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);
        if (executionYear != null) {
            final Set<Degree> degrees = administratedDegrees;
            for (DegreeCurricularPlan plan : executionYear.getDegreeCurricularPlans()) {
                if (degrees.contains(plan.getDegree())) {
                    result.add(plan);
                }
            }
        }
        return result;
    }
}
