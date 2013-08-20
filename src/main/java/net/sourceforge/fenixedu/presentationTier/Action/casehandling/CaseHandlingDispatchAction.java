package net.sourceforge.fenixedu.presentationTier.Action.casehandling;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class CaseHandlingDispatchAction extends FenixDispatchAction {

    abstract protected Class getProcessType();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setProcessName(request);
        setProcess(request);
        return super.execute(mapping, actionForm, request, response);
    }

    protected void setProcessName(final HttpServletRequest request) {
        request.setAttribute("processName", getProcessType().getSimpleName());
    }

    protected Process readProcess(final HttpServletRequest request) {
        return getDomainObject(request, "processId");
    }

    protected void setProcess(final HttpServletRequest request) {
        final Process process = readProcess(request);
        if (process != null) {
            request.setAttribute("process", process);
        }
    }

    protected Collection<Process> getAllowedProcessInstances(final IUserView userView) {
        final Set<Process> result = new TreeSet<Process>();
        for (final Process process : rootDomainObject.getProcesses()) {
            if (process.getClass().equals(getProcessType()) && process.canExecuteActivity(userView)) {
                result.add(process);
            }
        }
        return result;
    }

    protected Process getProcess(final HttpServletRequest request) {
        return (Process) request.getAttribute("process");
    }

    protected boolean hasProcess(final HttpServletRequest request) {
        return getProcess(request) != null;
    }

    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
        request.setAttribute("processes", getAllowedProcessInstances(AccessControl.getUserView()));
        return mapping.findForward("list-processes");
    }

    protected Object canCreateProcess(final String name) {
        try {
            final Activity<?> startActivity = Process.getStartActivity(name);
            startActivity.checkPreConditions(null, AccessControl.getUserView());
        } catch (PreConditionNotValidException e) {
            return false;
        }
        return true;
    }

    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final Process process = getProcess(request);
        request.setAttribute("activities", process.getAllowedActivities(AccessControl.getUserView()));
        return mapping.findForward("list-allowed-activities");
    }

    abstract public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response);

    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        Process process = CreateNewProcess.run(getProcessType().getName(), getRenderedObject());
        request.setAttribute("process", process);
        return listProcessAllowedActivities(mapping, form, request, response);
    }

    protected void executeActivity(Process process, String activityId) throws FenixServiceException {
        executeActivity(process, activityId, null);
    }

    protected Process executeActivity(Process process, String activityId, Object object) throws FenixServiceException {
        return ExecuteProcessActivity.run(process, activityId, object);
    }

}