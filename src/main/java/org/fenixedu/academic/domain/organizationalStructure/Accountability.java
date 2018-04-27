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
package org.fenixedu.academic.domain.organizationalStructure;

import java.util.Comparator;
import java.util.Date;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public class Accountability extends Accountability_Base {

    protected Accountability() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Accountability(Party parentParty, Party childParty, AccountabilityType accountabilityType) {
        this();
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(accountabilityType);
        setBeginDate(new YearMonthDay());
    }

    public void delete() {
        super.setAccountabilityType(null);
        super.setChildParty(null);
        super.setParentParty(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }
    
    public static Comparator<Accountability> getComparatorByBeginDate() {
        return new Comparator<Accountability>() {

            @Override
            public int compare(Accountability left, Accountability right) {
                final int result = left.getBeginDate().compareTo(right.getBeginDate());

                return result == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(left, right) : result;

            }
        };
    }

    public boolean belongsToPeriod(YearMonthDay begin, YearMonthDay end) {
        return ((end == null || !getBeginDate().isAfter(end)) && (getEndDate() == null || !getEndDate().isBefore(begin)));
    }

    public boolean isActive(YearMonthDay currentDate) {
        return belongsToPeriod(currentDate, currentDate);
    }

    public boolean isActive() {
        return isActive(new YearMonthDay());
    }

    public boolean isFinished() {
        return getEndDate() != null && getEndDate().isBefore(new YearMonthDay());
    }

    public Date getBeginDateInDateType() {
        return (getBeginDate() != null) ? getBeginDate().toDateTimeAtCurrentTime().toDate() : null;
    }

    public Date getEndDateInDateType() {
        return (getEndDate() != null) ? getEndDate().toDateTimeAtCurrentTime().toDate() : null;
    }

    @Override
    public void setChildParty(Party childParty) {
        if (childParty == null) {
            throw new DomainException("error.accountability.inexistent.childParty");
        }
        super.setChildParty(childParty);
    }

    @Override
    public void setParentParty(Party parentParty) {
        if (parentParty == null) {
            throw new DomainException("error.accountability.inexistent.parentParty");
        }
        super.setParentParty(parentParty);
    }

    @Override
    public void setAccountabilityType(AccountabilityType accountabilityType) {
        if (accountabilityType == null) {
            throw new DomainException("error.accountability.inexistent.accountabilityType");
        }
        super.setAccountabilityType(accountabilityType);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
        final YearMonthDay start = getBeginDate();
        final YearMonthDay end = getEndDate();
        return start != null && (end == null || !start.isAfter(end));
    }

}
