package net.sourceforge.fenixedu.dataTransferObject.student.elections;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Student;

public class StudentVoteBean implements Serializable {
	
	private DomainReference<Student> student;
	private boolean selectedStudent;
	
	public StudentVoteBean() {}
	
	public StudentVoteBean(Student Student) {
		setStudent(Student);
		setSelectedStudent(false);
	}
	
	public Student getStudent() {
		return (student == null ? null : student.getObject());
	}

	public void setStudent(Student student) {
		this.student = new DomainReference<Student>(student);
	}
	
	public boolean getSelectedStudent() {
		return selectedStudent;
	}

	public void setSelectedStudent(boolean selectedStudent) {
		this.selectedStudent = selectedStudent;
	}
}
