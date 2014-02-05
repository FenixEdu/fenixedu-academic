package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposalEditor;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

public class SubmitFinalWorkProposalAuthorization {

    public static final SubmitFinalWorkProposalAuthorization instance = new SubmitFinalWorkProposalAuthorization();

    public void execute(InfoProposalEditor infoProposal) throws NotAuthorizedException {
        final User userView = Authenticate.getUser();
        final InfoProposalEditor infoProposalEditor = infoProposal;
        if (infoProposalEditor.getExternalId() != null) {
            final Proposal proposal = FenixFramework.getDomainObject(infoProposalEditor.getExternalId());
            authorize(userView.getPerson(), proposal);

        } else {
            final String executionDegreeId = infoProposalEditor.getExecutionDegree().getExternalId();
            final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
            final Scheduleing scheduleing = executionDegree.getScheduling();
            authorize(userView.getPerson(), scheduleing);

        }
    }

    private void authorize(final Person person, final Scheduleing scheduleing) throws NotAuthorizedException {
        if (!isCoordinatorOrDepartmentAdminOffice(person, scheduleing)) {
            if (!person.hasRole(RoleType.TEACHER) && !person.hasAnyProfessorships() && !person.hasRole(RoleType.RESEARCHER)) {
                throw new NotAuthorizedException("finalDegreeWorkProposal.validator.NotAuthorized");
            }
            if (!scheduleing.isInsideProposalSubmissionPeriod()) {
                throw new NotAuthorizedException("finalDegreeWorkProposal.ProposalPeriod.validator.OutOfPeriod");
            }
        }
    }

    private void authorize(final Person person, final Proposal proposal) throws NotAuthorizedException {
        final Scheduleing scheduleing = proposal.getScheduleing();
        if (!isCoordinatorOrDepartmentAdminOffice(person, scheduleing)) {
            if (proposal.getOrientator() != person && proposal.getCoorientator() != person) {
                throw new NotAuthorizedException("finalDegreeWorkProposal.validator.NotAdvisorOrCoAdvisor");
            }
            if (!scheduleing.isInsideProposalSubmissionPeriod()) {
                throw new NotAuthorizedException("finalDegreeWorkProposal.ProposalPeriod.validator.OutOfPeriod");
            }
        }
    }

    private boolean isCoordinatorOrDepartmentAdminOffice(final Person person, final Scheduleing scheduleing) {
        for (final ExecutionDegree executionDegree : scheduleing.getExecutionDegreesSet()) {
            if (isCoordinatorFor(executionDegree, person)) {
                return true;
            }
            if (isDepartmentAdminOfficeMemberFor(executionDegree, person)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCoordinatorFor(final ExecutionDegree executionDegree, final Person person) {
        for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
            if (coordinator.getPerson() == person) {
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
        return false;
    }

    private boolean isDepartmentAdminOfficeMemberFor(final ExecutionDegree executionDegree, final Person person) {
        if (person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
            final Degree degree = executionDegree.getDegree();
            for (final Department department : degree.getDepartmentsSet()) {
                if (personWorksInDepartment(department, person)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean personWorksInDepartment(final Department department, final Person person) {
        final Employee employee = person.getEmployee();
        return employee.getCurrentDepartmentWorkingPlace() == department;
    }

}
