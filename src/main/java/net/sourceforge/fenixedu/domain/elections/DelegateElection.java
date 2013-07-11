package net.sourceforge.fenixedu.domain.elections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.beanutils.BeanComparator;
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
        setRootDomainObject(RootDomainObject.getInstance());
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
        super.getStudents().clear();
        super.getCandidates().clear();

        setElectedStudent(null);
        setDegree(null);
        setExecutionYear(null);

        super.setRootDomainObject(null);
        super.deleteDomainObject();
    }

    protected void deleteVotingPeriod(DelegateElectionVotingPeriod votingPeriod) {

        for (; votingPeriod.hasAnyVotes(); votingPeriod.getVotes().get(0).delete()) {
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
        for (Student student : getCandidates()) {
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
        return getVotingPeriodCount() > 0;
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
        for (DelegateElection election : RootDomainObject.getInstance().getDelegateElections()) {
            if (election.isCurrentDelegationElection() && election.getDegree().equals(degree)) {
                return election;
            }
        }
        return null;
    }

    @Override
    public List<Student> getCandidates() {
        if (!hasLastVotingPeriod() || getLastVotingPeriod().isFirstRoundElections()) {
            return super.getCandidates();
        }
        return getLastVotingPeriod().getCandidatesForNewRoundElections();

    }

    public List<Student> getNotCandidatedStudents() {
        if (!hasLastVotingPeriod() || getLastVotingPeriod().isFirstRoundElections()) {
            return getNotCandidates();
        }
        // Don't have candidates
        return new LinkedList<Student>();
    }

    private List<Student> getNotCandidates() {
        List<Student> result = new ArrayList<Student>(super.getStudents());
        result.removeAll(getCandidates());
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

}
