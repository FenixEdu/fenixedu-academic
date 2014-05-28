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
package net.sourceforge.fenixedu.domain.oldInquiries;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriodType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class InquiryResponsePeriod extends InquiryResponsePeriod_Base {

    public final static Comparator<InquiryResponsePeriod> PERIOD_COMPARATOR = new Comparator<InquiryResponsePeriod>() {

        @Override
        public int compare(InquiryResponsePeriod o1, InquiryResponsePeriod o2) {
            return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
        }

    };

    public InquiryResponsePeriod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public InquiryResponsePeriod(final ExecutionSemester executionSemester, final Date inquiryResponseBegin,
            final Date inquiryResponseEnd) {
        this();
        setExecutionPeriod(executionSemester);
        setPeriod(inquiryResponseBegin, inquiryResponseEnd);
    }

    public void setPeriod(final Date inquiryResponseBegin, final Date inquiryResponseEnd) {
        setBegin(new DateTime(inquiryResponseBegin));
        setEnd(new DateTime(inquiryResponseEnd));
    }

    public boolean isInsideResponsePeriod() {
        return getBegin().isBeforeNow() && getEnd().isAfterNow();
    }

    public boolean insidePeriod() {
        return !getBegin().isAfterNow() && !getEnd().isBeforeNow();
    }

    public static InquiryResponsePeriod readOpenPeriod(final InquiryResponsePeriodType type) {
        final Collection<InquiryResponsePeriod> inquiryResponsePeriods = Bennu.getInstance().getInquiryResponsePeriodsSet();
        for (final InquiryResponsePeriod inquiryResponsePeriod : inquiryResponsePeriods) {
            if (inquiryResponsePeriod.getType() == type && inquiryResponsePeriod.isOpen()) {
                return inquiryResponsePeriod;
            }
        }
        return null;
    }

    public boolean isOpen() {
        return getBegin().isBeforeNow() && getEnd().isAfterNow();
    }

    static public boolean hasOpenPeriod(final InquiryResponsePeriodType type) {
        return readOpenPeriod(type) != null;
    }

    public static InquiryResponsePeriod readLastPeriod(final InquiryResponsePeriodType type) {
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        while (executionSemester.getInquiryResponsePeriod(type) == null) {
            executionSemester = executionSemester.getPreviousExecutionPeriod();
        }
        return executionSemester.getInquiryResponsePeriod(type);
    }

    public boolean isBeforeNow() {
        return getEnd().isBeforeNow();
    }

    public boolean isAfterNow() {
        return getBegin().isAfterNow();
    }

    @Deprecated
    public boolean hasEnd() {
        return getEnd() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasIntroduction() {
        return getIntroduction() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasBegin() {
        return getBegin() != null;
    }

}
