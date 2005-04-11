package net.sourceforge.fenixedu.applicationTier.Filtro.equivalence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

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

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

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
            TipoCurso degreeType = (TipoCurso) serviceArgs[1];

            IStudent student = getStudent(studentNumber, degreeType);
            if (student == null) {
                return "errors.enrollment.equivalence.no.student.with.that.number.and.degreeType";
            }

            if (degreeType.equals(TipoCurso.LICENCIATURA_OBJ)) {
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
                if (!degreeType.equals(TipoCurso.LICENCIATURA_OBJ)) {
                    return "errors.enrollment.equivalence.data.not.authorized";
                }
            } else if (roles.contains(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
                if (!degreeType.equals(TipoCurso.MESTRADO_OBJ)) {
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
    private boolean isThisACoordinatorOfThisStudentsDegree(IUserView userView, IStudent student)
            throws ExcepcaoPersistencia {
        List executionDegreesOfThisCoordinator = getExecutionDegreesOfThisCoordinator(userView
                .getUtilizador());

        List degreeCurricularPlansOfThisCoordinator = (List) CollectionUtils.collect(
                executionDegreesOfThisCoordinator, new Transformer() {
                    public Object transform(Object obj) {
                        IExecutionDegree executionDegree = (IExecutionDegree) obj;
                        return executionDegree.getDegreeCurricularPlan();
                    }
                });

        IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);

        return degreeCurricularPlansOfThisCoordinator.contains(studentCurricularPlan
                .getDegreeCurricularPlan());
    }

    /**
     * @param studentNumber
     * @param degreeType
     * @return IStudent
     * @throws ExcepcaoPersistencia
     */
    private IStudent getStudent(Integer studentNumber, TipoCurso degreeType) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent studentDAO = persistenceDAO.getIPersistentStudent();
        return studentDAO.readStudentByNumberAndDegreeType(studentNumber, degreeType);
    }

    /**
     * @param username
     * @return List
     * @throws ExcepcaoPersistencia
     */
    private List getExecutionDegreesOfThisCoordinator(String username) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher teacherDAO = persistenceDAO.getIPersistentTeacher();
        IPersistentCoordinator coordinatorDAO = persistenceDAO.getIPersistentCoordinator();

        ITeacher teacher = teacherDAO.readTeacherByUsername(username);
        return coordinatorDAO.readExecutionDegreesByTeacher(teacher);
    }

    /**
     * @param student
     * @return IStudentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    private IStudentCurricularPlan getStudentCurricularPlan(IStudent student)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistenceDAO
                .getIStudentCurricularPlanPersistente();
        return studentCurricularPlanDAO.readActiveByStudentNumberAndDegreeType(student.getNumber(),
                student.getDegreeType());
    }

    /**
     * @param student
     * @return true/false
     * @throws ExcepcaoPersistencia
     */
    private boolean isThisStudentsDegreeTheOne(IStudent student) throws ExcepcaoPersistencia {
        IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);

        return studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals(
                DEGREE_ACRONYM);
    }
}