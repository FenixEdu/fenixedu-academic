/*
 * Created on 3/Jul/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author jpvl
 */
public class IndexAction extends Action {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        DynaValidatorForm executionPeriodForm = (DynaValidatorForm) form;

        List executionPeriodsNotClosed = (List) ServiceUtils.executeService(userView,
                "ReadNotClosedExecutionPeriods", null);

        setChoosedExecutionPeriod(request, executionPeriodsNotClosed, executionPeriodForm);

        BeanComparator initialDateComparator = new BeanComparator("beginDate");
        Collections.sort(executionPeriodsNotClosed, new ReverseComparator(initialDateComparator));

        request.setAttribute("executionPeriods", executionPeriodsNotClosed);

        return mapping.findForward("successfull-read");
    }

    /**
     * If the executionPeriod is not already selected it chooses the current
     * executionPeriod.
     * 
     * @param request
     * @param executionPeriodNotClosed
     * @param executionPeriodForm
     */
    private void setChoosedExecutionPeriod(HttpServletRequest request, List executionPeriodsNotClosed,
            DynaValidatorForm executionPeriodForm) {
        final Integer executionPeriodId = (Integer) executionPeriodForm.get("executionPeriodId");
        InfoExecutionPeriod infoExecutionPeriod = null;
        if (executionPeriodId == null) {
            infoExecutionPeriod = (InfoExecutionPeriod) CollectionUtils.find(executionPeriodsNotClosed,
                    new Predicate() {

                        public boolean evaluate(Object input) {
                            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;

                            return infoExecutionPeriod.getState().equals(PeriodState.CURRENT);
                        }
                    });
        } else {
            infoExecutionPeriod = (InfoExecutionPeriod) CollectionUtils.find(executionPeriodsNotClosed,
                    new Predicate() {

                        public boolean evaluate(Object input) {
                            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;

                            return infoExecutionPeriod.getIdInternal().equals(executionPeriodId);
                        }
                    });

        }
        request.setAttribute("infoExecutionPeriod", infoExecutionPeriod);
    }
}