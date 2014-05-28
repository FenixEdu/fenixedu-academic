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
 * Created on Dec 21, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ExecutionDegreeCoordinatorOrScientificCouncilmemberAuthorizationFilter {

    public static final ExecutionDegreeCoordinatorOrScientificCouncilmemberAuthorizationFilter instance =
            new ExecutionDegreeCoordinatorOrScientificCouncilmemberAuthorizationFilter();

    public void execute(ExecutionDegree executionDegree) throws NotAuthorizedException {
        final User userView = Authenticate.getUser();
        if (executionDegree == null) {
            throw new NotAuthorizedException();
        }

        if (userView == null || userView.getPerson().getPersonRolesSet() == null || !verifyCondition(userView, executionDegree)) {
            throw new NotAuthorizedException();
        }

        if (((userView != null && userView.getPerson().getPersonRolesSet() != null && !verifyCondition(userView, executionDegree)))
                || (userView == null) || (userView.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException();
        }

    }

    public static boolean verifyCondition(User id, ExecutionDegree executionDegree) {
        if (id != null) {
            final Person person = id.getPerson();
            if (person != null) {
                if (person.hasRole(RoleType.COORDINATOR)) {
                    for (final Coordinator coordinator : person.getCoordinators()) {
                        if (executionDegree == coordinator.getExecutionDegree()) {
                            return true;
                        }
                    }
                    for (final ScientificCommission scientificCommission : person.getScientificCommissionsSet()) {
                        if (executionDegree == scientificCommission.getExecutionDegree()
                                || (executionDegree.getDegreeCurricularPlan() == scientificCommission.getExecutionDegree()
                                        .getDegreeCurricularPlan() && executionDegree.getExecutionYear() == scientificCommission
                                        .getExecutionDegree().getExecutionYear().getPreviousExecutionYear())) {
                            return true;
                        }
                    }
                }
                if (person.getEmployee() != null && person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
                    final Employee employee = person.getEmployee();
                    final Department department = employee.getCurrentDepartmentWorkingPlace();
                    final Set<CompetenceCourse> competenceCourses = department.getCompetenceCoursesSet();
                    return hasDissertationCompetenceCourseForDepartment(executionDegree, competenceCourses)
                            || hasDissertationCompetenceCourseForDepartment(executionDegree, department.getDepartmentUnit());
                }
            }
        }

        return false;
    }

    protected static boolean hasDissertationCompetenceCourseForDepartment(final ExecutionDegree executionDegree,
            final Set<CompetenceCourse> competenceCourses) {
        for (final CompetenceCourse competenceCourse : competenceCourses) {
            for (final CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCoursesSet()) {
                if (curricularCourse.getType() == CurricularCourseType.TFC_COURSE || competenceCourse.isDissertation()) {
                    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                    if (degreeCurricularPlan.getExecutionDegreesSet().contains(executionDegree)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean hasDissertationCompetenceCourseForDepartment(final ExecutionDegree executionDegree, final Unit unit) {
        if (unit.isCompetenceCourseGroupUnit()) {
            final CompetenceCourseGroupUnit competenceCourseGroupUnit = (CompetenceCourseGroupUnit) unit;
            if (hasDissertationCompetenceCourseForDepartment(executionDegree, competenceCourseGroupUnit.getCompetenceCoursesSet())) {
                return true;
            }
        }
        for (final Accountability accountability : unit.getChildsSet()) {
            final Party party = accountability.getChildParty();
            if (party.isUnit() && hasDissertationCompetenceCourseForDepartment(executionDegree, (Unit) party)) {
                return true;
            }
        }
        return false;
    }

}