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
 * Created on Sep 26, 2005
 *	by mrsp
 */
package org.fenixedu.academic.domain.organizationalStructure;

import java.util.Comparator;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public abstract class Contract extends Contract_Base {

    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_BEGIN_DATE = new Comparator<Contract>() {

        @Override
        public int compare(Contract o1, Contract o2) {
            final int c = o1.getBeginDate().compareTo(o2.getBeginDate());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };
    public static final Comparator<Contract> CONTRACT_COMPARATOR_BY_PERSON_NAME = new Comparator<Contract>() {

        @Override
        public int compare(Contract o1, Contract o2) {
            final int c = o1.getPerson().getName().compareTo(o2.getPerson().getName());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    protected Contract() {
        super();
    }

    protected void init(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit) {
        setParentParty(unit);
        setChildParty(person);
        setBeginDate(beginDate);
        setEndDate(endDate);
    }

    @Override
    public void setChildParty(Party childParty) {
        if (childParty == null || !childParty.isPerson()) {
            throw new DomainException("error.invalid.child.party");
        }
        super.setChildParty(childParty);
    }

    @Override
    public void setParentParty(Party parentParty) {
        if (parentParty == null || !parentParty.isUnit()) {
            throw new DomainException("error.invalid.parent.party");
        }
        super.setParentParty(parentParty);
    }

    public Person getPerson() {
        return (Person) getChildParty();
    }

    public Unit getUnit() {
        return (Unit) getParentParty();
    }

    public Unit getMailingUnit() {
        return null;
    }

    public Unit getWorkingUnit() {
        return null;
    }

    public Employee getEmployee() {
        return null;
    }
}
