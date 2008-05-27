package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;

public class StudentsPerformanceInfoBean implements Serializable {
	private DomainReference<Person> person;
	private DomainReference<Degree> degree;
	private DomainReference<ExecutionYear> studentsEntryYear;
	private DomainReference<ExecutionYear> currentMonitoringYear;
	private Integer degreeCurricularPeriod = 5; //default
	
	public StudentsPerformanceInfoBean() {
		setDegree(null);
		setPerson(null);
		setStudentsEntryYear(null);
		setCurrentMonitoringYear(null);
	}

	public Degree getDegree() {
		return (degree == null ? null : degree.getObject());
	}

	public void setDegree(Degree degree) {
		this.degree = new DomainReference<Degree>(degree);
		this.degreeCurricularPeriod = (degree != null ? (int)degree.getDegreeType().getAcademicPeriod().getWeight() : getDegreeCurricularPeriod());
	}

	public ExecutionYear getCurrentMonitoringYear() {
		return (currentMonitoringYear == null ? null : currentMonitoringYear.getObject());
	}

	public void setCurrentMonitoringYear(ExecutionYear currentMonitoringYear) {
		this.currentMonitoringYear = new DomainReference<ExecutionYear>(currentMonitoringYear);
	}

	public ExecutionYear getStudentsEntryYear() {
		return (studentsEntryYear == null ? null : studentsEntryYear.getObject());
	}

	public void setStudentsEntryYear(ExecutionYear studentsEntryYear) {
		this.studentsEntryYear = new DomainReference<ExecutionYear>(studentsEntryYear);
	}

	public Person getPerson() {
		return (person == null ? null : person.getObject());
	}

	public void setPerson(Person person) {
		this.person = new DomainReference<Person>(person);
	}
	
	public void setStudentsEntryYearFromList(List<ExecutionYear> studentsEntryYears) {
		if(!studentsEntryYears.contains(this.studentsEntryYear.getObject())){
			setStudentsEntryYear(studentsEntryYears.get(0));
		}
	}
	
	public void setCurrentMonitoringYearFromList(List<ExecutionYear> monitoringYears) {
		if(!monitoringYears.contains(this.currentMonitoringYear.getObject())){
			setCurrentMonitoringYear(monitoringYears.get(0));
		}
	}
	
	public Integer getDegreeCurricularPeriod() {
		return degreeCurricularPeriod;
	}
}
