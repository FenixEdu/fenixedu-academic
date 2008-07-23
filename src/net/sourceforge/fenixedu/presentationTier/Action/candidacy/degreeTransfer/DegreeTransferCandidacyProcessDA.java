package net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeTransfer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.period.DegreeTransferCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingDegreeTransferCandidacyProcess", module = "academicAdminOffice", formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
	@Forward(name = "send-to-coordinator", path = "/candidacy/sendToCoordinator.jsp"),
	@Forward(name = "send-to-scientificCouncil", path = "/candidacy/sendToScientificCouncil.jsp"),
	@Forward(name = "view-candidacy-results", path = "/candidacy/degreeTransfer/viewCandidacyResults.jsp"),
	@Forward(name = "introduce-candidacy-results", path = "/candidacy/degreeTransfer/introduceCandidacyResults.jsp"),
	@Forward(name = "create-registrations", path = "/candidacy/createRegistrations.jsp")

})
public class DegreeTransferCandidacyProcessDA extends CandidacyProcessDA {

    @Override
    protected Class getProcessType() {
	return DegreeTransferCandidacyProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
	return DegreeTransferIndividualCandidacyProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
	return DegreeTransferCandidacyPeriod.class;
    }

    @Override
    protected CandidacyProcess getCandidacyProcess(final ExecutionInterval executionInterval) {
	return executionInterval.hasDegreeTransferCandidacyPeriod() ? executionInterval.getDegreeTransferCandidacyPeriod()
		.getDegreeTransferCandidacyProcess() : null;
    }

    @Override
    protected DegreeTransferCandidacyProcess getProcess(HttpServletRequest request) {
	return (DegreeTransferCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
	return mapping.findForward("intro");
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	if (!hasExecutionInterval(request)) {
	    request.setAttribute("executionInterval", ExecutionYear.readCurrentExecutionYear());
	}
	setCandidacyProcessInformation(request, getCandidacyProcess(getExecutionInterval(request)));
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
	// TODO Auto-generated method stub
	return null;
    }

}
