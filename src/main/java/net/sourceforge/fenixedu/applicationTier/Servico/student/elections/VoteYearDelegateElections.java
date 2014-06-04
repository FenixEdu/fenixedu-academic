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
package net.sourceforge.fenixedu.applicationTier.Servico.student.elections;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionBlankVote;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVote;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

public class VoteYearDelegateElections {

    @Atomic
    public static void run(YearDelegateElection yearDelegateElection, Student student, Student votedStudent)
            throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        DelegateElectionVotingPeriod votingPeriod = yearDelegateElection.getCurrentVotingPeriod();

        try {
            if (!votingPeriod.getVotingStudents().contains(student)) {
                final String fromName = getString("VoteYearDelegateElections.email.fromName");
                final String fromAddress = getString("VoteYearDelegateElections.email.fromAddress");
                final String subject = fromName + "-" + getString("VoteYearDelegateElections.email.subject");
                final String msg = getString("VoteYearDelegateElections.email.message");
                final Person person = student.getPerson();
                DelegateElectionVote vote = createDelegateElectionVote(votingPeriod, votedStudent);
                votingPeriod.addVotingStudents(student);
                votingPeriod.addVotes(vote);
                new Message(Bennu.getInstance().getSystemSender(), new ConcreteReplyTo(fromAddress).asCollection(),
                        new Recipient(UserGroup.of(person.getUser())).asCollection(), subject, msg, "");
            } else {
                throw new FenixServiceException("error.student.elections.voting.studentAlreadyVoted");
            }
        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage(), ex.getArgs());
        }
    }

    private static String getString(final String key) {
        return BundleUtil.getString(Bundle.DELEGATE, key);
    }

    private static DelegateElectionVote createDelegateElectionVote(DelegateElectionVotingPeriod votingPeriod, Student votedStudent) {
        if (DelegateElectionBlankVote.isBlankVote(votedStudent)) {
            return new DelegateElectionBlankVote(votingPeriod);
        }
        return new DelegateElectionVote(votingPeriod, votedStudent);
    }

}