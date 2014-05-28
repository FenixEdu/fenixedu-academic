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
package net.sourceforge.fenixedu.domain.elections;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public abstract class DelegateElectionPeriod extends DelegateElectionPeriod_Base {

    public DelegateElectionPeriod() {
        setRootDomainObject(Bennu.getInstance());
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
        if (getStartDate() != null && endDate.isBefore(getStartDate())) {
            throw new DomainException("error.elections.create.invalidEndDate");
        }

        super.setEndDate(endDate);
    }

    public boolean isCurrentPeriod() {
        YearMonthDay currentDate = new YearMonthDay();
        return (!currentDate.isBefore(getStartDate()) && !currentDate.isAfter(getEndDate()) ? true : false);
    }

    public boolean isPastPeriod() {
        YearMonthDay currentDate = new YearMonthDay();
        return (currentDate.isAfter(this.getEndDate()) ? true : false);
    }

    public boolean containsPeriod(YearMonthDay startDate, YearMonthDay endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        if (((startDate.isAfter(getStartDate()) || startDate.isEqual(getStartDate())) && (endDate.isBefore(getEndDate()) || endDate
                .isEqual(getEndDate())))) {
            return true;
        }
        return false;

    }

    public boolean endsBefore(DelegateElectionPeriod electionPeriod) {
        if (this.getEndDate().isBefore(electionPeriod.getStartDate())) {
            return true;
        }
        return false;
    }

    public boolean isCandidacyPeriod() {
        return (this instanceof DelegateElectionCandidacyPeriod);
    }

    public boolean isVotingPeriod() {
        return (this instanceof DelegateElectionVotingPeriod);
    }

    public String getPeriod() {
        return getStartDate().toString("dd/MM/yyyy") + " - " + getEndDate().toString("dd/MM/yyyy");
    }

    public void delete() {
        super.setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public abstract boolean isFirstRoundElections();

    public abstract boolean isSecondRoundElections();

    public abstract boolean isOpenRoundElections();

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    @Deprecated
    public boolean hasStartDate() {
        return getStartDate() != null;
    }

}
