/*
 * Created on 2003/12/25
 * 
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import framework.factory.ServiceManagerServiceFactory;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.logging.SystemInfo;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz
 */
public class MonitorSystemDA extends FenixDispatchAction {

	public ActionForward monitor(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		SystemInfo systemInfoApplicationServer = ServiceManagerServiceFactory.getSystemInfo(userView);
		request.setAttribute("systemInfoApplicationServer", systemInfoApplicationServer);

		SystemInfo systemInfoWebContainer = ServiceManagerServiceFactory.getSystemInfo(userView);
		request.setAttribute("systemInfoWebContainer", systemInfoWebContainer);

		return mapping.findForward("Show");
	}

}