package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposalEditor;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class SubmitFinalWorkProposalAuthorization extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	final InfoProposalEditor infoProposalEditor = (InfoProposalEditor) request.getServiceParameters().getParameter(0);
	final Proposal proposal = rootDomainObject.readProposalByOID(infoProposalEditor.getIdInternal());
	final IUserView userView = (IUserView) request.getRequester();
	if (!authorized(userView.getPerson(), proposal)) {
	    throw new NotAuthorizedFilterException();
	}
    }

    private boolean authorized(final Person person, final Proposal proposal) {
	final Scheduleing scheduleing = proposal.getScheduleing();
	if (proposal.getOrientator() == person || proposal.getCoorientator() == person) {
	    if (scheduleing.isInsideProposalSubmissionPeriod()) {
		return true;
	    }
	}
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
