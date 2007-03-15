package net.sourceforge.fenixedu.presentationTier.Action.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ClockingsDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkScheduleDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Months;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class AssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward showEmployeeInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        Employee employee = userView.getPerson().getEmployee();
        if (employee == null) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }
        HashMap<String, WorkScheduleDaySheet> workScheduleDays = new HashMap<String, WorkScheduleDaySheet>();
        if (employee.getAssiduousness() != null) {
            if (employee.getAssiduousness().getCurrentSchedule() != null) {
                ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
                        LanguageUtils.getLocale());
                WorkWeek workWeek = new WorkWeek(EnumSet.range(WeekDay.MONDAY, WeekDay.FRIDAY));
                for (WorkSchedule workSchedule : employee.getAssiduousness().getCurrentSchedule()
                        .getWorkSchedules()) {
                    workSchedule.setWorkScheduleDays(workScheduleDays, bundle);
                }
                workWeek.validateWorkScheduleDays(workScheduleDays, bundle);
                List<WorkScheduleDaySheet> workScheduleDaySheetList = new ArrayList<WorkScheduleDaySheet>(
                        workScheduleDays.values());
                Collections.sort(workScheduleDaySheetList, new BeanComparator("weekDay"));
                request.setAttribute("workScheduleDayList", workScheduleDaySheetList);
                request.setAttribute("hasFixedPeriod", employee.getAssiduousness().getCurrentSchedule()
                        .hasFixedPeriod());
            }
        }
        request.setAttribute("employee", employee);
        return mapping.findForward("show-employee-info");
    }

    public ActionForward showClockings(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        Employee employee = userView.getPerson().getEmployee();
        if (employee == null) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }
        YearMonth yearMonth = null;
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        if (viewState != null) {
            yearMonth = (YearMonth) viewState.getMetaObject().getObject();
        }
        if (yearMonth == null) {
            yearMonth = new YearMonth();
            yearMonth.setYear(new YearMonthDay().getYear());
            yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
        } else {
            ActionForward actionForward = verifyYearMonth("show-clockings", request, mapping, yearMonth);
            if (actionForward != null) {
                request.setAttribute("employee", employee);
                return actionForward;
            }
        }

        YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
                yearMonth.getMonth().ordinal() + 1, 01);
        YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
                beginDate.dayOfMonth().getMaximumValue());
        if (employee.getAssiduousness() != null) {
            List<Clocking> clockings = employee.getAssiduousness().getClockings(beginDate, endDate);
            Collections.sort(clockings, AssiduousnessRecord.COMPARATORY_BY_DATE);
            HashMap<YearMonthDay, ClockingsDaySheet> clockingsDaySheetList = new HashMap<YearMonthDay, ClockingsDaySheet>();
            for (Clocking clocking : clockings) {
                if (clockingsDaySheetList.containsKey(clocking.getDate().toYearMonthDay())) {
                    ClockingsDaySheet clockingsDaySheet = clockingsDaySheetList.get(clocking.getDate()
                            .toYearMonthDay());
                    clockingsDaySheet.addClocking(clocking);
                } else {
                    ClockingsDaySheet clockingsDaySheet = new ClockingsDaySheet();
                    clockingsDaySheet.setDate(clocking.getDate().toYearMonthDay());
                    clockingsDaySheet.addClocking(clocking);
                    clockingsDaySheetList.put(clocking.getDate().toYearMonthDay(), clockingsDaySheet);
                }
            }

            List<ClockingsDaySheet> orderedClockings = new ArrayList<ClockingsDaySheet>(
                    clockingsDaySheetList.values());
            Collections.sort(orderedClockings, new BeanComparator("date"));
            request.setAttribute("clockings", orderedClockings);
        }
        request.setAttribute("yearMonth", yearMonth);
        request.setAttribute("employee", employee);
        return mapping.findForward("show-clockings");
    }

    public ActionForward showJustifications(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        Employee employee = userView.getPerson().getEmployee();
        if (employee == null) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }
        YearMonth yearMonth = null;
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        if (viewState != null) {
            yearMonth = (YearMonth) viewState.getMetaObject().getObject();
        }
        if (yearMonth == null) {
            yearMonth = new YearMonth();
            yearMonth.setYear(new YearMonthDay().getYear());
            yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
        } else {
            ActionForward actionForward = verifyYearMonth("show-justifications", request, mapping,
                    yearMonth);
            if (actionForward != null) {
                request.setAttribute("employee", employee);
                return actionForward;
            }
        }

        if (employee.getAssiduousness() != null) {
            YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
                    .ordinal() + 1, 01);
            YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(),
                    yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth().getMaximumValue());
            List<Justification> justifications = new ArrayList<Justification>();
            justifications.addAll(employee.getAssiduousness().getLeaves(beginDate, endDate));
            justifications.addAll(employee.getAssiduousness().getMissingClockings(beginDate, endDate));
            List<Justification> orderedJustifications = new ArrayList<Justification>(justifications);
            Collections.sort(orderedJustifications, AssiduousnessRecord.COMPARATORY_BY_DATE);
            request.setAttribute("justifications", orderedJustifications);
        }
        request.setAttribute("yearMonth", yearMonth);
        request.setAttribute("employee", employee);
        return mapping.findForward("show-justifications");
    }

    public ActionForward showWorkSheet(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        Employee employee = userView.getPerson().getEmployee();
        if (employee == null) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }
        YearMonth yearMonth = null;
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        if (viewState != null) {
            yearMonth = (YearMonth) viewState.getMetaObject().getObject();
        }
        if (yearMonth == null) {
            yearMonth = new YearMonth();
            yearMonth.setYear(new YearMonthDay().getYear());
            yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
        } else {
            ActionForward actionForward = verifyYearMonth("show-work-sheet", request, mapping, yearMonth);
            if (actionForward != null) {
                EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
                employeeWorkSheet.setEmployee(employee);
                request.setAttribute("employeeWorkSheet", employeeWorkSheet);
                return actionForward;
            }
        }

        YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
                yearMonth.getMonth().ordinal() + 1, 01);
        int endDay = beginDate.dayOfMonth().getMaximumValue();
        if (yearMonth.getYear() == new YearMonthDay().getYear()
                && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
            endDay = new YearMonthDay().getDayOfMonth();
            request.setAttribute("displayCurrentDayNote", "true");
        }
        YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
                endDay);

        Object[] args = { employee.getAssiduousness(), beginDate, endDate };
        EmployeeWorkSheet employeeWorkSheet = (EmployeeWorkSheet) ServiceUtils.executeService(userView,
                "ReadEmployeeWorkSheet", args);

        if (yearMonth.getYear() == 2007 && yearMonth.getMonth().equals(Month.FEBRUARY)) {
            verifyFebruaryMultipleMonthBalanceJustification(employee.getAssiduousness(), employeeWorkSheet, request);
        } else if (yearMonth.getYear() == 2007 && yearMonth.getMonth().equals(Month.MARCH)) {
            verifyMarchMultipleMonthBalanceJustification(employee.getAssiduousness(), request);
        }
        
        request.setAttribute("employeeWorkSheet", employeeWorkSheet);
        request.setAttribute("yearMonth", yearMonth);
        return mapping.findForward("show-work-sheet");
    }

    private void verifyMarchMultipleMonthBalanceJustification(Assiduousness assiduousness,
            HttpServletRequest request) {
        if (assiduousness != null) {
            AssiduousnessClosedMonth assiduousnessClosedMonthFeb = assiduousness.getClosedMonth(2);
            if (!assiduousnessClosedMonthFeb.getBalanceToDiscount().isEqual(Duration.ZERO)) {
                AssiduousnessClosedMonth assiduousnessClosedMonthJan = assiduousness.getClosedMonth(1);
                Duration monthsBalance = assiduousnessClosedMonthFeb.getBalance();
                monthsBalance = monthsBalance.plus(assiduousnessClosedMonthJan.getBalance());
                if (monthsBalance.isShorterThan(Duration.ZERO)) {
                    request.setAttribute("hasToCompensateThisMonth", "hasToCompensateThisMonth");
                    return;
                }
            }
        }
    }

    private void verifyFebruaryMultipleMonthBalanceJustification(Assiduousness assiduousness,
            EmployeeWorkSheet employeeWorkSheet, HttpServletRequest request) {
        if (assiduousness != null) {
            AssiduousnessClosedMonth assiduousnessClosedMonth = assiduousness.getClosedMonth(1);
            Duration monthsBalance = assiduousnessClosedMonth.getBalance();
            monthsBalance = monthsBalance.plus(employeeWorkSheet.getTotalBalance());
            if (monthsBalance.isShorterThan(Duration.ZERO)) {
                for (WorkDaySheet workDaySheet : employeeWorkSheet.getWorkDaySheetList()) {
                    if (workDaySheet.hasLeaveType(JustificationType.MULTIPLE_MONTH_BALANCE)) {
                        request.setAttribute("hasToCompensateNextMonth", "hasToCompensateNextMonth");
                        return;
                    }
                }
            }
        }
    }
    
    private ActionForward verifyYearMonth(String returnPath, HttpServletRequest request,
            ActionMapping mapping, YearMonth yearMonth) {
        if (yearMonth.getYear() > new YearMonthDay().getYear()
                || (yearMonth.getYear() == new YearMonthDay().getYear() && yearMonth.getMonth()
                        .compareTo(Month.values()[new YearMonthDay().getMonthOfYear() - 1]) > 0)) {
            saveErrors(request, yearMonth, "error.invalidFutureDate");
            return mapping.findForward(returnPath);
        } else {
            DateTimeFieldType[] typeFields = { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() };
            int[] args = { yearMonth.getYear(), yearMonth.getNumberOfMonth() };
            Partial choosedPartial = new Partial(typeFields, args);
            YearMonthDay today = new YearMonthDay();
            args[0] = today.getYear();
            args[1] = today.getMonthOfYear();
            Partial thisMonthPartial = new Partial(typeFields, args);
            if (Months.monthsBetween(choosedPartial, thisMonthPartial).getMonths() > 1) {
                saveErrors(request, yearMonth, "error.currentMonthAndPrevious");
                return mapping.findForward(returnPath);
            }
        }
        return null;
    }

    private void saveErrors(HttpServletRequest request, YearMonth yearMonth, String message) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("message", new ActionMessage(message));
        saveMessages(request, actionMessages);
        request.setAttribute("yearMonth", yearMonth);
    }
}
