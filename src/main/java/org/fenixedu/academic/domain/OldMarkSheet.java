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
package org.fenixedu.academic.domain;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Collection;
import java.util.Date;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import org.fenixedu.academic.predicate.MarkSheetPredicates;
import org.fenixedu.academic.util.EnrolmentEvaluationState;
import org.joda.time.DateTime;

public class OldMarkSheet extends OldMarkSheet_Base {

    protected OldMarkSheet() {
        super();
    }

    public OldMarkSheet(CurricularCourse curricularCourse, ExecutionSemester executionSemester, Teacher responsibleTeacher,
            Date evaluationDate, EvaluationSeason season, MarkSheetState markSheetState,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Person creator) {
        this();
        checkParameters(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, season, markSheetState,
                evaluationBeans, creator);
        init(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, season, markSheetState, Boolean.FALSE,
                creator);

        for (MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {
            addEnrolmentEvaluationToMarkSheet(responsibleTeacher, evaluationBean);
        }

        generateCheckSum();

    }

    private void addEnrolmentEvaluationToMarkSheet(Teacher responsibleTeacher,
            final MarkSheetEnrolmentEvaluationBean evaluationBean) {
        check(this, MarkSheetPredicates.editPredicate);

        EnrolmentEvaluation enrolmentEvaluation =
                evaluationBean
                        .getEnrolment()
                        .getEnrolmentEvaluationBySeason(getEvaluationSeason())
                        .filter(e -> e.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ)
                                || e.isNotEvaluated()).findAny().orElse(null);

        if (enrolmentEvaluation == null) {
            enrolmentEvaluation =
                    evaluationBean.getEnrolment().addNewEnrolmentEvaluation(EnrolmentEvaluationState.TEMPORARY_OBJ,
                            getEvaluationSeason(), responsibleTeacher.getPerson(), evaluationBean.getGradeValue(),
                            getCreationDate(), evaluationBean.getEvaluationDate(), getExecutionPeriod(), null);
        } else {
            enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
            enrolmentEvaluation.edit(responsibleTeacher.getPerson(), evaluationBean.getGradeValue(), getCreationDate(),
                    evaluationBean.getEvaluationDate());
        }
        addEnrolmentEvaluations(enrolmentEvaluation);
    }

    private void checkParameters(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
            Teacher responsibleTeacher, Date evaluationDate, EvaluationSeason season, MarkSheetState markSheetState,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Person person) {

        if (curricularCourse == null || executionSemester == null || responsibleTeacher == null || evaluationDate == null
                || season == null || markSheetState == null || person == null) {
            throw new DomainException("error.markSheet.invalid.arguments");
        }
        if (evaluationBeans == null || evaluationBeans.size() == 0) {
            throw new DomainException("error.markSheet.create.with.invalid.enrolmentEvaluations.number");
        }
    }

    @Override
    public void confirm(Person validator) {
        check(this, MarkSheetPredicates.confirmPredicate);
        if (validator == null) {
            throw new DomainException("error.markSheet.invalid.arguments");
        }
        if (isNotConfirmed()) {
            setValidator(validator);

            for (final EnrolmentEvaluation enrolmentEvaluation : this.getEnrolmentEvaluationsSet()) {
                enrolmentEvaluation.confirmSubmission(getEnrolmentEvaluationStateToConfirm(), validator, "");
            }

            setConfirmationDateDateTime(new DateTime());
            setMarkSheetState(getMarkSheetStateToConfirm());

        } else {
            throw new DomainException("error.markSheet.already.confirmed");
        }
    }

    @Override
    protected void appendEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans) {
        for (MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {
            addEnrolmentEvaluationToMarkSheet(getResponsibleTeacher(), evaluationBean);
        }
    }

    @Override
    protected void editEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToEdit) {
        check(this, MarkSheetPredicates.editPredicate);

        for (final MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : evaluationBeansToEdit) {

            if (this.getEnrolmentEvaluationsSet().contains(enrolmentEvaluationBean.getEnrolmentEvaluation())) {

                final EnrolmentEvaluation enrolmentEvaluation = enrolmentEvaluationBean.getEnrolmentEvaluation();
                enrolmentEvaluation.edit(getResponsibleTeacher().getPerson(), enrolmentEvaluationBean.getGradeValue(),
                        new Date(), enrolmentEvaluationBean.getEvaluationDate());
            } else {
                // TODO:
                throw new DomainException("error.markSheet");
            }
        }
    }

    @Override
    protected void checkIfEvaluationDateIsInExamsPeriod(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionSemester executionSemester, Date evaluationDate, EvaluationSeason season) throws DomainException {
    }

    @Override
    protected void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, Teacher responsibleTeacher, EvaluationSeason season) throws DomainException {
    }
}
