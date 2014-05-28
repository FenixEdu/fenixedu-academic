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
package net.sourceforge.fenixedu.domain.phd;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;

public class PhdProgramContextPeriod extends PhdProgramContextPeriod_Base {

    public static final Comparator<PhdProgramContextPeriod> COMPARATOR_BY_BEGIN_DATE = new Comparator<PhdProgramContextPeriod>() {

        @Override
        public int compare(PhdProgramContextPeriod o1, PhdProgramContextPeriod o2) {
            return o1.getBeginDate().compareTo(o2.getBeginDate());
        }

    };

    protected PhdProgramContextPeriod() {
        super();
        setRootDomainObject(getRootDomainObject());
    }

    protected PhdProgramContextPeriod(PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
        super();
        init(phdProgram, beginPeriod, endPeriod);
    }

    protected void init(PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
        checkParameters(phdProgram, beginPeriod, endPeriod);

        setPhdProgram(phdProgram);
        setBeginDate(beginPeriod);
        setEndDate(endPeriod);
    }

    protected void checkParameters(PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
        if (phdProgram == null) {
            throw new DomainException("phd.PhdProgramContextPeriod.phdProgram.cannot.be.null");
        }

        if (beginPeriod == null) {
            throw new DomainException("phd.PhdProgramContextPeriod.beginPeriod.cannot.be.null");
        }

        if (endPeriod != null && !endPeriod.isAfter(beginPeriod)) {
            throw new PhdDomainOperationException(
                    "error.net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod.endPeriod.is.after.of.beginPeriod");
        }

        checkOverlaps(phdProgram, beginPeriod, endPeriod);
    }

    private void checkOverlaps(PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
        for (PhdProgramContextPeriod period : phdProgram.getPhdProgramContextPeriods()) {
            if (period == this) {
                continue;
            }

            if (period.overlaps(beginPeriod, endPeriod)) {
                throw new PhdDomainOperationException(
                        "error.net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod.period.is.overlaping.another");
            }
        }
    }

    private boolean overlaps(DateTime beginPeriod, DateTime endPeriod) {
        return getInterval().overlaps(new Interval(beginPeriod, endPeriod));
    }

    public boolean contains(DateTime dateTime) {
        return getInterval().contains(dateTime);
    }

    public Interval getInterval() {
        return new Interval(getBeginDate(), getEndDate());
    }

    @Atomic
    public void edit(DateTime beginDate, DateTime endDate) {
        checkParameters(getPhdProgram(), beginDate, endDate);
    }

    @Atomic
    public void closePeriod(DateTime endPeriod) {
        if (endPeriod == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod.endPeriod.cannot.be.null");
        }

        setEndDate(endPeriod);
    }

    @Atomic
    public static PhdProgramContextPeriod create(final PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
        return new PhdProgramContextPeriod(phdProgram, beginPeriod, endPeriod);
    }

    @Atomic
    public static PhdProgramContextPeriod create(PhdProgramContextPeriodBean bean) {
        return create(bean.getPhdProgram(), bean.getBeginDateAtMidnight(), bean.getEndDateBeforeMidnight());
    }

    @Atomic
    public void deletePeriod() {
        delete();
    }

    private void delete() {
        setPhdProgram(null);
        setRootDomainObject(null);

        deleteDomainObject();
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
    public boolean hasPhdProgram() {
        return getPhdProgram() != null;
    }

}
