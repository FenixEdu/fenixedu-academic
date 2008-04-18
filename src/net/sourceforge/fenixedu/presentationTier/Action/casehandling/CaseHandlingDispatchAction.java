package net.sourceforge.fenixedu.presentationTier.Action.casehandling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class CaseHandlingDispatchAction extends FenixDispatchAction {

    protected abstract Class getProcessType();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("processName", getProcessType().getSimpleName());

	Integer processId = getIntegerFromRequest(request, "processId");
	if (processId != null) {
	    request.setAttribute("process", rootDomainObject.readProcessByOID(Integer.valueOf(processId)));
	}

	return super.execute(mapping, actionForm, request, response);
    }

    protected Collection<Process> getAllowedProcessInstances(IUserView userView) {
	Set<Process> result = new TreeSet<Process>();
	for (Process process : rootDomainObject.getProcesses()) {
	    if (process.getClass().equals(getProcessType()) && process.canExecuteActivity(userView)) {
		result.add(process);
	    }
	}
	return result;
    }

    protected List<Activity> getAllowedActivities(IUserView userView, Process process) {
	List<Activity> result = new ArrayList<Activity>();
	for (Activity activity : process.getActivities()) {
	    try {
		activity.checkPreConditions(process, userView);
		result.add(activity);
	    } catch (Exception e) {
	    }
	}
	return result;
    }

    protected Process getProcess(HttpServletRequest request) {
	return (Process) request.getAttribute("process");
    }

    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Activity startActivity = Process.getStartActivity(getProcessType().getName());
	try {
	    startActivity.checkPreConditions(null, AccessControl.getUserView());
	    request.setAttribute("canCreateProcess", true);
	} catch (Exception e) {
	    request.setAttribute("canCreateProcess", false);
	}

	request.setAttribute("processes", getAllowedProcessInstances(AccessControl.getUserView()));
	return mapping.findForward("list-processes");
    }

    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Process process = getProcess(request);
	request.setAttribute("activities", getAllowedActivities(AccessControl.getUserView(), process));
	return mapping.findForward("list-allowed-activities");
    }

    public abstract ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response);

    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Process process = (Process) executeService("CreateNewProcess", getProcessType().getName(), getRenderedObject());
	request.setAttribute("process", process);
	return listProcessAllowedActivities(mapping, form, request, response);
    }

    protected void executeActivity(Process process, String activityId, Object object) throws FenixFilterException,
	    FenixServiceException {
	executeService("ExecuteProcessActivity", process, activityId, object);
    }

}
