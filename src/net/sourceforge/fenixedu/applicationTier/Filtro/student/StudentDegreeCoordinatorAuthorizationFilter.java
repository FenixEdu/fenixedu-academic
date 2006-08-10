/*
 * Created on Nov 15, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AccessControlFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author André Fernandes / João Brito
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StudentDegreeCoordinatorAuthorizationFilter extends AccessControlFilter {
    public StudentDegreeCoordinatorAuthorizationFilter() {
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        IUserView id = (IUserView) request.getRequester();
        String messageException;

        if (id == null || id.getRoleTypes() == null || !id.hasRoleType(getRoleType())) {
            throw new NotAuthorizedFilterException();
        }
        messageException = authorizedCoordinator(id, request.getServiceParameters().parametersArray());
        if (messageException != null)
            throw new NotAuthorizedFilterException(messageException);
    }

    /*
     * (String username, StudentCurricularPlanIDDomainType curricularPlanID,
     * EnrollmentStateSelectionType criterio)
     */
    // devolve null se tudo OK
    // noAuthorization se algum prob
    private String authorizedCoordinator(IUserView id, Object[] arguments) {
        try {
            String username = (String) arguments[0];

            Registration student1 = Registration.readByUsername(username);

            List students = student1.getPerson().getStudents();

            // for each of the Person's Registration roles
            for (Iterator studentsIterator = students.iterator(); studentsIterator.hasNext();) {
                Registration student = (Registration) studentsIterator.next();

                Group group = student.findFinalDegreeWorkGroupForCurrentExecutionYear();

                if (group != null) {
                    ExecutionDegree executionDegree = group.getExecutionDegree();
                    List coordinators = executionDegree.getCoordinatorsList();

                    for (Iterator it = coordinators.iterator(); it.hasNext();) {
                        Coordinator coordinator = (Coordinator) it.next();
                        if (coordinator.getTeacher().getPerson().getUsername()
                                .equals(id.getUtilizador())) {
                            // The student is a candidate for a final degree
                            // work of
                            // the degree of the
                            // coordinator making the request. Allow access.
                            return null;
                        }
                    }

                    List groupProposals = group.getGroupProposals();

                    for (Iterator it = groupProposals.iterator(); it.hasNext();) {
                        GroupProposal groupProposal = (GroupProposal) it.next();
                        Proposal proposal = groupProposal.getFinalDegreeWorkProposal();
                        Teacher teacher = proposal.getOrientator();

                        if (teacher.getPerson().getUsername().equals(id.getUtilizador())) {
                            // The student is a candidate for a final degree
                            // work of
                            // oriented by the
                            // teacher making the request. Allow access.
                            return null;
                        }

                        teacher = proposal.getCoorientator();
                        if (teacher != null
                                && teacher.getPerson().getUsername().equals(id.getUtilizador())) {
                            // The student is a candidate for a final degree
                            // work of
                            // cooriented by the
                            // teacher making the request. Allow access.
                            return null;
                        }
                    }
                }
                /*-----*/

                List studentCurricularPlans = student.getStudentCurricularPlans();

                for (Iterator scpIterator = studentCurricularPlans.iterator(); scpIterator.hasNext();) {
                    StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) scpIterator
                            .next();

                    List executionDegrees = studentCurricularPlan.getDegreeCurricularPlan().getExecutionDegrees();
                    if (executionDegrees == null || executionDegrees.isEmpty()) {
                        continue;
                    }

                    for (Iterator executionDegreeIterator = executionDegrees.iterator(); executionDegreeIterator
                            .hasNext();) {
                        ExecutionDegree executionDegree = (ExecutionDegree) executionDegreeIterator
                                .next();
                        List<Coordinator> coordinatorsList = executionDegree.getCoordinatorsList();

                        if (coordinatorsList == null || coordinatorsList.isEmpty()) {
                            continue;
                        }

                        final String coordinatorUsername = id.getUtilizador();

                        Coordinator coordinator = (Coordinator) CollectionUtils.find(coordinatorsList,
                                new Predicate() {

                                    public boolean evaluate(Object input) {
                                        Coordinator coordinatorTemp = (Coordinator) input;
                                        if (coordinatorUsername.equals(coordinatorTemp.getTeacher()
                                                .getPerson().getUsername())) {
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                        if (coordinator == null) {
                            continue;
                        }

                        // if this is a coordinator of the Degree for this
                        // Registration
                        if (coordinator.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                                .getIdInternal().equals(
                                        studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                                                .getIdInternal())) {
                            return null;
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
        return "noAuthorization";
    }

    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }
}
