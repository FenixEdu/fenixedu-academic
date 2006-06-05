package net.sourceforge.fenixedu.presentationTier.Action.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
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

    public ActionForward prepareChooseDate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        String action = (String) request.getParameter("action");
        System.out.println("---------------> " + action);
        YearMonth yearMonth = new YearMonth();
        yearMonth.setYear(new YearMonthDay().getYear());
        yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
        request.setAttribute("yearMonth", yearMonth);
        request.setAttribute("action", action);
        return mapping.findForward("choose-date");
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
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        YearMonth yearMonth = (YearMonth) viewState.getMetaObject().getObject();

        if (yearMonth.getYear() > new YearMonthDay().getYear()) {
            // erro - não pode ver nos anos futuros
            System.out.println("erro - não pode ver nos anos futuros");
        }
        System.out.println();

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
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        YearMonth yearMonth = (YearMonth) viewState.getMetaObject().getObject();

        if (yearMonth.getYear() > new YearMonthDay().getYear()) {
            // erro - não pode ver nos anos futuros
            System.out.println("erro - não pode ver nos anos futuros");
        }
        System.out.println();
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
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        YearMonth yearMonth = (YearMonth) viewState.getMetaObject().getObject();

        YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
                yearMonth.getMonth().ordinal() + 1, 01);
        int endDay = beginDate.dayOfMonth().getMaximumValue();
        if (yearMonth.getYear() > new YearMonthDay().getYear()) {
            // erro - não pode ver nos anos futuros
            System.out.println("erro - não pode ver nos anos futuros");
        } else if (yearMonth.getYear() == new YearMonthDay().getYear()
                && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
            endDay = new YearMonthDay().getDayOfMonth();
        }

        YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
                endDay);
        request.setAttribute("workSheet", getWorkDaySheet(employee.getAssiduousness(), beginDate,
                endDate, request));
        return mapping.findForward("show-work-sheet");
    }

    protected List<WorkDaySheet> getWorkDaySheet(Assiduousness assiduousness, YearMonthDay beginDate,
            YearMonthDay endDate, HttpServletRequest request) {
        List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();

        int maxClockingColumns = 0;
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
                if (schedule == null) {
                    workDaySheet.setNotes(notes);
                    workDaySheet.setWorkScheduleAcronym("");
                    workDaySheet.setBalanceTime(new Period(0));
                    workDaySheet.setUnjustifiedTime(new Duration(0));
                    workSheet.add(workDaySheet);
                } else {
                    WorkSchedule workSchedule = schedule.workScheduleWithDate(thisDay);
                    if (workSchedule != null) {
                        DateTime init = thisDay.toDateTime(workSchedule.getWorkScheduleType()
                                .getWorkTime());
                        DateTime end = thisDay.toDateTime(workSchedule.getWorkScheduleType()
                                .getWorkEndTime());
                        if (workSchedule.getWorkScheduleType().isNextDay()) {
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

                        List<Leave> leaves = new ArrayList<Leave>(assiduousness.getLeaves(thisDay,
                                thisDay));
                        Collections.sort(leaves, new BeanComparator("date"));
                        for (Leave leave : leaves) {
                            if (notes.length() != 0) {
                                notes.concat("/");
                            }
                            notes.concat(leave.getJustificationMotive().getAcronym());
                        }
                        workDaySheet.setNotes(notes);
                        DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                        workDaySheet
                                .setBalanceTime(dailyBalance.getNormalWorkPeriodBalance().toPeriod());
                        workDaySheet.setUnjustifiedTime(dailyBalance.getFixedPeriodAbsence());
                        workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType()
                                .getAcronym());
                        workSheet.add(workDaySheet);
                    } else {
                        workDaySheet.setNotes(notes);
                        workDaySheet.setWorkScheduleAcronym("");
                        workDaySheet.setBalanceTime(new Period(0));
                        workDaySheet.setUnjustifiedTime(new Duration(0));
                        workSheet.add(workDaySheet);
                    }
                }
            }
        }
        request.setAttribute("maxClockingColumns", maxClockingColumns == 0 ? 1 : maxClockingColumns);
        return workSheet;
    }
}
