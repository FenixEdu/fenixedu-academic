package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ClockingsDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeBalanceResume;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeMonthyBalanceResume;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeScheduleFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.security.UserView;

public class ViewEmployeeAssiduousnessDispatchAction extends FenixDispatchAction {
    public ActionForward chooseEmployee(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	String action = request.getParameter("action");
	request.setAttribute("action", action);
	request.setAttribute("yearMonth", getYearMonth(request, null));
	return mapping.findForward("choose-employee");
    }

    public ActionForward showWorkSheet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final IUserView userView = UserView.getUser();
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);
	if (yearMonth == null) {
	    return mapping.findForward("show-employee-work-sheet");
	}
	LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	int endDay = beginDate.dayOfMonth().getMaximumValue();
	if (yearMonth.getYear() == new LocalDate().getYear()
		&& yearMonth.getMonth().ordinal() + 1 == new LocalDate().getMonthOfYear()) {
	    // endDay = new LocalDate().getDayOfMonth();
	    request.setAttribute("displayCurrentDayNote", "true");
	}
	LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, endDay);

	Object[] args = { employee.getAssiduousness(), beginDate, endDate };
	EmployeeWorkSheet employeeWorkSheet = (EmployeeWorkSheet) ServiceUtils.executeService("ReadEmployeeWorkSheet", args);

	request.setAttribute("employeeWorkSheet", employeeWorkSheet);
	setEmployeeStatus(request, employee, beginDate, endDate);
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("show-employee-work-sheet");
    }

    public ActionForward showSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);

	List<Schedule> schedules = new ArrayList<Schedule>(employee.getAssiduousness().getSchedules());
	if (!schedules.isEmpty()) {
	    Collections.sort(schedules, new BeanComparator("beginDate"));
	}

	Schedule choosenSchedule = null;
	Integer scheduleID = getIntegerFromRequest(request, "scheduleID");
	if (scheduleID != null) {
	    for (Schedule schedule : schedules) {
		if (schedule.getIdInternal().equals(scheduleID)) {
		    choosenSchedule = schedule;
		    break;
		}
	    }
	}
	request.setAttribute("scheduleList", schedules);
	EmployeeScheduleFactory employeeScheduleFactory = new EmployeeScheduleFactory(employee, null, choosenSchedule);
	request.setAttribute("employeeScheduleBean", employeeScheduleFactory);
	if (yearMonth != null) {
	    LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	    LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth()
		    .getMaximumValue());
	    setEmployeeStatus(request, employee, beginDate, endDate);
	    request.setAttribute("yearMonth", yearMonth);
	    request.setAttribute("employee", employee);
	}
	return mapping.findForward("show-schedule");
    }

    public ActionForward showClockings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);
	if (yearMonth == null) {
	    return mapping.findForward("show-clockings");
	}

	LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth()
		.getMaximumValue());
	if (employee.getAssiduousness() != null) {
	    List<Clocking> clockings = employee.getAssiduousness().getClockingsAndAnulatedClockings(beginDate, endDate);
	    Collections.sort(clockings, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    HashMap<LocalDate, ClockingsDaySheet> clockingsDaySheetList = new HashMap<LocalDate, ClockingsDaySheet>();
	    for (Clocking clocking : clockings) {
		if (clockingsDaySheetList.containsKey(clocking.getDate().toLocalDate())) {
		    ClockingsDaySheet clockingsDaySheet = clockingsDaySheetList.get(clocking.getDate().toLocalDate());
		    clockingsDaySheet.addClocking(clocking);
		} else {
		    ClockingsDaySheet clockingsDaySheet = new ClockingsDaySheet();
		    clockingsDaySheet.setDate(clocking.getDate().toLocalDate());
		    clockingsDaySheet.addClocking(clocking);
		    clockingsDaySheetList.put(clocking.getDate().toLocalDate(), clockingsDaySheet);
		}
	    }

	    List<ClockingsDaySheet> orderedClockings = new ArrayList<ClockingsDaySheet>(clockingsDaySheetList.values());
	    Collections.sort(orderedClockings, new BeanComparator("date"));
	    request.setAttribute("clockings", orderedClockings);
	}
	setEmployeeStatus(request, employee, beginDate, endDate);
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-clockings");
    }

    public ActionForward showJustifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);
	if (yearMonth == null) {
	    return mapping.findForward("show-justifications");
	}

	if (employee.getAssiduousness() != null) {
	    LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	    LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth()
		    .getMaximumValue());
	    List<Justification> justifications = new ArrayList<Justification>();
	    justifications.addAll(employee.getAssiduousness().getLeaves(beginDate, endDate));
	    justifications.addAll(employee.getAssiduousness().getMissingClockings(beginDate, endDate));
	    List<Justification> orderedJustifications = new ArrayList<Justification>(justifications);
	    Collections.sort(orderedJustifications, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    request.setAttribute("justifications", orderedJustifications);
	    setEmployeeStatus(request, employee, beginDate, endDate);
	}
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-justifications");
    }

    public ActionForward showVacations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);
	if (yearMonth == null) {
	    return mapping.findForward("show-vacations");
	}

	if (employee.getAssiduousness() != null) {
	    LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	    LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth()
		    .getMaximumValue());
	    request.setAttribute("vacations", employee.getAssiduousness().getAssiduousnessVacationsByYear(yearMonth.getYear()));
	    setEmployeeStatus(request, employee, beginDate, endDate);
	}
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-vacations");
    }

    public ActionForward showStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);
	if (yearMonth == null) {
	    return mapping.findForward("show-status");
	}

	if (employee.getAssiduousness() != null) {
	    List<AssiduousnessStatusHistory> employeeStatusList = new ArrayList<AssiduousnessStatusHistory>(employee
		    .getAssiduousness().getAssiduousnessStatusHistories());
	    Collections.sort(employeeStatusList, new BeanComparator("beginDate"));
	    request.setAttribute("statusList", employeeStatusList);
	    // setEmployeeStatus(request, employee, beginDate, endDate);
	}
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-status");
    }

    public ActionForward showPhoto(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer personID = new Integer(request.getParameter("personID"));
	Party party = rootDomainObject.readPartyByOID(personID);
	if (party.isPerson()) {
	    Person person = (Person) party;
	    Photograph personalPhoto = person.getPersonalPhoto();
	    if (personalPhoto != null) {
		try {
		    response.setContentType(personalPhoto.getContentType().getMimeType());
		    DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		    dos.write(personalPhoto.getContents());
		    dos.close();
		} catch (java.io.IOException e) {
		    throw new FenixActionException(e);
		}
	    }
	}
	return null;
    }

    public ActionForward showBalanceResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	EmployeeMonthyBalanceResume employeeMonthyBalanceResume = new EmployeeMonthyBalanceResume(employee);
	YearMonth yearMonth = getYearMonth(request, employee);
	if (yearMonth == null) {
	    request.setAttribute("employeeMonthyBalanceResume", employeeMonthyBalanceResume);
	    return mapping.findForward("show-balance-resume");
	}

	if (employee.getAssiduousness() != null) {
	    List<EmployeeBalanceResume> employeeBalanceResumeList = new ArrayList<EmployeeBalanceResume>();

	    LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	    LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth()
		    .getMaximumValue());

	    List<AssiduousnessStatusHistory> assiduousnessStatusHistoryList = employee.getAssiduousness().getStatusBetween(
		    beginDate, endDate);
	    final IUserView userView = UserView.getUser();
	    ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	    for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistoryList) {
		EmployeeBalanceResume employeeBalanceResume = new EmployeeBalanceResume();
		if (closedMonth != null) {
		    AssiduousnessClosedMonth assiduosunessClosedMonth = closedMonth
			    .getAssiduousnessClosedMonth(assiduousnessStatusHistory);
		    employeeBalanceResume.setEmployeeBalanceResume(assiduosunessClosedMonth);
		} else {
		    Object[] args = { employee.getAssiduousness(), beginDate, endDate };
		    EmployeeWorkSheet employeeWorkSheet = (EmployeeWorkSheet) ServiceUtils.executeService(
			    "ReadEmployeeWorkSheet", args);
		    employeeBalanceResume.setEmployeeBalanceResume(employeeWorkSheet.getTotalBalance() == null ? Duration.ZERO
			    : employeeWorkSheet.getTotalBalance(),
			    employeeWorkSheet.getBalanceToCompensate() == null ? Duration.ZERO : employeeWorkSheet
				    .getBalanceToCompensate(), yearMonth.getPartial(), assiduousnessStatusHistory);
		    request.setAttribute("employeeBalanceResume", employeeBalanceResume);
		}
		employeeBalanceResumeList.add(employeeBalanceResume);
	    }
	    employeeMonthyBalanceResume.setEmployeeBalanceResumeList(employeeBalanceResumeList);
	    request.setAttribute("employeeMonthyBalanceResume", employeeMonthyBalanceResume);
	    setEmployeeStatus(request, employee, beginDate, endDate);
	}
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("show-balance-resume");
    }

    private ActionForward validateEmployee(ActionMapping mapping, HttpServletRequest request, final Employee employee) {
	if (employee == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
	    saveMessages(request, actionMessages);
	    return mapping.getInputForward();
	} else if (employee.getAssiduousness() == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidEmployeeAssiduousness"));
	    saveMessages(request, actionMessages);
	    return mapping.getInputForward();
	}
	return null;
    }

    private Employee getEmployee(HttpServletRequest request, DynaActionForm form) {
	Integer employeeNumber = null;
	String employeeNumberString = form.getString("employeeNumber");
	if (StringUtils.isEmpty(employeeNumberString)) {
	    Object number = getFromRequest(request, "employeeNumber");
	    employeeNumber = number instanceof String ? new Integer((String) number) : (Integer) number;
	} else {
	    employeeNumber = new Integer(employeeNumberString);
	}
	return Employee.readByNumber(employeeNumber);
    }

    private YearMonth getYearMonth(HttpServletRequest request, Employee employee) {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    yearMonth = (YearMonth) request.getAttribute("yearMonth");
	}
	if (yearMonth == null) {
	    yearMonth = new YearMonth();
	    String year = request.getParameter("year");
	    String month = request.getParameter("month");

	    if (StringUtils.isEmpty(year) || StringUtils.isEmpty(month)) {
		ClosedMonth lastClosedMonth = ClosedMonth.getLastMonthClosed();
		if (lastClosedMonth != null) {
		    yearMonth = new YearMonth(lastClosedMonth.getClosedYearMonth().get(DateTimeFieldType.year()), lastClosedMonth
			    .getClosedYearMonth().get(DateTimeFieldType.monthOfYear()));
		    yearMonth.addMonth();
		} else {
		    yearMonth = new YearMonth(new LocalDate());
		}
	    } else {
		yearMonth.setYear(new Integer(year));
		yearMonth.setMonth(Month.valueOf(month));
	    }
	}
	if (yearMonth.getYear() < 2006) {
	    saveErrors(request, employee, yearMonth, "error.invalidPastDateNoData");
	    return null;
	}
	return yearMonth;
    }

    private void saveErrors(HttpServletRequest request, Employee employee, YearMonth yearMonth, String message) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("message", new ActionMessage(message));
	saveMessages(request, actionMessages);
	request.setAttribute("yearMonth", yearMonth);
	LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth()
		.getMaximumValue());
	if (employee != null) {
	    EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
	    employeeWorkSheet.setEmployee(employee);
	    request.setAttribute("employee", employee);
	    request.setAttribute("employeeWorkSheet", employeeWorkSheet);
	    setEmployeeStatus(request, employee, beginDate, endDate);
	}
    }

    private void setEmployeeStatus(HttpServletRequest request, final Employee employee, LocalDate beginDate, LocalDate endDate) {
	List<AssiduousnessStatusHistory> employeeStatusList = employee.getAssiduousness().getStatusBetween(beginDate, endDate);
	Collections.sort(employeeStatusList, new BeanComparator("beginDate"));
	request.setAttribute("employeeStatusList", employeeStatusList);
    }

}