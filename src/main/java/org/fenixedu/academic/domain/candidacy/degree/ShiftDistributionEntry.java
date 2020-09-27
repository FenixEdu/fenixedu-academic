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
package org.fenixedu.academic.domain.candidacy.degree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

public class ShiftDistributionEntry extends ShiftDistributionEntry_Base {

    static final public Comparator<ShiftDistributionEntry> NUMBER_COMPARATOR = new Comparator<ShiftDistributionEntry>() {
        @Override
        public int compare(ShiftDistributionEntry o1, ShiftDistributionEntry o2) {
            return o1.getAbstractStudentNumber().compareTo(o2.getAbstractStudentNumber());
        }
    };

    private ShiftDistributionEntry() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setDistributed(Boolean.FALSE);
    }

    public ShiftDistributionEntry(ShiftDistribution shiftDistribution, ExecutionDegree executionDegree, Shift shift,
            Integer abstractStudentNumber) {
        this();
        init(shiftDistribution, executionDegree, shift, abstractStudentNumber);
    }

    private void checkParameters(ShiftDistribution shiftDistribution, ExecutionDegree executionDegree, Shift shift,
            Integer abstractStudentNumber) {
        if (shiftDistribution == null) {
            throw new DomainException("error.candidacy.degree.ShiftDistributionEntry.shiftDistribution.cannot.be.null");
        }
        if (executionDegree == null) {
            throw new DomainException("error.candidacy.degree.ShiftDistributionEntry.executionDegree.cannot.be.null");
        }
        if (shift == null) {
            throw new DomainException("error.candidacy.degree.ShiftDistributionEntry.shift.cannot.be.null");
        }
        if (abstractStudentNumber == null) {
            throw new DomainException("error.candidacy.degree.ShiftDistributionEntry.abstractStudentNumber.cannot.be.null");
        }
    }

    protected void init(ShiftDistribution shiftDistribution, ExecutionDegree executionDegree, Shift shift,
            Integer abstractStudentNumber) {

        checkParameters(shiftDistribution, executionDegree, shift, abstractStudentNumber);

        super.setShiftDistribution(shiftDistribution);
        super.setExecutionDegree(executionDegree);
        super.setShift(shift);
        super.setAbstractStudentNumber(abstractStudentNumber);
    }

    public void delete() {
        super.setShiftDistribution(null);
        super.setExecutionDegree(null);
        super.setShift(null);

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public void changeShift(Shift newShift) {
        super.setShift(newShift);
    }

    public void changeExecutionDegree(final ExecutionDegree executionDegree) {
        super.setExecutionDegree(executionDegree);
    }

    private boolean isFor(final ExecutionYear executionYear) {
        return getShiftDistribution().getExecutionYear() == executionYear;
    }

    @Override
    public void setShiftDistribution(ShiftDistribution shiftDistribution) {
        throw new DomainException("error.candidacy.degree.ShiftDistributionEntry.cannot.modify.shiftDistribution");
    }

    @Override
    public void setExecutionDegree(ExecutionDegree executionDegree) {
        throw new DomainException("error.candidacy.degree.ShiftDistributionEntry.cannot.modify.executionDegree");
    }

    @Override
    public void setShift(Shift shift) {
        throw new DomainException("error.candidacy.degree.ShiftDistributionEntry.cannot.modify.shiftName");
    }

    public boolean alreadyDistributed() {
        return getDistributed().booleanValue();
    }

    static public List<ShiftDistributionEntry> readByAbstractNumber(Integer abstractNumber, final ExecutionYear executionYear) {
        final List<ShiftDistributionEntry> result = new ArrayList<ShiftDistributionEntry>();
        for (final ShiftDistributionEntry entry : Bennu.getInstance().getShiftDistributionEntriesSet()) {
            if (entry.getAbstractStudentNumber().equals(abstractNumber) && entry.isFor(executionYear)) {
                result.add(entry);
            }
        }
        return result;
    }

}
