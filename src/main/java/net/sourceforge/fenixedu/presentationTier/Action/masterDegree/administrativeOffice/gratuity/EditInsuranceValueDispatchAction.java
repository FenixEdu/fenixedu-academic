package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearByID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.EditInsuranceValueByExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadInsuranceValueByExecutionYearID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoInsuranceValue;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.Data;

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
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/editInsuranceValue",
        input = "/editInsuranceValue.do?method=chooseExecutionYear&page=0", attribute = "editInsuranceForm",
        formBean = "editInsuranceForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "defineSuccess", path = "defineInsuranceValueSuccess", tileProperties = @Tile(title = "teste44")),
        @Forward(name = "chooseExecutionYear", path = "chooseExecutionYear", tileProperties = @Tile(title = "teste45")),
        @Forward(name = "defineInsuranceValue", path = "defineInsuranceValue", tileProperties = @Tile(title = "teste46")) })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EditInsuranceValueDispatchAction extends FenixDispatchAction {

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();

        // execution years
        List executionYears = null;

        executionYears = ReadNotClosedExecutionYears.run();
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

    public ActionForward readInsuranceValue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm editInsuranceForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        String executionYearId = (String) editInsuranceForm.get("executionYear");

        InfoInsuranceValue infoInsuranceValue = null;
        try {
            infoInsuranceValue = ReadInsuranceValueByExecutionYearID.run(executionYearId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoInsuranceValue != null) {
            editInsuranceForm.set("insuranceValue", infoInsuranceValue.getAnnualValue());
        }

        InfoExecutionYear infoExecutionYear = null;
        infoExecutionYear = ReadExecutionYearByID.run(executionYearId);
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

        request.setAttribute(PresentationConstants.MONTH_DAYS_KEY, Data.getMonthDays());
        request.setAttribute(PresentationConstants.MONTH_LIST_KEY, Data.getMonths());
        request.setAttribute(PresentationConstants.YEARS_KEY,
                Data.getCustomYears(Integer.parseInt(executionYears[0]), Integer.parseInt(executionYears[1])));

        return mapping.findForward("defineInsuranceValue");

    }

    public ActionForward defineInsuranceValue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm editInsuranceForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        String executionYearId = (String) editInsuranceForm.get("executionYear");
        Double insuranceValue = (Double) editInsuranceForm.get("insuranceValue");
        Integer endDateDay = (Integer) editInsuranceForm.get("endDateDay");
        Integer endDateMonth = (Integer) editInsuranceForm.get("endDateMonth");
        Integer endDateYear = (Integer) editInsuranceForm.get("endDateYear");

        Date endDate = null;

        if ((endDateDay.intValue() > 0) && (endDateMonth.intValue() > 0) && (endDateYear.intValue() > 0)
                && Data.validDate(endDateDay, endDateMonth, endDateYear)) {
            Calendar officialDateCalendar =
                    new GregorianCalendar(endDateYear.intValue(), endDateMonth.intValue(), endDateDay.intValue());
            endDate = officialDateCalendar.getTime();
        } else {
            // invalid date!!
            ActionErrors errors = new ActionErrors();
            errors.add("invalidDate", new ActionError("error.impossible.invalidDate"));
            saveErrors(request, errors);
            return readInsuranceValue(mapping, form, request, response);
        }

        EditInsuranceValueByExecutionYearID.run(executionYearId, insuranceValue, endDate);

        return mapping.findForward("defineSuccess");

    }

    private List buildLabelValueBeanForJsp(List infoExecutionYears) {
        List executionYearLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionYears, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

                LabelValueBean executionYear =
                        new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getExternalId().toString());
                return executionYear;
            }
        }, executionYearLabels);
        return executionYearLabels;
    }

}