package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class StudentsSearchBean implements Serializable {

    private Integer number;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Set<Student> search() {
        final Set<Student> students = new HashSet<Student>();
        final Student student = Student.readStudentByNumber(number);
        if (student != null) {
            students.add(student);
        }
        return students;
    }

    // Convinience method for invocation as bean.
    public Set<Student> getSearch() {
        return search();
    }

}
