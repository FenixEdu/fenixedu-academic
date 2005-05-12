/*
 * Created on 2003/12/24
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz
 */
public class MonitorServicesDA extends FenixDispatchAction {

    public ActionForward monitor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        Boolean loggingIsOn = ServiceManagerServiceFactory.serviceLoggingIsOn(userView);
        request.setAttribute("loggingIsOn", loggingIsOn);

        Map serviceLogs = ServiceManagerServiceFactory.getServicesLogInfo(userView);
        ComparatorChain comparator = new ComparatorChain(new BeanComparator("averageExecutionTime"), true);
        SortedSet sortederviceLogs = new TreeSet(comparator);
        sortederviceLogs.addAll(serviceLogs.values());
        request.setAttribute("serviceLogs", sortederviceLogs);

        return mapping.findForward("Show");
    }

    public ActionForward activateMonotoring(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        ServiceManagerServiceFactory.turnServiceLoggingOn(userView);

        return monitor(mapping, form, request, response);
    }

    public ActionForward deactivateMonotoring(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        ServiceManagerServiceFactory.turnServiceLoggingOff(userView);

        return monitor(mapping, form, request, response);
    }

    public ActionForward clearServiceLogs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        ServiceManagerServiceFactory.clearServiceLogHistory(userView);

        return monitor(mapping, form, request, response);
    }

}