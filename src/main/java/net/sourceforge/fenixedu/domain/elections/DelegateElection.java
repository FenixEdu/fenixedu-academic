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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public abstract class DelegateElection extends DelegateElection_Base {

    public static final Comparator<DelegateElection> ELECTION_COMPARATOR_BY_CANDIDACY_START_DATE = new BeanComparator(
            "candidacyStartDate");

    public static final Comparator<DelegateElection> ELECTION_COMPARATOR_BY_VOTING_START_DATE = new BeanComparator(
            "lastVotingStartDate");

    public static final Comparator<DelegateElection> ELECTION_COMPARATOR_BY_VOTING_START_DATE_AND_CANDIDACY_START_DATE =
            new Comparator<DelegateElection>() {
                @Override
                public int compare(DelegateElection e1, DelegateElection e2) {
                    if (e1.getLastVotingStartDate() == null && e2.getLastVotingStartDate() != null) {
                        return -1;
                    } else if (e1.getLastVotingStartDate() != null && e2.getLastVotingStartDate() == null) {
                        return 1;
                    } else if (e1.getLastVotingStartDate() == null && e2.getLastVotingStartDate() == null) {
                        return (e1.getCandidacyStartDate().isBefore(e2.getCandidacyStartDate()) ? -1 : 1);
                    } else {
                        return (e1.getLastVotingStartDate().isBefore(e2.getLastVotingStartDate()) ? -1 : 1);
                    }
                }
            };

    protected DelegateElection() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setSentResultsToCandidates(Boolean.FALSE);
    }

    public YearMonthDay getCandidacyStartDate() {
        if (hasCandidacyPeriod()) {
            return getCandidacyPeriod().getStartDate();
        } else {
            return null;
        }
    }

    public YearMonthDay getCandidacyEndDate() {
        if (hasCandidacyPeriod()) {
            return getCandidacyPeriod().getEndDate();
        } else {
            return null;
        }
    }

    public YearMonthDay getLastVotingStartDate() {
        if (hasLastVotingPeriod()) {
            return getLastVotingPeriod().getStartDate();
        } else {
            return null;
        }
    }

    public YearMonthDay getLastVotingEndDate() {
        if (hasLastVotingPeriod()) {
            return getLastVotingPeriod().getEndDate();
        } else {
            return null;
        }
    }

    /**
     * This method is responsible for deleting the object and all its references
     */
    public void delete() {
        if (hasCandidacyPeriod()) {
            getCandidacyPeriod().delete();
        }

        if (hasAnyVotingPeriod()) {
            for (DelegateElectionVotingPeriod votingPeriod : getVotingPeriod()) {
                deleteVotingPeriod(votingPeriod);
            }
        }
        super.getStudentsSet().clear();
        super.getCandidatesSet().clear();

        setElectedStudent(null);
        setDegree(null);
        setExecutionYear(null);

        super.setRootDomainObject(null);
        super.deleteDomainObject();
    }

    protected void deleteVotingPeriod(DelegateElectionVotingPeriod votingPeriod) {

        for (; votingPeriod.hasAnyVotes(); votingPeriod.getVotes().iterator().next().delete()) {
            ;
        }

        if (hasElectedStudent()) {
            setElectedStudent(null);
        }

        votingPeriod.getVotingStudents().clear();

        votingPeriod.delete();

    }

    public DelegateElectionPeriod getLastElectionPeriod() {
        if (hasLastVotingPeriod()) {
            return getLastVotingPeriod();
        } else {
            return getCandidacyPeriod();
        }
    }

    public DelegateElectionPeriod getCurrentElectionPeriod() {
        if (hasLastVotingPeriod() && getLastVotingPeriod().isCurrentPeriod()) {
            return getLastVotingPeriod();
        } else if (getCandidacyPeriod().isCurrentPeriod()) {
            return getCandidacyPeriod();
        }
        return null;
    }

    public boolean hasCandidacyPeriodIntersecting(YearMonthDay startDate, YearMonthDay endDate) {
        if (!(startDate.isAfter(getCandidacyEndDate()) || startDate.isEqual(getCandidacyEndDate())
                || endDate.isBefore(getCandidacyStartDate()) || endDate.isEqual(getCandidacyStartDate()))) {
            return true;
        }
        return false;
    }

    public boolean hasVotingPeriodIntersecting(YearMonthDay startDate, YearMonthDay endDate) {
        if (!(startDate.isAfter(getLastVotingEndDate()) || startDate.isEqual(getLastVotingEndDate())
                || endDate.isBefore(getLastVotingStartDate()) || endDate.isEqual(getLastVotingStartDate()))) {
            return true;
        }
        return false;
    }

    public List<Student> getCandidaciesHadVoted(DelegateElectionVotingPeriod votingPeriod) {
        List<Student> candidateshadVoted = new ArrayList<Student>();
        for (Student student : getCandidatesSet()) {
            if (votingPeriod.hasVotedStudent(student)) {
                candidateshadVoted.add(student);
            }
        }
        return candidateshadVoted;
    }

    public List<Student> getNotCandidaciesHadVoted(DelegateElectionVotingPeriod votingPeriod) {
        List<Student> candidacieshadVoted = new ArrayList<Student>();
        for (Student student : getNotCandidatedStudents()) {
            if (votingPeriod.hasVotedStudent(student)) {
                candidacieshadVoted.add(student);
            }
        }
        return candidacieshadVoted;
    }

    public DelegateElectionVotingPeriod getLastVotingPeriod() {
        DelegateElectionVotingPeriod lastVotingPeriod = null;
        for (DelegateElectionVotingPeriod votingPeriod : getVotingPeriod()) {
            if (lastVotingPeriod == null) {
                lastVotingPeriod = votingPeriod;
            } else {
                if (!votingPeriod.endsBefore(lastVotingPeriod)) {
                    lastVotingPeriod = votingPeriod;
                }
            }
        }
        return lastVotingPeriod;
    }

    public boolean hasLastVotingPeriod() {
        return getVotingPeriodSet().size() > 0;
    }

    public boolean hasVotingPeriod(YearMonthDay startDate, YearMonthDay endDate) {
        return getVotingPeriod(startDate, endDate) != null;
    }

    public DelegateElectionVotingPeriod getVotingPeriod(YearMonthDay startDate, YearMonthDay endDate) {
        for (DelegateElectionVotingPeriod votingPeriod : getVotingPeriod()) {
            if (votingPeriod.containsPeriod(startDate, endDate)) {
                return votingPeriod;
            }
        }
        return null;
    }

    public boolean isCurrentDelegationElection() {
        return getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear());
    }

    public static DelegateElection readCurrentDelegateElectionByDegree(Degree degree) {
        for (DelegateElection election : Bennu.getInstance().getDelegateElectionsSet()) {
            if (election.isCurrentDelegationElection() && election.getDegree().equals(degree)) {
                return election;
            }
        }
        return null;
    }

    @Override
    public Set<Student> getCandidatesSet() {
        if (!hasLastVotingPeriod() || getLastVotingPeriod().isFirstRoundElections()) {
            return super.getCandidatesSet();
        }
        return getLastVotingPeriod().getCandidatesForNewRoundElections();

    }

    public Collection<Student> getNotCandidatedStudents() {
        if (!hasLastVotingPeriod() || getLastVotingPeriod().isFirstRoundElections()) {
            return getNotCandidates();
        }
        // Don't have candidates
        return new LinkedList<Student>();
    }

    private Collection<Student> getNotCandidates() {
        List<Student> result = new ArrayList<Student>(super.getStudentsSet());
        result.removeAll(getCandidatesSet());
        return result;
    }

    /*
     * DOMAIN SERVICES
     */
    public abstract void createVotingPeriod(YearMonthDay startDate, YearMonthDay endDate);

    public abstract void editCandidacyPeriod(YearMonthDay startDate, YearMonthDay endDate);

    public abstract void editVotingPeriod(YearMonthDay startDate, YearMonthDay endDate, DelegateElectionVotingPeriod votingPeriod);

    public abstract void deleteCandidacyPeriod();

    public abstract void deleteVotingPeriod(DelegateElectionVotingPeriod votingPeriod, boolean removeElection);

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod> getVotingPeriod() {
        return getVotingPeriodSet();
    }

    @Deprecated
    public boolean hasAnyVotingPeriod() {
        return !getVotingPeriodSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Student> getStudents() {
        return getStudentsSet();
    }

    @Deprecated
    public boolean hasAnyStudents() {
        return !getStudentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyCandidates() {
        return !getCandidatesSet().isEmpty();
    }

    @Deprecated
    public boolean hasElectedStudent() {
        return getElectedStudent() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSentResultsToCandidates() {
        return getSentResultsToCandidates() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasCandidacyPeriod() {
        return getCandidacyPeriod() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
