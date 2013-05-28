package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.system.CronRegistry;
import net.sourceforge.fenixedu.domain.system.CronScriptInvocation;
import net.sourceforge.fenixedu.domain.system.CronScriptState;
import net.sourceforge.fenixedu.domain.system.CronScriptState.RunNowExecutor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "manager", path = "/cron", input = "/cron.do?method=showScripts&page=0", scope = "request",
        parameter = "method")
@Forwards(value = { @Forward(name = "showCronScript", path = "/manager/showCronScript.jsp"),
        @Forward(name = "showCronScriptInvocationLog", path = "/manager/showCronScriptInvocationLog.jsp"),
        @Forward(name = "showCronScripts", path = "/manager/showCronScripts.jsp") })
public class CronDA extends FenixDispatchAction {

    public ActionForward showScripts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("cronRegistry", CronRegistry.getInstance());

        final List<CronScriptState> activeCronScriptStates = new ArrayList<CronScriptState>();
        final List<CronScriptState> inActiveCronScriptStates = new ArrayList<CronScriptState>();
        for (final CronScriptState cronScriptState : rootDomainObject.getCronScriptStatesSet()) {
            if (cronScriptState.getActive().booleanValue()) {
                activeCronScriptStates.add(cronScriptState);
            } else {
                inActiveCronScriptStates.add(cronScriptState);
            }
        }

        Collections.sort(activeCronScriptStates, CronScriptState.COMPARATOR_BY_ABSOLUTE_EXECUTION_ORDER);
        Collections.sort(inActiveCronScriptStates, CronScriptState.COMPARATOR_BY_CRON_SCRIPT_CLASSNAME);
        request.setAttribute("activeCronScriptStates", activeCronScriptStates);
        request.setAttribute("inActiveCronScriptStates", inActiveCronScriptStates);

        return mapping.findForward("showCronScripts");
    }

    public ActionForward runNow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws  FenixServiceException {
        final String cronScriptStateIDString = request.getParameter("cronScriptStateID");
        final CronScriptState cronScriptState =
                AbstractDomainObject.fromExternalId(Integer.valueOf(cronScriptStateIDString));

        executeFactoryMethod(new RunNowExecutor(cronScriptState));

        return showScripts(mapping, form, request, response);
    }

    public ActionForward showScript(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String cronScriptStateIDString = request.getParameter("cronScriptStateID");
        final CronScriptState cronScriptState =
                AbstractDomainObject.fromExternalId(Integer.valueOf(cronScriptStateIDString));
        request.setAttribute("cronScriptState", cronScriptState);

        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber =
                pageNumberString != null && pageNumberString.length() > 0 ? Integer.valueOf(pageNumberString) : Integer
                        .valueOf(1);
        request.setAttribute("pageNumber", pageNumber);

        final SortedSet<CronScriptInvocation> cronScriptInvocations =
                cronScriptState.getCronScriptInvocationsSetSortedByInvocationStartTime();
        final CollectionPager<CronScriptInvocation> cronScriptInvocationPager =
                new CollectionPager<CronScriptInvocation>(cronScriptInvocations, 200);
        request.setAttribute("cronScriptInvocationsPage", cronScriptInvocationPager.getPage(pageNumber.intValue()));
        request.setAttribute("numberOfPages", Integer.valueOf(cronScriptInvocationPager.getNumberOfPages()));

        return mapping.findForward("showCronScript");
    }

    public ActionForward showScriptInvocationLog(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String cronScriptInvocationIDString = request.getParameter("cronScriptInvocationID");
        final CronScriptInvocation cronScriptInvocation =
                AbstractDomainObject.fromExternalId(Integer.valueOf(cronScriptInvocationIDString));
        request.setAttribute("cronScriptInvocation", cronScriptInvocation);
        return mapping.findForward("showCronScriptInvocationLog");
    }

}
