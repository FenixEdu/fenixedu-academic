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
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;

public class GroupStudent extends GroupStudent_Base {

    public static final Comparator<GroupStudent> COMPARATOR_BY_STUDENT_NUMBER = new BeanComparator("student.number");

    public static final Comparator<GroupStudent> COMPARATOR_BY_YEAR = new Comparator<GroupStudent>() {

        @Override
        public int compare(final GroupStudent gs1, final GroupStudent gs2) {
            final ExecutionDegree ed1 = gs1.getFinalDegreeDegreeWorkGroup().getExecutionDegree();
            final ExecutionDegree ed2 = gs2.getFinalDegreeDegreeWorkGroup().getExecutionDegree();
            final ExecutionYear ey1 = ed1.getExecutionYear();
            final ExecutionYear ey2 = ed2.getExecutionYear();
            return ey1 == ey2 ? ed1.getDegree().getSigla().compareTo(ed2.getDegree().getSigla()) : ey1.compareTo(ey2);
        }

    };

    public static final Comparator<GroupStudent> COMPARATOR_BY_YEAR_REVERSE = new Comparator<GroupStudent>() {

        @Override
        public int compare(final GroupStudent gs1, final GroupStudent gs2) {
            return 0 - COMPARATOR_BY_YEAR.compare(gs1, gs2);
        }

    };

    public GroupStudent() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setRootDomainObject(null);
        setFinalDegreeDegreeWorkGroup(null);
        setFinalDegreeWorkProposalConfirmation(null);
        setRegistration(null);
        deleteDomainObject();
    }

    @Override
    @Deprecated
    public Registration getStudent() {
        return getRegistration();
    }

    @Deprecated
    public boolean hasStudent() {
        return hasRegistration();
    }

    @Override
    @Deprecated
    public void setStudent(Registration registration) {
        setRegistration(registration);
    }

    public Registration getRegistration() {
        return super.getStudent();
    }

    public boolean hasRegistration() {
        return super.getStudent() != null;
    }

    public void removeRegistration() {
        super.setStudent(null);
    }

    public void setRegistration(Registration registration) {
        super.setStudent(registration);
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFinalDegreeWorkProposalConfirmation() {
        return getFinalDegreeWorkProposalConfirmation() != null;
    }

    @Deprecated
    public boolean hasFinalDegreeDegreeWorkGroup() {
        return getFinalDegreeDegreeWorkGroup() != null;
    }

}
