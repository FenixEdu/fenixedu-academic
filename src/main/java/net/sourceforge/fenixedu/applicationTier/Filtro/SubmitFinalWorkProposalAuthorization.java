package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposalEditor;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class SubmitFinalWorkProposalAuthorization {

    public static final SubmitFinalWorkProposalAuthorization instance = new SubmitFinalWorkProposalAuthorization();

    public void execute(InfoProposalEditor infoProposal) throws NotAuthorizedException {
        final IUserView userView = AccessControl.getUserView();
        final InfoProposalEditor infoProposalEditor = infoProposal;
        if (infoProposalEditor.getExternalId() != null) {
            final Proposal proposal = RootDomainObject.getInstance().readProposalByOID(infoProposalEditor.getExternalId());
            if (!authorized(userView.getPerson(), proposal)) {
                throw new NotAuthorizedException();
            }
        } else {
            final Integer executionDegreeId = infoProposalEditor.getExecutionDegree().getExternalId();
            final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);
            final Scheduleing scheduleing = executionDegree.getScheduling();
            if (!authorized(userView.getPerson(), scheduleing)) {
                throw new NotAuthorizedException();
            }
        }
    }

    private boolean authorized(final Person person, final Scheduleing scheduleing) {
        if ((person.hasRole(RoleType.TEACHER) || person.hasAnyProfessorships() || person.hasRole(RoleType.RESEARCHER))
                && scheduleing.isInsideProposalSubmissionPeriod()) {
            return true;
        }
        return isCoordinatorOrDepartmentAdminOffice(person, scheduleing);
    }

    private boolean authorized(final Person person, final Proposal proposal) {
        final Scheduleing scheduleing = proposal.getScheduleing();
        if (proposal.getOrientator() == person || proposal.getCoorientator() == person) {
            if (scheduleing.isInsideProposalSubmissionPeriod()) {
                return true;
            }
        }
        return isCoordinatorOrDepartmentAdminOffice(person, scheduleing);
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
