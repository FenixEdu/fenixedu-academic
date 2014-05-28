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
 * Created on 2004/08/24
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

/**
 * @author Luis Cruz
 */
public class EnrolmentPeriodInCurricularCourses extends EnrolmentPeriodInCurricularCourses_Base {

    public static final Comparator<EnrolmentPeriodInCurricularCourses> COMPARATOR_BY_START =
            new Comparator<EnrolmentPeriodInCurricularCourses>() {

                @Override
                public int compare(EnrolmentPeriodInCurricularCourses o1, EnrolmentPeriodInCurricularCourses o2) {
                    return o1.getStartDateDateTime().compareTo(o2.getStartDateDateTime());
                }

            };

    public EnrolmentPeriodInCurricularCourses(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionSemester executionSemester, final Date startDate, final Date endDate) {
        super();
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

    public EnrolmentPeriodInCurricularCourses(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionSemester executionSemester, final DateTime startDate, final DateTime endDate) {

        super();

        checkParameters(degreeCurricularPlan, executionSemester);
        init(degreeCurricularPlan, executionSemester, startDate, endDate);
    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan, ExecutionSemester executionSemester) {

        String[] args = {};
        if (degreeCurricularPlan == null) {
            throw new DomainException("error.EnrolmentPeriodInCurricularCourses.invalid.degreeCurricularPlan", args);
        }
        String[] args1 = {};
        if (executionSemester == null) {
            throw new DomainException("error.EnrolmentPeriodInCurricularCourses.invalid.executionSemester", args1);
        }

        if (executionSemester.getEnrolmentPeriod(getClass(), degreeCurricularPlan) != null) {
            throw new DomainException("error.EnrolmentPeriodInCurricularCourses.dcp.already.has.enrolment.period.for.semester");
        }
    }

}