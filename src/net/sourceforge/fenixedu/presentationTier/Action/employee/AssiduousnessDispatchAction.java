package net.sourceforge.fenixedu.presentationTier.Action.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.DailyBalance;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class AssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward showEmployeeInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        Employee employee = userView.getPerson().getEmployee();
        if (employee == null) {
            // erro
            System.out.println("não existe empregado");
            return mapping.findForward("index");
        }
        if (employee.getAssiduousness() != null) {
            request.setAttribute("schedule", employee.getAssiduousness().getCurrentSchedule());
        }
        return mapping.findForward("show-employee-info");
    }

    public ActionForward showClockings(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        Employee employee = userView.getPerson().getEmployee();
        if (employee == null) {
            // erro
            System.out.println("não existe empregado");
            return mapping.findForward("index");
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
        YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
                beginDate.dayOfMonth().getMaximumValue());
        if (employee.getAssiduousness() != null) {
            List<Clocking> clockings = employee.getAssiduousness().getClockings(beginDate, endDate);
            List<Clocking> orderedClockings = new ArrayList<Clocking>(clockings);
            Collections.sort(orderedClockings, new BeanComparator("date"));
            request.setAttribute("clockings", orderedClockings);
        }
        request.setAttribute("yearMonth", yearMonth);
        return mapping.findForward("show-clockings");
    }

    public ActionForward showJustifications(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        Employee employee = userView.getPerson().getEmployee();
        if (employee == null) {
            // erro
            System.out.println("não existe empregado");
            return mapping.findForward("index");
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
        if (employee.getAssiduousness() != null) {
            YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
                    .ordinal() + 1, 01);
            YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(),
                    yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth().getMaximumValue());
            List<Leave> justificationsLeaves = employee.getAssiduousness().getLeaves(beginDate, endDate);
            List<Leave> orderedJustifications = new ArrayList<Leave>(justificationsLeaves);
            Collections.sort(orderedJustifications, new BeanComparator("date"));
            request.setAttribute("leaves", orderedJustifications);
        }
        request.setAttribute("yearMonth", yearMonth);
        return mapping.findForward("show-justifications");
    }

    public ActionForward showWorkSheet(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        Employee employee = userView.getPerson().getEmployee();
        if (employee == null) {
            // erro
            System.out.println("não existe empregado");
            return mapping.findForward("index");
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
        return mapping.findForward("show-work-sheet");
    }

    protected List<WorkDaySheet> getWorkDaySheet(Assiduousness assiduousness, YearMonthDay beginDate,
            YearMonthDay endDate, HttpServletRequest request) {
        List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();

        int maxClockingColumns = 0;
        Duration totalBalance = new Duration(0);
        Duration totalUnjustified = new Duration(0);
        for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            WorkDaySheet workDaySheet = new WorkDaySheet();
            workDaySheet.setDate(thisDay);
            // workDaySheet.setBalanceTime();
            // workDaySheet.setUnjustifiedTime();
            // workDaySheet.setWorkScheduleAcronym();
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

                        if (clockings.size() > maxClockingColumns) {
                            maxClockingColumns = clockings.size();
                        }

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
                        }
                        workDaySheet.setNotes(notes);
                        DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                        workDaySheet
                                .setBalanceTime(dailyBalance.getNormalWorkPeriodBalance().toPeriod());
                        totalBalance = totalBalance.plus(dailyBalance.getNormalWorkPeriodBalance());
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
                        totalBalance = totalBalance.plus(dailyBalance.getTotalBalance());
                        workDaySheet.setNotes(notes);
                        for (AssiduousnessRecord assiduousnessRecord : clockings) {
                            workDaySheet.addClockings(assiduousnessRecord.getDate().toTimeOfDay());
                        }
                        if (isDayHoliday) {
                            ResourceBundle bundle = ResourceBundle
                                    .getBundle("resources.EmployeeResources");
                            workDaySheet.setWorkScheduleAcronym(bundle.getString("label.holiday"));
                        } else {
                            ResourceBundle bundle = ResourceBundle
                                    .getBundle("resources.EnumerationResources");
                            workDaySheet.setWorkScheduleAcronym(bundle.getString(WeekDay
                                    .fromJodaTimeToWeekDay(thisDay.toDateTimeAtMidnight()).toString()));
                        }
                        workDaySheet.setUnjustifiedTime(new Duration(0));
                        workSheet.add(workDaySheet);
                    }
                }
            }
        }
        WorkDaySheet workDaySheet = new WorkDaySheet();
        workDaySheet.setBalanceTime(totalBalance.toPeriod());
        workDaySheet.setUnjustifiedTime(totalUnjustified);
        workSheet.add(workDaySheet);

        request.setAttribute("maxClockingColumns", maxClockingColumns == 0 ? 1 : maxClockingColumns);
        return workSheet;
    }
}
