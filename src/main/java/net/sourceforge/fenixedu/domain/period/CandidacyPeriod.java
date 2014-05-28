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
package net.sourceforge.fenixedu.domain.period;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

abstract public class CandidacyPeriod extends CandidacyPeriod_Base {

    static final public Comparator<CandidacyPeriod> LAST_CANDIDACY_PERIOD = new Comparator<CandidacyPeriod>() {
        @Override
        public int compare(CandidacyPeriod o1, CandidacyPeriod o2) {
            int result = o1.getStart().compareTo(o2.getStart());
            return -1 * ((result == 0) ? o1.getEnd().compareTo(o2.getEnd()) : result);
        }
    };

    protected CandidacyPeriod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected void init(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        checkParameters(executionInterval, start, end);
        setExecutionInterval(executionInterval);
        setStart(start);
        setEnd(end);
    }

    protected void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
        String[] args = {};
        if (executionInterval == null) {
            throw new DomainException("error.CandidacyPeriod.invalid.academic.period", args);
        }
        checkDates(start, end);
    }

    protected void checkDates(final DateTime start, final DateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new DomainException("error.CandidacyPeriod.invalid.dates");
        }
    }

    public void edit(final DateTime start, final DateTime end) {
        checkDates(start, end);
        super.setStart(start);
        super.setEnd(end);
    }

    public boolean isOpen() {
        return contains(new DateTime());
    }

    public boolean isOpen(final DateTime date) {
        return contains(date);
    }

    public boolean contains(final DateTime date) {
        return !(getStart().isAfter(date) || getEnd().isBefore(date));
    }

    public boolean intercept(final DateTime start, final DateTime end) {
        checkDates(start, end);
        return contains(start) || contains(end);
    }

    public boolean isPhdCandidacyPeriod() {
        return false;
    }

    public boolean isEpflCandidacyPeriod() {
        return false;
    }

    public boolean isInstitutionCandidacyPeriod() {
        return false;
    }

    public boolean isOutboundMobilityCandidacyPeriod() {
        return false;
    }

    public String getPresentationName() {
        return getStart().toString("dd/MM/yyyy") + " - " + getEnd().toString("dd/MM/yyyy");
    }

    public static List<CandidacyPeriod> readAllByType(Class<? extends CandidacyPeriod> clazz) {
        List<CandidacyPeriod> result = new ArrayList<CandidacyPeriod>();

        Collection<CandidacyPeriod> candidacyPeriods = Bennu.getInstance().getCandidacyPeriodsSet();

        for (CandidacyPeriod candidacyPeriod : candidacyPeriods) {
            if (clazz.equals(candidacyPeriod.getClass())) {
                result.add(candidacyPeriod);
            }

        }

        Collections.sort(result, LAST_CANDIDACY_PERIOD);

        return result;
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
    public boolean hasExecutionInterval() {
        return getExecutionInterval() != null;
    }

    @Deprecated
    public boolean hasStart() {
        return getStart() != null;
    }

}
