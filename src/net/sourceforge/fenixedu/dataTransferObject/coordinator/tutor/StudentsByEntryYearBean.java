package net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.util.Month;

public class StudentsByEntryYearBean implements Serializable {
	private Integer teacherNumber;
	
	private DomainReference<Teacher> teacher;
	
	private Integer executionDegreeID;
	
	private Integer degreeCurricularPlanID;
	
	private Month tutorshipEndMonth;
	
	private Integer tutorshipEndYear;
	
	private List<DomainReference<StudentCurricularPlan>> studentsList;
	
	private List<DomainReference<StudentCurricularPlan>> studentsToCreateTutorshipList;
	
	private Integer numberOfStudentsToCreateTutorship;
	
	private DomainReference<ExecutionYear> executionYear;

	public Integer getTeacherNumber() {
		return teacherNumber;
	}

	public void setTeacherNumber(Integer teacherNumber) {
		this.teacherNumber = teacherNumber;
	}

	public StudentsByEntryYearBean(ExecutionYear executionYear) {
		this.studentsList = new ArrayList<DomainReference<StudentCurricularPlan>>();
		this.studentsToCreateTutorshipList = new ArrayList<DomainReference<StudentCurricularPlan>>();
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}

	public List<StudentCurricularPlan> getStudentsList() {
		List<StudentCurricularPlan> students = new ArrayList<StudentCurricularPlan>();
		for (DomainReference<StudentCurricularPlan> student : this.studentsList) {
			students.add(student.getObject());
		}
		return students;
	}

	public void setStudentsList(List<StudentCurricularPlan> students) {
		this.studentsList = new ArrayList<DomainReference<StudentCurricularPlan>>();
		for (StudentCurricularPlan student : students) {
			this.studentsList.add(new DomainReference<StudentCurricularPlan>(student));
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

	public Integer getNumberOfStudentsWithoutTutor() {
		return studentsList.size();
	}

	public Integer getDegreeCurricularPlanID() {
		return degreeCurricularPlanID;
	}

	public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
		this.degreeCurricularPlanID = degreeCurricularPlanID;
	}

	public Integer getExecutionDegreeID() {
		return executionDegreeID;
	}

	public void setExecutionDegreeID(Integer executionDegreeID) {
		this.executionDegreeID = executionDegreeID;
	}

	public List<StudentCurricularPlan> getStudentsToCreateTutorshipList() {
		List<StudentCurricularPlan> students = new ArrayList<StudentCurricularPlan>();
		for (DomainReference<StudentCurricularPlan> student : this.studentsToCreateTutorshipList) {
			students.add(student.getObject());
		}
		return students;
	}

	public void setStudentsToCreateTutorshipList(List<StudentCurricularPlan> students) {
		this.studentsToCreateTutorshipList = new ArrayList<DomainReference<StudentCurricularPlan>>();
		for (StudentCurricularPlan student : students) {
			this.studentsToCreateTutorshipList.add(new DomainReference<StudentCurricularPlan>(student));
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
		this.studentsToCreateTutorshipList = new ArrayList<DomainReference<StudentCurricularPlan>>();
		int i = 0;
		Iterator iterator = this.studentsList.iterator();
		while(iterator.hasNext() && i < this.numberOfStudentsToCreateTutorship) {
			StudentCurricularPlan scp = ((DomainReference<StudentCurricularPlan>)iterator.next()).getObject();
			if (scp.isLastStudentCurricularPlanFromRegistration() && scp.getRegistration().isActive()) {
				this.studentsToCreateTutorshipList.add(new DomainReference<StudentCurricularPlan>(scp));
				i++;
			}
		}
		setNumberOfStudentsToCreateTutorship(this.studentsToCreateTutorshipList.size());//subtract students without active registration state type
	}
	
	public void clearSelectedStudentsToCreateTutorshipList() {
		this.studentsToCreateTutorshipList.clear();
	}
	
}
