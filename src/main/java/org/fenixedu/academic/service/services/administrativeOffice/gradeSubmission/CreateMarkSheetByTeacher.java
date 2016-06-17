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
package org.fenixedu.academic.service.services.administrativeOffice.gradeSubmission;

import pt.ist.fenixframework.Atomic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.FinalMark;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import org.fenixedu.academic.dto.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean;
import org.fenixedu.academic.dto.teacher.gradeSubmission.MarkSheetTeacherMarkBean;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.joda.time.YearMonthDay;

public class CreateMarkSheetByTeacher {

    @Atomic
    public static List<EnrolmentEvaluation> run(MarkSheetTeacherGradeSubmissionBean submissionBean)
            throws InvalidArgumentsServiceException {
        ExecutionCourse executionCourse = submissionBean.getExecutionCourse();
        Teacher teacher = submissionBean.getResponsibleTeacher();

        checkIfTeacherLecturesExecutionCourse(teacher, executionCourse);

        Map<CurricularCourse, Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation =
                new HashMap<CurricularCourse, Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>>>();

        createMarkSheetEnrolmentEvaluationBeans(submissionBean, executionCourse, markSheetsInformation);
        return createMarkSheets(markSheetsInformation, executionCourse, teacher, submissionBean.getEvaluationDate());
    }

    private static void createMarkSheetEnrolmentEvaluationBeans(MarkSheetTeacherGradeSubmissionBean submissionBean,
            ExecutionCourse executionCourse,
            Map<CurricularCourse, Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation)
            throws InvalidArgumentsServiceException {

        Date nowDate = new Date();
        for (MarkSheetTeacherMarkBean markBean : submissionBean.getSelectedMarksToSubmit()) {
            final Enrolment enrolment = markBean.getAttends().getEnrolment();
            CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            final Grade grade = getGrade(markBean.getAttends(), markBean, markBean.getEvaluationDate(), nowDate);

            addMarkSheetEvaluationBeanToMap(markSheetsInformation, curricularCourse, executionCourse,
                    new MarkSheetEnrolmentEvaluationBean(enrolment, markBean.getEvaluationDate(), grade));
        }
    }

    private static List<EnrolmentEvaluation> createMarkSheets(
            Map<CurricularCourse, Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation,
            ExecutionCourse executionCourse, Teacher responsibleTeacher, Date evaluationDate)
            throws InvalidArgumentsServiceException {
        List<EnrolmentEvaluation> enrolmetnEvaluations = new ArrayList<EnrolmentEvaluation>();
        for (Entry<CurricularCourse, Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>>> curricularCourseEntry : markSheetsInformation
                .entrySet()) {

            CurricularCourse curricularCourse = curricularCourseEntry.getKey();

            if (!curricularCourse.isGradeSubmissionAvailableFor(executionCourse.getExecutionPeriod())) {
                throw new InvalidArgumentsServiceException("error.curricularCourse.is.not.available.toSubmit.grades");
            }

            for (Entry<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>> seasonEntry : curricularCourseEntry
                    .getValue().entrySet()) {

                EvaluationSeason season = seasonEntry.getKey();
                Collection<MarkSheetEnrolmentEvaluationBean> markSheetEnrolmentEvaluationBeans = seasonEntry.getValue();

                if (markSheetEnrolmentEvaluationBeans != null) {
                    MarkSheet markSheet =
                            curricularCourse.createNormalMarkSheet(executionCourse.getExecutionPeriod(), responsibleTeacher,
                                    evaluationDate, season, Boolean.TRUE, markSheetEnrolmentEvaluationBeans,
                                    responsibleTeacher.getPerson());
                    enrolmetnEvaluations.addAll(markSheet.getEnrolmentEvaluationsSet());
                }
            }
        }
        return enrolmetnEvaluations;
    }

    private static void checkIfTeacherLecturesExecutionCourse(Teacher teacher, ExecutionCourse executionCourse)
            throws InvalidArgumentsServiceException {
        if (!teacher.hasProfessorshipForExecutionCourse(executionCourse)) {
            throw new InvalidArgumentsServiceException("error.teacher.doesnot.lectures.executionCourse");
        }
    }

    private static void addMarkSheetEvaluationBeanToMap(
            Map<CurricularCourse, Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation,
            CurricularCourse curricularCourse, ExecutionCourse executionCourse,
            MarkSheetEnrolmentEvaluationBean markSheetEvaluationBean) throws InvalidArgumentsServiceException {

        Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>> evaluationBeansForSeason =
                getEvaluationBeansForSeason(markSheetsInformation, curricularCourse);

        EvaluationSeason season = findSeason(executionCourse, markSheetEvaluationBean.getEnrolment());
        Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans = getEvaluationBeans(evaluationBeansForSeason, season);

        evaluationBeans.add(markSheetEvaluationBean);
    }

    private static Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>> getEvaluationBeansForSeason(
            Map<CurricularCourse, Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation,
            CurricularCourse curricularCourse) {

        Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>> evaluationBeansForSeason =
                markSheetsInformation.get(curricularCourse);
        if (evaluationBeansForSeason == null) {
            evaluationBeansForSeason = new HashMap<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>>();
            markSheetsInformation.put(curricularCourse, evaluationBeansForSeason);
        }
        return evaluationBeansForSeason;
    }

    private static Collection<MarkSheetEnrolmentEvaluationBean> getEvaluationBeans(
            Map<EvaluationSeason, Collection<MarkSheetEnrolmentEvaluationBean>> evaluationBeansForSeason, EvaluationSeason season) {
        Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans = evaluationBeansForSeason.get(season);
        if (evaluationBeans == null) {
            evaluationBeans = new ArrayList<MarkSheetEnrolmentEvaluationBean>();
            evaluationBeansForSeason.put(season, evaluationBeans);
        }
        return evaluationBeans;
    }

    private static EvaluationSeason findSeason(ExecutionCourse executionCourse, Enrolment enrolment)
            throws InvalidArgumentsServiceException {

        if (enrolment.isImprovementForExecutionCourse(executionCourse)
                && enrolment.hasImprovementFor(executionCourse.getExecutionPeriod())) {
            return EvaluationSeason.readImprovementSeason();

        } else {
            return enrolment.getEvaluationSeason();
        }
    }

    private static Grade getGrade(Attends attends, MarkSheetTeacherMarkBean markBean, Date evaluationDate, Date nowDate) {
        final String value;

        final FinalMark finalMark = attends.getFinalMark();
        if (finalMark != null) {
            finalMark.setSubmitedMark(markBean.getGradeValue());
            finalMark.setSubmitDateYearMonthDay(YearMonthDay.fromDateFields(evaluationDate));
            finalMark.setWhenSubmitedYearMonthDay(YearMonthDay.fromDateFields(nowDate));
            value = markBean.getGradeValue();
        } else {
            value = GradeScale.NA;
        }

        return Grade.createGrade(value, attends.getEnrolment().getGradeScale());
    }

}