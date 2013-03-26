package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;

public class StudentsByTutorBean implements Serializable {
    private Teacher teacher;
    private ExecutionYear studentsEntryYear = null;
    private List<Tutorship> studentsList = new ArrayList<Tutorship>();

    public StudentsByTutorBean(Teacher teacher) {
        setTeacher(teacher);
    }

    public StudentsByTutorBean(Teacher teacher, ExecutionYear studentsEntryYear, List<Tutorship> studentsList) {
        setTeacher(teacher);
        setStudentsEntryYear(studentsEntryYear);
        setStudentsList(studentsList);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ExecutionYear getStudentsEntryYear() {
        return studentsEntryYear;
    }

    public void setStudentsEntryYear(ExecutionYear studentsEntryYear) {
        this.studentsEntryYear = studentsEntryYear;
    }

    public List<Tutorship> getStudentsList() {
        List<Tutorship> students = new ArrayList<Tutorship>();
        students.addAll(studentsList);
        return students;
    }

    public List<Tutorship> getActiveTutorshipsMatchingEntryYear() {
        List<Tutorship> matchingTutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTeacher().getActiveTutorships()) {
            if (getStudentsEntryYear() == null
                    || (tutorship.getStudentCurricularPlan().getRegistration().getIngressionYear() == getStudentsEntryYear())) {
                matchingTutorships.add(tutorship);
            }
        }

        return matchingTutorships;
    }

    public void setStudentsList(List<Tutorship> students) {
        studentsList = new ArrayList<Tutorship>();
        studentsList.addAll(students);
    }
}