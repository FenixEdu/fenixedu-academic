package net.sourceforge.fenixedu.presentationTier.Action.candidacy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

abstract public class CandidacyProcessDA extends CaseHandlingDispatchAction {

    abstract protected Class getChildProcessType();

    abstract public ActionForward intro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response);

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return intro(mapping, form, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setCandidacyProcessInformation(request, getProcess(request));
	return mapping.findForward("intro");
    }

    @Override
    protected CandidacyProcess getProcess(HttpServletRequest request) {
	return (CandidacyProcess) super.getProcess(request);
    }

    protected void setCandidacyProcessInformation(final HttpServletRequest request, final CandidacyProcess process) {
	if (process != null) {
	    request.setAttribute("process", process);
	    request.setAttribute("processActivities", process.getAllowedActivities(AccessControl.getUserView()));
	    request.setAttribute("childProcesses", process.getChildProcesses());
	    request.setAttribute("canCreateChildProcess", canCreateProcess(getChildProcessType().getName()));
	    request.setAttribute("childProcessName", getChildProcessType().getSimpleName());
	}
	request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
    }
}
