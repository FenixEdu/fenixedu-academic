/*
 * Created on 2003/07/16
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ExecutionPeriodComparator;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Crus & Sara Ribeiro
 */
public class ManageExecutionPeriodsDA extends FenixDispatchAction {

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        try {
            List infoExecutionPeriods = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionPeriods", null);

            if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {

                Collections.sort(infoExecutionPeriods, new ExecutionPeriodComparator());

                if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
                    request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, infoExecutionPeriods);
                }

            }
        } catch (FenixServiceException ex) {
            throw new FenixActionException("Problemas de comunicação com a base de dados.", ex);
        }

        return mapping.findForward("Manage");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward alterExecutionPeriodState(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String year = request.getParameter("year");
        final Integer semester = new Integer(request.getParameter("semester"));
        final String periodStateToSet = request.getParameter("periodState");
        final IUserView userView = SessionUtils.getUserView(request);
        final PeriodState periodState = new PeriodState(periodStateToSet);

        final Object[] args = { year, semester, periodState };
        try {
            ServiceUtils.executeService(userView, "AlterExecutionPeriodState", args);
        } catch (InvalidArgumentsServiceException ex) {
            throw new FenixActionException("errors.nonExisting.executionPeriod", ex);
        } 

        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
    	final String executionPeriodIDString = request.getParameter("executionPeriodID");
    	final Integer executionPeriodID = Integer.valueOf(executionPeriodIDString);
    	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
    	request.setAttribute("executionPeriod", executionPeriod);
    	return mapping.findForward("editExecutionPeriod");
    }

}