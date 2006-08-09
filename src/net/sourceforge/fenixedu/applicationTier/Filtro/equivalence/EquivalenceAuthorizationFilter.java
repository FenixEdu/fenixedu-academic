package net.sourceforge.fenixedu.applicationTier.Filtro.equivalence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author David Santos in May 19, 2004
 */

public class EquivalenceAuthorizationFilter extends Filtro {
    private static String DEGREE_ACRONYM = "LEEC";

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = (IUserView) request.getRequester();
        String messageException = hasPrevilege(id, request.getServiceParameters().parametersArray());

        if (messageException != null) {
            throw new NotAuthorizedException(messageException);
        }
    }

    private List getRoleList(Collection roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

    protected Collection getNeededRoles() {
        List roles = new ArrayList();
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.COORDINATOR)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)));
        return roles;
    }

    private String hasPrevilege(IUserView userView, Object[] serviceArgs) {
        try {
            if ((userView == null) || (userView.getRoles() == null)) {
                return "errors.enrollment.equivalence.operation.not.authorized";
            }

            if (!containsAtLeastOneOfTheRoles(userView)) {
                return "errors.enrollment.equivalence.operation.not.authorized";
            }

            List roles = getRoleList(userView.getRoles());

            Integer studentNumber = (Integer) serviceArgs[0];
            DegreeType degreeType = (DegreeType) serviceArgs[1];

            Student student = getStudent(studentNumber, degreeType);
            if (student == null) {
                return "errors.enrollment.equivalence.no.student.with.that.number.and.degreeType";
            }

            if (degreeType.equals(DegreeType.DEGREE)) {
                if (!isThisStudentsDegreeTheOne(student)) {
                    return "errors.enrollment.equivalence.data.not.authorized";
                }
            }

            if ((roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE) || roles
                    .contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))
                    && (roles.contains(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE))) {
                if (!degreeType.equals(student.getDegreeType())) {
                    return "errors.enrollment.equivalence.data.not.authorized";
                }
            } else if (roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                    || roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
                if (!degreeType.equals(DegreeType.DEGREE)) {
                    return "errors.enrollment.equivalence.data.not.authorized";
                }
            } else if (roles.contains(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
                if (!degreeType.equals(DegreeType.MASTER_DEGREE)) {
                    return "errors.enrollment.equivalence.data.not.authorized";
                }
            } else if (roles.contains(RoleType.COORDINATOR)) {
                if (!isThisACoordinatorOfThisStudentsDegree(userView, student)) {
                    return "errors.enrollment.equivalence.data.not.authorized";
                }
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            return "errors.enrollment.equivalence.operation.not.authorized";
        }

        return null;
    }

    /**
     * @param userView
     * @param userRoles
     * @return true/false
     */
    private boolean containsAtLeastOneOfTheRoles(IUserView userView) {
        List neededRoles = getRoleList(getNeededRoles());
        List userRoles = getRoleList(userView.getRoles());

        for (int i = 0; i < neededRoles.size(); i++) {
            if (userRoles.contains(neededRoles.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param userView
     * @param student
     * @return true/false
     */
    private boolean isThisACoordinatorOfThisStudentsDegree(IUserView userView, Student student)
            throws ExcepcaoPersistencia {
        List executionDegreesOfThisCoordinator = getExecutionDegreesOfThisCoordinator(userView
                .getUtilizador());

        List degreeCurricularPlansOfThisCoordinator = (List) CollectionUtils.collect(
                executionDegreesOfThisCoordinator, new Transformer() {
                    public Object transform(Object obj) {
                        ExecutionDegree executionDegree = (ExecutionDegree) obj;
                        return executionDegree.getDegreeCurricularPlan();
                    }
                });

        StudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();

        return degreeCurricularPlansOfThisCoordinator.contains(studentCurricularPlan
                .getDegreeCurricularPlan());
    }

    /**
     * @param studentNumber
     * @param degreeType
     * @return IStudent
     * @throws ExcepcaoPersistencia
     */
    private Student getStudent(Integer studentNumber, DegreeType degreeType) throws ExcepcaoPersistencia {
        return Student.readStudentByNumberAndDegreeType(studentNumber, degreeType);
    }

    /**
     * @param username
     * @return List
     * @throws ExcepcaoPersistencia
     */
    private List getExecutionDegreesOfThisCoordinator(String username) throws ExcepcaoPersistencia {
        Teacher teacher = Teacher.readTeacherByUsername(username);
        return teacher.getCoordinatedExecutionDegrees();
    }

 
    /**
     * @param student
     * @return true/false
     * @throws ExcepcaoPersistencia
     */
    private boolean isThisStudentsDegreeTheOne(Student student) throws ExcepcaoPersistencia {
        StudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();
        return studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals(
                DEGREE_ACRONYM);
    }
}