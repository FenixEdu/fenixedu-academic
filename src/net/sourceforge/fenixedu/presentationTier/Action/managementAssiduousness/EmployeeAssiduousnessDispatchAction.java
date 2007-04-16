package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeScheduleFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkWeekScheduleBean;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.RegularizationMonthFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.EmployeeAnulateJustificationFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.EmployeeJustificationFactoryCreator;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.EmployeeJustificationFactoryEditor;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.SeveralEmployeeJustificationFactoryCreator;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.file.FileManagerException;

public class EmployeeAssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateMissingClockingMonth(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {

	YearMonth yearMonth = null;
	String dateString = request.getParameter("date");
	RegularizationMonthFactory regularizationMonthFactory = (RegularizationMonthFactory) getFactoryObject();
	if (regularizationMonthFactory != null) {
	    // RenderUtils.invalidateViewState();
	    yearMonth = regularizationMonthFactory.getYearMonth();
	} else {
	    Employee employee = Employee.readByNumber(new Integer(getFromRequest(request,
		    "employeeNumber").toString()));
	    YearMonthDay date = null;
	    if (!StringUtils.isEmpty(dateString)) {
		date = new YearMonthDay(dateString);
	    }
	    yearMonth = getYearMonth(request, date);
	    regularizationMonthFactory = new RegularizationMonthFactory(yearMonth, employee
		    .getAssiduousness(), SessionUtils.getUserView(request).getPerson().getEmployee());
	}
	request.setAttribute("regularizationMonthFactory", regularizationMonthFactory);
	request.setAttribute("yearMonth", yearMonth);
	if (StringUtils.isEmpty(dateString)) {
	    new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form, request,
		    response);
	    return mapping.findForward("create-missing-clocking-month");
	}
	return new ViewEmployeeAssiduousnessDispatchAction().showWorkSheet(mapping, form, request,
		response);
    }

    public ActionForward createMissingClockingMonth(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {

	RegularizationMonthFactory regularizationMonthFactory = (RegularizationMonthFactory) getFactoryObject();
	executeService(request, "ExecuteFactoryMethod", new Object[] { regularizationMonthFactory });
	request.setAttribute("employeeNumber", regularizationMonthFactory.getAssiduousness()
		.getEmployee().getEmployeeNumber());
	request.setAttribute("yearMonth", regularizationMonthFactory.getYearMonth());
	return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form, request,
		response);
    }

    public ActionForward prepareCreateEmployeeJustification(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {

	Employee employee = Employee.readByNumber(new Integer(getFromRequest(request, "employeeNumber")
		.toString()));
	String dateString = request.getParameter("date");
	YearMonthDay date = null;
	if (!StringUtils.isEmpty(dateString)) {
	    date = new YearMonthDay(dateString);
	}
	YearMonth yearMonth = getYearMonth(request, date);
	EmployeeJustificationFactoryCreator employeeJustificationFactory = new EmployeeJustificationFactoryCreator(
		employee, date, EmployeeJustificationFactory.CorrectionType.valueOf(request
			.getParameter("correction")), yearMonth);
	request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
	request.setAttribute("yearMonth", yearMonth);
	if (StringUtils.isEmpty(dateString)) {
	    return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form,
		    request, response);
	}
	return new ViewEmployeeAssiduousnessDispatchAction().showWorkSheet(mapping, form, request,
		response);
    }

    public ActionForward prepareEditEmployeeJustification(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	Justification justification = (Justification) rootDomainObject
		.readAssiduousnessRecordByOID(new Integer(getFromRequest(request, "idInternal")
			.toString()));
	EmployeeJustificationFactoryEditor employeeJustificationFactory = new EmployeeJustificationFactoryEditor(
		justification, getYearMonth(request, null));
	request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
	request.setAttribute("yearMonth", employeeJustificationFactory.getYearMonth());
	request.setAttribute("employeeNumber", employeeJustificationFactory.getEmployee()
		.getEmployeeNumber());
	return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form, request,
		response);
    }

    public ActionForward chooseJustificationMotivePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	EmployeeJustificationFactory employeeJustificationFactory = (EmployeeJustificationFactory) getFactoryObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("yearMonth", employeeJustificationFactory.getYearMonth());
	request.setAttribute("employeeNumber", employeeJustificationFactory.getEmployee()
		.getEmployeeNumber());
	request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
	if (employeeJustificationFactory.getDate() == null) {
	    return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, actionForm,
		    request, response);
	}
	return new ViewEmployeeAssiduousnessDispatchAction().showWorkSheet(mapping, actionForm, request,
		response);
    }

    public ActionForward editEmployeeJustification(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	EmployeeJustificationFactory employeeJustificationFactory = (EmployeeJustificationFactory) getFactoryObject();
	try {
	    Object result = executeService(request, "ExecuteFactoryMethod",
		    new Object[] { employeeJustificationFactory });
	    if (result != null) {
		setError(request, "errorMessage", (ActionMessage) result);
		request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
		return chooseJustificationMotivePostBack(mapping, form, request, response);
	    }
	} catch (FileManagerException ex) {
	    setError(request, "errorMessage", (ActionMessage) new ActionMessage(ex.getKey(), ex
		    .getArgs()));
	    RenderUtils.invalidateViewState();
	    request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
	    return chooseJustificationMotivePostBack(mapping, form, request, response);
	}
	request.setAttribute("employeeNumber", employeeJustificationFactory.getEmployee()
		.getEmployeeNumber());
	request.setAttribute("yearMonth", employeeJustificationFactory.getYearMonth());
	if (employeeJustificationFactory.getDate() == null) {
	    return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form,
		    request, response);
	}
	return new ViewEmployeeAssiduousnessDispatchAction().showWorkSheet(mapping, form, request,
		response);
    }

    public ActionForward deleteEmployeeJustification(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	Justification justification = (Justification) rootDomainObject
		.readAssiduousnessRecordByOID(new Integer(getFromRequest(request, "idInternal")
			.toString()));
	Employee modifiedBy = getUserView(request).getPerson().getEmployee();

	EmployeeAnulateJustificationFactory employeeAnulateJustificationFactory = new EmployeeAnulateJustificationFactory(
		justification, getYearMonth(request, null), modifiedBy);

	request.setAttribute("employeeNumber", justification.getAssiduousness().getEmployee()
		.getEmployeeNumber());
	request.setAttribute("yearMonth", employeeAnulateJustificationFactory.getYearMonth());
	try {
	    Object result = executeService(request, "ExecuteFactoryMethod",
		    new Object[] { employeeAnulateJustificationFactory });
	    if (result != null) {
		setError(request, "errorMessage", (ActionMessage) result);
		request
			.setAttribute("employeeJustificationFactory",
				employeeAnulateJustificationFactory);
		return chooseJustificationMotivePostBack(mapping, form, request, response);
	    }
	} catch (FileManagerException ex) {
	    setError(request, "errorMessage", (ActionMessage) new ActionMessage(ex.getKey(), ex
		    .getArgs()));
	    RenderUtils.invalidateViewState();
	}

	return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form, request,
		response);
    }

    public ActionForward prepareAssociateEmployeeWorkSchedule(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	Integer scheduleID = getIntegerFromRequest(request, "scheduleID");
	EmployeeScheduleFactory employeeScheduleFactory = null;
	if (scheduleID != null) {
	    Schedule schedule = (Schedule) RootDomainObject.readDomainObjectByOID(Schedule.class,
		    scheduleID);
	    employeeScheduleFactory = new EmployeeScheduleFactory(schedule, SessionUtils.getUserView(
		    request).getPerson().getEmployee());
	} else {
	    employeeScheduleFactory = (EmployeeScheduleFactory) getFactoryObject();
	    if (employeeScheduleFactory != null) {
		DynaActionForm actionForm = (DynaActionForm) form;
		String addWorkWeek = actionForm.getString("addWorkWeek");
		if (addWorkWeek.equalsIgnoreCase("yes")) {
		    employeeScheduleFactory.addEmployeeWorkWeekSchedule();
		    RenderUtils.invalidateViewState();
		} else if (addWorkWeek.equalsIgnoreCase("remove")) {
		    employeeScheduleFactory.removeEmployeeWorkWeekSchedule();
		}
	    } else {
		Integer employeeID = getIntegerFromRequest(request, "employeeID");
		Employee employee = rootDomainObject.readEmployeeByOID(employeeID);
		Schedule currentSchedule = employee.getAssiduousness() != null ? employee
			.getAssiduousness().getCurrentSchedule() : null;
		employeeScheduleFactory = new EmployeeScheduleFactory(employee, SessionUtils
			.getUserView(request).getPerson().getEmployee(), currentSchedule);
	    }
	}
	request.setAttribute("yearMonth", getYearMonth(request, null));
	request.setAttribute("employeeScheduleBean", employeeScheduleFactory);
	return mapping.findForward("prepare-associate-schedule");
    }

    public ActionForward chooseWorkSchedule(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	EmployeeScheduleFactory employeeScheduleFactory = (EmployeeScheduleFactory) getFactoryObject();
	Schedule schedule = employeeScheduleFactory.getSchedule();
	if (employeeScheduleFactory.getEmployee().getAssiduousness().overlapsOtherSchedules(schedule,
		employeeScheduleFactory.getBeginDate(), employeeScheduleFactory.getEndDate())) {
	    setError(request, "errorMessage", (ActionMessage) new ActionMessage(
		    "error.schedule.overlapsWithOther"));
	    return mapping.getInputForward();
	}

	List<WorkScheduleType> workScheduleList = new ArrayList<WorkScheduleType>();
	for (WorkScheduleType workScheduleType : rootDomainObject.getWorkScheduleTypes()) {
	    if (workScheduleType.isValidWorkScheduleType()) {
		workScheduleList.add(workScheduleType);
	    }
	}
	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("ojbConcreteClass"));
	comparatorChain.addComparator(new BeanComparator("acronym"));
	Collections.sort(workScheduleList, comparatorChain);
	request.setAttribute("workScheduleList", workScheduleList);

	request.setAttribute("employeeScheduleBean", employeeScheduleFactory);
	return mapping.findForward("associate-schedule");
    }

    public ActionForward associateEmployeeWorkSchedule(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	EmployeeScheduleFactory employeeScheduleFactory = (EmployeeScheduleFactory) getFactoryObject();
	Integer workScheduleTypeID = getInteger((DynaActionForm) form, "workScheduleID");
	WorkScheduleType workScheduleType = rootDomainObject
		.readWorkScheduleTypeByOID(workScheduleTypeID);
	employeeScheduleFactory.setChoosenWorkSchedule(workScheduleType);

	Schedule schedule = employeeScheduleFactory.getSchedule();
	if (hasAnythingChanged(employeeScheduleFactory)) {
	    schedule = (Schedule) executeService(request, "ExecuteFactoryMethod",
		    new Object[] { employeeScheduleFactory });
	}
	request.setAttribute("scheduleID", schedule.getIdInternal());
	RenderUtils.invalidateViewState();
	request.setAttribute("employeeID", employeeScheduleFactory.getEmployee().getIdInternal());
	return prepareAssociateEmployeeWorkSchedule(mapping, form, request, response);
    }

    public ActionForward deleteWorkScheduleDays(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	EmployeeScheduleFactory employeeScheduleFactory = (EmployeeScheduleFactory) getFactoryObject();
	Schedule schedule = employeeScheduleFactory.getSchedule();
	if (employeeScheduleFactory.getEmployee().getAssiduousness().overlapsOtherSchedules(schedule,
		employeeScheduleFactory.getBeginDate(), employeeScheduleFactory.getEndDate())) {
	    setError(request, "errorMessage", (ActionMessage) new ActionMessage(
		    "error.schedule.overlapsWithOther"));
	    return mapping.getInputForward();
	}

	DynaActionForm actionForm = (DynaActionForm) form;
	String workWeek = actionForm.getString("workWeek");
	if (!StringUtils.isEmpty(workWeek)) {
	    employeeScheduleFactory.selectAllCheckBoxes(new Integer(workWeek));
	}
	employeeScheduleFactory.setToDeleteDays(true);
	if (canDeleteDays(employeeScheduleFactory)) {
	    if (!areEmptyDays(employeeScheduleFactory)) {
		schedule = (Schedule) executeService(request, "ExecuteFactoryMethod",
			new Object[] { employeeScheduleFactory });
	    }
	} else {
	    setError(request, "errorMessage", (ActionMessage) new ActionMessage(
		    "error.schedule.canNotDeleteAllDays"));
	    RenderUtils.invalidateViewState();
	    return mapping.getInputForward();
	}
	request.setAttribute("scheduleID", schedule.getIdInternal());
	RenderUtils.invalidateViewState();
	request.setAttribute("employeeID", employeeScheduleFactory.getEmployee().getIdInternal());
	return prepareAssociateEmployeeWorkSchedule(mapping, form, request, response);
    }

    public ActionForward deleteClocking(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final Integer clockingId = getIntegerFromRequest(request, "idInternal");
	final Clocking clocking = (Clocking) rootDomainObject.readAssiduousnessRecordByOID(clockingId);
	final YearMonth yearMonth = new YearMonth(clocking.getDate().toYearMonthDay());
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employeeNumber", clocking.getAssiduousness().getEmployee()
		.getEmployeeNumber());

	if (!yearMonth.getIsThisYearMonthClosed()) {
	    final IUserView userView = SessionUtils.getUserView(request);
	    Employee employee = userView.getPerson().getEmployee();
	    ServiceUtils.executeService(userView, "DeleteClocking", new Object[] { clocking, employee });
	}
	return new ViewEmployeeAssiduousnessDispatchAction().showClockings(mapping, form, request,
		response);
    }

    public ActionForward restoreClocking(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final Integer clockingId = getIntegerFromRequest(request, "idInternal");
	final Clocking clocking = (Clocking) rootDomainObject.readAssiduousnessRecordByOID(clockingId);
	final YearMonth yearMonth = new YearMonth(clocking.getDate().toYearMonthDay());
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employeeNumber", clocking.getAssiduousness().getEmployee()
		.getEmployeeNumber());
	if (!yearMonth.getIsThisYearMonthClosed()) {
	    final IUserView userView = SessionUtils.getUserView(request);
	    Employee employee = userView.getPerson().getEmployee();
	    ServiceUtils
		    .executeService(userView, "RestoreClocking", new Object[] { clocking, employee });
	}
	return new ViewEmployeeAssiduousnessDispatchAction().showClockings(mapping, form, request,
		response);
    }

    public ActionForward prepareInsertJustification(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	request.setAttribute("employeeJustificationFactory",
		new SeveralEmployeeJustificationFactoryCreator());
	return mapping.findForward("insert-justification");
    }

    public ActionForward chooseInsertJustificationMotivePostBack(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws FenixServiceException, FenixFilterException {
	EmployeeJustificationFactory employeeJustificationFactory = (EmployeeJustificationFactory) getFactoryObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
	return mapping.findForward("insert-justification");
    }

    public ActionForward insertJustification(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	SeveralEmployeeJustificationFactoryCreator severalEmployeeJustificationFactoryCreator = (SeveralEmployeeJustificationFactoryCreator) getFactoryObject();
	Object result = executeService(request, "ExecuteFactoryMethod",
		new Object[] { severalEmployeeJustificationFactoryCreator });
	RenderUtils.invalidateViewState();
	if (result != null) {
	    setError(request, "errorMessage", (ActionMessage) result);
	}
	return prepareInsertJustification(mapping, form, request, response);
    }

    private boolean areEmptyDays(EmployeeScheduleFactory employeeScheduleFactory) {
	for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory
		.getEmployeeWorkWeekScheduleList()) {
	    if (!workWeekScheduleBean.areSelectedDaysEmpty()) {
		return false;
	    }
	}
	return true;
    }

    private boolean canDeleteDays(EmployeeScheduleFactory employeeScheduleFactory) {
	for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory
		.getEmployeeWorkWeekScheduleList()) {
	    if (!workWeekScheduleBean.isValidWeekChecked()) {
		return true;
	    }
	}
	return false;
    }

    private boolean hasAnythingChanged(EmployeeScheduleFactory employeeScheduleFactory) {
	Schedule schedule = employeeScheduleFactory.getSchedule();
	if (schedule == null) {
	    return true;
	}
	boolean differencesInWorkSchedules = true;
	boolean differencesInDates = true;
	if (schedule.getBeginDate().isEqual(employeeScheduleFactory.getBeginDate())
		&& ((schedule.getEndDate() == null && employeeScheduleFactory.getEndDate() == null) || (schedule
			.getEndDate() != null
			&& employeeScheduleFactory.getEndDate() != null && schedule.getEndDate()
			.isEqual(employeeScheduleFactory.getEndDate())))) {
	    differencesInDates = false;
	}
	for (EmployeeWorkWeekScheduleBean workWeekScheduleBean : employeeScheduleFactory
		.getEmployeeWorkWeekScheduleList()) {
	    WorkWeek workWeek = workWeekScheduleBean.getWorkWeekByCheckedBox();
	    for (WorkSchedule workSchedule : schedule.getWorkSchedules()) {
		if (workSchedule.getPeriodicity().getWorkWeekNumber().equals(
			workWeekScheduleBean.getWorkWeekNumber())) {
		    if (workSchedule.getWorkScheduleType() == employeeScheduleFactory
			    .getChoosenWorkSchedule()
			    && workWeek != null && workSchedule.getWorkWeek().contains(workWeek)) {
			differencesInWorkSchedules = false;
			break;
		    }
		}
	    }
	}
	employeeScheduleFactory.setDifferencesInDates(differencesInDates);
	employeeScheduleFactory.setDifferencesInWorkSchedules(differencesInWorkSchedules);
	return differencesInWorkSchedules || differencesInDates;
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add(error, actionMessage);
	saveMessages(request, actionMessages);
    }

    private YearMonth getYearMonth(HttpServletRequest request, YearMonthDay date) {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (date == null) {
	    if (yearMonth == null) {
		yearMonth = new YearMonth();

		String year = request.getParameter("year");
		String month = request.getParameter("month");

		if (StringUtils.isEmpty(year)) {
		    yearMonth.setYear(new YearMonthDay().getYear());
		} else {
		    yearMonth.setYear(new Integer(year));
		}
		if (StringUtils.isEmpty(month)) {
		    yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
		} else {
		    yearMonth.setMonth(Month.valueOf(month));
		}
	    }
	} else {
	    yearMonth = new YearMonth(date);
	}

	if (yearMonth.getYear() < 2006) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidPastDateNoData"));
	    saveMessages(request, actionMessages);
	    request.setAttribute("yearMonth", yearMonth);
	    return null;
	}
	return yearMonth;
    }
}