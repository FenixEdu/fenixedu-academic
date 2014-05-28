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
package net.sourceforge.fenixedu.dataTransferObject.student.elections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionPeriod;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class StudentVoteBean implements Serializable {

    private Student student;
    private boolean selectedStudent;

    public StudentVoteBean() {
    }

    public StudentVoteBean(Student student) {
        setStudent(student);
        setSelectedStudent(false);
    }

    public Student getStudent() {
        return (this.student == null) ? null : student;

    }

    public void setStudent(Student student) {
        this.student = student;

    }

    public boolean getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(boolean selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public List<Student> getSelectedStudentVote(String studentType) {
        final User userView = Authenticate.getUser();
        final Student student = userView.getPerson().getStudent();
        final YearDelegateElection yearDelegateElection = getYearDelegateElectionForStudent(student);

        final DelegateElectionPeriod currentPeriod =
                (yearDelegateElection != null ? yearDelegateElection.getCurrentElectionPeriod() : null);
        Collection<Student> otherStudentsList = new ArrayList<Student>();

        if (currentPeriod != null && currentPeriod.isVotingPeriod()) {
            if (studentType.equals("notCandidate")) {
                otherStudentsList = getNotCandidatedStudents(yearDelegateElection);
            }
            if (studentType.equals("candidate")) {
                otherStudentsList = getCandidates(yearDelegateElection);
            }
            List<Student> otherStudentsBeanList = new ArrayList<Student>();
            for (final Student studentList : otherStudentsList) {
                otherStudentsBeanList.add(studentList);
            }

            Collections.sort(otherStudentsBeanList, Student.NAME_COMPARATOR);
            return otherStudentsBeanList;
        }
        return new ArrayList<Student>();
    }

    public YearDelegateElection getYearDelegateElectionForStudent(Student student) {
        YearDelegateElection yearDelegateElection = null;

        final Registration registration = student.getLastActiveRegistration();

        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (registration != null) {
            final int curricularYear = registration.getCurricularYear(currentExecutionYear);
            yearDelegateElection =
                    registration.getDegree().getYearDelegateElectionWithLastCandidacyPeriod(currentExecutionYear,
                            CurricularYear.readByYear(curricularYear));
            return yearDelegateElection;
        }
        return null;
    }

    private Collection<Student> getCandidates(YearDelegateElection yearDelegateElection) {
        if (yearDelegateElection.getLastVotingPeriod().isSecondRoundElections()) {
            return yearDelegateElection.getLastVotingPeriod().getCandidatesForNewRoundElections();
        }
        return yearDelegateElection.getCandidatesSet();
    }

    private Collection<Student> getNotCandidatedStudents(YearDelegateElection yearDelegateElection) {
        if (yearDelegateElection.getLastVotingPeriod().isSecondRoundElections()) {
            return new LinkedList<Student>();
        }
        return yearDelegateElection.getNotCandidatedStudents();
    }

}
