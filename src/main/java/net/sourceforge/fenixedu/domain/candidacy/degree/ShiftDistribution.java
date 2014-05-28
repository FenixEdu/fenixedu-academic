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
package net.sourceforge.fenixedu.domain.candidacy.degree;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class ShiftDistribution extends ShiftDistribution_Base {

    private ShiftDistribution() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    public ShiftDistribution(ExecutionYear executionYear) {
        this();
        init(executionYear);
    }

    private void checkParameters(ExecutionYear executionYear) {
        if (executionYear == null) {
            throw new DomainException("error.candidacy.degree.ShiftDistribution.executionYear.cannot.be.null");
        }
    }

    protected void init(ExecutionYear executionYear) {
        checkParameters(executionYear);
        if (executionYear.hasShiftDistribution()) {
            throw new DomainException("error.candidacy.degree.ShiftDistribution.executionYear.already.has.a.shiftDistribution");
        }
        super.setExecutionYear(executionYear);
    }

    public void delete() {
        for (; !getShiftDistributionEntriesSet().isEmpty(); getShiftDistributionEntriesSet().iterator().next().delete()) {
            ;
        }
        super.setExecutionYear(null);

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public List<ShiftDistributionEntry> getEntriesByStudentNumber(Integer studentNumber) {
        final List<ShiftDistributionEntry> result = new ArrayList<ShiftDistributionEntry>();
        for (final ShiftDistributionEntry shiftDistributionEntry : getShiftDistributionEntriesSet()) {
            if (shiftDistributionEntry.getAbstractStudentNumber().equals(studentNumber)) {
                result.add(shiftDistributionEntry);
            }
        }
        return result;
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        throw new DomainException("error.candidacy.degree.ShiftDistribution.cannot.modify.executionYear");
    }

    public boolean contains(int abstractStudentNumber, final ExecutionDegree executionDegree) {
        for (final ShiftDistributionEntry distributionEntry : executionDegree.getShiftDistributionEntriesSet()) {
            if (distributionEntry.getAbstractStudentNumber().intValue() == abstractStudentNumber) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistributionEntry> getShiftDistributionEntries() {
        return getShiftDistributionEntriesSet();
    }

    @Deprecated
    public boolean hasAnyShiftDistributionEntries() {
        return !getShiftDistributionEntriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
