/*
 * Created on Dec 21, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ExecutionDegreeCoordinatorAuthorizationFilter extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        final IUserView userView = getRemoteUser(request);
        final Object[] arguments = getServiceCallArguments(request);
        if (arguments == null || arguments.length < 1 || arguments[0] == null) {
            throw new NotAuthorizedFilterException();
        }
        final Integer idInternal = (arguments[0] instanceof Integer) ? (Integer) arguments[0] : ((InfoObject)arguments[0]).getIdInternal();

        if (userView == null || userView.getRoleTypes() == null || !verifyCondition(userView, idInternal)) {
            throw new NotAuthorizedFilterException();
        }

        if (((userView != null && userView.getRoleTypes() != null && !verifyCondition(userView, idInternal)))
                || (userView == null) || (userView.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }

    }

    public static boolean verifyCondition(IUserView id, Integer objectId) throws ExcepcaoPersistencia {
    	if (id != null) {
        final Person person = id.getPerson();
        if (person != null) {
            final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(objectId);
            if (person.hasRole(RoleType.COORDINATOR)) {
                for (final Coordinator coordinator : person.getCoordinators()) {
                    if (executionDegree.getCoordinatorsListSet().contains(coordinator)) {
                        return true;
                    }
                }
            }
            if (person.getEmployee() != null && person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
                final Employee employee = person.getEmployee();
                final Department department = employee.getCurrentDepartmentWorkingPlace();
                for (final CompetenceCourse competenceCourse : department.getCompetenceCoursesSet()) {
                    for (final CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCoursesSet()) {
                        if (curricularCourse.getType() == CurricularCourseType.TFC_COURSE) {
                            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                            if (degreeCurricularPlan.getExecutionDegreesSet().contains(executionDegree)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
    	}

        return false;
    }

}