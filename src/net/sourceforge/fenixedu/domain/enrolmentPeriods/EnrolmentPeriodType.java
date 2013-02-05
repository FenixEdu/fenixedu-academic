package net.sourceforge.fenixedu.domain.enrolmentPeriods;

import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesFlunkedSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import net.sourceforge.fenixedu.domain.ReingressionPeriod;

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
