package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;

public class YearDelegateElectionsPeriodsByDegreeBean implements Serializable {
    private DomainReference<ExecutionYear> executionYear;
    private DomainReference<Degree> degree;
    private List<DomainReference<YearDelegateElection>> elections;

    private List<DomainReference<DelegateElection>> firstYearElections;
    private List<DomainReference<DelegateElection>> secondYearElections;
    private List<DomainReference<DelegateElection>> thirdYearElections;
    private List<DomainReference<DelegateElection>> fourthYearElections;
    private List<DomainReference<DelegateElection>> fifthYearElections;

    public YearDelegateElectionsPeriodsByDegreeBean(Degree degree, ExecutionYear executionYear,
	    List<YearDelegateElection> elections) {
	setDegree(degree);
	setElections(elections);
	setExecutionYear(executionYear);

	for (int i = 1; i <= degree.getDegreeType().getYears(); i++)
	    setElectionsByYear(i);
    }

    public Degree getDegree() {
	return (degree == null ? null : degree.getObject());
    }

    public void setDegree(Degree degree) {
	this.degree = new DomainReference<Degree>(degree);
    }

    public List<YearDelegateElection> getElections() {
	List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
	for (DomainReference<YearDelegateElection> electionDR : this.elections) {
	    result.add(electionDR.getObject());
	}
	return result;
    }

    public void setElections(List<YearDelegateElection> elections) {
	this.elections = new ArrayList<DomainReference<YearDelegateElection>>();
	for (YearDelegateElection election : elections) {
	    this.elections.add(new DomainReference<YearDelegateElection>(election));
	}
    }

    public ExecutionYear getExecutionYear() {
	return (executionYear == null ? null : executionYear.getObject());
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    private void setElectionsByYear(int year) {
	switch (year) {
	case 1:
	    this.firstYearElections = getElectionsGivenYear(year);
	    break;
	case 2:
	    this.secondYearElections = getElectionsGivenYear(year);
	    break;
	case 3:
	    this.thirdYearElections = getElectionsGivenYear(year);
	    break;
	case 4:
	    this.fourthYearElections = getElectionsGivenYear(year);
	    break;
	case 5:
	    this.fifthYearElections = getElectionsGivenYear(year);
	    break;
	default:
	    break;
	}
    }

    public List<DomainReference<DelegateElection>> getElectionsGivenYear(int year) {
	List<DomainReference<DelegateElection>> givenYearElections = new ArrayList<DomainReference<DelegateElection>>();
	for (DelegateElection election : getElections()) {
	    if (((YearDelegateElection) election).getCurricularYear().getYear() == year) {
		givenYearElections.add(new DomainReference<DelegateElection>(election));
	    }
	}
	return givenYearElections;
    }

    public List<YearDelegateElection> getFirstYearElections() {
	List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
	for (DomainReference<DelegateElection> electionDR : this.firstYearElections) {
	    result.add((YearDelegateElection) electionDR.getObject());
	}
	return result;
    }

    public List<YearDelegateElection> getSecondYearElections() {
	List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
	for (DomainReference<DelegateElection> electionDR : this.secondYearElections) {
	    result.add((YearDelegateElection) electionDR.getObject());
	}
	return result;
    }

    public List<YearDelegateElection> getThirdYearElections() {
	List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
	for (DomainReference<DelegateElection> electionDR : this.thirdYearElections) {
	    result.add((YearDelegateElection) electionDR.getObject());
	}
	return result;
    }

    public List<YearDelegateElection> getFourthYearElections() {
	List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
	for (DomainReference<DelegateElection> electionDR : this.fourthYearElections) {
	    result.add((YearDelegateElection) electionDR.getObject());
	}
	return result;
    }

    public List<YearDelegateElection> getFifthYearElections() {
	List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
	for (DomainReference<DelegateElection> electionDR : this.fifthYearElections) {
	    result.add((YearDelegateElection) electionDR.getObject());
	}
	return result;
    }

    // CANDIDACY PERIODS
    public YearDelegateElection getFirstYearLastCandidacyPeriod() {
	return (!getFirstYearElections().isEmpty() ? (YearDelegateElection) Collections.max(getFirstYearElections(),
		DelegateElection.ELECTION_COMPARATOR_BY_CANDIDACY_START_DATE) : null);
    }

    public YearDelegateElection getSecondYearLastCandidacyPeriod() {
	return (!getSecondYearElections().isEmpty() ? (YearDelegateElection) Collections.max(getSecondYearElections(),
		DelegateElection.ELECTION_COMPARATOR_BY_CANDIDACY_START_DATE) : null);
    }

    public YearDelegateElection getThirdYearLastCandidacyPeriod() {
	return (!getThirdYearElections().isEmpty() ? (YearDelegateElection) Collections.max(getThirdYearElections(),
		DelegateElection.ELECTION_COMPARATOR_BY_CANDIDACY_START_DATE) : null);
    }

    public YearDelegateElection getFourthYearLastCandidacyPeriod() {
	return (!getFourthYearElections().isEmpty() ? (YearDelegateElection) Collections.max(getFourthYearElections(),
		DelegateElection.ELECTION_COMPARATOR_BY_CANDIDACY_START_DATE) : null);
    }

    public YearDelegateElection getFifthYearLastCandidacyPeriod() {
	return (!getFifthYearElections().isEmpty() ? (YearDelegateElection) Collections.max(getFifthYearElections(),
		DelegateElection.ELECTION_COMPARATOR_BY_CANDIDACY_START_DATE) : null);
    }

    // VOTING PERIODS
    public YearDelegateElection getFirstYearVotingPeriod() {
	return getFirstYearLastCandidacyPeriod();
    }

    public YearDelegateElection getSecondYearVotingPeriod() {
	return getSecondYearLastCandidacyPeriod();
    }

    public YearDelegateElection getThirdYearVotingPeriod() {
	return getThirdYearLastCandidacyPeriod();
    }

    public YearDelegateElection getFourthYearVotingPeriod() {
	return getFourthYearLastCandidacyPeriod();
    }

    public YearDelegateElection getFifthYearVotingPeriod() {
	return getFifthYearLastCandidacyPeriod();
    }
}
