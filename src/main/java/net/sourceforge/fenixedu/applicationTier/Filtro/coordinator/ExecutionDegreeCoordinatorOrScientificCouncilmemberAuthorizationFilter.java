/*
 * Created on Dec 21, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
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
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ExecutionDegreeCoordinatorOrScientificCouncilmemberAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request) throws Exception {
        final IUserView userView = AccessControl.getUserView();
        final Object[] arguments = request.getServiceParameters().parametersArray();
        if (arguments == null || arguments.length < 1 || arguments[0] == null) {
            throw new NotAuthorizedFilterException();
        }
        final ExecutionDegree executionDegree = (ExecutionDegree) arguments[0];

        if (userView == null || userView.getRoleTypes() == null || !verifyCondition(userView, executionDegree)) {
            throw new NotAuthorizedFilterException();
        }

        if (((userView != null && userView.getRoleTypes() != null && !verifyCondition(userView, executionDegree)))
                || (userView == null) || (userView.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }

    }

    public static boolean verifyCondition(IUserView id, ExecutionDegree executionDegree) {
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