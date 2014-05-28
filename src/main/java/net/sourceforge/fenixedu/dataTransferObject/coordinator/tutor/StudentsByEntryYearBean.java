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
package net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Month;
import pt.ist.fenixframework.FenixFramework;

public class StudentsByEntryYearBean implements Serializable {
    private String teacherId;

    private Teacher teacher;

    private String executionDegreeID;

    private String degreeCurricularPlanID;

    private Month tutorshipEndMonth;

    private Integer tutorshipEndYear;

    private List<StudentCurricularPlan> studentsList;

    private List<StudentCurricularPlan> studentsToCreateTutorshipList;

    private Integer numberOfStudentsToCreateTutorship;

    private ExecutionYear executionYear;

    private String showAll;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public StudentsByEntryYearBean(ExecutionYear executionYear) {
        this.studentsList = new ArrayList<StudentCurricularPlan>();
        this.studentsToCreateTutorshipList = new ArrayList<StudentCurricularPlan>();
        this.executionYear = executionYear;
    }

    public List<StudentCurricularPlan> getStudentsList() {
        List<StudentCurricularPlan> students = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan student : this.studentsList) {
            students.add(student);
        }
        return students;
    }

    public void setStudentsList(List<StudentCurricularPlan> students) {
        this.studentsList = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan student : students) {
            this.studentsList.add(student);
        }
    }

    public ExecutionYear getExecutionYear() {
        return (executionYear);
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public Teacher getTeacher() {
        return (teacher);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getNumberOfStudentsWithoutTutor() {
        return studentsList.size();
    }

    public String getDegreeCurricularPlanID() {
        return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(String degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public String getExecutionDegreeID() {
        return executionDegreeID;
    }

    public void setExecutionDegreeID(String executionDegreeID) {
        this.executionDegreeID = executionDegreeID;
    }

    public List<StudentCurricularPlan> getStudentsToCreateTutorshipList() {
        List<StudentCurricularPlan> students = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan student : this.studentsToCreateTutorshipList) {
            students.add(student);
        }
        return students;
    }

    public void setStudentsToCreateTutorshipList(List<StudentCurricularPlan> students) {
        this.studentsToCreateTutorshipList = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan student : students) {
            this.studentsToCreateTutorshipList.add(student);
        }
    }

    public Integer getNumberOfStudentsToCreateTutorship() {
        return numberOfStudentsToCreateTutorship;
    }

    public void setNumberOfStudentsToCreateTutorship(Integer numberOfStudentsToCreateTutorship) {
        this.numberOfStudentsToCreateTutorship = numberOfStudentsToCreateTutorship;
    }

    public Month getTutorshipEndMonth() {
        return tutorshipEndMonth;
    }

    public void setTutorshipEndMonth(Month tutorshipEndMonth) {
        this.tutorshipEndMonth = tutorshipEndMonth;
    }

    public Integer getTutorshipEndYear() {
        return tutorshipEndYear;
    }

    public void setTutorshipEndYear(Integer tutorshipEndYear) {
        this.tutorshipEndYear = tutorshipEndYear;
    }

    public void selectStudentsToCreateTutorshipList() {
        this.studentsToCreateTutorshipList = new ArrayList<StudentCurricularPlan>();
        int i = 0;
        Iterator iterator = this.studentsList.iterator();
        while (iterator.hasNext() && i < this.numberOfStudentsToCreateTutorship) {
            StudentCurricularPlan scp = ((StudentCurricularPlan) iterator.next());
            if (scp.isLastStudentCurricularPlanFromRegistration() && scp.getRegistration().isActive()) {
                this.studentsToCreateTutorshipList.add(scp);
                i++;
            }
        }
        setNumberOfStudentsToCreateTutorship(this.studentsToCreateTutorshipList.size());// subtract
        // students
        // without
        // active
        // registration
        // state
        // type
    }

    /**
     * Inserts students in bean
     * 
     * 
     * @param personsExternalIds
     * @param executionSemester
     * @param executionDegree
     * @return list of persons with no registration in execution course
     */
    public List<Person> receiveStudentsToCreateTutorshipList(String[] personsExternalIds, ExecutionDegree executionDegree) {
        List<StudentCurricularPlan> students = new ArrayList<StudentCurricularPlan>();
        List<Person> studentsNotRegistered = new ArrayList<Person>();
        if (personsExternalIds != null) {
            for (String studentIID : personsExternalIds) {
                Person person = FenixFramework.getDomainObject(studentIID);
                Student student = person.getStudent();
                Registration registration = student.getActiveRegistrationFor(executionDegree.getDegree());
                if (registration != null) {
                    students.add(registration.getActiveStudentCurricularPlan());
                } else {
                    studentsNotRegistered.add(person);
                }
            }
            this.setStudentsToCreateTutorshipList(students);
        }
        return studentsNotRegistered;
    }

    public void clearSelectedStudentsToCreateTutorshipList() {
        this.studentsToCreateTutorshipList.clear();
    }

    public String getShowAll() {
        return showAll;
    }

    public void setShowAll(String showAll) {
        this.showAll = showAll;
    }

}
