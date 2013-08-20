package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.department.InsertTeacherPersonalExpectation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.department.TeacherPersonalExpectationBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "departmentMember", path = "/personalExpectationManagement", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "manageResearchAndDevelopmentExpectations",
                path = "/departmentMember/expectationManagement/researchAndDevelopmentExpectationsManagement.jsp"),
        @Forward(name = "viewTeacherPersonalExpectations",
                path = "/departmentMember/expectationManagement/viewTeacherPersonalExpectation.jsp"),
        @Forward(name = "manageProfessionalActivitiesExpectations",
                path = "/departmentMember/expectationManagement/professionalActivitiesExpectationsManagement.jsp"),
        @Forward(name = "manageEducationExpectations",
                path = "/departmentMember/expectationManagement/educationExpectationsManagement.jsp"),
        @Forward(name = "manageUniversityServicesExpectations",
                path = "/departmentMember/expectationManagement/universityServicesExpectationsManagement.jsp") })
public class PersonalExpectationManagement extends FenixDispatchAction {

    public ActionForward viewTeacherPersonalExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Teacher teacher = getLoggedTeacher(request);
        if (teacher != null) {
            ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
            readAndSetTeacherPersonalExpectationByExecutionYear(request, teacher, executionYear);
            request.setAttribute("teacherPersonalExpectationBean", new TeacherPersonalExpectationBean(executionYear, teacher));
        }
        return mapping.findForward("viewTeacherPersonalExpectations");
    }

    public ActionForward viewTeacherPersonalExpectationInSelectedExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationInSelectedExecutionYear");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        if (bean != null) {
            ExecutionYear executionYear = bean.getExecutionYear();
            Teacher teacher = getLoggedTeacher(request);
            readAndSetTeacherPersonalExpectationByExecutionYear(request, teacher, executionYear);
            request.setAttribute("teacherPersonalExpectationBean", bean);
        }
        return mapping.findForward("viewTeacherPersonalExpectations");
    }

    public ActionForward prepareDefineTeacherPersonalExpection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        Teacher teacher = getLoggedTeacher(request);
        if (teacher != null) {
            ExecutionYear executionYear = getExecutionYearFromParameter(request);
            if (teacher.getTeacherPersonalExpectationByExecutionYear(executionYear) == null) {
                request.setAttribute("teacherPersonalExpectationBean", new TeacherPersonalExpectationBean(executionYear, teacher));
            }
        }
        return mapping.findForward("manageEducationExpectations");
    }

    public ActionForward prepareManageResearchAndDevelopment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithEducationMainFocus");
        if (viewState == null) {
            viewState = RenderUtils.getViewState();
        }
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        request.setAttribute("teacherPersonalExpectationBean", bean);
        return mapping.findForward("manageResearchAndDevelopmentExpectations");
    }

    public ActionForward prepareManageUniversityServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithOrientationMainFocus");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        request.setAttribute("teacherPersonalExpectationBean", bean);
        return mapping.findForward("manageUniversityServicesExpectations");
    }

    public ActionForward prepareManageProfessionalActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithMainFocusUniversityServices");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        request.setAttribute("teacherPersonalExpectationBean", bean);
        return mapping.findForward("manageProfessionalActivitiesExpectations");
    }

    public ActionForward createTeacherPersonalExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        IViewState viewState = RenderUtils.getViewState("teacherPersonalExpectationWithMainFocusProfessionalActivities");
        TeacherPersonalExpectationBean bean = (TeacherPersonalExpectationBean) viewState.getMetaObject().getObject();
        TeacherPersonalExpectation teacherPersonalExpectation = null;

        try {
            teacherPersonalExpectation = InsertTeacherPersonalExpectation.runInsertTeacherPersonalExpectation(bean);

        } catch (DomainException exception) {
            saveMessages(request, exception);
        }

        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectationBean", bean);
        return mapping.findForward("viewTeacherPersonalExpectations");
    }

    public ActionForward prepareEditEducationExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixActionException {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        checkTeacherAndPeriodToEdit(request, teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        return mapping.findForward("manageEducationExpectations");
    }

    public ActionForward editEducationExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithEducationMainFocus", mapping);
    }

    public ActionForward prepareEditResearchAndDevelopmentExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixActionException {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        checkTeacherAndPeriodToEdit(request, teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        return mapping.findForward("manageResearchAndDevelopmentExpectations");
    }

    public ActionForward editResearchAndDevelopmentExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithOrientationMainFocus", mapping);
    }

    public ActionForward prepareEditUniversityServicesExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixActionException {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        checkTeacherAndPeriodToEdit(request, teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        return mapping.findForward("manageUniversityServicesExpectations");
    }

    public ActionForward editUniversityServicesExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithMainFocusUniversityServices", mapping);
    }

    public ActionForward prepareEditProfessionalActivitiesExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixActionException {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        checkTeacherAndPeriodToEdit(request, teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        return mapping.findForward("manageProfessionalActivitiesExpectations");
    }

    public ActionForward editProfessionalActivitiesExpectations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        return viewTeacherPersonalExpectation(request, "teacherPersonalExpectationWithMainFocusProfessionalActivities", mapping);
    }

    // Private Methods

    private ActionForward viewTeacherPersonalExpectation(HttpServletRequest request, String viewStateName, ActionMapping mapping) {
        IViewState viewState = RenderUtils.getViewState(viewStateName);
        TeacherPersonalExpectation teacherPersonalExpectation =
                (TeacherPersonalExpectation) viewState.getMetaObject().getObject();
        Teacher teacher = teacherPersonalExpectation.getTeacher();
        ExecutionYear executionYear = teacherPersonalExpectation.getExecutionYear();
        request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        request.setAttribute("teacherPersonalExpectationBean", new TeacherPersonalExpectationBean(executionYear, teacher));
        return mapping.findForward("viewTeacherPersonalExpectations");
    }

    private void readAndSetTeacherPersonalExpectationByExecutionYear(HttpServletRequest request, Teacher teacher,
            ExecutionYear executionYear) {
        TeacherPersonalExpectation teacherPersonalExpectation =
                teacher.getTeacherPersonalExpectationByExecutionYear(executionYear);
        Department department = teacher.getCurrentWorkingDepartment();
        if (department != null) {
            TeacherExpectationDefinitionPeriod period =
                    department.getTeacherExpectationDefinitionPeriodForExecutionYear(executionYear);
            request.setAttribute("periodOpen", period != null ? period.isPeriodOpen().booleanValue() : false);
            request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        }
    }

    private void checkTeacherAndPeriodToEdit(HttpServletRequest request, TeacherPersonalExpectation teacherPersonalExpectation)
            throws FenixActionException {
        ExecutionYear executionYear = teacherPersonalExpectation.getExecutionYear();
        Department department = teacherPersonalExpectation.getTeacher().getCurrentWorkingDepartment();
        if (department != null) {
            TeacherExpectationDefinitionPeriod period =
                    department.getTeacherExpectationDefinitionPeriodForExecutionYear(executionYear);
            if (period == null || !period.isPeriodOpen()
                    || !getLoggedTeacher(request).equals(teacherPersonalExpectation.getTeacher())) {
                throw new FenixActionException();
            }
        } else {
            throw new FenixActionException();
        }
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
        Person loggedPerson = getLoggedPerson(request);
        return (loggedPerson != null) ? loggedPerson.getTeacher() : null;
    }

    private ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
        final String executionYearIDString = request.getParameter("executionYearID");
        return AbstractDomainObject.fromExternalId(executionYearIDString);
    }

    private TeacherPersonalExpectation getTeacherPersonalExpectationFromParameter(final HttpServletRequest request) {
        final String teacherPersonalExpectationIDString = request.getParameter("teacherPersonalExpectationID");
        return AbstractDomainObject.fromExternalId(teacherPersonalExpectationIDString);
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }
}
