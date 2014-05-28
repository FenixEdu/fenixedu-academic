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

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.student.Student;

public class DelegateElectionResultsByStudentDTO extends DataTranferObject {
    private Student student;
    private DelegateElection election;
    private Integer votesNumber;
    private Double votesPercentage;
    private Double votesRelativePercentage;

    public DelegateElectionResultsByStudentDTO(DelegateElection election, Student Student) {
        setElection(election);
        setStudent(Student);
        setVotesNumber(1);
        setVotesPercentage(0.0);
        setVotesRelativePercentage(0.0);
    }

    public void calculateResults(int totalStudentsCount, int totalVoteCount) {
        double percentage = ((double) getVotesNumber() / (double) totalStudentsCount);
        setVotesPercentage(((int) (percentage * 100) / 100.0) * 100);
        double relativePercentage = ((double) getVotesNumber() / (double) totalVoteCount);
        setVotesRelativePercentage(((int) (relativePercentage * 100) / 100.0) * 100);
    }

    public Student getStudent() {
        return (student);
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public DelegateElection getElection() {
        return (election);
    }

    public void setElection(DelegateElection election) {
        this.election = election;
    }

    public Integer getVotesNumber() {
        return votesNumber;
    }

    public void setVotesNumber(Integer votesNumber) {
        this.votesNumber = votesNumber;
    }

    public Double getVotesPercentage() {
        return votesPercentage;
    }

    public void setVotesPercentage(Double votesPercentage) {
        this.votesPercentage = votesPercentage;
    }

    public Double getVotesRelativePercentage() {
        return votesRelativePercentage;
    }

    public void setVotesRelativePercentage(Double votesRelativePercentage) {
        this.votesRelativePercentage = votesRelativePercentage;
    }

    public boolean getIsElectedStudent() {
        return (getElection().hasElectedStudent() && getElection().getElectedStudent().equals(getStudent()) ? true : false);
    }
}
