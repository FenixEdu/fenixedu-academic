/*
 * Created on 3/Jul/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionPeriods;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author jpvl
 */
public class IndexAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaValidatorForm executionPeriodForm = (DynaValidatorForm) form;

        List executionPeriodsNotClosed = ReadNotClosedExecutionPeriods.run();

        removeCreditsPointZeroExecutionPeriod(executionPeriodsNotClosed);
        setChoosedExecutionPeriod(request, executionPeriodsNotClosed, executionPeriodForm);

        BeanComparator initialDateComparator = new BeanComparator("beginDate");
        Collections.sort(executionPeriodsNotClosed, new ReverseComparator(initialDateComparator));

        request.setAttribute("executionPeriods", executionPeriodsNotClosed);

        return mapping.findForward("successfull-read");
    }

    private void removeCreditsPointZeroExecutionPeriod(List<InfoExecutionPeriod> executionPeriodsNotClosed) {
        for (InfoExecutionPeriod infoExecutionPeriod : executionPeriodsNotClosed) {
            if (infoExecutionPeriod.getExternalId().equals(1)) {
                executionPeriodsNotClosed.remove(infoExecutionPeriod);
                break;
            }
        }
    }

    /**
     * If the executionPeriod is not already selected it chooses the current
     * executionPeriod.
     * 
     */
    private void setChoosedExecutionPeriod(HttpServletRequest request, List executionPeriodsNotClosed,
            DynaValidatorForm executionPeriodForm) {
        final String executionPeriodId = (String) executionPeriodForm.get("executionPeriodId");
        InfoExecutionPeriod infoExecutionPeriod = null;
        if (StringUtils.isEmpty(executionPeriodId)) {
            infoExecutionPeriod = (InfoExecutionPeriod) CollectionUtils.find(executionPeriodsNotClosed, new Predicate() {

                @Override
                public boolean evaluate(Object input) {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;

                    return infoExecutionPeriod.getState().equals(PeriodState.CURRENT);
                }
            });
        } else {
            infoExecutionPeriod = (InfoExecutionPeriod) CollectionUtils.find(executionPeriodsNotClosed, new Predicate() {

                @Override
                public boolean evaluate(Object input) {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;

                    return infoExecutionPeriod.getExternalId().equals(executionPeriodId);
                }
            });

        }
        request.setAttribute("infoExecutionPeriod", infoExecutionPeriod);
    }
}