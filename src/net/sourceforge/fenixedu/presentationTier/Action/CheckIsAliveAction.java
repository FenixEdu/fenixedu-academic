package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.CheckIsAliveService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CheckIsAliveAction extends FenixAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final Boolean result = CheckIsAliveService.run();

		if (result != null && result.booleanValue()) {
			request.setAttribute("isAlive", "ok");
		} else {
			request.setAttribute("isAlive", "ko");
		}

		final String timeout = request.getParameter("timeout");
		if (timeout != null && !timeout.isEmpty()) {
			final long t = Long.parseLong(timeout);
			final Thread thread = Thread.currentThread();
			System.out.println("Thread: " + thread.hashCode() + " will sleep for: " + t + "ms.");
			Thread.sleep(t);
			System.out.println("Thread: " + thread.hashCode() + " has woken up.");
		}

		return mapping.findForward("Success");
	}

}