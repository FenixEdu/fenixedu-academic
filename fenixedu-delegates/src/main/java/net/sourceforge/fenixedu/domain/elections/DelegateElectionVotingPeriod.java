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
package org.fenixedu.academic.domain.elections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.student.Student;

import org.joda.time.YearMonthDay;

public class DelegateElectionVotingPeriod extends DelegateElectionVotingPeriod_Base {

    private DelegateElectionVotingPeriod() {
        super();
    }

    public DelegateElectionVotingPeriod(YearMonthDay startDate, YearMonthDay endDate) {
        this();
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public boolean hasVotedStudent(Student student) {

        for (DelegateElectionVote vote : getVotesSet()) {
            if (vote.getStudent() != null && vote.getStudent().equals(student)) {
                return true;
            }
        }
        return false;
    }

    public int getBlankVotesElection() {
        int nrBlankVotes = 0;
        for (DelegateElectionVote vote : getVotesSet()) {
            if (vote instanceof DelegateElectionBlankVote) {
                nrBlankVotes++;
            }
        }
        return nrBlankVotes;
    }

    public int getNrVotesByStudent(Student student) {
        int nrVotes = 0;
        for (DelegateElectionVote vote : getVotesSet()) {
            if (vote instanceof DelegateElectionVote) {
                if (vote.getStudent() != null && vote.getStudent().equals(student)) {
                    nrVotes++;
                }
            }
        }
        return nrVotes;
    }

    public int getTotalPercentageElection(Student student) {

        int votesNumberStudent = getNrVotesByStudent(student);
        double relativePercentage = ((double) votesNumberStudent / (double) getVotesSet().size());
        return (int) (((int) (relativePercentage * 100) / 100.0) * 100);
    }

    public List<DelegateElectionResultsByStudentDTO> getDelegateElectionResults() {
        Map<Student, DelegateElectionResultsByStudentDTO> votingResultsMap =
                new HashMap<Student, DelegateElectionResultsByStudentDTO>();

        Student student = null;
        int totalVoteCount = 0;
        int studentVotesCount = 0;
        DelegateElection election = getDelegateElection();
        int totalStudentsCount = election.getCandidatesSet().size() + election.getStudentsSet().size();

        for (DelegateElectionVote vote : getVotesSet()) {
            totalVoteCount++;
            if (vote.getStudent() == null) {
                continue;
            }
            student = vote.getStudent();
            if (votingResultsMap.containsKey(student)) {
                DelegateElectionResultsByStudentDTO votingResults = votingResultsMap.get(student);
                studentVotesCount = votingResults.getVotesNumber() + 1;
                votingResults.setVotesNumber(studentVotesCount);
            } else {
                votingResultsMap.put(student, new DelegateElectionResultsByStudentDTO(election, student));
            }
        }

        List<DelegateElectionResultsByStudentDTO> votingResultsBeanList =
                new ArrayList<DelegateElectionResultsByStudentDTO>(votingResultsMap.values());
        for (DelegateElectionResultsByStudentDTO results : votingResultsBeanList) {
            results.calculateResults(totalStudentsCount, totalVoteCount);
        }

        return votingResultsBeanList;
    }

    @Override
    public void delete() {
        setDelegateElection(null);
        super.delete();
    }

    @Override
    public boolean isFirstRoundElections() {
        return isRoundElections(1);

    }

    @Override
    public boolean isSecondRoundElections() {
        return isRoundElections(2);
    }

    @Override
    public boolean isOpenRoundElections() {
        return isPastPeriod() && !getVotesSet().isEmpty();
    }

    private boolean isRoundElections(int votingPeriod) {
        if (getDelegateElection().getVotingPeriodSet().size() == votingPeriod) {
            return true;
        }
        return false;
    }

}
