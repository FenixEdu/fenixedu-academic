/*
 * Created on 2003/12/24
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz
 */
public class MonitorUsersDA extends FenixDispatchAction {

    public ActionForward monitor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        Boolean userLoggingIsOn = ServiceManagerServiceFactory.userLoggingIsOn(userView);
        request.setAttribute("userLoggingIsOn", userLoggingIsOn);

        Map userLogs = ServiceManagerServiceFactory.getUsersLogInfo(userView);
        request.setAttribute("userLogs", userLogs);

        return mapping.findForward("Show");
    }

    public ActionForward activateMonotoring(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        ServiceManagerServiceFactory.turnUserLoggingOn(userView);

        return monitor(mapping, form, request, response);
    }

    public ActionForward deactivateMonotoring(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        ServiceManagerServiceFactory.turnUserLoggingOff(userView);

        return monitor(mapping, form, request, response);
    }

    public ActionForward clearUserLogs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        ServiceManagerServiceFactory.clearUserLogHistory(userView);

        return monitor(mapping, form, request, response);
    }

}