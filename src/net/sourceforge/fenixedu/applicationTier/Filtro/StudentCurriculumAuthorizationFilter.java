package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 * @author Fernanda Quitério 12/Fev/2004
 */
public class StudentCurriculumAuthorizationFilter extends Filtro {

    public StudentCurriculumAuthorizationFilter() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = (IUserView) request.getRequester();
        String messageException = hasProvilege(id, request.getServiceParameters().parametersArray());
        if ((id == null) || (id.getRoles() == null) || (!containsRole(id.getRoles()))
                || (messageException != null)) {
            throw new NotAuthorizedFilterException(messageException);
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection getNeededRoles() {
        List roles = new ArrayList();
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.COORDINATOR)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.TEACHER)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.STUDENT)));
        return roles;
    }

    private List getRoleList(Collection roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

    /**
     * @param id
     * @param argumentos
     * @return null if authorized string with message if not authorized
     */
    private String hasProvilege(IUserView id, Object[] arguments) {
        List roles = getRoleList(id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        Integer studentCurricularPlanID = (Integer) arguments[1];

        StudentCurricularPlan studentCurricularPlan = null;

        // Read The DegreeCurricularPlan
        try {
        	studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);

        } catch (Exception e) {
            return "noAuthorization";
        }

        if (studentCurricularPlan == null || studentCurricularPlan.getStudent() == null) {
            return "noAuthorization";
        }

        try {
            Student student = studentCurricularPlan.getStudent();

            Group group = student.findFinalDegreeWorkGroupForCurrentExecutionYear();
            if (group != null) {
                ExecutionDegree executionDegree = group.getExecutionDegree();
                for (int i = 0; i < executionDegree.getCoordinatorsList().size(); i++) {
                    Coordinator coordinator = executionDegree.getCoordinatorsList().get(i);
                    if (coordinator.getTeacher().getPerson().getUsername().equals(id.getUtilizador())) {
                        // The student is a candidate for a final degree work of
                        // the degree of the
                        // coordinator making the request. Allow access.
                        return null;
                    }
                }

                for (int i = 0; i < group.getGroupProposals().size(); i++) {
                    GroupProposal groupProposal = group.getGroupProposals().get(i);
                    Proposal proposal = groupProposal.getFinalDegreeWorkProposal();
                    Teacher teacher = proposal.getOrientator();

                    if (teacher.getPerson().getUsername().equals(id.getUtilizador())) {
                        // The student is a candidate for a final degree work of
                        // oriented by the
                        // teacher making the request. Allow access.
                        return null;
                    }

                    teacher = proposal.getCoorientator();
                    if (teacher != null && teacher.getPerson().getUsername().equals(id.getUtilizador())) {
                        // The student is a candidate for a final degree work of
                        // cooriented by the
                        // teacher making the request. Allow access.
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            // check other possible authorizations
        }

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            if (!studentCurricularPlan.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                    DegreeType.MASTER_DEGREE)
                    && !(roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE) || roles
                            .contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))) {
                return "noAuthorization";
            }
            if (!(roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE) || roles
                    .contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))) {
                return null;
            }
        }

        if (arguments[0] != null) {
            roleTemp = new ArrayList();
            roleTemp.add(RoleType.COORDINATOR);
            if (CollectionUtils.containsAny(roles, roleTemp)) {
                try {
                    ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID((Integer) arguments[0]);

                    if (executionDegree == null) {
                        return "noAuthorization";
                    }
                    
                    List<Coordinator> coordinatorsList = executionDegree.getCoordinatorsList();
                    if (coordinatorsList == null) {
                        return "noAuthorization";
                    }

                    final String username = id.getUtilizador();
                    Coordinator coordinator = (Coordinator) CollectionUtils.find(coordinatorsList,
                            new Predicate() {

                                public boolean evaluate(Object input) {
                                    Coordinator coordinatorTemp = (Coordinator) input;
                                    if (username.equals(coordinatorTemp.getTeacher().getPerson()
                                            .getUsername())) {
                                        return true;
                                    }
                                    return false;
                                }
                            });
                    if (coordinator == null) {
                        return "noAuthorization";
                    }

                    if (!coordinator.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                            .getIdInternal().equals(
                                    studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                                            .getIdInternal())) {
                        return "noAuthorization";
                    }
                    /*
                     * IStudentCurricularPlanPersistente
                     * persistentStudentCurricularPlan = sp
                     * .getIStudentCurricularPlanPersistente(); List
                     * activeStudentCurricularPlans =
                     * persistentStudentCurricularPlan
                     * .readAllActiveStudentCurricularPlan(studentCurricularPlan
                     * .getStudent().getNumber()); boolean
                     * hasAnActiveCurricularPlanThatCoincidesWithTheCoordinatorsCurricularPlan =
                     * false; for (int i = 0; i <
                     * activeStudentCurricularPlans.size(); i++) {
                     * StudentCurricularPlan activeStudentCurricularPlan =
                     * (StudentCurricularPlan) activeStudentCurricularPlans
                     * .get(i); if (coordinator.getExecutionDegree()
                     * .getDegreeCurricularPlan().getIdInternal().equals(
                     * activeStudentCurricularPlan .getDegreeCurricularPlan()
                     * .getIdInternal())) {
                     * hasAnActiveCurricularPlanThatCoincidesWithTheCoordinatorsCurricularPlan =
                     * true; } } if
                     * (!hasAnActiveCurricularPlanThatCoincidesWithTheCoordinatorsCurricularPlan) {
                     * return "noAuthorization"; }
                     */
                } catch (Exception e) {
                    return "noAuthorization";
                }
                return null;
            }
        }
        roleTemp = new ArrayList();
        roleTemp.add(RoleType.STUDENT);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            try {
                if (!id.getUtilizador().equals(
                        studentCurricularPlan.getStudent().getPerson().getUsername())) {
                    return "noAuthorization";
                }
            } catch (Exception e) {
                return "noAuthorization";
            }
            return null;
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.TEACHER);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            try {
                Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
                if (teacher == null) {
                    return "noAuthorization";
                }

                if (!verifyStudentTutor(teacher, studentCurricularPlan.getStudent())) {
                    return new String("error.enrollment.notStudentTutor+"
                            + studentCurricularPlan.getStudent().getNumber().toString());
                }

            } catch (Exception e) {
                return "noAuthorization";
            }
            return null;
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roleTemp.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            if (!studentCurricularPlan.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                    DegreeType.DEGREE)) {
                return "noAuthorization";
            }
            return null;
        }
        return "noAuthorization";
    }

    private boolean verifyStudentTutor(Teacher teacher, Student student) throws ExcepcaoPersistencia {
        if (student.getAssociatedTutor() == null) {
            return false;
        }
        
        return student.getAssociatedTutor().getTeacher().equals(teacher);
    }
}