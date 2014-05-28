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
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Interval;

public class Delegate extends Delegate_Base {

    public Delegate() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public boolean isActiveForExecutionYear(final ExecutionYear executionYear) {
        return hasDelegateFunction()
                && getDelegateFunction().belongsToPeriod(executionYear.getBeginDateYearMonthDay(),
                        executionYear.getEndDateYearMonthDay());
    }

    public boolean isActiveForFirstExecutionYear(final ExecutionYear executionYear) {
        if (hasDelegateFunction() && getDelegateFunction().getBeginDate() != null) {
            Interval interval =
                    new Interval(getDelegateFunction().getBeginDate().toDateTimeAtMidnight(), getDelegateFunction().getEndDate()
                            .toDateTimeAtMidnight().plusDays(1));
            return executionYear.overlapsInterval(interval);
        }
        return false;
    }

    protected Degree getDegree() {
        return ((DegreeUnit) getDelegateFunction().getFunction().getUnit()).getDegree();
    }

    public void delete() {
        getDelegateFunction().delete();
        setDelegateFunction(null);
        setRegistration(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDelegateFunction() {
        return getDelegateFunction() != null;
    }

}
