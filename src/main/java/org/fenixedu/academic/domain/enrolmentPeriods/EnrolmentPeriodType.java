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
package org.fenixedu.academic.domain.enrolmentPeriods;

import org.fenixedu.academic.domain.EnrolmentPeriod;
import org.fenixedu.academic.domain.EnrolmentPeriodInClasses;
import org.fenixedu.academic.domain.EnrolmentPeriodInClassesMobility;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCourses;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCoursesFlunkedSeason;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import org.fenixedu.academic.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import org.fenixedu.academic.domain.EnrolmentPeriodInExtraordinarySeasonEvaluations;
import org.fenixedu.academic.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import org.fenixedu.academic.domain.ReingressionPeriod;

public enum EnrolmentPeriodType {
    REINGRESSION {

        @Override
        protected Class<? extends EnrolmentPeriod> getClassFor() {
            return ReingressionPeriod.class;
        }

        @Override
        public boolean isReingressionPeriod() {
            return true;
        }
    },

    ENROLMENT_PERIOD_IN_SPECIAL_SEASON_EVALUATIONS {

        @Override
        protected Class<? extends EnrolmentPeriod> getClassFor() {
            return EnrolmentPeriodInSpecialSeasonEvaluations.class;
        }
    },

    ENROLMENT_PERIOD_IN_EXTRAORDINARY_SEASON_EVALUATIONS {

        @Override
        protected Class<? extends EnrolmentPeriod> getClassFor() {
            return EnrolmentPeriodInExtraordinarySeasonEvaluations.class;
        }
    },

    ENROLMENT_PERIOD_IN_IMPROVEMENT_OF_APPROVED_ENROLMENT {

        @Override
        protected Class<? extends EnrolmentPeriod> getClassFor() {
            return EnrolmentPeriodInImprovementOfApprovedEnrolment.class;
        }
    },

    ENROLMENT_PERIOD_IN_CURRICULAR_COURSES_SPECIAL_SEASON {

        @Override
        protected Class<? extends EnrolmentPeriod> getClassFor() {
            return EnrolmentPeriodInCurricularCoursesSpecialSeason.class;
        }
    },

    ENROLMENT_PERIOD_IN_CURRICULAR_COURSES_FLUNKED_SEASON {

        @Override
        protected Class<? extends EnrolmentPeriod> getClassFor() {
            return EnrolmentPeriodInCurricularCoursesFlunkedSeason.class;
        }
    },

    ENROLMENT_PERIOD_IN_CURRICULAR_COURSES {

        @Override
        protected Class<? extends EnrolmentPeriod> getClassFor() {
            return EnrolmentPeriodInCurricularCourses.class;
        }
    },

    ENROLMENT_PERIOD_IN_CLASSES {

        @Override
        protected Class<? extends EnrolmentPeriod> getClassFor() {
            return EnrolmentPeriodInClasses.class;
        }
    },

    ENROLMENT_PERIOD_IN_CLASSES_MOBILITY {

        @Override
        protected Class<? extends EnrolmentPeriod> getClassFor() {
            return EnrolmentPeriodInClassesMobility.class;
        }
    };

    protected abstract Class<? extends EnrolmentPeriod> getClassFor();

    public boolean is(EnrolmentPeriod enrolmentPeriod) {
        return this.getClassFor().equals(enrolmentPeriod.getClass());
    }

    public boolean isReingressionPeriod() {
        return false;
    }

    public static EnrolmentPeriodType readTypeByClass(Class<? extends EnrolmentPeriod> clazz) {
        EnrolmentPeriodType[] values = EnrolmentPeriodType.values();

        for (EnrolmentPeriodType enrolmentPeriodType : values) {
            if (enrolmentPeriodType.getClassFor().equals(clazz)) {
                return enrolmentPeriodType;
            }
        }

        return null;
    }

}
