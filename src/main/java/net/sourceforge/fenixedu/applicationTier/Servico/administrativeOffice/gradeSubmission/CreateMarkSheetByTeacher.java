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
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherMarkBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FinalMark;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class CreateMarkSheetByTeacher {

    @Atomic
    public static List<EnrolmentEvaluation> run(MarkSheetTeacherGradeSubmissionBean submissionBean)
            throws InvalidArgumentsServiceException {
        check(RolePredicates.TEACHER_PREDICATE);

        ExecutionCourse executionCourse = submissionBean.getExecutionCourse();
        Teacher teacher = submissionBean.getResponsibleTeacher();

        checkIfTeacherLecturesExecutionCourse(teacher, executionCourse);

        Map<CurricularCourse, Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation =
                new HashMap<CurricularCourse, Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>>>();

        createMarkSheetEnrolmentEvaluationBeans(submissionBean, executionCourse, markSheetsInformation);
        return createMarkSheets(markSheetsInformation, executionCourse, teacher, submissionBean.getEvaluationDate());
    }

    private static void createMarkSheetEnrolmentEvaluationBeans(MarkSheetTeacherGradeSubmissionBean submissionBean,
            ExecutionCourse executionCourse,
            Map<CurricularCourse, Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation)
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
            Map<CurricularCourse, Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation,
            ExecutionCourse executionCourse, Teacher responsibleTeacher, Date evaluationDate)
            throws InvalidArgumentsServiceException {
        List<EnrolmentEvaluation> enrolmetnEvaluations = new ArrayList<EnrolmentEvaluation>();
        for (Entry<CurricularCourse, Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>>> curricularCourseEntry : markSheetsInformation
                .entrySet()) {

            CurricularCourse curricularCourse = curricularCourseEntry.getKey();

            if (!curricularCourse.isGradeSubmissionAvailableFor(executionCourse.getExecutionPeriod())) {
                throw new InvalidArgumentsServiceException("error.curricularCourse.is.not.available.toSubmit.grades");
            }

            for (Entry<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>> markSheetTypeEntry : curricularCourseEntry
                    .getValue().entrySet()) {

                MarkSheetType markSheetType = markSheetTypeEntry.getKey();
                Collection<MarkSheetEnrolmentEvaluationBean> markSheetEnrolmentEvaluationBeans = markSheetTypeEntry.getValue();

                if (markSheetEnrolmentEvaluationBeans != null) {
                    MarkSheet markSheet =
                            curricularCourse.createNormalMarkSheet(executionCourse.getExecutionPeriod(), responsibleTeacher,
                                    evaluationDate, markSheetType, Boolean.TRUE, markSheetEnrolmentEvaluationBeans,
                                    responsibleTeacher.getPerson());
                    enrolmetnEvaluations.addAll(markSheet.getEnrolmentEvaluations());
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
            Map<CurricularCourse, Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation,
            CurricularCourse curricularCourse, ExecutionCourse executionCourse,
            MarkSheetEnrolmentEvaluationBean markSheetEvaluationBean) throws InvalidArgumentsServiceException {

        Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>> evaluationBeansForMarkSheetType =
                getEvaluationBeansForMarkSheetType(markSheetsInformation, curricularCourse);

        MarkSheetType markSheetType = findMarkSheetType(executionCourse, markSheetEvaluationBean.getEnrolment());
        Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans =
                getEvaluationBeans(evaluationBeansForMarkSheetType, markSheetType);

        evaluationBeans.add(markSheetEvaluationBean);
    }

    private static Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>> getEvaluationBeansForMarkSheetType(
            Map<CurricularCourse, Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>>> markSheetsInformation,
            CurricularCourse curricularCourse) {

        Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>> evaluationBeansForMarkSheetType =
                markSheetsInformation.get(curricularCourse);
        if (evaluationBeansForMarkSheetType == null) {
            evaluationBeansForMarkSheetType = new HashMap<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>>();
            markSheetsInformation.put(curricularCourse, evaluationBeansForMarkSheetType);
        }
        return evaluationBeansForMarkSheetType;
    }

    private static Collection<MarkSheetEnrolmentEvaluationBean> getEvaluationBeans(
            Map<MarkSheetType, Collection<MarkSheetEnrolmentEvaluationBean>> evaluationBeansForMarkSheetType,
            MarkSheetType markSheetType) {
        Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans = evaluationBeansForMarkSheetType.get(markSheetType);
        if (evaluationBeans == null) {
            evaluationBeans = new ArrayList<MarkSheetEnrolmentEvaluationBean>();
            evaluationBeansForMarkSheetType.put(markSheetType, evaluationBeans);
        }
        return evaluationBeans;
    }

    private static MarkSheetType findMarkSheetType(ExecutionCourse executionCourse, Enrolment enrolment)
            throws InvalidArgumentsServiceException {

        if (enrolment.isImprovementForExecutionCourse(executionCourse)
                && enrolment.hasImprovementFor(executionCourse.getExecutionPeriod())) {
            return MarkSheetType.IMPROVEMENT;

        } else {
            switch (enrolment.getEnrolmentEvaluationType()) {
            case NORMAL:
                return MarkSheetType.NORMAL;
            case IMPROVEMENT:
                return MarkSheetType.IMPROVEMENT;
            case SPECIAL_SEASON:
                return MarkSheetType.SPECIAL_SEASON;
            default:
                throw new InvalidArgumentsServiceException("error.markSheetType.invalid.enrolmentEvaluationType");
            }
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