package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class TSDProcessAction extends FenixDispatchAction {
    private static final Integer NOT_SELECTED_EXECUTION_PERIOD = -1;

    public ActionForward prepareTSDProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("showMain");
    }

    public ActionForward prepareForTSDProcessCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("showTSDProcessCreationOptions");
    }

    public ActionForward prepareForEmptyTSDProcessCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IUserView userView = UserView.getUser();

	DynaActionForm dynaForm = (DynaActionForm) form;

	List<ExecutionYear> executionYearList = ExecutionYear.readNotClosedExecutionYears();
	Collections.sort(executionYearList, new BeanComparator("year"));

	ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, executionYearList);
	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>(selectedExecutionYear
		.getExecutionPeriods());
	setCurrentExecutionYearInDynamicForm(userView, dynaForm, selectedExecutionYear);

	Collections.sort(executionPeriodList, new BeanComparator("semester"));

	request.setAttribute("departmentName", userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace()
		.getRealName());
	request.setAttribute("executionYearList", executionYearList);
	request.setAttribute("executionPeriodsList", executionPeriodList);

	return mapping.findForward("showTSDProcessCreationForm");
    }

    public ActionForward createTSDProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IUserView userView = UserView.getUser();

	DynaActionForm dynaForm = (DynaActionForm) form;

	List<Integer> selectedExecutionPeriodIdList = new ArrayList<Integer>();

	Integer selectedExecutionPeriodId = (Integer) dynaForm.get("executionPeriod");
	if (selectedExecutionPeriodId.equals(NOT_SELECTED_EXECUTION_PERIOD)) {
	    ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, null);

	    for (ExecutionSemester executionSemester : selectedExecutionYear.getExecutionPeriods()) {
		selectedExecutionPeriodIdList.add(executionSemester.getIdInternal());
	    }
	} else {
	    selectedExecutionPeriodIdList.add(selectedExecutionPeriodId);
	}

	Integer selectedDepartmentId = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getIdInternal();
	String name = (String) dynaForm.get("name");

	Object[] parameters = new Object[] { selectedExecutionPeriodIdList, selectedDepartmentId,
		userView.getPerson().getIdInternal(), name };

	TSDProcess tsdProcess = (TSDProcess) ServiceUtils.executeService("CreateTSDProcess", parameters);

	return loadTSDProcessServices(mapping, request, tsdProcess.getIdInternal(), userView);
    }

    public ActionForward prepareForTSDProcessEdition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IUserView userView = UserView.getUser();

	DynaActionForm dynaForm = (DynaActionForm) form;

	List<ExecutionYear> executionYearList = ExecutionYear.readNotClosedExecutionYears();
	Collections.sort(executionYearList, new BeanComparator("year"));

	ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, null);

	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
	if (selectedExecutionYear != null) {
	    executionPeriodList.addAll(selectedExecutionYear.getExecutionPeriods());
	}

	Department selectedDepartment = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();

	Collections.sort(executionPeriodList, new BeanComparator("semester"));

	List<TSDProcess> tsdProcessList;

	ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm);

	if (selectedExecutionPeriod != null) {
	    tsdProcessList = selectedDepartment.getTSDProcessesByExecutionPeriod(selectedExecutionPeriod);
	} else if (selectedExecutionYear != null) {
	    tsdProcessList = selectedDepartment.getTSDProcessesByExecutionYear(selectedExecutionYear);
	} else {
	    tsdProcessList = new ArrayList<TSDProcess>(selectedDepartment.getTSDProcesses());
	}

	final Person person = userView.getPerson();
	tsdProcessList = (List<TSDProcess>) CollectionUtils.select(tsdProcessList, new Predicate() {

	    public boolean evaluate(Object arg0) {
		TSDProcess tsd = (TSDProcess) arg0;
		return tsd.hasAnyPermission(person);
	    }
	});

	Collections.sort(tsdProcessList, new BeanComparator("name"));

	request.setAttribute("departmentName", userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace()
		.getRealName());
	request.setAttribute("executionYearList", executionYearList);
	request.setAttribute("executionPeriodsList", executionPeriodList);
	request.setAttribute("tsdProcessList", tsdProcessList);

	return mapping.findForward("showTSDProcesss");
    }

    public ActionForward showTSDProcessServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer tsdProcessId = new Integer(request.getParameter("tsdProcess"));
	IUserView userView = UserView.getUser();

	return loadTSDProcessServices(mapping, request, tsdProcessId, userView);
    }

    private ActionForward loadTSDProcessServices(ActionMapping mapping, HttpServletRequest request, Integer tsdProcessId,
	    IUserView userView) {
	setPermissionsOnRequest(request, rootDomainObject.readTSDProcessByOID(tsdProcessId), userView.getPerson());

	request.setAttribute("tsdProcess", rootDomainObject.readTSDProcessByOID(tsdProcessId));
	return mapping.findForward("showTSDProcessServices");
    }

    private void setPermissionsOnRequest(HttpServletRequest request, TSDProcess tsdProcess, Person userViewPerson) {

	Boolean permissionCourseValuation = tsdProcess.hasPermissionToCoursesValuation(userViewPerson)
		|| tsdProcess.getHasSuperUserPermission(userViewPerson);
	Boolean permissionTeacherValuation = tsdProcess.hasPermissionToTeachersValuation(userViewPerson)
		|| tsdProcess.getHasSuperUserPermission(userViewPerson);
	Boolean permissionToCoursesManagement = tsdProcess.hasPermissionToCourseManagement(userViewPerson)
		|| tsdProcess.getHasSuperUserPermission(userViewPerson);
	Boolean permissionToTeachersManagement = tsdProcess.hasPermissionToTeacherManagement(userViewPerson)
		|| tsdProcess.getHasSuperUserPermission(userViewPerson);

	Boolean phaseManagementPermission = tsdProcess.getIsMemberOfPhasesManagementGroup(userViewPerson);

	Boolean automaticValuationPermission = tsdProcess.getIsMemberOfAutomaticValuationGroup(userViewPerson);

	Boolean omissionConfigurationPermission = tsdProcess.getIsMemberOfOmissionConfigurationGroup(userViewPerson);

	Boolean tsdCoursesAndTeachersManagementPermission = tsdProcess
		.getIsMemberOfCompetenceCoursesAndTeachersManagementGroup(userViewPerson);

	request.setAttribute("coursesValuationPermission", permissionCourseValuation);
	request.setAttribute("teachersValuationPermission", permissionTeacherValuation);
	request.setAttribute("coursesManagementPermission", permissionToCoursesManagement);
	request.setAttribute("teachersManagementPermission", permissionToTeachersManagement);

	request.setAttribute("phaseManagementPermission", phaseManagementPermission);

	request.setAttribute("automaticValuationPermission", automaticValuationPermission);

	request.setAttribute("omissionConfigurationPermission", omissionConfigurationPermission);

	request.setAttribute("tsdCoursesAndTeachersManagementPermission", tsdCoursesAndTeachersManagementPermission);

	request.setAttribute("permissionsGrantPermission", tsdProcess.getHavePermissionSettings(userViewPerson));

	request.setAttribute("viewTSDProcessValuationPermission", permissionCourseValuation || permissionTeacherValuation
		|| permissionToCoursesManagement || permissionToTeachersManagement || phaseManagementPermission
		|| automaticValuationPermission || omissionConfigurationPermission || tsdCoursesAndTeachersManagementPermission);
    }

    public ActionForward prepareForTSDProcessCopy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	List<ExecutionYear> executionYearList = ExecutionYear.readNotClosedExecutionYears();
	Collections.sort(executionYearList, new BeanComparator("year"));

	ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, null);

	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
	if (selectedExecutionYear != null) {
	    executionPeriodList.addAll(selectedExecutionYear.getExecutionPeriods());
	}

	ExecutionYear selectedExecutionYearForCopy = getSelectedExecutionYearForCopy(userView, dynaForm, executionYearList);

	List<ExecutionSemester> executionPeriodListForCopy = new ArrayList<ExecutionSemester>();
	if (selectedExecutionYearForCopy != null) {
	    executionPeriodListForCopy.addAll(selectedExecutionYearForCopy.getExecutionPeriods());
	}

	Department selectedDepartment = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();

	Collections.sort(executionPeriodList, new BeanComparator("semester"));
	Collections.sort(executionPeriodListForCopy, new BeanComparator("semester"));

	List<TSDProcess> tsdProcessList;

	ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm);

	if (selectedExecutionPeriod != null) {
	    tsdProcessList = selectedDepartment.getTSDProcessesByExecutionPeriod(selectedExecutionPeriod);
	} else if (selectedExecutionYear != null) {
	    tsdProcessList = selectedDepartment.getTSDProcessesByExecutionYear(selectedExecutionYear);
	} else {
	    tsdProcessList = new ArrayList<TSDProcess>(selectedDepartment.getTSDProcesses());
	}

	if (tsdProcessList.size() > 0) {
	    Collections.sort(tsdProcessList, new BeanComparator("name"));
	    dynaForm.set("tsdProcess", tsdProcessList.get(0).getIdInternal());
	}

	request.setAttribute("departmentName", userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace()
		.getRealName());
	dynaForm.set("executionYearForCopy", selectedExecutionYearForCopy.getIdInternal());
	request.setAttribute("executionYearList", executionYearList);
	request.setAttribute("executionPeriodsList", executionPeriodList);
	request.setAttribute("executionPeriodsListForCopy", executionPeriodListForCopy);
	request.setAttribute("tsdProcessList", tsdProcessList);

	return mapping.findForward("showTSDProcesssForCopy");
    }

    public ActionForward copyTSDProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	ExecutionYear selectedExecutionYear = getSelectedExecutionYearForCopy(userView, dynaForm, null);
	ExecutionSemester selectedExecutionPeriodForCopy = getSelectedExecutionPeriodForCopy(userView, dynaForm);
	String name = (String) dynaForm.get("name");
	TSDProcess selectedTSDProcess = getSelectedTSDProcess(userView, dynaForm);

	List<Integer> selectedExecutionPeriodListForCopyId = new ArrayList<Integer>();
	if (selectedExecutionPeriodForCopy != null) {
	    selectedExecutionPeriodListForCopyId.add(selectedExecutionPeriodForCopy.getIdInternal());
	} else {
	    for (ExecutionSemester executionSemester : selectedExecutionYear.getExecutionPeriods())
		selectedExecutionPeriodListForCopyId.add(executionSemester.getIdInternal());
	}

	Object[] parameters = new Object[] { selectedExecutionPeriodListForCopyId, selectedTSDProcess.getIdInternal(),
		userView.getPerson().getIdInternal(), name };

	TSDProcess tsdProcess = (TSDProcess) ServiceUtils.executeService("CopyTSDProcess", parameters);

	request.setAttribute("tsdProcess", tsdProcess);
	return loadTSDProcessServices(mapping, request, tsdProcess.getIdInternal(), userView);
    }

    public ActionForward deleteTSDProcessServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer tsdProcessId = new Integer(request.getParameter("tsdProcess"));
	IUserView userView = UserView.getUser();

	ServiceUtils.executeService("DeleteTSDProcess", new Object[] { tsdProcessId });

	return prepareForTSDProcessEdition(mapping, form, request, response);
    }

    private ExecutionYear getSelectedExecutionYear(IUserView userView, DynaActionForm dynaForm,
	    List<ExecutionYear> executionYearList) throws FenixServiceException, FenixFilterException {
	Integer selectedExecutionYearId = (Integer) dynaForm.get("executionYear");

	ExecutionYear selectedExecutionYear = rootDomainObject.readExecutionYearByOID(selectedExecutionYearId);

	if (selectedExecutionYear == null) {
	    if (executionYearList != null && !executionYearList.isEmpty()) {
		return executionYearList.get(executionYearList.size() - 1);
	    } else {
		return null;
	    }
	}

	return selectedExecutionYear;
    }

    private ExecutionSemester getSelectedExecutionPeriod(IUserView userView, DynaActionForm dynaForm)
	    throws FenixServiceException, FenixFilterException {
	Integer selectedExeuctionPeriodId = (Integer) dynaForm.get("executionPeriod");

	if (selectedExeuctionPeriodId == NOT_SELECTED_EXECUTION_PERIOD) {
	    return null;
	}

	ExecutionSemester selectedExecutionPeriod = rootDomainObject.readExecutionSemesterByOID(selectedExeuctionPeriodId);

	return selectedExecutionPeriod;
    }

    private void setCurrentExecutionYearInDynamicForm(IUserView userView, DynaActionForm dynaForm, ExecutionYear executionYear)
	    throws FenixServiceException, FenixFilterException {
	dynaForm.set("executionYear", executionYear.getIdInternal());
    }

    private ExecutionYear getCurrentExecutionYear(List<ExecutionYear> executionYearList) {
	return (ExecutionYear) CollectionUtils.find(executionYearList, new Predicate() {
	    public boolean evaluate(Object arg0) {
		ExecutionYear executionYear = (ExecutionYear) arg0;
		return executionYear.getState().equals(PeriodState.CURRENT);
	    }
	});
    }

    private ExecutionYear getSelectedExecutionYearForCopy(IUserView userView, DynaActionForm dynaForm,
	    List<ExecutionYear> executionYearList) throws FenixFilterException, FenixServiceException {
	Integer selectedExecutionYearId = (Integer) dynaForm.get("executionYearForCopy");

	ExecutionYear selectedExecutionYear = rootDomainObject.readExecutionYearByOID(selectedExecutionYearId);

	if (selectedExecutionYear == null) {
	    if (executionYearList != null && !executionYearList.isEmpty()) {
		return getCurrentExecutionYear(executionYearList);
	    } else {
		return null;
	    }
	}

	return selectedExecutionYear;
    }

    private ExecutionSemester getSelectedExecutionPeriodForCopy(IUserView userView, DynaActionForm dynaForm)
	    throws FenixFilterException, FenixServiceException {
	Integer selectedExecutionPeriodId = (Integer) dynaForm.get("executionPeriodForCopy");

	ExecutionSemester selectedExecutionPeriod = rootDomainObject.readExecutionSemesterByOID(selectedExecutionPeriodId);

	return selectedExecutionPeriod;
    }

    private TSDProcess getSelectedTSDProcess(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException,
	    FenixFilterException {
	Integer tsdProcessId = (Integer) dynaForm.get("tsdProcess");
	TSDProcess tsdProcess = rootDomainObject.readTSDProcessByOID(tsdProcessId);

	return tsdProcess;
    }
}
