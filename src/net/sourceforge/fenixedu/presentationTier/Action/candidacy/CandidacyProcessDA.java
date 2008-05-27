package net.sourceforge.fenixedu.presentationTier.Action.candidacy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

abstract public class CandidacyProcessDA extends CaseHandlingDispatchAction {

    abstract protected Class getChildProcessType();

    abstract protected Class getCandidacyPeriodType();

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setExecutionInterval(request);
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward intro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	setStartInformation(actionForm, request, response);
	return introForward(mapping);
    }

    protected void setExecutionInterval(final HttpServletRequest request) {
	final Integer executionIntervalId = getIntegerFromRequest(request, "executionIntervalId");
	if (executionIntervalId != null) {
	    request.setAttribute("executionInterval", rootDomainObject.readExecutionIntervalByOID(executionIntervalId));
	}
    }

    protected ExecutionInterval getExecutionInterval(final HttpServletRequest request) {
	return (ExecutionInterval) request.getAttribute("executionInterval");
    }

    protected boolean hasExecutionInterval(final HttpServletRequest request) {
	return getExecutionInterval(request) != null;
    }

    abstract protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response);

    abstract protected ActionForward introForward(final ActionMapping mapping);

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return intro(mapping, form, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setCandidacyProcessInformation(request, getProcess(request));
	return introForward(mapping);
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
	    request.setAttribute("executionIntervalId", process.getCandidacyExecutionInterval().getIdInternal());
	}
	request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
	request.setAttribute("executionIntervals", ExecutionInterval
		.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType()));
    }

    abstract protected CandidacyProcess getCandidacyProcess(final ExecutionInterval executionInterval);

    static public class CandidacyProcessForm extends FenixActionForm {
	private Integer executionIntervalId;

	public Integer getExecutionIntervalId() {
	    return executionIntervalId;
	}

	public void setExecutionIntervalId(Integer executionIntervalId) {
	    this.executionIntervalId = executionIntervalId;
	}
    }
}
