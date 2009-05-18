package net.sourceforge.fenixedu.presentationTier.Action.phd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

abstract public class PhdProcessDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Process process = getProcess(request);
	if (process != null) {
	    request.setAttribute("processId", process.getExternalId());
	    request.setAttribute("process", process);
	}

	return super.execute(mapping, actionForm, request, response);
    }

    protected Process getProcess(HttpServletRequest request) {
	return getDomainObject(request, "processId");
    }

    protected ActionForward executeActivity(Class<? extends Activity<? extends Process>> activity, Object activityParameter,
	    HttpServletRequest request, ActionMapping mapping, String errorForward, String sucessForward) {
	return executeActivity(activity, activityParameter, request, mapping, errorForward, sucessForward, null);
    }

    protected ActionForward executeActivity(Class<? extends Activity<? extends Process>> activityClass, Object activityParameter,
	    HttpServletRequest request, ActionMapping mapping, String errorForward, String sucessForward, String sucessMessage,
	    String... sucessMessageArgs) {

	try {

	    ExecuteProcessActivity.run(getProcess(request), activityClass.getSimpleName(), activityParameter);

	    if (!StringUtils.isEmpty(sucessMessage)) {
		addSuccessMessage(request, sucessMessage, sucessMessageArgs);
	    }

	    return mapping.findForward(sucessForward);

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());

	    return mapping.findForward(errorForward);
	}
    }

    protected void addErrorMessage(HttpServletRequest request, String key, String... args) {
	addActionMessage("error", request, key, args);
    }

    protected void addSuccessMessage(HttpServletRequest request, String key, String... args) {
	addActionMessage("success", request, key, args);
    }

}
