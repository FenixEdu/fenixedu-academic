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

import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipSummaryPeriodBean;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class TutorshipSummaryPeriod extends TutorshipSummaryPeriod_Base {

    public TutorshipSummaryPeriod(ExecutionSemester executionSemester, LocalDate beginDate, LocalDate endDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setExecutionSemester(executionSemester);
        setBeginDate(beginDate);
        setEndDate(endDate);
    }

    @Atomic
    public void update(TutorshipSummaryPeriodBean bean) {
        setBeginDate(bean.getBeginDate());
        setEndDate(bean.getEndDate());
    }

    @Atomic
    public static TutorshipSummaryPeriod create(TutorshipSummaryPeriodBean bean) {
        TutorshipSummaryPeriod tutorshipSummaryPeriod =
                new TutorshipSummaryPeriod(bean.getExecutionSemester(), bean.getBeginDate(), bean.getEndDate());

        return tutorshipSummaryPeriod;
    }

    public boolean isOpenNow() {
        Interval interval =
                new Interval(getBeginDate().toDateTimeAtStartOfDay(), getEndDate().plusDays(1).toDateTimeAtStartOfDay());
        return interval.containsNow();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    @Deprecated
    public boolean hasBeginDate() {
        return getBeginDate() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

}
