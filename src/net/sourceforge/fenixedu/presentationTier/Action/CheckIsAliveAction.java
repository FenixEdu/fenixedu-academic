package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CheckIsAliveAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		final Boolean result = (Boolean) ServiceManagerServiceFactory
				.executeService(null, "CheckIsAliveService", null);

		if (result != null && result.booleanValue()) {
			request.setAttribute("isAlive", "ok");
		} else {
			request.setAttribute("isAlive", "ko");
		}

		return mapping.findForward("Success");
	}

}
