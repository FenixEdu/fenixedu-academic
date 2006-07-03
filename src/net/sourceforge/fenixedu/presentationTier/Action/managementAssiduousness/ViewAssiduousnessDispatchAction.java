package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.DailyBalance;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class ViewAssiduousnessDispatchAction extends FenixDispatchAction {
    public ActionForward chooseEmployee(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        String action = request.getParameter("action");
        request.setAttribute("action", action);
        return mapping.findForward("choose-employee");
    }

    public ActionForward showWorkSheet(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final Integer employeeNumber = new Integer(((DynaActionForm) form).getString("employeeNumber"));

        Employee employee = Employee.readByNumber(employeeNumber);
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
        } else if (yearMonth.getYear() > new YearMonthDay().getYear()) {
            // erro - não pode ver nos anos futuros
            System.out.println("erro - não pode ver nos anos futuros");
        }

        YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
                yearMonth.getMonth().ordinal() + 1, 01);
        int endDay = beginDate.dayOfMonth().getMaximumValue();
        if (yearMonth.getYear() == new YearMonthDay().getYear()
                && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
            endDay = new YearMonthDay().getDayOfMonth();
        }

        YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
                endDay);
        request.setAttribute("workSheet", getWorkDaySheet(employee.getAssiduousness(), beginDate,
                endDate, request));
        request.setAttribute("yearMonth", yearMonth);
        request.setAttribute("employee", employee);
        return mapping.findForward("show-employee-work-sheet");
    }

    protected List<WorkDaySheet> getWorkDaySheet(Assiduousness assiduousness, YearMonthDay beginDate,
            YearMonthDay endDate, HttpServletRequest request) {
        List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();
        YearMonthDay today = new YearMonthDay();
        Duration totalBalance = new Duration(0);
        Duration totalUnjustified = new Duration(0);
        HashMap<String, String> subtitles = new HashMap<String, String>();
        for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            WorkDaySheet workDaySheet = new WorkDaySheet();
            workDaySheet.setDate(thisDay);
            String notes = "";
            if (assiduousness != null) {

                Schedule schedule = assiduousness.getSchedule(thisDay);
                boolean isDayHoliday = assiduousness.isHoliday(thisDay);

                if (schedule == null) {
                    workDaySheet.setNotes(notes);
                    workDaySheet.setBalanceTime(new Period(0));
                    workDaySheet.setUnjustifiedTime(new Duration(0));
                    workSheet.add(workDaySheet);
                } else {
                    WorkSchedule workSchedule = schedule.workScheduleWithDate(thisDay);
                    if (workSchedule != null && !isDayHoliday) {
                        DateTime init = thisDay.toDateTime(workSchedule.getWorkScheduleType()
                                .getWorkTime());
                        DateTime end = thisDay.toDateTime(workSchedule.getWorkScheduleType()
                                .getWorkEndTime());
                        if (workSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                            end = end.plusDays(1);
                        }

                        List<AssiduousnessRecord> clockings = assiduousness
                                .getClockingsAndMissingClockings(init, end);

                        Collections.sort(clockings, new BeanComparator("date"));
                        for (AssiduousnessRecord assiduousnessRecord : (List<AssiduousnessRecord>) clockings) {
                            workDaySheet.addClockings(assiduousnessRecord.getDate().toTimeOfDay());
                        }

                        List<Leave> leaves = new ArrayList<Leave>(assiduousness.getLeaves(thisDay));
                        Collections.sort(leaves, new BeanComparator("date"));
                        for (Leave leave : leaves) {
                            if (notes.length() != 0) {
                                notes = notes.concat(" / ");
                            }
                            notes = notes.concat(leave.getJustificationMotive().getAcronym());
                            putSubtitle(subtitles, leave.getJustificationMotive());
                        }
                        workDaySheet.setNotes(notes);
                        DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                        workDaySheet
                                .setBalanceTime(dailyBalance.getNormalWorkPeriodBalance().toPeriod());
                        if (!thisDay.equals(today)
                                || (thisDay.equals(today) && !workDaySheet.getBalanceTime().equals(
                                        Duration.ZERO.minus(
                                                workSchedule.getWorkScheduleType().getNormalWorkPeriod()
                                                        .getWorkPeriodDuration()).toPeriod()))) {
                            totalBalance = totalBalance.plus(dailyBalance.getNormalWorkPeriodBalance());
                        }
                        workDaySheet.setUnjustifiedTime(dailyBalance.getFixedPeriodAbsence());
                        totalUnjustified = totalUnjustified.plus(workDaySheet.getUnjustifiedTime());
                        workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType()
                                .getAcronym());
                        workSheet.add(workDaySheet);
                    } else {
                        DateTime init = thisDay.toDateTime(new TimeOfDay(7, 30, 0, 0));
                        DateTime end = thisDay.toDateTime(new TimeOfDay(23, 59, 59, 99));
                        List<AssiduousnessRecord> clockings = assiduousness
                                .getClockingsAndMissingClockings(init, end);
                        Collections.sort(clockings, new BeanComparator("date"));
                        DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                        workDaySheet.setBalanceTime(dailyBalance.getTotalBalance().toPeriod());
                        if (!thisDay.equals(today)
                                || (thisDay.equals(today) && !workDaySheet.getBalanceTime().equals(
                                        Duration.ZERO.minus(
                                                workSchedule.getWorkScheduleType().getNormalWorkPeriod()
                                                        .getWorkPeriodDuration()).toPeriod()))) {
                            totalBalance = totalBalance.plus(dailyBalance.getTotalBalance());
                        }
                        workDaySheet.setNotes(notes);
                        for (AssiduousnessRecord assiduousnessRecord : clockings) {
                            workDaySheet.addClockings(assiduousnessRecord.getDate().toTimeOfDay());
                        }
                        if (isDayHoliday) {
                            ResourceBundle bundle = ResourceBundle
                                    .getBundle("resources.AssiduousnessResources");
                            workDaySheet.setWorkScheduleAcronym(bundle.getString("label.holiday"));
                        }
                        workDaySheet.setUnjustifiedTime(new Duration(0));
                        workSheet.add(workDaySheet);
                    }
                }
            }
        }
        PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
                .appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
        MutablePeriod finalTotalBalance = new MutablePeriod(totalBalance.getMillis());
        if (totalBalance.toPeriod().getMinutes() < 0) {
            finalTotalBalance.setMinutes(-totalBalance.toPeriod().getMinutes());
        }
        request.setAttribute("totalBalance", fmt.print(finalTotalBalance));
        request.setAttribute("totalUnjustified", fmt.print(totalUnjustified.toPeriod()));
        request.setAttribute("subtitles", subtitles.values());
        return workSheet;
    }

    private void putSubtitle(HashMap<String, String> subtitles, JustificationMotive justificationMotive) {
        if (subtitles.get(justificationMotive.getAcronym()) == null) {
            subtitles.put(justificationMotive.getAcronym(), justificationMotive.getAcronym() + " - "
                    + justificationMotive.getDescription());
        }
    }

}
