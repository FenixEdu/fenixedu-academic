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

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;

import org.fenixedu.academic.domain.curriculum.EnrollmentState;
import org.fenixedu.academic.domain.curriculum.EnrolmentEvaluationContext;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class EvaluationConfiguration extends EvaluationConfiguration_Base {

    protected EvaluationConfiguration() {
        super();
        setRoot(Bennu.getInstance());
    }

    public static EvaluationConfiguration getInstance() {
        if (Bennu.getInstance().getEvaluationConfiguration() == null) {
            return initialize();
        }
        return Bennu.getInstance().getEvaluationConfiguration();
    }

    @Atomic(mode = TxMode.WRITE)
    private static EvaluationConfiguration initialize() {
        if (Bennu.getInstance().getEvaluationConfiguration() == null) {
            return new EvaluationConfiguration();
        }
        return Bennu.getInstance().getEvaluationConfiguration();
    }

    private static Comparator<EnrolmentEvaluation> ENROLMENT_EVALUATION_ORDER = new EnrolmentComparator();

    public static void setEnrolmentEvaluationOrder(Comparator<EnrolmentEvaluation> order) {
        ENROLMENT_EVALUATION_ORDER = order;
    }

    public Optional<EnrolmentEvaluation> getFinalEnrolmentEvaluation(Enrolment enrolment) {
        Predicate<EnrolmentEvaluation> isFinal = EnrolmentEvaluation::isFinal;
        return enrolment.getEvaluationsSet().stream().filter(isFinal).max(ENROLMENT_EVALUATION_ORDER);
    }

    public Optional<EnrolmentEvaluation> getFinalEnrolmentEvaluation(Enrolment enrolment, EvaluationSeason season) {
        Predicate<EnrolmentEvaluation> isFinal = EnrolmentEvaluation::isFinal;
        Predicate<EnrolmentEvaluation> isSeason = e -> e.getEvaluationSeason().equals(season);
        return enrolment.getEvaluationsSet().stream().filter(isFinal.and(isSeason)).max(ENROLMENT_EVALUATION_ORDER);
    }

    public Optional<EnrolmentEvaluation> getEnrolmentEvaluationForConclusionDate(Enrolment enrolment) {
        Predicate<EnrolmentEvaluation> isFinal = EnrolmentEvaluation::isFinal;
        Predicate<EnrolmentEvaluation> isImprovement = e -> e.getEvaluationSeason().isImprovement();
        Predicate<EnrolmentEvaluation> hasExam = e -> e.getExamDateYearMonthDay() != null;
        return enrolment.getEvaluationsSet().stream().filter(isFinal.and(isImprovement.negate()).and(hasExam))
                .max(ENROLMENT_EVALUATION_ORDER);
    }

    public Optional<EnrolmentEvaluation> getCurrentEnrolmentEvaluation(Enrolment enrolment, EvaluationSeason season) {
        Predicate<EnrolmentEvaluation> isSeason = e -> e.getEvaluationSeason().equals(season);
        return enrolment.getEvaluationsSet().stream().filter(isSeason).max(EnrolmentEvaluation.COMPARATORY_BY_WHEN);
    }

    @Atomic(mode = TxMode.WRITE)
    public void delete() {

        super.setDefaultEvaluationSeason(null);
        getEvaluationSeasonSet().clear();
        super.setRoot(null);

        super.deleteDomainObject();
    }

    public static class EnrolmentComparator implements Comparator<EnrolmentEvaluation> {
        @Override
        public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {

            if (isInCurriculumValidationContextAndIsFinal(o1) && !isInCurriculumValidationContextAndIsFinal(o2)) {
                return 1;
            } else if (!isInCurriculumValidationContextAndIsFinal(o1) && isInCurriculumValidationContextAndIsFinal(o2)) {
                return -1;
            } else if (isInCurriculumValidationContextAndIsFinal(o1) && isInCurriculumValidationContextAndIsFinal(o2)) {
                return compareMyWhenAlteredDateToAnotherWhenAlteredDate(o1.getWhen(), o2.getWhen());
            } else if (o1.getEvaluationSeason().equals(o2.getEvaluationSeason())) {
                return compareByGrade(o1, o1.getGrade(), o2.getGrade());

            } else {
                return compareByGrade(o1, o1.getGrade(), o2.getGrade());
            }
        }

        private int compareByGrade(EnrolmentEvaluation enrolmentEvaluation, final Grade grade, final Grade otherGrade) {
            EnrollmentState gradeEnrolmentState = enrolmentEvaluation.getEnrollmentStateByGrade();
            EnrollmentState otherGradeEnrolmentState = otherGrade.getEnrolmentState();
            if (gradeEnrolmentState != otherGradeEnrolmentState) {
                if (gradeEnrolmentState == EnrollmentState.APROVED) {
                    return 1;
                }

                if (otherGradeEnrolmentState == EnrollmentState.APROVED) {
                    return -1;
                }
            }

            return grade.compareTo(otherGrade);
        }

        private int compareMyWhenAlteredDateToAnotherWhenAlteredDate(Date when1, Date whenAltered) {
            if (when1 == null) {
                return -1;
            }
            if (whenAltered == null) {
                return 1;
            }

            return when1.compareTo(whenAltered);

        }

        public boolean isInCurriculumValidationContextAndIsFinal(EnrolmentEvaluation enrolmentEvaluation) {
            return enrolmentEvaluation.getContext() != null
                    && enrolmentEvaluation.getContext().equals(EnrolmentEvaluationContext.CURRICULUM_VALIDATION_EVALUATION)
                    && enrolmentEvaluation.isFinal();
        }
    }

}
