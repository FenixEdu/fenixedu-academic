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
/*
 * Created on May 18, 2006
 */
package org.fenixedu.academic.ui.struts.action.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationConfiguration;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.FinalMark;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean;
import org.fenixedu.academic.dto.teacher.gradeSubmission.MarkSheetTeacherMarkBean;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.administrativeOffice.gradeSubmission.CreateMarkSheetByTeacher;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@Mapping(path = "/markSheetManagement", module = "teacher", functionality = ManageExecutionCourseDA.class)
@Forwards(@Forward(name = "mainPage", path = "/teacher/evaluation/finalEvaluationIndex.faces"))
public class MarkSheetTeacherManagementDispatchAction extends ManageExecutionCourseDA {

    private ActionForward doForward(HttpServletRequest request, String path) {
        request.setAttribute("teacher$actual$page", path);
        return new ActionForward("/evaluation/evaluationFrame.jsp");
    }

    private void addMessage(HttpServletRequest request, ActionMessages actionMessages, String keyMessage, String... args) {
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(keyMessage, args));
        saveMessages(request, actionMessages);
    }

    public ActionForward evaluationIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return doForward(request, "/teacher/evaluation/evaluationIndex.jsp");
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("submissionBean", getObjectFromViewState("submissionBean-invisible"));
        RenderUtils.invalidateViewState();
        return doForward(request, "/teacher/evaluation/gradeSubmission/gradeSubmissionStepTwo.jsp");
    }

    public ActionForward prepareSubmitMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        if (!executionCourse.getAvailableGradeSubmission()) {
            addActionMessage(request, "error.teacher.gradeSubmission.gradeSubmission.not.available");
            return mapping.findForward("mainPage");
        }

        MarkSheetTeacherGradeSubmissionBean submissionBean = new MarkSheetTeacherGradeSubmissionBean();
        submissionBean.setExecutionCourse(executionCourse);

        request.setAttribute("submissionBean", submissionBean);
        return doForward(request, "/teacher/evaluation/gradeSubmission/gradeSubmissionStepOne.jsp");
    }

    public ActionForward gradeSubmissionStepOne(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetTeacherGradeSubmissionBean submissionBean =
                (MarkSheetTeacherGradeSubmissionBean) RenderUtils.getViewState().getMetaObject().getObject();
        request.setAttribute("submissionBean", submissionBean);

        ActionMessages actionMessages = new ActionMessages();
        boolean canSubmitMarksAnyCurricularCourse =
                checkIfCanSubmitMarksToAnyCurricularCourse(submissionBean.getAllCurricularCourses(), submissionBean
                        .getExecutionCourse().getExecutionPeriod(), request, actionMessages);
        calculateMarksToSubmit(request, submissionBean);
        request.setAttribute("executionCourse", submissionBean.getExecutionCourse());
        if (submissionBean.getMarksToSubmit().isEmpty()) {
            addMessage(
                    request,
                    actionMessages,
                    (!canSubmitMarksAnyCurricularCourse) ? "error.teacher.gradeSubmission.noStudentsToSubmitMarksInPeriods" : "error.teacher.gradeSubmission.noStudentsToSubmitMarks");
            return doForward(request, "/teacher/evaluation/gradeSubmission/gradeSubmissionStepOne.jsp");
        }

        return doForward(request, "/teacher/evaluation/gradeSubmission/gradeSubmissionStepTwo.jsp");
    }

    public ActionForward gradeSubmissionStepTwo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        User userView = getUserView(request);
        MarkSheetTeacherGradeSubmissionBean submissionBean =
                (MarkSheetTeacherGradeSubmissionBean) RenderUtils.getViewState("submissionBean-invisible").getMetaObject()
                        .getObject();
        submissionBean.setResponsibleTeacher(userView.getPerson().getTeacher());

        ActionMessages actionMessages = new ActionMessages();
        request.setAttribute("executionCourse", submissionBean.getExecutionCourse());
        try {
            List<EnrolmentEvaluation> marksSubmited = CreateMarkSheetByTeacher.run(submissionBean);
            request.setAttribute("marksSubmited", marksSubmited);
            return doForward(request, "/teacher/evaluation/gradeSubmission/viewGradesSubmited.jsp");
        } catch (IllegalDataAccessException e) {
            addMessage(request, actionMessages, "error.notAuthorized");
        } catch (InvalidArgumentsServiceException e) {
            addMessage(request, actionMessages, e.getMessage());
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
        }

        request.setAttribute("submissionBean", submissionBean);
        return doForward(request, "/teacher/evaluation/gradeSubmission/gradeSubmissionStepTwo.jsp");
    }

    public ActionForward backToMainPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        return mapping.findForward("mainPage");
    }

    private void calculateMarksToSubmit(final HttpServletRequest request, final MarkSheetTeacherGradeSubmissionBean submissionBean) {
        final Collection<MarkSheetTeacherMarkBean> marksToSubmit = new HashSet<MarkSheetTeacherMarkBean>();
        final List<Student> studentsWithImpossibleEnrolments = new ArrayList<Student>();

        for (final Enrolment enrolment : getEnrolmentsNotInAnyMarkSheet(submissionBean)) {
            if (enrolment.isImpossible()) {
                final Student student = enrolment.getStudentCurricularPlan().getRegistration().getStudent();
                if (!studentsWithImpossibleEnrolments.contains(student)) {
                    studentsWithImpossibleEnrolments.add(student);
                }
            } else {
                Attends attends = enrolment.getAttendsByExecutionCourse(submissionBean.getExecutionCourse());
                if (attends != null) {
                    marksToSubmit.add(new MarkSheetTeacherMarkBean(attends, submissionBean.getEvaluationDate(), getMark(attends),
                            getEvaluationSeason(submissionBean, enrolment), getMark(attends).length() != 0));
                }
            }

        }

        submissionBean.setMarksToSubmit(marksToSubmit);
        request.setAttribute("studentsWithImpossibleEnrolments", studentsWithImpossibleEnrolments);
    }

    private EvaluationSeason getEvaluationSeason(MarkSheetTeacherGradeSubmissionBean submissionBean, Enrolment enrolment) {
        return enrolment.isImprovementForExecutionCourse(submissionBean.getExecutionCourse()) ? EvaluationSeason
                .readImprovementSeason() : enrolment.getEvaluationSeason();
    }

    private String getMark(Attends attends) {
        FinalMark finalMark = attends.getFinalMark();
        return (finalMark != null) ? finalMark.getMark() : "";
    }

    private Collection<Enrolment> getEnrolmentsNotInAnyMarkSheet(MarkSheetTeacherGradeSubmissionBean submissionBean) {
        Collection<Enrolment> enrolmentsNotInAnyMarkSheet = new HashSet<Enrolment>();
        for (CurricularCourse curricularCourse : submissionBean.getAllCurricularCourses()) {
            for (EvaluationSeason season : EvaluationConfiguration.getInstance().getEvaluationSeasonSet()) {
                if (season.isGradeSubmissionAvailable(curricularCourse, submissionBean.getExecutionCourse().getExecutionPeriod())) {
                    enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(season, submissionBean
                            .getExecutionCourse().getExecutionPeriod()));
                }
            }
        }
        return enrolmentsNotInAnyMarkSheet;
    }

    private boolean checkIfCanSubmitMarksToAnyCurricularCourse(Collection<CurricularCourse> curricularCourses,
            ExecutionSemester executionSemester, HttpServletRequest request, ActionMessages actionMessages) {
        boolean result = true;
        String dateFormat = "dd/MM/yyyy";
        for (CurricularCourse curricularCourse : curricularCourses) {
            if (!curricularCourse.isGradeSubmissionAvailableFor(executionSemester)) {
                ExecutionDegree executionDegree = curricularCourse.getExecutionDegreeFor(executionSemester.getExecutionYear());
                addMessage(request, actionMessages, "error.teacher.gradeSubmission.invalid.date.for.curricularCourse",
                        curricularCourse.getDegreeCurricularPlan().getName() + " > " + curricularCourse.getName());
                EvaluationSeason.all().forEach(season -> {
                    addMessageGradeSubmissionPeriods(request, actionMessages, dateFormat, season, executionDegree);
                });
                result = false;
            }
        }
        return result;
    }

    private void addMessageGradeSubmissionPeriods(HttpServletRequest request, ActionMessages actionMessages, String dateFormat,
            EvaluationSeason season, ExecutionDegree executionDegree) {
        String period =
                season.getGradeSubmissionPeriods(executionDegree, null)
                        .map(o -> o.getStartYearMonthDay().toString(dateFormat) + "-"
                                + o.getEndYearMonthDay().toString(dateFormat)).collect(Collectors.joining(", "));
        if (!Strings.isNullOrEmpty(period)) {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.dates", season.getName().getContent(), period);
        }
    }

    public ActionForward viewSubmitedMarkSheets(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        Collection<MarkSheet> associatedMarkSheets = executionCourse.getAssociatedMarkSheets();

        request.setAttribute("markSheets", associatedMarkSheets);
        request.setAttribute("executionCourseID", executionCourse.getExternalId());
        return doForward(request, "/teacher/evaluation/gradeSubmission/viewSubmitedMarkSheets.jsp");
    }

    public ActionForward viewMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        String markSheetID = request.getParameter("msID");
        MarkSheet markSheet = FenixFramework.getDomainObject(markSheetID);
        request.setAttribute("markSheet", markSheet);
        request.setAttribute("executionCourseID", executionCourse.getExternalId());
        return doForward(request, "/teacher/evaluation/gradeSubmission/viewMarkSheet.jsp");
    }
}