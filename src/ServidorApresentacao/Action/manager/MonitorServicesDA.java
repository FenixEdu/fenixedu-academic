/*
 * Created on 2003/12/24
 * 
 */
package ServidorApresentacao.Action.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz
 */
public class MonitorServicesDA extends FenixDispatchAction {

	public ActionForward monitor(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		Boolean loggingIsOn = ServiceManagerServiceFactory.loggingIsOn(userView);
		request.setAttribute("loggingIsOn", loggingIsOn);

		Map serviceLogs = ServiceManagerServiceFactory.getServicesLogInfo(userView);
		request.setAttribute("serviceLogs", serviceLogs);
		
		return mapping.findForward("Show");
	}

	public ActionForward activateMonotoring(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		ServiceManagerServiceFactory.turnLoggingOn(userView);

		return monitor(mapping, form, request, response);
	}

	public ActionForward deactivateMonotoring(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		ServiceManagerServiceFactory.turnLoggingOff(userView);

		return monitor(mapping, form, request, response);
	}

}
