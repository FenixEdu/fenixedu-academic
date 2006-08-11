package net.sourceforge.fenixedu.applicationTier.Filtro.equivalence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
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

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.COORDINATOR);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        return roles;
    }

    private String hasPrevilege(IUserView userView, Object[] serviceArgs) {
        if ((userView == null) || (userView.getRoleTypes() == null)) {
            return "errors.enrollment.equivalence.operation.not.authorized";
        }

        if (!containsRoleType(userView.getRoleTypes())) {
            return "errors.enrollment.equivalence.operation.not.authorized";
        }

        Integer studentNumber = (Integer) serviceArgs[0];
        DegreeType degreeType = (DegreeType) serviceArgs[1];

        Registration student = getStudent(studentNumber, degreeType);
        if (student == null) {
            return "errors.enrollment.equivalence.no.student.with.that.number.and.degreeType";
        }

        if (degreeType.equals(DegreeType.DEGREE)) {
            if (!isThisStudentsDegreeTheOne(student)) {
                return "errors.enrollment.equivalence.data.not.authorized";
            }
        }

        if ((userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE) || userView
                .hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))
                && userView.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            if (!degreeType.equals(student.getDegreeType())) {
                return "errors.enrollment.equivalence.data.not.authorized";
            }
        } else if (userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                || userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
            if (!degreeType.equals(DegreeType.DEGREE)) {
                return "errors.enrollment.equivalence.data.not.authorized";
            }
        } else if (userView.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            if (!degreeType.equals(DegreeType.MASTER_DEGREE)) {
                return "errors.enrollment.equivalence.data.not.authorized";
            }
        } else if (userView.hasRoleType(RoleType.COORDINATOR)) {
            if (!isThisACoordinatorOfThisStudentsDegree(userView, student)) {
                return "errors.enrollment.equivalence.data.not.authorized";
            }
        }

        return null;
    }

    /**
     * @param userView
     * @param student
     * @return true/false
     */
    private boolean isThisACoordinatorOfThisStudentsDegree(IUserView userView, Registration student) {
        List executionDegreesOfThisCoordinator = getExecutionDegreesOfThisCoordinator(userView);

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
    private Registration getStudent(Integer studentNumber, DegreeType degreeType) {
        return Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
    }

    /**
     * @param username
     * @return List
     * @throws ExcepcaoPersistencia
     */
    private List getExecutionDegreesOfThisCoordinator(IUserView userView) {
        final Person person = userView.getPerson();
        final Teacher teacher = person != null ? person.getTeacher() : null;
        return teacher.getCoordinatedExecutionDegrees();
    }

    /**
     * @param student
     * @return true/false
     * @throws ExcepcaoPersistencia
     */
    private boolean isThisStudentsDegreeTheOne(Registration student) {
        StudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();
        return studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals(
                DEGREE_ACRONYM);
    }
}