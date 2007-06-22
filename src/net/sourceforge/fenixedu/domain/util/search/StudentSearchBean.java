package net.sourceforge.fenixedu.domain.util.search;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

public class StudentSearchBean implements FactoryExecutor, Serializable {

    private Integer studentNumber;

    public Object execute() {
	return search();
    }

    public Student search() {
	return Student.readStudentByNumber(getStudentNumber());
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(final Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

}
