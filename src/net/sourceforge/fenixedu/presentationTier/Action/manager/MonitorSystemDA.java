/*
 * Created on 2003/12/25
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Luis Cruz
 */
public class MonitorSystemDA extends FenixDispatchAction {

    public ActionForward monitor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	IUserView userView = UserView.getUser();

	SystemInfo systemInfoApplicationServer = ServiceManagerServiceFactory.getSystemInfo(userView);
	request.setAttribute("systemInfoApplicationServer", systemInfoApplicationServer);

	SystemInfo systemInfoWebContainer = ServiceManagerServiceFactory.getSystemInfo(userView);
	request.setAttribute("systemInfoWebContainer", systemInfoWebContainer);

	return mapping.findForward("Show");
    }

}