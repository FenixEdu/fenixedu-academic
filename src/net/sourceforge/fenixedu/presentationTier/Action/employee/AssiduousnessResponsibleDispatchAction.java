package net.sourceforge.fenixedu.presentationTier.Action.employee;

import java.io.DataOutputStream;
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
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ClockingsDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.UnitEmployees;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkScheduleDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.YearMonthDay;

public class AssiduousnessResponsibleDispatchAction extends FenixDispatchAction {

    private final YearMonthDay firstMonth = new YearMonthDay(2006, 9, 1);

    public ActionForward showEmployeeList(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final IUserView userView = SessionUtils.getUserView(request);
	final YearMonth yearMonth = getYearMonth(request);
	if (yearMonth == null) {
	    return mapping.getInputForward();
	}
	request.setAttribute("yearMonth", yearMonth);
	YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
		yearMonth.getMonth().ordinal() + 1, 01);
	YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
		beginDate.dayOfMonth().getMaximumValue());
	List<UnitEmployees> unitEmployeesList = new ArrayList<UnitEmployees>();
	for (PersonFunction personFunction : userView.getPerson().getPersonFuntions(beginDate, endDate)) {
	    if (personFunction.getFunction().getFunctionType() == FunctionType.ASSIDUOUSNESS_RESPONSIBLE) {
		List<Employee> employeeList = personFunction.getFunction().getUnit()
			.getAllWorkingEmployees(beginDate, endDate);
		if (!employeeList.isEmpty()) {
		    UnitEmployees unitEmployees = new UnitEmployees();
		    unitEmployees.setUnit(personFunction.getFunction().getUnit());
		    unitEmployees.setEmployeeList(employeeList);
		    Collections.sort(unitEmployees.getEmployeeList(), new BeanComparator(
			    "employeeNumber"));
		    unitEmployeesList.add(unitEmployees);
		}
		for (Unit unit : personFunction.getFunction().getUnit().getAllActiveSubUnits(
			new YearMonthDay())) {
		    employeeList = unit.getAllWorkingEmployees(beginDate, endDate);
		    if (!employeeList.isEmpty()) {
			UnitEmployees unitEmployees = new UnitEmployees();
			unitEmployees.setUnit(unit);
			unitEmployees.setEmployeeList(employeeList);
			Collections.sort(unitEmployees.getEmployeeList(), new BeanComparator(
				"employeeNumber"));
			unitEmployeesList.add(unitEmployees);
		    }
		}
	    }
	}
	unitEmployeesList = filterRepeatedUnits(unitEmployeesList);
	Collections.sort(unitEmployeesList, new BeanComparator("unitCode"));
	request.setAttribute("unitEmployeesList", unitEmployeesList);
	return mapping.findForward("show-employee-list");
    }

    private List<UnitEmployees> filterRepeatedUnits(List<UnitEmployees> unitEmployeesList) {
	List<UnitEmployees> result = new ArrayList<UnitEmployees>();
	for (UnitEmployees unitEmployees : unitEmployeesList) {
	    if (!containsUnit(result, unitEmployees.getUnit())) {
		result.add(unitEmployees);
	    }
	}
	return result;
    }

    private boolean containsUnit(List<UnitEmployees> unitEmployeesList, Unit unit) {
	for (UnitEmployees tempUnitEmployees : unitEmployeesList) {
	    if (tempUnitEmployees.getUnit() == unit) {
		return true;
	    }
	}
	return false;
    }

    public ActionForward showEmployeeWorkSheet(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final IUserView userView = SessionUtils.getUserView(request);
	final Employee employee = getEmployee(request);
	if (employee == null) {
	    return mapping.findForward("show-clockings");
	}
	final YearMonth yearMonth = getYearMonth(request);
	if (yearMonth == null) {
	    EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
	    employeeWorkSheet.setEmployee(employee);
	    request.setAttribute("employeeWorkSheet", employeeWorkSheet);
	    return mapping.findForward("show-employee-work-sheet");
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
	EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
	employeeWorkSheet.setEmployee(employee);
	if (employee.getAssiduousness() != null) {
	    try {
		Object[] args = { employee.getAssiduousness(), beginDate, endDate };
		employeeWorkSheet = (EmployeeWorkSheet) ServiceUtils.executeService(userView,
			"ReadAssiduousnessResponsibleWorkSheet", args);
		request.setAttribute("employeeWorkSheet", employeeWorkSheet);
	    } catch (NotAuthorizedFilterException e) {
		saveErrors(request, "error.notAuthorized");
		return mapping.findForward("show-employee-work-sheet");
	    }
	}
	request.setAttribute("employeeWorkSheet", employeeWorkSheet);
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("show-employee-work-sheet");
    }

    public ActionForward showClockings(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final Employee employee = getEmployee(request);
	if (employee == null) {
	    return mapping.findForward("show-clockings");
	}
	final YearMonth yearMonth = getYearMonth(request);
	if (yearMonth == null) {
	    return mapping.findForward("show-clockings");
	}

	YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
		yearMonth.getMonth().ordinal() + 1, 01);
	YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
		beginDate.dayOfMonth().getMaximumValue());
	if (employee.getAssiduousness() != null) {
	    List<Clocking> clockings = employee.getAssiduousness().getClockingsAndAnulatedClockings(
		    beginDate, endDate);
	    Collections.sort(clockings, AssiduousnessRecord.COMPARATOR_BY_DATE);
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
	final Employee employee = getEmployee(request);
	if (employee == null) {
	    return mapping.findForward("show-justifications");
	}
	final YearMonth yearMonth = getYearMonth(request);
	if (yearMonth == null) {
	    return mapping.findForward("show-justifications");
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
	    Collections.sort(orderedJustifications, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    request.setAttribute("justifications", orderedJustifications);
	}
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-justifications");
    }

    public ActionForward showSchedule(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final Employee employee = getEmployee(request);
	if (employee == null) {
	    return mapping.findForward("show-schedule");
	}
	final YearMonth yearMonth = getYearMonth(request);
	if (yearMonth == null) {
	    return mapping.findForward("show-schedule");
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
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("show-schedule");
    }

    public ActionForward showVacations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final Employee employee = getEmployee(request);
	if (employee == null) {
	    return mapping.findForward("show-vacations");
	}
	final YearMonth yearMonth = getYearMonth(request);
	if (yearMonth == null) {
	    return mapping.findForward("show-vacations");
	}

	if (employee.getAssiduousness() != null) {
	    request.setAttribute("vacations", employee.getAssiduousness()
		    .getAssiduousnessVacationsByYear(yearMonth.getYear()));
	}
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-vacations");
    }

    private YearMonth getYearMonth(HttpServletRequest request) {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    String year = request.getParameter("year");
	    String month = request.getParameter("month");
	    if (StringUtils.isEmpty(year) || StringUtils.isEmpty(month)) {
		yearMonth = new YearMonth();
		yearMonth.setYear(new YearMonthDay().getYear());
		yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
	    } else {
		yearMonth = new YearMonth();
		yearMonth.setYear(new Integer(year));
		yearMonth.setMonth(Month.valueOf(month));
	    }
	}
	if (yearMonth.getYear() > new YearMonthDay().getYear()
		|| (yearMonth.getYear() == new YearMonthDay().getYear() && yearMonth.getMonth()
			.compareTo(Month.values()[new YearMonthDay().getMonthOfYear() - 1]) > 0)) {
	    saveErrors(request, "error.invalidFutureDate");
	    yearMonth = new YearMonth();
	    yearMonth.setYear(new YearMonthDay().getYear());
	    yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
	    request.setAttribute("yearMonth", yearMonth);
	    return null;
	} else if (yearMonth.getYear() < firstMonth.getYear()
		|| (yearMonth.getYear() == firstMonth.getYear() && yearMonth.getMonth()
			.getNumberOfMonth() < firstMonth.getMonthOfYear())) {
	    final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources",
		    LanguageUtils.getLocale());
	    saveErrors(request, "error.invalidDateBefore", new Object[] {
		    bundle.getString(Month.values()[firstMonth.getMonthOfYear() - 1].toString()),
		    new Integer(firstMonth.getYear()).toString() });
	    request.setAttribute("yearMonth", yearMonth);
	    return null;
	}
	return yearMonth;
    }

    private Employee getEmployee(HttpServletRequest request) {
	Integer employeeNumber = new Integer(request.getParameter("employeeNumber"));
	Employee employee = Employee.readByNumber(employeeNumber);
	if (employee == null || employee.getAssiduousness() == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
	    saveMessages(request, actionMessages);
	    return null;
	}
	return employee;
    }

    private void saveErrors(HttpServletRequest request, String message) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("message", new ActionMessage(message));
	saveMessages(request, actionMessages);
    }

    private void saveErrors(HttpServletRequest request, String message, Object[] args) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("message", new ActionMessage(message, args));
	saveMessages(request, actionMessages);
    }

    public ActionForward showPhoto(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	Integer personID = new Integer(request.getParameter("personID"));
	Party party = rootDomainObject.readPartyByOID(personID);
	if (party.isPerson()) {
	    Person person = (Person) party;
	    FileEntry personalPhoto = person.getPersonalPhoto();
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

}
