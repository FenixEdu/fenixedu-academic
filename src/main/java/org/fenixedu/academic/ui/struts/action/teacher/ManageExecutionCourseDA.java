/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Curriculum;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.LessonPlanning;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.enrollment.shift.ShiftEnrollmentErrorReport;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.dto.teacher.CreateLessonPlanningBean;
import org.fenixedu.academic.dto.teacher.ImportLessonPlanningsBean;
import org.fenixedu.academic.dto.teacher.ImportLessonPlanningsBean.ImportType;
import org.fenixedu.academic.dto.teacher.executionCourse.ImportContentBean;
import org.fenixedu.academic.service.services.enrollment.shift.EnrollStudentInShifts;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.teacher.CreateLessonPlanning;
import org.fenixedu.academic.service.services.teacher.DeleteLessonPlanning;
import org.fenixedu.academic.service.services.teacher.ImportBibliographicReferences;
import org.fenixedu.academic.service.services.teacher.ImportEvaluationMethod;
import org.fenixedu.academic.service.services.teacher.ImportLessonPlannings;
import org.fenixedu.academic.service.services.teacher.MoveLessonPlanning;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.teacher.TeacherApplication.TeacherTeachingApp;
import org.fenixedu.academic.ui.struts.action.teacher.executionCourse.ExecutionCourseBaseAction;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = TeacherTeachingApp.class, path = "execution-course-management",
        titleKey = "label.executionCourseManagement.menu.management")
@Mapping(path = "/manageExecutionCourse", module = "teacher")
public class ManageExecutionCourseDA extends ExecutionCourseBaseAction {

