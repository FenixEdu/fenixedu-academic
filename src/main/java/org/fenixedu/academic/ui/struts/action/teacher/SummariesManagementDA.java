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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonPlanning;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.ShowSummariesBean;
import org.fenixedu.academic.dto.ShowSummariesBean.ListSummaryType;
import org.fenixedu.academic.dto.ShowSummariesBean.SummariesOrder;
import org.fenixedu.academic.dto.SummariesCalendarBean;
import org.fenixedu.academic.dto.SummariesCalendarBean.LessonCalendarViewType;
import org.fenixedu.academic.dto.SummariesManagementBean;
import org.fenixedu.academic.dto.SummariesManagementBean.SummaryType;
import org.fenixedu.academic.dto.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean;
import org.fenixedu.academic.dto.teacher.executionCourse.SummaryTeacherBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.teacher.CreateSummary;
import org.fenixedu.academic.service.services.teacher.DeleteSummary;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Manuel Pinto
 * 
 *         Sep/2006 Fenix
 * 
 */
@Mapping(module = "teacher", path = "/summariesManagement", formBean = "summariesManagementForm",
        functionality = ManageExecutionCourseDA.class)
@Forwards({ @Forward(name = "prepareInsertSummary", path = "/teacher/executionCourse/createSummary.jsp"),
        @Forward(name = "prepareShowSummaries", path = "/teacher/executionCourse/showSummaries.jsp"),
        @Forward(name = "showSummariesCalendar", path = "/teacher/executionCourse/showSummariesCalendar.jsp"),
        @Forward(name = "prepareInsertComplexSummary", path = "/teacher/executionCourse/createComplexSummary.jsp") })
