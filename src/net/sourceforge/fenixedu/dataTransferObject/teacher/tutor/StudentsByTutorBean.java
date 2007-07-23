package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;


public class StudentsByTutorBean implements Serializable {
	private DomainReference<Teacher> teacher;
	private DomainReference<ExecutionYear> studentsEntryYear;
	private List<DomainReference<Tutorship>> studentsList;
	
	public StudentsByTutorBean(Teacher teacher) {
		setTeacher(teacher);
		studentsList = new ArrayList<DomainReference<Tutorship>>();
	}
	
	public StudentsByTutorBean(Teacher teacher, ExecutionYear studentsEntryYear, List<Tutorship> studentsList) {
		setTeacher(teacher);
		setStudentsEntryYear(studentsEntryYear);
		setStudentsList(studentsList);
	}
	
	public Teacher getTeacher() {
		return (teacher == null ? null : teacher.getObject());
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = new DomainReference<Teacher>(teacher);
	}
	
	public ExecutionYear getStudentsEntryYear() {
		return (studentsEntryYear == null ? null : studentsEntryYear.getObject());
	}

	public void setStudentsEntryYear(ExecutionYear studentsEntryYear) {
		this.studentsEntryYear = new DomainReference<ExecutionYear>(studentsEntryYear);
	}
	
	public List<Tutorship> getStudentsList() {
		List<Tutorship> students = new ArrayList<Tutorship>();
		for(DomainReference<Tutorship> tutor : this.studentsList) {
			students.add(tutor.getObject());
		}
		return students;
	}
	
	public void setStudentsList(List<Tutorship> students) {
		this.studentsList = new ArrayList<DomainReference<Tutorship>>();
		for(Tutorship tutor : students) {
			this.studentsList.add(new DomainReference<Tutorship>(tutor));
		}
	}
}