    @EntryPoint
    public ActionForward instructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return forward(request, "/teacher/executionCourse/instructions.jsp");
    }

    protected void prepareImportContentPostBack(HttpServletRequest request) {
        IViewState viewState = RenderUtils.getViewState("importContentBean");
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("importContentBean", bean);
    }

    protected void prepareImportContentInvalid(HttpServletRequest request) {
        IViewState viewState = RenderUtils.getViewState("importContentBeanWithExecutionCourse");
        viewState = (viewState == null) ? RenderUtils.getViewState("importContentBean") : viewState;
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);
    }

    protected void listExecutionCoursesToImportContent(HttpServletRequest request) {
        final IViewState viewState = RenderUtils.getViewState("importContentBean");
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);
    }

    protected void importContent(HttpServletRequest request, String importContentService) throws FenixServiceException {
        final ExecutionCourse executionCourseTo = (ExecutionCourse) request.getAttribute("executionCourse");
        final IViewState viewState = RenderUtils.getViewState("importContentBeanWithExecutionCourse");
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);

        final ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        try {
            if (importContentService.equals("ImportBibliographicReferences")) {
                ImportBibliographicReferences.runImportBibliographicReferences(executionCourseTo.getExternalId(),
                        executionCourseTo, executionCourseFrom, null);
            } else if (importContentService.equals("ImportEvaluationMethod")) {
                ImportEvaluationMethod.runImportEvaluationMethod(executionCourseTo.getExternalId(), executionCourseTo,
                        executionCourseFrom, null);
            } else {
                throw new UnsupportedOperationException("Sorry, cannot import using " + importContentService);
            }
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }
    }

    public void prepareCurricularCourse(HttpServletRequest request) {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final String curricularCourseIDString = request.getParameter("curricularCourseID");
        if (executionCourse != null && curricularCourseIDString != null && curricularCourseIDString.length() > 0) {
            final CurricularCourse curricularCourse = findCurricularCourse(executionCourse, curricularCourseIDString);
            request.setAttribute("curricularCourse", curricularCourse);
        }
    }

    private CurricularCourse findCurricularCourse(final ExecutionCourse executionCourse, final String curricularCourseID) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.getExternalId().equals(curricularCourseID)) {
                return curricularCourse;
            }
        }
        return null;
    }

    // LESSON PLANNINGS
    public ActionForward submitDataToImportLessonPlannings(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final IViewState viewState = RenderUtils.getViewState();
        final ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();
        request.setAttribute("importLessonPlanningBean", bean);
        return forward(request, "/teacher/executionCourse/importLessonPlannings.jsp");
    }

    public ActionForward submitDataToImportLessonPlanningsPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final IViewState viewState = RenderUtils.getViewState();
        final ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();
        if (bean.getCurricularYear() == null || bean.getExecutionPeriod() == null || bean.getExecutionDegree() == null) {
            bean.setExecutionCourse(null);
            bean.setImportType(null);
            bean.setShift(null);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("importLessonPlanningBean", bean);
        return forward(request, "/teacher/executionCourse/importLessonPlannings.jsp");
    }

    public ActionForward prepareImportLessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionCourse executionCourseTo = (ExecutionCourse) request.getAttribute("executionCourse");
        request.setAttribute("importLessonPlanningBean", new ImportLessonPlanningsBean(executionCourseTo));
        return forward(request, "/teacher/executionCourse/importLessonPlannings.jsp");
    }

    public ActionForward importLessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState();
        ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();
        request.setAttribute("importLessonPlanningBean", bean);

        ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        ExecutionCourse executionCourseTo = bean.getExecutionCourseTo();
        ImportType importType = bean.getImportType();

        if (importType != null && importType.equals(ImportLessonPlanningsBean.ImportType.PLANNING)) {
            try {
                ImportLessonPlannings.runImportLessonPlannings(executionCourseTo.getExternalId(), executionCourseTo,
                        executionCourseFrom, null);
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            }

        } else if (importType != null && importType.equals(ImportLessonPlanningsBean.ImportType.SUMMARIES)) {
            return forward(request, "/teacher/executionCourse/importLessonPlannings.jsp");
        }

        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward importLessonPlanningsBySummaries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState();
        ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();

        ExecutionCourse executionCourseTo = bean.getExecutionCourseTo();
        Shift shiftFrom = bean.getShift();

        try {
            ImportLessonPlannings.runImportLessonPlannings(executionCourseTo.getExternalId(), executionCourseTo,
                    shiftFrom.getExecutionCourse(), shiftFrom);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        request.setAttribute("importLessonPlanningBean", bean);
        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward lessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        Map<ShiftType, List<LessonPlanning>> lessonPlanningsMap = new TreeMap<ShiftType, List<LessonPlanning>>();
        for (ShiftType shiftType : executionCourse.getShiftTypes()) {
            List<LessonPlanning> lessonPlanningsOrderedByOrder = executionCourse.getLessonPlanningsOrderedByOrder(shiftType);
            if (!lessonPlanningsOrderedByOrder.isEmpty()) {
                lessonPlanningsMap.put(shiftType, lessonPlanningsOrderedByOrder);
            }
        }
        request.setAttribute("lessonPlanningsMap", lessonPlanningsMap);
        return forward(request, "/teacher/executionCourse/lessonPlannings.jsp");
    }

    public ActionForward moveUpLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        LessonPlanning lessonPlanning = FenixFramework.getDomainObject(request.getParameter("lessonPlanningID"));
        try {
            MoveLessonPlanning.runMoveLessonPlanning(lessonPlanning.getExecutionCourse().getExternalId(), lessonPlanning,
                    (lessonPlanning.getOrderOfPlanning() - 1));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward moveDownLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        LessonPlanning lessonPlanning = FenixFramework.getDomainObject(request.getParameter("lessonPlanningID"));
        try {
            MoveLessonPlanning.runMoveLessonPlanning(lessonPlanning.getExecutionCourse().getExternalId(), lessonPlanning,
                    (lessonPlanning.getOrderOfPlanning() + 1));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward prepareCreateLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        request.setAttribute("lessonPlanningBean", new CreateLessonPlanningBean(executionCourse));
        return forward(request, "/teacher/executionCourse/createLessonPlanning.jsp");
    }

    public ActionForward prepareEditLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        LessonPlanning lessonPlanning = FenixFramework.getDomainObject(request.getParameter("lessonPlanningID"));
        request.setAttribute("lessonPlanning", lessonPlanning);
        return forward(request, "/teacher/executionCourse/createLessonPlanning.jsp");
    }

    public ActionForward createLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        IViewState viewState = RenderUtils.getViewState();
        final CreateLessonPlanningBean lessonPlanningBean = (CreateLessonPlanningBean) viewState.getMetaObject().getObject();

        try {
            CreateLessonPlanning.runCreateLessonPlanning(lessonPlanningBean.getExecutionCourse().getExternalId(),
                    lessonPlanningBean.getTitle(), lessonPlanningBean.getPlanning(), lessonPlanningBean.getLessonType(),
                    lessonPlanningBean.getExecutionCourse());
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("lessonPlanningBean", lessonPlanningBean);
            return forward(request, "/teacher/executionCourse/createLessonPlanning.jsp");
        }
        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward deleteLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        LessonPlanning lessonPlanning = FenixFramework.getDomainObject(request.getParameter("lessonPlanningID"));
        if (lessonPlanning != null) {
            try {
                DeleteLessonPlanning.runDeleteLessonPlanning(lessonPlanning.getExecutionCourse().getExternalId(), lessonPlanning,
                        null, null);
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            }
        }
        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward deleteLessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ShiftType lessonType = ShiftType.valueOf(request.getParameter("shiftType"));
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        if (lessonType != null && executionCourse != null) {
            try {
                DeleteLessonPlanning.runDeleteLessonPlanning(executionCourse.getExternalId(), null, executionCourse, lessonType);
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            }
        }
        return lessonPlannings(mapping, form, request, response);
    }

    protected Curriculum findCurriculum(final ExecutionCourse executionCourse, final String curriculumID) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            for (final Curriculum curriculum : curricularCourse.getAssociatedCurriculumsSet()) {
                if (curriculum.getExternalId().equals(curriculumID)) {
                    return curriculum;
                }
            }
        }
        return null;
    }

    public ActionForward prepareImportSectionsPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        prepareImportContentPostBack(request);
        return forward(request, "/teacher/executionCourse/site/importSections.jsp");
    }

    public ActionForward prepareImportSectionsInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        prepareImportContentInvalid(request);
        return forward(request, "/teacher/executionCourse/site/importSections.jsp");
    }

    public ActionForward listExecutionCoursesToImportSections(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        listExecutionCoursesToImportContent(request);
        return forward(request, "/teacher/executionCourse/site/importSections.jsp");
    }

    public ActionForward importSections(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        importContent(request, "ImportSections");
        return new ActionForward("/manageExecutionCourseSite.do?method=sections");
    }

    public ActionForward prepareImportSections(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("importContentBean", new ImportContentBean());
        return forward(request, "/teacher/executionCourse/site/importSections.jsp");
    }

    @Override
    public ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        return (ExecutionCourse) request.getAttribute("executionCourse");
    }

    public ActionForward manageShifts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String executionCourseID = request.getParameter("executionCourseID");

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
        SortedSet<Shift> shifts = executionCourse.getShiftsOrderedByLessons();

        request.setAttribute("shifts", shifts);
        request.setAttribute("executionCourseID", executionCourseID);

        return forward(request, "/teacher/executionCourse/manageShifts.jsp");
    }

    public ActionForward editShift(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String shiftID = request.getParameter("shiftID");
        String executionCourseID = request.getParameter("executionCourseID");
        String registrationID = request.getParameter("registrationID");

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        Shift shift = FenixFramework.getDomainObject(shiftID);
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

        if (registrationID != null) {
            Registration registration = FenixFramework.getDomainObject(registrationID);
            shift.removeAttendFromShift(registration, executionCourse);
            request.setAttribute("registration", registration);
        }

        List<Registration> registrations = new ArrayList<Registration>();
        registrations.addAll(shift.getStudentsSet());
        Collections.sort(registrations, Registration.NUMBER_COMPARATOR);

        request.setAttribute("registrations", registrations);
        request.setAttribute("shift", shift);
        request.setAttribute("executionCourseID", executionCourseID);

        request.setAttribute("personBean", new PersonBean());

        return forward(request, "/teacher/executionCourse/editShift.jsp");
    }

    @Atomic
    public ActionForward insertStudentInShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        PersonBean bean = getRenderedObject("personBean");
        String id = bean.getUsername();
        Person person = null;
        final User user = User.findByUsername(id);
        if (user != null) {
            person = user.getPerson();
        } else {
            try {
                final Student student = Student.readStudentByNumber(Integer.valueOf(id));
                if (student != null) {
                    person = student.getPerson();
                }
            } catch (NumberFormatException e) {
            }
        }
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(request.getParameter("executionCourseID"));

        final ActionErrors actionErrors = new ActionErrors();
        if (person != null) {
            try {
                ShiftEnrollmentErrorReport errorReport =
                        new EnrollStudentInShifts().run(executionCourse.getRegistration(person), request.getParameter("shiftID"));
                if (errorReport.getUnAvailableShifts().size() > 0) {
                    actionErrors.add("error", new ActionMessage("error.exception.shift.full"));
                }
            } catch (FenixServiceException e) {
                if (e.getMessage() != null) {
                    actionErrors.add("error", new ActionMessage(e.getMessage()));
                } else {
                    actionErrors.add("error", new ActionMessage("label.invalid.student.number"));
                }
            } catch (DomainException e) {
                actionErrors.add("error", new ActionMessage(e.getLocalizedMessage(), false));
            }
        } else {
            actionErrors.add("error", new ActionMessage("label.invalid.student.number"));
        }
        saveErrors(request, actionErrors);
        return editShift(mapping, form, request, response);
    }

    public ActionForward removeAttendsFromShift(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String shiftID = request.getParameter("shiftID");
        String registrationID = request.getParameter("registrationID");
        String executionCourseID = request.getParameter("executionCourseID");
        String removeAll = request.getParameter("removeAll");

        Shift shift = FenixFramework.getDomainObject(shiftID);

        if (removeAll != null) {
            request.setAttribute("removeAll", removeAll);
        } else {
            Registration registration = FenixFramework.getDomainObject(registrationID);
            request.setAttribute("registration", registration);
        }

        request.setAttribute("shift", shift);
        request.setAttribute("executionCourseID", executionCourseID);

        return forward(request, "/teacher/executionCourse/removeAttendsFromShift.jsp");
    }

    public ActionForward removeAllAttendsFromShift(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String executionCourseID = request.getParameter("executionCourseID");
        String shiftID = request.getParameter("shiftID");

        Shift shift = FenixFramework.getDomainObject(shiftID);
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

        for (Registration registration : shift.getStudentsSet()) {
            shift.removeAttendFromShift(registration, executionCourse);
        }

        request.setAttribute("shift", shift);
        request.setAttribute("executionCourseID", executionCourseID);
        request.setAttribute("registrations", shift.getStudentsSet());
        request.setAttribute("personBean", new PersonBean());

        return forward(request, "/teacher/executionCourse/editShift.jsp");
    }
}