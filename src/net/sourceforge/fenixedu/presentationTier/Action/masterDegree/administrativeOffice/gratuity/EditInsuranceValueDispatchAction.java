package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class EditInsuranceValueDispatchAction extends FenixDispatchAction {

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();

        //execution years
        List executionYears = null;
        Object[] args = {};
        try {
            executionYears = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadNotClosedExecutionYears", args);
        } catch (FenixServiceException e) {
            errors.add("noExecutionYears", new ActionError("error.impossible.insertExemptionGratuity"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (executionYears == null || executionYears.size() <= 0) {
            errors.add("noExecutionYears", new ActionError("error.impossible.insertExemptionGratuity"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(new BeanComparator("year"), true);
        Collections.sort(executionYears, comparator);

        List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
        request.setAttribute("executionYears", executionYearLabels);

        return mapping.findForward("chooseExecutionYear");

    }

    public ActionForward readInsuranceValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm editInsuranceForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer executionYearId = (Integer) editInsuranceForm.get("executionYear");

        InfoInsuranceValue infoInsuranceValue = null;
        Object argsInsuranceValue[] = { executionYearId };
        try {
            infoInsuranceValue = (InfoInsuranceValue) ServiceUtils.executeService(userView,
                    "ReadInsuranceValueByExecutionYearID", argsInsuranceValue);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoInsuranceValue != null) {
            editInsuranceForm.set("insuranceValue", infoInsuranceValue.getAnnualValue());
        }

        InfoExecutionYear infoExecutionYear = null;
        try {
            infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(userView,
                    "ReadExecutionYearByID", argsInsuranceValue);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (infoExecutionYear == null) {
            throw new FenixActionException("Invalid Execution Year");
        }

        String[] executionYears = infoExecutionYear.getYear().split("/");

        if ((infoInsuranceValue != null) && (infoInsuranceValue.getEndDate() != null)) {
            Date endDate = infoInsuranceValue.getEndDate();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);

            editInsuranceForm.set("endDateDay", new Integer(calendar.get(Calendar.DAY_OF_MONTH)));
            editInsuranceForm.set("endDateMonth", new Integer(calendar.get(Calendar.MONTH)));
            editInsuranceForm.set("endDateYear", new Integer(calendar.get(Calendar.YEAR)));
        } else {
            editInsuranceForm.set("endDateDay", Data.OPTION_DEFAULT);
            editInsuranceForm.set("endDateMonth", Data.OPTION_DEFAULT);
            editInsuranceForm.set("endDateYear", Data.OPTION_DEFAULT);
        }

        request.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
        request.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
        request.setAttribute(SessionConstants.YEARS_KEY, Data.getCustomYears(Integer
                .parseInt(executionYears[0]), Integer.parseInt(executionYears[1])));

        return mapping.findForward("defineInsuranceValue");

    }

    public ActionForward defineInsuranceValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm editInsuranceForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer executionYearId = (Integer) editInsuranceForm.get("executionYear");
        Double insuranceValue = (Double) editInsuranceForm.get("insuranceValue");
        Integer endDateDay = (Integer) editInsuranceForm.get("endDateDay");
        Integer endDateMonth = (Integer) editInsuranceForm.get("endDateMonth");
        Integer endDateYear = (Integer) editInsuranceForm.get("endDateYear");

        Date endDate = null;

        if ((endDateDay.intValue() > 0) && (endDateMonth.intValue() > 0) && (endDateYear.intValue() > 0)
                && Data.validDate(endDateDay, endDateMonth, endDateYear)) {
            Calendar officialDateCalendar = new GregorianCalendar(endDateYear.intValue(), endDateMonth
                    .intValue(), endDateDay.intValue());
            endDate = officialDateCalendar.getTime();
        } else {
            //invalid date!!
            ActionErrors errors = new ActionErrors();
            errors.add("invalidDate", new ActionError("error.impossible.invalidDate"));
            saveErrors(request, errors);
            return readInsuranceValue(mapping, form, request, response);
        }

        Object argsInsuranceValue[] = { executionYearId, insuranceValue, endDate };
        try {
            ServiceUtils.executeService(userView, "EditInsuranceValueByExecutionYearID",
                    argsInsuranceValue);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("defineSuccess");

    }

    private List buildLabelValueBeanForJsp(List infoExecutionYears) {
        List executionYearLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionYears, new Transformer() {
            public Object transform(Object arg0) {
                InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

                LabelValueBean executionYear = new LabelValueBean(infoExecutionYear.getYear(),
                        infoExecutionYear.getIdInternal().toString());
                return executionYear;
            }
        }, executionYearLabels);
        return executionYearLabels;
    }

}