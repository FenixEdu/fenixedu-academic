package net.sourceforge.fenixedu.presentationTier.Action.candidacy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class IndividualCandidacyProcessDA extends CaseHandlingDispatchAction {

    abstract protected Class getParentProcessType();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setParentProcess(request);
	return super.execute(mapping, actionForm, request, response);
    }

    protected void setParentProcess(HttpServletRequest request) {
	final Integer parentProcessId = getIntegerFromRequest(request, "parentProcessId");
	if (parentProcessId != null) {
	    request.setAttribute("parentProcess", rootDomainObject.readProcessByOID(Integer.valueOf(parentProcessId)));
	} else {
	    setProcess(request);
	    if (hasProcess(request)) {
		request.setAttribute("parentProcess", getProcess(request).getCandidacyProcess());
	    }
	}
    }

    protected CandidacyProcess getParentProcess(final HttpServletRequest request) {
	return (CandidacyProcess) request.getAttribute("parentProcess");
    }

    protected boolean hasParentProcess(final HttpServletRequest request) {
	return getParentProcess(request) != null;
    }

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setMainCandidacyProcessInformation(request, getParentProcess(request));
	return mapping.findForward("intro");
    }

    @Override
    protected IndividualCandidacyProcess getProcess(HttpServletRequest request) {
	return (IndividualCandidacyProcess) super.getProcess(request);
    }

    protected void setMainCandidacyProcessInformation(final HttpServletRequest request, final CandidacyProcess process) {
	request.setAttribute("process", process);
	request.setAttribute("processName", getParentProcessType().getSimpleName());
	request.setAttribute("canCreateProcess", canCreateProcess(getParentProcessType().getName()));
	request.setAttribute("processActivities", process.getAllowedActivities(AccessControl.getUserView()));
	request.setAttribute("canCreateChildProcess", canCreateProcess(getProcessType().getName()));
	request.setAttribute("childProcessName", getProcessType().getSimpleName());
	request.setAttribute("childProcesses", process.getChildProcesses());
    }

}
