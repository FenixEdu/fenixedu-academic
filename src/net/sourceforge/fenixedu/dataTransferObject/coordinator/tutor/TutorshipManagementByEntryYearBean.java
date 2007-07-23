package net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;

public class TutorshipManagementByEntryYearBean implements Serializable {
	private List<DomainReference<Tutorship>> studentsList;
	
	private DomainReference<ExecutionYear> executionYear;
	
	private DomainReference<Teacher> teacher;

	public TutorshipManagementByEntryYearBean(ExecutionYear executionYear, Teacher teacher) {
		this.studentsList = new ArrayList<DomainReference<Tutorship>>();
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
		this.teacher = new DomainReference<Teacher>(teacher);
	}

	public List<Tutorship> getStudentsList() {
		List<Tutorship> students = new ArrayList<Tutorship>();
		for (DomainReference<Tutorship> tutor : this.studentsList) {
			students.add(tutor.getObject());
		}
		return students;
	}

	public void setStudentsList(List<Tutorship> students) {
		this.studentsList = new ArrayList<DomainReference<Tutorship>>();
		for (Tutorship tutor : students) {
			this.studentsList.add(new DomainReference<Tutorship>(tutor));
		}
	}
	
	public ExecutionYear getExecutionYear() {
		return (executionYear == null ? null : executionYear.getObject());
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}
	
	public Teacher getTeacher() {
		return (teacher == null ? null : teacher.getObject());
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = new DomainReference<Teacher>(teacher);
	}

}
