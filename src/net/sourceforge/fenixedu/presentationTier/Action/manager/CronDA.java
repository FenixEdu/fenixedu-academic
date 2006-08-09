package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.system.CronRegistry;
import net.sourceforge.fenixedu.domain.system.CronScriptInvocation;
import net.sourceforge.fenixedu.domain.system.CronScriptState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class CronDA extends FenixDispatchAction {

    public ActionForward showScripts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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

    public ActionForward showScript(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	final String cronScriptStateIDString = request.getParameter("cronScriptStateID");
    	final CronScriptState cronScriptState = rootDomainObject.readCronScriptStateByOID(Integer.valueOf(cronScriptStateIDString));
    	request.setAttribute("cronScriptState", cronScriptState);

        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber = pageNumberString != null && pageNumberString.length() > 0 ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);
        request.setAttribute("pageNumber", pageNumber);

        final SortedSet<CronScriptInvocation> cronScriptInvocations = cronScriptState.getCronScriptInvocationsSetSortedByInvocationStartTime();
        final CollectionPager<CronScriptInvocation> cronScriptInvocationPager = new CollectionPager<CronScriptInvocation>(cronScriptInvocations, 200);
        request.setAttribute("cronScriptInvocationsPage", cronScriptInvocationPager.getPage(pageNumber.intValue()));
        request.setAttribute("numberOfPages", Integer.valueOf(cronScriptInvocationPager.getNumberOfPages()));

    	return mapping.findForward("showCronScript");
    }

    public ActionForward showScriptInvocationLog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	final String cronScriptInvocationIDString = request.getParameter("cronScriptInvocationID");
    	final CronScriptInvocation cronScriptInvocation = rootDomainObject.readCronScriptInvocationByOID(Integer.valueOf(cronScriptInvocationIDString));
    	request.setAttribute("cronScriptInvocation", cronScriptInvocation);
    	return mapping.findForward("showCronScriptInvocationLog");
    }

}
