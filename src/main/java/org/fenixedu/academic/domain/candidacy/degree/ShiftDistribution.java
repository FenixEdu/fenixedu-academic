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

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

import java.util.stream.Stream;

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
        if (executionYear.getShiftDistribution() != null) {
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

    public Stream<ShiftDistributionEntry> getEntriesByStudentNumber(final Integer studentNumber) {
        return getShiftDistributionEntriesSet().stream()
                .filter(entry -> entry.getAbstractStudentNumber().equals(studentNumber));
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

}
