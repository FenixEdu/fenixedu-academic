/*
 * Created on Jul 5, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * 
 */
public class CoordinatorEnrolmentAuthorizationFilter extends AuthorizationByManyRolesFilter {

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByManyRolesFilter#hasPrevilege(ServidorAplicacao.IUserView,
     *      java.lang.Object[])
     */
    protected String hasPrevilege(IUserView id, Object[] arguments) {
        if (id.hasRoleType(RoleType.COORDINATOR) && arguments[0] != null) {
            Teacher teacher = readTeacher(id);
            if (teacher == null) {
                return "noAuthorization";
            }

            if (!verifyCoordinator(teacher, arguments)) {
                return "noAuthorization";
            }
        } else {
            return "noAuthorization";
        }
        return null;
    }

    protected StudentCurricularPlan readStudentCurricularPlan(Object[] arguments) {
        StudentCurricularPlan studentCurricularPlan = null;
        if (arguments[1] != null) {

            studentCurricularPlan = rootDomainObject
                    .readStudentCurricularPlanByOID((Integer) arguments[1]);
        } else {
            Registration registration = Registration.readStudentByNumberAndDegreeType((Integer) arguments[2],
                    DegreeType.DEGREE);
            if (registration != null) {
                studentCurricularPlan = registration.getActiveOrConcludedStudentCurricularPlan();
            }
        }
        return studentCurricularPlan;
    }

    protected Registration readStudent(IUserView id) throws ExcepcaoPersistencia {
        return Registration.readByUsername(id.getUtilizador());
    }

    protected Registration readStudent(Object[] arguments) throws ExcepcaoPersistencia {
        StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments);
        if (studentCurricularPlan == null) {
            return null;
        }

        return studentCurricularPlan.getRegistration();
    }

    protected Teacher readTeacher(IUserView id) {
        return id.getPerson().getTeacher();
    }

    protected boolean verifyCoordinator(Teacher teacher, Object[] arguments) {

        ExecutionDegree executionDegree = rootDomainObject
                .readExecutionDegreeByOID((Integer) arguments[0]);
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(teacher);

        if (coordinator == null) {
            return false;
        }

        StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments);
        if (studentCurricularPlan == null) {
            return false;
        }
        return studentCurricularPlan.getDegreeCurricularPlan().equals(
                coordinator.getExecutionDegree().getDegreeCurricularPlan());

    }
}
