/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on May 18, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission.CreateMarkSheetByTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherMarkBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FinalMark;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

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
                            getEnrolmentEvaluationType(submissionBean, enrolment), getMark(attends).length() != 0));
                }
            }

        }

        submissionBean.setMarksToSubmit(marksToSubmit);
        request.setAttribute("studentsWithImpossibleEnrolments", studentsWithImpossibleEnrolments);
    }

    private EnrolmentEvaluationType getEnrolmentEvaluationType(MarkSheetTeacherGradeSubmissionBean submissionBean,
            Enrolment enrolment) {
        return enrolment.isImprovementForExecutionCourse(submissionBean.getExecutionCourse()) ? EnrolmentEvaluationType.IMPROVEMENT : enrolment
                .getEnrolmentEvaluationType();
    }

    private String getMark(Attends attends) {
        FinalMark finalMark = attends.getFinalMark();
        return (finalMark != null) ? finalMark.getMark() : "";
    }

    private Collection<Enrolment> getEnrolmentsNotInAnyMarkSheet(MarkSheetTeacherGradeSubmissionBean submissionBean) {

        Collection<Enrolment> enrolmentsNotInAnyMarkSheet = new HashSet<Enrolment>();
        for (CurricularCourse curricularCourse : submissionBean.getAllCurricularCourses()) {

            if (curricularCourse.isGradeSubmissionAvailableFor(submissionBean.getExecutionCourse().getExecutionPeriod(),
                    MarkSheetType.NORMAL)) {
                enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(MarkSheetType.NORMAL,
                        submissionBean.getExecutionCourse().getExecutionPeriod()));
            }
            if (curricularCourse.isGradeSubmissionAvailableFor(submissionBean.getExecutionCourse().getExecutionPeriod(),
                    MarkSheetType.IMPROVEMENT)) {
                enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(MarkSheetType.IMPROVEMENT,
                        submissionBean.getExecutionCourse().getExecutionPeriod()));
            }
            if (curricularCourse.isGradeSubmissionAvailableFor(submissionBean.getExecutionCourse().getExecutionPeriod(),
                    MarkSheetType.SPECIAL_SEASON)) {
                enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(MarkSheetType.SPECIAL_SEASON,
                        submissionBean.getExecutionCourse().getExecutionPeriod()));
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
                addMessageGradeSubmissionNormalSeasonFirstSemester(request, actionMessages, dateFormat, executionDegree);
                addMessageGradeSubmissionNormalSeasonSecondSemester(request, actionMessages, dateFormat, executionDegree);
                addMessageGradeSubmissionSpecialSeason(request, actionMessages, dateFormat, executionDegree);
                result = false;
            }
        }
        return result;
    }

    private void addMessageGradeSubmissionSpecialSeason(HttpServletRequest request, ActionMessages actionMessages,
            String dateFormat, ExecutionDegree executionDegree) {
        if (executionDegree.getPeriodGradeSubmissionSpecialSeason() != null) {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.specialSeason.dates", executionDegree
                    .getPeriodGradeSubmissionSpecialSeason().getStartYearMonthDay().toString(dateFormat), executionDegree
                    .getPeriodGradeSubmissionSpecialSeason().getEndYearMonthDay().toString(dateFormat));
        } else {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.specialSeason.notDefined");
        }
    }

    private void addMessageGradeSubmissionNormalSeasonSecondSemester(HttpServletRequest request, ActionMessages actionMessages,
            String dateFormat, ExecutionDegree executionDegree) {
        if (executionDegree.getPeriodGradeSubmissionNormalSeasonSecondSemester() != null) {
            addMessage(
                    request,
                    actionMessages,
                    "error.teacher.gradeSubmission.secondSemester.normalSeason.dates",
                    executionDegree.getPeriodGradeSubmissionNormalSeasonSecondSemester().getStartYearMonthDay()
                            .toString(dateFormat), executionDegree.getPeriodGradeSubmissionNormalSeasonSecondSemester()
                            .getEndYearMonthDay().toString(dateFormat));
        } else {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.secondSemester.normalSeason.notDefined");
        }
    }

    private void addMessageGradeSubmissionNormalSeasonFirstSemester(HttpServletRequest request, ActionMessages actionMessages,
            String dateFormat, ExecutionDegree executionDegree) {
        if (executionDegree.getPeriodGradeSubmissionNormalSeasonFirstSemester() != null) {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.firstSemester.normalSeason.dates", executionDegree
                    .getPeriodGradeSubmissionNormalSeasonFirstSemester().getStartYearMonthDay().toString(dateFormat),
                    executionDegree.getPeriodGradeSubmissionNormalSeasonFirstSemester().getEndYearMonthDay().toString(dateFormat));
        } else {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.firstSemester.normalSeason.notDefined");
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