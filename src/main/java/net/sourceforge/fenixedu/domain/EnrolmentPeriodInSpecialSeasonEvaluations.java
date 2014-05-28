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
