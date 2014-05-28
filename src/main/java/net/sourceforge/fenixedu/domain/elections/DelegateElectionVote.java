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
import net.sourceforge.fenixedu.domain.student.Student;

import org.fenixedu.bennu.core.domain.Bennu;

public class DelegateElectionVote extends DelegateElectionVote_Base {

    protected DelegateElectionVote() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public DelegateElectionVote(DelegateElectionVotingPeriod votingPeriod, Student student) {
        this();
        checkParameters(student, votingPeriod);
        setDelegateElection(votingPeriod);
        setStudent(student);
    }

    public void delete() {
        setStudent(null);
        setDelegateElection(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private void checkParameters(final Student student, final DelegateElectionVotingPeriod votingPeriod) {
        if (student == null) {
            throw new DomainException("error.student.cannot.be.null");
        }
        if (votingPeriod == null) {
            throw new DomainException("error.votingPeriod.cannot.be.null");
        }
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDelegateElection() {
        return getDelegateElection() != null;
    }

}