public class SummariesManagementDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState();
        ExecutionCourse executionCourse = null;

        if (viewState != null && viewState.getMetaObject().getObject() instanceof SummariesManagementBean) {
            executionCourse = ((SummariesManagementBean) viewState.getMetaObject().getObject()).getExecutionCourse();
        } else if (viewState != null && viewState.getMetaObject().getObject() instanceof ShowSummariesBean) {
            executionCourse = ((ShowSummariesBean) viewState.getMetaObject().getObject()).getExecutionCourse();
        } else if (viewState != null && viewState.getMetaObject().getObject() instanceof NextPossibleSummaryLessonsAndDatesBean) {
            executionCourse = ((NextPossibleSummaryLessonsAndDatesBean) viewState.getMetaObject().getObject()).getLesson()
                    .getShift().getDisciplinaExecucao();
        } else {
            executionCourse = readAndSaveExecutionCourse(request);
        }

        String teacherId = request.getParameter("teacherId_");

        Teacher loggedTeacher;
        Professorship loggedProfessorship;
        if (!StringUtils.isEmpty(teacherId)) {
            loggedTeacher = Teacher.readByIstId(teacherId);
            loggedProfessorship = loggedTeacher.getProfessorshipByExecutionCourse(executionCourse);
        } else {
            loggedProfessorship = AccessControl.getPerson().getProfessorshipByExecutionCourse(executionCourse);
        }

        if (loggedProfessorship == null) {
            throw new FenixActionException("error.summariesManagement.empty.loggedProfessorship");
        }

        request.setAttribute("loggedTeacherProfessorship", loggedProfessorship);
        request.setAttribute("loggedIsResponsible", loggedProfessorship.isResponsibleFor());
        request.setAttribute("executionCourse", executionCourse);
        request.setAttribute("executionCourseID", executionCourse.getExternalId());
        request.setAttribute("professorship", loggedProfessorship);

        Map<String, Object> requestContext = new HashMap<String, Object>();
        requestContext.put("professorship", loggedProfessorship);
        requestContext.put("executionCourse", executionCourse);
        PortalLayoutInjector.addContextExtension(requestContext);

        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareInsertSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        buildSummariesManagementBean(form, request, SummaryType.NORMAL_SUMMARY);
        return mapping.findForward("prepareInsertSummary");
    }

    public ActionForward prepareInsertTaughtSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        invalidateAndReloadView(request, "summariesManagementBeanWithNotTaughtSummary");
        return mapping.findForward("prepareInsertSummary");
    }

    public ActionForward prepareInsertNotTaughtSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        invalidateAndReloadView(request, "summariesManagementBeanWithSummary");
        return mapping.findForward("prepareInsertSummary");
    }

    public ActionForward prepareInsertExtraSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        buildSummariesManagementBean(form, request, SummaryType.EXTRA_SUMMARY);
        request.setAttribute("notShowLessonPlanningsAndSummaries", Boolean.TRUE);
        return mapping.findForward("prepareInsertSummary");
    }

    public ActionForward goToInsertSummaryAgain(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        final IViewState viewState = RenderUtils.getViewState();
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        readAndSaveTeacher(bean, (DynaActionForm) form, request, mapping);
        return goToSummaryManagementPageAgain(mapping, request, dynaActionForm, bean);
    }

    public ActionForward chooseLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithLessonPlanning");
        if (summaryViewState == null) {
            summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
        }

        SummariesManagementBean bean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
        LessonPlanning lessonPlanning = bean.getLessonPlanning();
        if (lessonPlanning != null) {
            bean.setSummaryText(lessonPlanning.getPlanning());
            bean.setTitle(lessonPlanning.getTitle());
            bean.setLastSummary(null);
        }

        return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
    }

    public ActionForward chooseLastSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithLastSummary");
        if (summaryViewState == null) {
            summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
        }

        SummariesManagementBean bean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
        Summary lastSummary = bean.getLastSummary();
        if (lastSummary != null) {
            bean.setSummaryText(lastSummary.getSummaryText());
            bean.setTitle(lastSummary.getTitle());
            bean.setLessonPlanning(null);
        }

        return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
    }

    public ActionForward chooseShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final IViewState viewState = RenderUtils.getViewState("summariesManagementBeanWithShifts");
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();

        bean.setLesson(null);
        bean.setSummaryDate(null);
        bean.setLessonType(null);

        if (bean.getSummaryType() != null && bean.getSummaryType().equals(SummaryType.EXTRA_SUMMARY)) {
            request.setAttribute("notShowLessonPlanningsAndSummaries", Boolean.TRUE);
        }

        return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
    }

    public ActionForward chooseLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final IViewState viewState = RenderUtils.getViewState("summariesManagementBeanWithLessons");
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();

        bean.setSummaryDate(null);
        bean.setLessonType(null);

        Lesson lesson = bean.getLesson();
        if (lesson != null && lesson.getShift().getCourseLoadsSet().size() == 1) {
            bean.setLessonType(lesson.getShift().getCourseLoadsSet().iterator().next().getType());
        }

        return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
    }

    public ActionForward chooseLessonType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final IViewState viewState = RenderUtils.getViewState("summariesManagementBeanWithLessonTypes");
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
    }

    public ActionForward chooseDate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        final IViewState viewState = RenderUtils.getViewState("summariesManagementBeanWithDate");
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        return goToSummaryManagementPageAgain(mapping, request, dynaActionForm, bean);
    }

    public ActionForward chooseSummaryType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        final IViewState viewState = RenderUtils.getViewState();
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();

        bean.setShift(null);
        bean.setLesson(null);
        bean.setSummaryDate(null);
        bean.setSummaryRoom(null);
        bean.setSummaryTime(null);
        bean.setLessonType(null);

        if (bean.getSummaryType() != null && bean.getSummaryType().equals(SummaryType.EXTRA_SUMMARY)) {
            request.setAttribute("notShowLessonPlanningsAndSummaries", Boolean.TRUE);
        }

        return goToSummaryManagementPageAgain(mapping, request, dynaActionForm, bean);
    }

    public ActionForward createSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState();
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        readAndSaveTeacher(bean, (DynaActionForm) form, request, mapping);

        if (bean.getTaught() == false) {
            bean.setTitle(new LocalizedString(I18N.getLocale(), "Not Taught."));
        }

        try {
            if (bean.getSummary() == null) {
                CreateSummary.runCreateSummary(bean);
            } else {
                CreateSummary.runEditSummary(bean);
            }
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
        } catch (NotAuthorizedException e) {
            addActionMessage(request, e.getMessage());
            return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
        }

        return prepareShowSummaries(mapping, form, request, response);
    }

    public ActionForward createSummaryAndNew(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState();
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        readAndSaveTeacher(bean, (DynaActionForm) form, request, mapping);

        try {
            CreateSummary.runCreateSummary(bean);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
        } catch (NotAuthorizedException e) {
            addActionMessage(request, e.getMessage());
            return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
        }

        RenderUtils.invalidateViewState();
        return prepareInsertSummary(mapping, form, request, response);
    }

    public ActionForward createSummaryAndSame(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState();
        SummariesManagementBean bean = (SummariesManagementBean) viewState.getMetaObject().getObject();
        readAndSaveTeacher(bean, (DynaActionForm) form, request, mapping);

        try {
            CreateSummary.runCreateSummary(bean);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
        } catch (NotAuthorizedException e) {
            addActionMessage(request, e.getMessage());
            return goToSummaryManagementPageAgain(mapping, request, (DynaActionForm) form, bean);
        }

        RenderUtils.invalidateViewState();
        DynaActionForm dynaActionForm = (DynaActionForm) form;

        Professorship loggedProfessorship = (Professorship) request.getAttribute("loggedTeacherProfessorship");
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        dynaActionForm.set("teacher", loggedProfessorship.getExternalId().toString());

        SummariesManagementBean newBean = new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY,
                executionCourse, loggedProfessorship, null);
        newBean.setSummaryText(bean.getSummaryText());
        newBean.setTitle(bean.getTitle());

        request.setAttribute("summariesManagementBean", newBean);
        return mapping.findForward("prepareInsertSummary");
    }

    public ActionForward prepareEditSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        Professorship teacherLogged = ((Professorship) request.getAttribute("loggedTeacherProfessorship"));
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Summary summary = getSummaryFromParameter(request);

        SummaryType summaryType;
        if (summary.isExtraSummary()) {
            summaryType = SummaryType.EXTRA_SUMMARY;
            request.setAttribute("notShowLessonPlanningsAndSummaries", Boolean.TRUE);
        } else {
            summaryType = SummaryType.NORMAL_SUMMARY;
        }

        DateTimeFieldType[] dateTimeFieldTypes = { DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour() };
        HourMinuteSecond time = summary.getSummaryHourHourMinuteSecond();
        int[] timeArray = { time.getHour(), time.getMinuteOfHour() };
        Partial timePartial = new Partial(dateTimeFieldTypes, timeArray);

        SummariesManagementBean bean = new SummariesManagementBean(summary.getTitle(), summary.getSummaryText(),
                summary.getStudentsNumber(), summaryType, summary.getProfessorship(), summary.getTeacherName(),
                summary.getTeacher(), summary.getShift(), summary.getLesson(), summary.getSummaryDateYearMonthDay(),
                summary.getRoom(), timePartial, summary, teacherLogged, summary.getSummaryType(), summary.getTaught());

        return goToSummaryManagementPageAgain(mapping, request, dynaActionForm, bean);
    }

    public ActionForward deleteSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        Summary summary = getSummaryFromParameter(request);
        Professorship professorshipLogged = (Professorship) request.getAttribute("loggedTeacherProfessorship");
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        try {
            DeleteSummary.runDeleteSummary(executionCourse, summary, professorshipLogged);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return prepareShowSummaries(mapping, form, request, response);
        } catch (NotAuthorizedException e) {
            addActionMessage(request, e.getMessage());
            return prepareShowSummaries(mapping, form, request, response);
        }

        return prepareShowSummaries(mapping, form, request, response);
    }

    public ActionForward prepareShowSummaries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        Professorship professorshipLogged = (Professorship) request.getAttribute("loggedTeacherProfessorship");
        Set<Summary> teacherSummaries = new TreeSet<Summary>(Summary.COMPARATOR_BY_DATE_AND_HOUR);
        teacherSummaries.addAll(professorshipLogged.getAssociatedSummariesSet());

        readAndSaveNextPossibleSummaryLessonsAndDates(request, executionCourse);
        request.setAttribute("showSummariesBean", new ShowSummariesBean(new SummaryTeacherBean(professorshipLogged),
                executionCourse, ListSummaryType.ALL_CONTENT, professorshipLogged));
        if (professorshipLogged.getTeacher() != null) {
            request.setAttribute("teacherId", professorshipLogged.getTeacher().getPerson().getUsername());
        }
        request.setAttribute("summaries", teacherSummaries);
        return mapping.findForward("prepareShowSummaries");
    }

    public ActionForward showSummariesPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final IViewState viewState = RenderUtils.getViewState();
        ShowSummariesBean bean = (ShowSummariesBean) viewState.getMetaObject().getObject();

        ExecutionCourse executionCourse = bean.getExecutionCourse();
        ShiftType shiftType = bean.getShiftType();
        Shift shift = bean.getShift();

        SummaryTeacherBean summaryTeacher = bean.getSummaryTeacher();
        Professorship teacher = (summaryTeacher != null) ? summaryTeacher.getProfessorship() : null;
        Boolean otherTeachers = (summaryTeacher != null) ? summaryTeacher.getOthers() : null;
        SummariesOrder summariesOrder = bean.getSummariesOrder();

        Set<Summary> summariesToShow =
                summariesOrder == null || summariesOrder.equals(SummariesOrder.DECREASING) ? new TreeSet<Summary>(
                        Summary.COMPARATOR_BY_DATE_AND_HOUR) : new TreeSet<Summary>(
                                new ReverseComparator(Summary.COMPARATOR_BY_DATE_AND_HOUR));

        for (Summary summary : executionCourse.getAssociatedSummariesSet()) {
            boolean insert = true;
            if ((shift != null && (summary.getShift() == null || !summary.getShift().equals(shift)))
                    || (teacher != null && (summary.getProfessorship() == null || !summary.getProfessorship().equals(teacher)))
                    || (shiftType != null && (summary.getSummaryType() == null || !summary.getSummaryType().equals(shiftType)))
                    || (otherTeachers != null && otherTeachers && summary.getProfessorship() != null)) {
                insert = false;
            }
            if (insert) {
                summariesToShow.add(summary);
            }
        }

        readAndSaveNextPossibleSummaryLessonsAndDates(request, executionCourse);
        request.setAttribute("showSummariesBean", bean);
        request.setAttribute("summaries", summariesToShow);
        return mapping.findForward("prepareShowSummaries");
    }

    public ActionForward prepareCreateComplexSummaryInSummariesCalendarMode(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        Professorship loggedProfessorship = (Professorship) request.getAttribute("loggedTeacherProfessorship");
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        NextPossibleSummaryLessonsAndDatesBean nextSummaryDateBean = getNextSummaryDateBeanFromParameter(request);

        if (!getLessons(executionCourse).contains(nextSummaryDateBean.getLesson())) {
            throw new FenixActionException();
        }

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        List<NextPossibleSummaryLessonsAndDatesBean> nextPossibleLessonsDates =
                new ArrayList<NextPossibleSummaryLessonsAndDatesBean>();
        nextPossibleLessonsDates.add(nextSummaryDateBean);
        SummariesManagementBean bean = new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY,
                executionCourse, loggedProfessorship, nextPossibleLessonsDates);

        Shift shift = nextSummaryDateBean.getLesson().getShift();
        if (shift.getCourseLoadsSet().size() != 1) {
            request.setAttribute("notShowLessonPlanningsAndSummaries", Boolean.TRUE);
        } else {
            nextSummaryDateBean.setLessonType(shift.getCourseLoadsSet().iterator().next().getType());
            bean.setLessonType(nextSummaryDateBean.getLessonType());
        }

        request.setAttribute("summariesManagementBean", bean);
        dynaActionForm.set("teacher", loggedProfessorship.getExternalId().toString());
        return mapping.findForward("prepareInsertComplexSummary");
    }

    public ActionForward prepareCreateTaughtComplexSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        invalidateAndReloadView(request, "summariesManagementBeanWithNotTaughtSummary");
        return mapping.findForward("prepareInsertComplexSummary");
    }

    private void invalidateAndReloadView(HttpServletRequest request, String view) {
        final IViewState summaryViewState = RenderUtils.getViewState(view);
        SummariesManagementBean summaryBean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
        if (summaryBean.getTitle().equals(new LocalizedString(I18N.getLocale(), "Not Taught."))) {
            summaryBean.setTitle(new LocalizedString(I18N.getLocale(), ""));
        }
        RenderUtils.invalidateViewState(view);
        request.setAttribute("summariesManagementBean", summaryBean);
    }

    public ActionForward prepareCreateNotTaughtComplexSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        invalidateAndReloadView(request, "summariesManagementBeanWithSummary");
        return mapping.findForward("prepareInsertComplexSummary");
    }

    public ActionForward prepareCreateComplexSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Professorship loggedProfessorship = (Professorship) request.getAttribute("loggedTeacherProfessorship");
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String[] selectedLessons = request.getParameterValues("selectedLessonAndDate");

        if (selectedLessons == null || selectedLessons.length == 0) {
            return goToInsertComplexSummaryAgain(request, mapping, response, form);
        } else if (selectedLessons != null && selectedLessons.length != 0) {

            boolean uniqueType = true;
            ShiftType shiftType = null;
            List<NextPossibleSummaryLessonsAndDatesBean> nextPossibleLessonsDates =
                    new ArrayList<NextPossibleSummaryLessonsAndDatesBean>();

            for (String lessonRepresentation : selectedLessons) {

                NextPossibleSummaryLessonsAndDatesBean nextLesson =
                        NextPossibleSummaryLessonsAndDatesBean.getNewInstance(lessonRepresentation);
                if (nextLesson.getLesson().getShift().getCourseLoadsSet().size() == 1) {
                    nextLesson.setLessonType(nextLesson.getLesson().getShift().getCourseLoadsSet().iterator().next().getType());
                }

                nextPossibleLessonsDates.add(nextLesson);

                ShiftType lessonType = null;
                if (nextLesson.getLesson().getShift().getCourseLoadsSet().size() == 1) {
                    lessonType = nextLesson.getLesson().getShift().getCourseLoadsSet().iterator().next().getType();
                    if (shiftType == null) {
                        shiftType = lessonType;
                    } else if (!shiftType.equals(lessonType)) {
                        request.setAttribute("notShowLessonPlanningsAndSummaries", Boolean.TRUE);
                        uniqueType = false;
                    }
                } else {
                    request.setAttribute("notShowLessonPlanningsAndSummaries", Boolean.TRUE);
                    uniqueType = false;
                }
            }

            SummariesManagementBean bean = new SummariesManagementBean(SummariesManagementBean.SummaryType.NORMAL_SUMMARY,
                    executionCourse, loggedProfessorship, nextPossibleLessonsDates);
            bean.setTaught(true);

            if (uniqueType) {
                bean.setLessonType(shiftType);
            }

            request.setAttribute("summariesManagementBean", bean);
            dynaActionForm.set("teacher", loggedProfessorship.getExternalId().toString());
            return mapping.findForward("prepareInsertComplexSummary");
        }

        return prepareShowSummaries(mapping, form, request, response);
    }

    public ActionForward createComplexSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        SummariesManagementBean summaryBean = getSummariesManagementBean();
        readAndSaveTeacher(summaryBean, (DynaActionForm) form, request, mapping);

        for (NextPossibleSummaryLessonsAndDatesBean bean : summaryBean.getNextPossibleSummaryLessonsAndDatesBean()) {

            summaryBean.setShift(bean.getLesson().getShift());
            summaryBean.setLesson(bean.getLesson());
            summaryBean.setSummaryDate(bean.getDate());
            summaryBean.setStudentsNumber(bean.getStudentsNumber());
            summaryBean.setLessonType(bean.getLessonType());

            if (summaryBean.getTaught() == false) {
                summaryBean.setTitle(new LocalizedString(I18N.getLocale(), "Not Taught."));
            }

            try {
                CreateSummary.runCreateSummary(summaryBean);

            } catch (DomainException e) {
                return returnToCreateComplexSummary(mapping, form, request, summaryBean, e);

            } catch (NotAuthorizedException e) {
                return returnToCreateComplexSummary(mapping, form, request, summaryBean, e);
            }
        }

        return prepareShowSummaries(mapping, form, request, response);
    }

    private SummariesManagementBean getSummariesManagementBean() {
        final IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
        if (summaryViewState != null) {
            return (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
        }
        final IViewState notTaughtSummaryViewState = RenderUtils.getViewState("summariesManagementBeanWithNotTaughtSummary");
        if (notTaughtSummaryViewState != null) {
            return (SummariesManagementBean) notTaughtSummaryViewState.getMetaObject().getObject();
        }
        return null;
    }

    public ActionForward chooseLessonPlanningToCreateComplexSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithLessonPlanning");
        if (summaryViewState == null) {
            summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
        }

        SummariesManagementBean summaryBean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
        LessonPlanning lessonPlanning = summaryBean.getLessonPlanning();
        if (lessonPlanning != null) {
            summaryBean.setSummaryText(lessonPlanning.getPlanning());
            summaryBean.setTitle(lessonPlanning.getTitle());
            summaryBean.setLastSummary(null);
        }

        return returnToCreateComplexSummary(mapping, form, request, summaryBean, null);
    }

    public ActionForward chooseLastSummaryToCreateComplexSummary(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithLastSummary");
        if (summaryViewState == null) {
            summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
        }

        SummariesManagementBean summaryBean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
        Summary lastSummary = summaryBean.getLastSummary();
        if (lastSummary != null) {
            summaryBean.setSummaryText(lastSummary.getSummaryText());
            summaryBean.setTitle(lastSummary.getTitle());
            summaryBean.setLessonPlanning(null);
        }

        return returnToCreateComplexSummary(mapping, form, request, summaryBean, null);
    }

    public ActionForward showSummariesCalendar(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourse executionCourse = getExecutinCourseFromParameter(request);
        SummariesCalendarBean summariesCalendarBean = new SummariesCalendarBean(executionCourse);
        Set<NextPossibleSummaryLessonsAndDatesBean> summariesCalendar = new TreeSet<NextPossibleSummaryLessonsAndDatesBean>(
                NextPossibleSummaryLessonsAndDatesBean.COMPARATOR_BY_DATE_AND_HOUR);
        Set<Shift> associatedShifts = executionCourse.getAssociatedShifts();

        for (Shift shift : associatedShifts) {
            for (Lesson lesson : shift.getAssociatedLessonsSet()) {
                for (YearMonthDay lessonDate : lesson.getAllLessonDates()) {
                    summariesCalendar.add(new NextPossibleSummaryLessonsAndDatesBean(lesson, lessonDate));
                }
            }
            addExtraSummariesToSummariesCalendar(summariesCalendar, shift, summariesCalendarBean.getCalendarViewType());
        }

        request.setAttribute("showSummariesCalendarBean", summariesCalendarBean);
        request.setAttribute("summariesCalendarResult", summariesCalendar);
        return mapping.findForward("showSummariesCalendar");
    }

    public ActionForward showSummariesCalendarPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final IViewState viewState = RenderUtils.getViewState();
        SummariesCalendarBean bean = (SummariesCalendarBean) viewState.getMetaObject().getObject();

        ExecutionCourse executionCourse = bean.getExecutionCourse();
        Shift shift = bean.getShift();
        ShiftType shiftType = bean.getShiftType();
        LessonCalendarViewType calendarViewType = bean.getCalendarViewType();

        Set<NextPossibleSummaryLessonsAndDatesBean> summariesCalendar = new TreeSet<NextPossibleSummaryLessonsAndDatesBean>(
                NextPossibleSummaryLessonsAndDatesBean.COMPARATOR_BY_DATE_AND_HOUR);

        for (Lesson lesson : getLessons(executionCourse)) {

            boolean insert = true;
            if ((shift != null && !shift.getAssociatedLessonsSet().contains(lesson))
                    || (shiftType != null && !lesson.getShift().containsType(shiftType))) {
                insert = false;
            }

            if (insert) {
                for (YearMonthDay lessonDate : lesson.getAllLessonDates()) {
                    if ((calendarViewType.equals(LessonCalendarViewType.ALL_LESSONS))
                            || (calendarViewType.equals(LessonCalendarViewType.PAST_LESSON)
                                    && lesson.isTimeValidToInsertSummary(new HourMinuteSecond(), lessonDate))
                            || (calendarViewType.equals(LessonCalendarViewType.PAST_LESSON_WITHOUT_SUMMARY)
                                    && lesson.getSummaryByDate(lessonDate) == null)
                                    && lesson.isTimeValidToInsertSummary(new HourMinuteSecond(), lessonDate)) {

                        summariesCalendar.add(new NextPossibleSummaryLessonsAndDatesBean(lesson, lessonDate));
                    }
                }
            }
        }

        // Extra Summaries
        if (shift != null) {
            if (shiftType == null) {
                addExtraSummariesToSummariesCalendar(summariesCalendar, shift, calendarViewType);
            }
        } else {
            for (Shift executionCourseShift : executionCourse.getAssociatedShifts()) {
                if (shiftType == null) {
                    addExtraSummariesToSummariesCalendar(summariesCalendar, executionCourseShift, calendarViewType);
                }
            }
        }

        request.setAttribute("summariesCalendarResult", summariesCalendar);
        request.setAttribute("showSummariesCalendarBean", bean);
        return mapping.findForward("showSummariesCalendar");
    }

    // -------- Private Methods --------- //

    private void addExtraSummariesToSummariesCalendar(Set<NextPossibleSummaryLessonsAndDatesBean> summariesCalendar, Shift shift,
            LessonCalendarViewType calendarViewType) {

        if (!calendarViewType.equals(LessonCalendarViewType.PAST_LESSON_WITHOUT_SUMMARY)) {
            List<Summary> extraSummaries = shift.getExtraSummaries();
            for (Summary summary : extraSummaries) {
                if (calendarViewType.equals(LessonCalendarViewType.ALL_LESSONS)
                        || (calendarViewType.equals(LessonCalendarViewType.PAST_LESSON)
                                && summary.getSummaryDateTime().isBeforeNow())) {
                    summariesCalendar.add(new NextPossibleSummaryLessonsAndDatesBean(shift, summary.getSummaryDateYearMonthDay(),
                            summary.getSummaryHourHourMinuteSecond(), summary.getRoom()));
                }
            }
        }
    }

    private void buildSummariesManagementBean(ActionForm form, HttpServletRequest request, SummaryType summaryType) {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        Professorship loggedProfessorship = (Professorship) request.getAttribute("loggedTeacherProfessorship");
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        dynaActionForm.set("teacher", loggedProfessorship.getExternalId().toString());
        request.setAttribute("summariesManagementBean",
                new SummariesManagementBean(summaryType, executionCourse, loggedProfessorship, null));
    }

    private ActionForward goToInsertComplexSummaryAgain(HttpServletRequest request, ActionMapping mapping,
            HttpServletResponse response, ActionForm form) {

        final IViewState summaryViewState = RenderUtils.getViewState("summariesManagementBeanWithSummary");
        if (summaryViewState != null) {
            SummariesManagementBean summaryBean = (SummariesManagementBean) summaryViewState.getMetaObject().getObject();
            readAndSaveTeacher(summaryBean, (DynaActionForm) form, request, mapping);
            return returnToCreateComplexSummary(mapping, form, request, summaryBean, null);
        }
        return prepareShowSummaries(mapping, form, request, response);
    }

    private ActionForward returnToCreateComplexSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            SummariesManagementBean summaryBean, Exception e) {

        if (e != null) {
            addActionMessage(request, e.getMessage());
        }
        setTeacherDataToFormBean((DynaActionForm) form, summaryBean);
        request.setAttribute("summariesManagementBean", summaryBean);
        return mapping.findForward("prepareInsertComplexSummary");
    }

    private void readAndSaveTeacher(SummariesManagementBean bean, DynaActionForm dynaActionForm, HttpServletRequest request,
            ActionMapping mapping) {

        Professorship professorship = getProfessorshipFromParameter(request);
        if (professorship != null) {
            bean.setProfessorship(professorship);
            bean.setTeacher(null);
            bean.setTeacherName("");

        } else if (dynaActionForm.getString("teacher").equals("-1")
                && !StringUtils.isEmpty(dynaActionForm.getString("teacherName"))) {
            bean.setTeacherName(dynaActionForm.getString("teacherName"));
            bean.setTeacher(null);
            bean.setProfessorship(null);

        } else if (dynaActionForm.getString("teacher").equals("0")
                && !StringUtils.isEmpty(dynaActionForm.getString("teacherId"))) {
            Teacher teacher = null;
            try {
                teacher = Teacher.readByIstId(dynaActionForm.getString("teacherId"));
            } catch (NumberFormatException e) {
                addActionMessage(request, "error.summary.teacherNumber.invalid");
            }

            if (teacher == null) {
                addActionMessage(request, "error.summary.inexistent.teacher");
            }

            bean.setTeacher(teacher);
            bean.setTeacherName(null);
            bean.setProfessorship(null);
        }
    }

    private void readAndSaveNextPossibleSummaryLessonsAndDates(HttpServletRequest request, ExecutionCourse executionCourse) {
        Set<NextPossibleSummaryLessonsAndDatesBean> possibleLessonsAndDates = new TreeSet<NextPossibleSummaryLessonsAndDatesBean>(
                NextPossibleSummaryLessonsAndDatesBean.COMPARATOR_BY_DATE_AND_HOUR);
        for (Shift shift : executionCourse.getAssociatedShifts()) {
            for (Lesson lesson : shift.getAssociatedLessonsSet()) {
                YearMonthDay nextPossibleSummaryDate = lesson.getNextPossibleSummaryDate();
                if (nextPossibleSummaryDate != null) {
                    possibleLessonsAndDates.add(new NextPossibleSummaryLessonsAndDatesBean(lesson, nextPossibleSummaryDate));
                }
            }
        }
        request.setAttribute("nextPossibleLessonsDates", possibleLessonsAndDates);
    }

    private ActionForward goToSummaryManagementPageAgain(ActionMapping mapping, HttpServletRequest request,
            DynaActionForm dynaActionForm, SummariesManagementBean bean) {

        setTeacherDataToFormBean(dynaActionForm, bean);
        request.setAttribute("summariesManagementBean", bean);
        return mapping.findForward("prepareInsertSummary");
    }

    private void setTeacherDataToFormBean(DynaActionForm dynaActionForm, SummariesManagementBean bean) {
        if (!bean.getTeacherChoose().equals("")) {
            dynaActionForm.set("teacher", bean.getTeacherChoose());
            dynaActionForm.set("teacherName", (bean.getTeacherName() != null) ? bean.getTeacherName() : "");
            dynaActionForm.set("teacherId",
                    (bean.getTeacher() != null) ? String.valueOf(bean.getTeacher().getPerson().getUsername()) : "");
        }
    }

    private ExecutionCourse readAndSaveExecutionCourse(HttpServletRequest request) {
        ExecutionCourse executionCourse = getExecutinCourseFromParameter(request);
        request.setAttribute("executionCourse", executionCourse);
        return executionCourse;
    }

    private ExecutionCourse getExecutinCourseFromParameter(final HttpServletRequest request) {
        final String executionCourseIDString = request.getParameterMap().containsKey("executionCourseID") ? request
                .getParameter("executionCourseID") : (String) request.getAttribute("executionCourseID");
        return FenixFramework.getDomainObject(executionCourseIDString);
    }

    private Summary getSummaryFromParameter(final HttpServletRequest request) {
        final String summaryIDString = request.getParameterMap().containsKey("summaryID") ? request
                .getParameter("summaryID") : (String) request.getAttribute("summaryID");
        return FenixFramework.getDomainObject(summaryIDString);
    }

    private NextPossibleSummaryLessonsAndDatesBean getNextSummaryDateBeanFromParameter(final HttpServletRequest request) {
        final String summaryDateString = request.getParameterMap().containsKey("summaryDate") ? request
                .getParameter("summaryDate") : (String) request.getAttribute("summaryDate");
        return StringUtils.isEmpty(summaryDateString) ? null : NextPossibleSummaryLessonsAndDatesBean
                .getNewInstance(summaryDateString);
    }

    private Professorship getProfessorshipFromParameter(final HttpServletRequest request) {
        final String professorshipIDString = request.getParameterMap().containsKey("teacher") ? request
                .getParameter("teacher") : (String) request.getAttribute("teacher");
        if (!StringUtils.isEmpty(professorshipIDString)
                && !(professorshipIDString.equals("0") || professorshipIDString.equals("-1"))) {
            return FenixFramework.getDomainObject(professorshipIDString);
        }
        return null;
    }

    private static Set<Lesson> getLessons(ExecutionCourse executionCourse) {
        final Set<Lesson> lessons = new HashSet<Lesson>();
        for (final Shift shift : executionCourse.getAssociatedShifts()) {
            lessons.addAll(shift.getAssociatedLessonsSet());
        }
        return lessons;
    }
}
