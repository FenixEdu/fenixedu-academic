package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

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

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = (IUserView) request.getRequester();
        String messageException = hasProvilege(id, request.getServiceParameters().parametersArray());
        if ((id == null) || (id.getRoleTypes() == null) || (!containsRoleType(id.getRoleTypes()))
                || (messageException != null)) {
            throw new NotAuthorizedFilterException(messageException);
        }
    }

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(RoleType.COORDINATOR);
        roles.add(RoleType.TEACHER);
        roles.add(RoleType.STUDENT);
        return roles;
    }

    private String hasProvilege(IUserView id, Object[] arguments) {
        Integer studentCurricularPlanID = (Integer) arguments[1];

        StudentCurricularPlan studentCurricularPlan = null;

        studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);

        if (studentCurricularPlan == null || studentCurricularPlan.getStudent() == null) {
            return "noAuthorization";
        }

        Registration student = studentCurricularPlan.getStudent();

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

        if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            if (!studentCurricularPlan.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                    DegreeType.MASTER_DEGREE)
                    && !(id.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE) || id
                            .hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))) {
                return "noAuthorization";
            }
            if (!(id.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE) || id
                    .hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))) {
                return null;
            }
        }

        if (arguments[0] != null) {
            if (id.hasRoleType(RoleType.COORDINATOR)) {
                ExecutionDegree executionDegree = rootDomainObject
                        .readExecutionDegreeByOID((Integer) arguments[0]);

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
                return null;
            }
        }
        if (id.hasRoleType(RoleType.STUDENT)) {
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

        if (id.hasRoleType(RoleType.TEACHER)) {
            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
            if (teacher == null) {
                return "noAuthorization";
            }

            if (!verifyStudentTutor(teacher, studentCurricularPlan.getStudent())) {
                return new String("error.enrollment.notStudentTutor+"
                        + studentCurricularPlan.getStudent().getNumber().toString());
            }
            return null;
        }

        if (id.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                || id.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
            if (!studentCurricularPlan.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                    DegreeType.DEGREE)) {
                return "noAuthorization";
            }
            return null;
        }
        return "noAuthorization";
    }

    private boolean verifyStudentTutor(Teacher teacher, Registration registration) {
        return registration.getAssociatedTutor() != null
                && registration.getAssociatedTutor().getTeacher().equals(teacher);
    }
}