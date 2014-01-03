package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.CopyTSDProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.CreateTSDProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.DeleteTSDProcess;
import org.apache.commons.collections.CollectionUtils;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

public class TSDProcessAction extends FenixDispatchAction {
    private static final String NOT_SELECTED_EXECUTION_PERIOD = "NOT_SELECTED";

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
        User userView = Authenticate.getUser();

        DynaActionForm dynaForm = (DynaActionForm) form;

        List<ExecutionYear> executionYearList = ExecutionYear.readNotClosedExecutionYears();
        Collections.sort(executionYearList, new BeanComparator("year"));

        ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, executionYearList);
        List<ExecutionSemester> executionPeriodList =
                new ArrayList<ExecutionSemester>(selectedExecutionYear.getExecutionPeriods());
        setCurrentExecutionYearInDynamicForm(userView, dynaForm, selectedExecutionYear);

        Collections.sort(executionPeriodList, new BeanComparator("semester"));

        request.setAttribute("departmentName", userView.getPerson().getTeacher().getCurrentWorkingDepartment().getRealName());
        request.setAttribute("executionYearList", executionYearList);
        request.setAttribute("executionPeriodsList", executionPeriodList);

        return mapping.findForward("showTSDProcessCreationForm");
    }

    public ActionForward createTSDProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User userView = Authenticate.getUser();

        DynaActionForm dynaForm = (DynaActionForm) form;

        List<String> selectedExecutionPeriodIdList = new ArrayList<String>();

        String selectedExecutionPeriodId = (String) dynaForm.get("executionPeriod");
        if (selectedExecutionPeriodId.equals(NOT_SELECTED_EXECUTION_PERIOD)) {
            ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, null);

            for (ExecutionSemester executionSemester : selectedExecutionYear.getExecutionPeriods()) {
                selectedExecutionPeriodIdList.add(executionSemester.getExternalId());
            }
        } else {
            selectedExecutionPeriodIdList.add(selectedExecutionPeriodId);
        }

        String selectedDepartmentId = userView.getPerson().getTeacher().getCurrentWorkingDepartment().getExternalId();
        String name = (String) dynaForm.get("name");

        TSDProcess tsdProcess =
                CreateTSDProcess.runCreateTSDProcess(selectedExecutionPeriodIdList, selectedDepartmentId, userView.getPerson()
                        .getExternalId(), name);

        return loadTSDProcessServices(mapping, request, tsdProcess.getExternalId(), userView);
    }

    public ActionForward prepareForTSDProcessEdition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User userView = Authenticate.getUser();

        DynaActionForm dynaForm = (DynaActionForm) form;

        List<ExecutionYear> executionYearList = ExecutionYear.readNotClosedExecutionYears();
        Collections.sort(executionYearList, new BeanComparator("year"));

        ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, null);

        List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
        if (selectedExecutionYear != null) {
            executionPeriodList.addAll(selectedExecutionYear.getExecutionPeriods());
        }

        Department selectedDepartment = userView.getPerson().getTeacher().getCurrentWorkingDepartment();

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

            @Override
            public boolean evaluate(Object arg0) {
                TSDProcess tsd = (TSDProcess) arg0;
                return tsd.hasAnyPermission(person);
            }
        });

        Collections.sort(tsdProcessList, new BeanComparator("name"));

        request.setAttribute("departmentName", userView.getPerson().getTeacher().getCurrentWorkingDepartment().getRealName());
        request.setAttribute("executionYearList", executionYearList);
        request.setAttribute("executionPeriodsList", executionPeriodList);
        request.setAttribute("tsdProcessList", tsdProcessList);

        return mapping.findForward("showTSDProcesss");
    }

    public ActionForward showTSDProcessServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tsdProcessId = request.getParameter("tsdProcess");
        User userView = Authenticate.getUser();

        return loadTSDProcessServices(mapping, request, tsdProcessId, userView);
    }

    private ActionForward loadTSDProcessServices(ActionMapping mapping, HttpServletRequest request, String tsdProcessId,
            User userView) {
        setPermissionsOnRequest(request, FenixFramework.<TSDProcess> getDomainObject(tsdProcessId), userView.getPerson());

        request.setAttribute("tsdProcess", FenixFramework.getDomainObject(tsdProcessId));
        return mapping.findForward("showTSDProcessServices");
    }

    private void setPermissionsOnRequest(HttpServletRequest request, TSDProcess tsdProcess, Person userViewPerson) {

        Boolean permissionCourseValuation =
                tsdProcess.hasPermissionToCoursesValuation(userViewPerson)
                        || tsdProcess.getHasSuperUserPermission(userViewPerson);
        Boolean permissionTeacherValuation =
                tsdProcess.hasPermissionToTeachersValuation(userViewPerson)
                        || tsdProcess.getHasSuperUserPermission(userViewPerson);
        Boolean permissionToCoursesManagement =
                tsdProcess.hasPermissionToCourseManagement(userViewPerson)
                        || tsdProcess.getHasSuperUserPermission(userViewPerson);
        Boolean permissionToTeachersManagement =
                tsdProcess.hasPermissionToTeacherManagement(userViewPerson)
                        || tsdProcess.getHasSuperUserPermission(userViewPerson);

        Boolean phaseManagementPermission = tsdProcess.getIsMemberOfPhasesManagementGroup(userViewPerson);

        Boolean automaticValuationPermission = tsdProcess.getIsMemberOfAutomaticValuationGroup(userViewPerson);

        Boolean omissionConfigurationPermission = tsdProcess.getIsMemberOfOmissionConfigurationGroup(userViewPerson);

        Boolean tsdCoursesAndTeachersManagementPermission =
                tsdProcess.getIsMemberOfCompetenceCoursesAndTeachersManagementGroup(userViewPerson);

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
        User userView = Authenticate.getUser();
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

        Department selectedDepartment = userView.getPerson().getTeacher().getCurrentWorkingDepartment();

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
            dynaForm.set("tsdProcess", tsdProcessList.iterator().next().getExternalId());
        }

        request.setAttribute("departmentName", userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace()
                .getRealName());
        dynaForm.set("executionYearForCopy", selectedExecutionYearForCopy.getExternalId());
        request.setAttribute("executionYearList", executionYearList);
        request.setAttribute("executionPeriodsList", executionPeriodList);
        request.setAttribute("executionPeriodsListForCopy", executionPeriodListForCopy);
        request.setAttribute("tsdProcessList", tsdProcessList);

        return mapping.findForward("showTSDProcesssForCopy");
    }

    public ActionForward copyTSDProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        ExecutionYear selectedExecutionYear = getSelectedExecutionYearForCopy(userView, dynaForm, null);
        ExecutionSemester selectedExecutionPeriodForCopy = getSelectedExecutionPeriodForCopy(userView, dynaForm);
        String name = (String) dynaForm.get("name");
        TSDProcess selectedTSDProcess = getSelectedTSDProcess(userView, dynaForm);

        List<String> selectedExecutionPeriodListForCopyId = new ArrayList<String>();
        if (selectedExecutionPeriodForCopy != null) {
            selectedExecutionPeriodListForCopyId.add(selectedExecutionPeriodForCopy.getExternalId());
        } else {
            for (ExecutionSemester executionSemester : selectedExecutionYear.getExecutionPeriods()) {
                selectedExecutionPeriodListForCopyId.add(executionSemester.getExternalId());
            }
        }

        TSDProcess tsdProcess =
                CopyTSDProcess.runCopyTSDProcess(selectedExecutionPeriodListForCopyId, selectedTSDProcess.getExternalId(),
                        userView.getPerson().getExternalId(), name);

        request.setAttribute("tsdProcess", tsdProcess);
        return loadTSDProcessServices(mapping, request, tsdProcess.getExternalId(), userView);
    }

    public ActionForward deleteTSDProcessServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tsdProcessId = request.getParameter("tsdProcess");
        User userView = Authenticate.getUser();

        DeleteTSDProcess.runDeleteTSDProcess(tsdProcessId);

        return prepareForTSDProcessEdition(mapping, form, request, response);
    }

    private ExecutionYear getSelectedExecutionYear(User userView, DynaActionForm dynaForm,
            List<ExecutionYear> executionYearList) {
        ExecutionYear selectedExecutionYear = getDomainObject(dynaForm, "executionYear");

        if (selectedExecutionYear == null) {
            if (executionYearList != null && !executionYearList.isEmpty()) {
                return executionYearList.get(executionYearList.size() - 1);
            } else {
                return null;
            }
        }

        return selectedExecutionYear;
    }

    private ExecutionSemester getSelectedExecutionPeriod(User userView, DynaActionForm dynaForm) {
        String selectedExeuctionPeriodId = (String) dynaForm.get("executionPeriod");

        if (selectedExeuctionPeriodId.equals(NOT_SELECTED_EXECUTION_PERIOD)) {
            return null;
        }

        return getDomainObject(dynaForm, "executionPeriod");
    }

    private void setCurrentExecutionYearInDynamicForm(User userView, DynaActionForm dynaForm, ExecutionYear executionYear) {
        dynaForm.set("executionYear", executionYear.getExternalId());
    }

    private ExecutionYear getCurrentExecutionYear(List<ExecutionYear> executionYearList) {
        return (ExecutionYear) CollectionUtils.find(executionYearList, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                ExecutionYear executionYear = (ExecutionYear) arg0;
                return executionYear.getState().equals(PeriodState.CURRENT);
            }
        });
    }

    private ExecutionYear getSelectedExecutionYearForCopy(User userView, DynaActionForm dynaForm,
            List<ExecutionYear> executionYearList) {
        ExecutionYear selectedExecutionYear = getDomainObject(dynaForm, "executionYearForCopy");

        if (selectedExecutionYear == null) {
            if (executionYearList != null && !executionYearList.isEmpty()) {
                return getCurrentExecutionYear(executionYearList);
            } else {
                return null;
            }
        }

        return selectedExecutionYear;
    }

    private ExecutionSemester getSelectedExecutionPeriodForCopy(User userView, DynaActionForm dynaForm) {
        ExecutionSemester selectedExecutionPeriod = getDomainObject(dynaForm, "executionPeriodForCopy");
        return selectedExecutionPeriod;
    }

    private TSDProcess getSelectedTSDProcess(User userView, DynaActionForm dynaForm) {
        TSDProcess tsdProcess = getDomainObject(dynaForm, "tsdProcess");

        return tsdProcess;
    }
}
