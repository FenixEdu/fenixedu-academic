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
package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;

public class YearDelegateElectionsPeriodsByDegreeBean implements Serializable {
    private ExecutionYear executionYear;
    private Degree degree;
    private List<YearDelegateElection> elections;

    private List<DelegateElection> firstYearElections;
    private List<DelegateElection> secondYearElections;
    private List<DelegateElection> thirdYearElections;
    private List<DelegateElection> fourthYearElections;
    private List<DelegateElection> fifthYearElections;

    public YearDelegateElectionsPeriodsByDegreeBean(Degree degree, ExecutionYear executionYear,
            List<YearDelegateElection> elections) {
        setDegree(degree);
        setElections(elections);
        setExecutionYear(executionYear);

        for (int i = 1; i <= degree.getDegreeType().getYears(); i++) {
            setElectionsByYear(i);
        }
    }

    public Degree getDegree() {
        return (degree);
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public List<YearDelegateElection> getElections() {
        List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
        for (YearDelegateElection electionDR : this.elections) {
            result.add(electionDR);
        }
        return result;
    }

    public void setElections(List<YearDelegateElection> elections) {
        this.elections = new ArrayList<YearDelegateElection>();
        for (YearDelegateElection election : elections) {
            this.elections.add(election);
        }
    }

    public ExecutionYear getExecutionYear() {
        return (executionYear);
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
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

    public List<DelegateElection> getElectionsGivenYear(int year) {
        List<DelegateElection> givenYearElections = new ArrayList<DelegateElection>();
        for (DelegateElection election : getElections()) {
            if (((YearDelegateElection) election).getCurricularYear().getYear() == year) {
                givenYearElections.add(election);
            }
        }
        return givenYearElections;
    }

    public List<YearDelegateElection> getFirstYearElections() {
        List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
        for (DelegateElection electionDR : this.firstYearElections) {
            result.add((YearDelegateElection) electionDR);
        }
        return result;
    }

    public List<YearDelegateElection> getSecondYearElections() {
        List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
        for (DelegateElection electionDR : this.secondYearElections) {
            result.add((YearDelegateElection) electionDR);
        }
        return result;
    }

    public List<YearDelegateElection> getThirdYearElections() {
        List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
        for (DelegateElection electionDR : this.thirdYearElections) {
            result.add((YearDelegateElection) electionDR);
        }
        return result;
    }

    public List<YearDelegateElection> getFourthYearElections() {
        List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
        for (DelegateElection electionDR : this.fourthYearElections) {
            result.add((YearDelegateElection) electionDR);
        }
        return result;
    }

    public List<YearDelegateElection> getFifthYearElections() {
        List<YearDelegateElection> result = new ArrayList<YearDelegateElection>();
        for (DelegateElection electionDR : this.fifthYearElections) {
            result.add((YearDelegateElection) electionDR);
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
