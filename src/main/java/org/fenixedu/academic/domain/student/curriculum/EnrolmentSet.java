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
package net.sourceforge.fenixedu.domain.student.curriculum;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class EnrolmentSet extends TreeSet<Enrolment> {

    private final ExecutionYear executionYear;

    public EnrolmentSet(final ExecutionYear executionYear) {
        super(Enrolment.REVERSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
        this.executionYear = executionYear;
    }

    @Override
    public boolean add(final Enrolment enrolment) {
        final ExecutionYear enrolmentExecutionYear = enrolment.getExecutionPeriod().getExecutionYear();
        return executionYear == null || executionYear.compareTo(enrolmentExecutionYear) > 0 ? super.add(enrolment) : false;
    }

    @Override
    public boolean addAll(final Collection<? extends Enrolment> enrolments) {
        boolean changed = false;
        for (final Enrolment enrolment : enrolments) {
            changed &= add(enrolment);
        }
        return changed;
    }

    @Override
    public EnrolmentSet clone() {
        final EnrolmentSet clone = new EnrolmentSet(executionYear);
        clone.addAll(this);
        return clone;
    }

}