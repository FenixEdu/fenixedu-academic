package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

public class EnrolmentPeriodInSpecialSeasonEvaluations extends EnrolmentPeriodInSpecialSeasonEvaluations_Base {

    public static final Comparator<EnrolmentPeriodInSpecialSeasonEvaluations> COMPARATOR_BY_START =
            new Comparator<EnrolmentPeriodInSpecialSeasonEvaluations>() {

                @Override
                public int compare(EnrolmentPeriodInSpecialSeasonEvaluations o1, EnrolmentPeriodInSpecialSeasonEvaluations o2) {
                    return o1.getStartDateDateTime().compareTo(o2.getStartDateDateTime());
                }

            };

    public EnrolmentPeriodInSpecialSeasonEvaluations() {
        super();
    }

    public EnrolmentPeriodInSpecialSeasonEvaluations(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
        super();
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

}
