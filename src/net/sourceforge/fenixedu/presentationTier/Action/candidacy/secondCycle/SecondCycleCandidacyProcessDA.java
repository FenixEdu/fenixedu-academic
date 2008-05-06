package net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

@Mapping(path = "/caseHandlingSecondCycleCandidacyProcess", module = "academicAdminOffice", formBean = "candidacyForm")
@Forwards( { @Forward(name = "intro", path = "/candidacy/secondCycle/intro.jsp"),
	@Forward(name = "list-processes", path = "/academicAdminOffice/caseHandling/listProcesses.jsp"),
	@Forward(name = "list-allowed-activities", path = "/academicAdminOffice/caseHandling/listActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/secondCycle/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/secondCycle/editCandidacyPeriod.jsp")

})
public class SecondCycleCandidacyProcessDA extends CaseHandlingDispatchAction {

    @Override
    protected Class getProcessType() {
	return SecondCycleCandidacyProcess.class;
    }

    public ActionForward intro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("intro");
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("secondCycleCandidacyProcessBean", new SecondCycleCandidacyProcessBean(ExecutionYear
		.readCurrentExecutionYear()));
	return mapping.findForward("prepare-create-new-process");
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    return super.createNewProcess(mapping, form, request, response);
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("secondCycleCandidacyProcessBean", getRenderedObject("secondCycleCandidacyProcessBean"));
	    return mapping.findForward("prepare-create-new-process");
	}
    }

    public ActionForward createNewProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("secondCycleCandidacyProcessBean", getRenderedObject("secondCycleCandidacyProcessBean"));
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward prepareExecuteEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final SecondCycleCandidacyProcess process = getProcess(request);
	request.setAttribute("secondCycleCandidacyProcessBean", new SecondCycleCandidacyProcessBean(process));
	return mapping.findForward("prepare-edit-candidacy-period");
    }

    public ActionForward executeEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "EditCandidacyPeriod", getRenderedObject("secondCycleCandidacyProcessBean"));
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("secondCycleCandidacyProcessBean", getRenderedObject("secondCycleCandidacyProcessBean"));
	    return mapping.findForward("prepare-edit-candidacy-period");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeEditCandidacyPeriodInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("secondCycleCandidacyProcessBean", getRenderedObject("secondCycleCandidacyProcessBean"));
	return mapping.findForward("prepare-edit-candidacy-period");
    }

    @Override
    protected SecondCycleCandidacyProcess getProcess(HttpServletRequest request) {
	return (SecondCycleCandidacyProcess) super.getProcess(request);
    }
}
