package net.sourceforge.fenixedu.presentationTier.Action.candidacy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class Over23CandidacyProcessDA extends CaseHandlingDispatchAction {

    @Override
    protected Class getProcessType() {
	return Over23CandidacyProcess.class;
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("over23CandidacyProcessBean", new Over23CandidacyProcessBean(ExecutionYear
		.readCurrentExecutionYear()));
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward prepareExecuteEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final Over23CandidacyProcess process = getProcess(request);
	request.setAttribute("over23CandidacyProcessBean", new Over23CandidacyProcessBean(process));
	return mapping.findForward("prepare-edit-candidacy-period");
    }

    public ActionForward executeEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
	try {
	    executeActivity(getProcess(request), "EditCandidacyPeriod", getRenderedObject("over23CandidacyProcessBean"));
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("over23CandidacyProcessBean", getRenderedObject("over23CandidacyProcessBean"));
	    return mapping.findForward("prepare-edit-candidacy-period");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    @Override
    protected Over23CandidacyProcess getProcess(HttpServletRequest request) {
	return (Over23CandidacyProcess) super.getProcess(request);
    }
}
