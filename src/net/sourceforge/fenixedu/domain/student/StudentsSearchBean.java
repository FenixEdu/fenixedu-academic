package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

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

    public Set<Student> searchForOffice(Unit office) {
	final Set<Student> students = new HashSet<Student>();
	for (Student student : search()) {
	    if (student.hasRegistrationForOffice(office)) {
		students.add(student);
	    }
	}
	return students;
    }

    // Convenience method for invocation as bean.
    public Set<Student> getSearch() {
	return search();
    }

}
