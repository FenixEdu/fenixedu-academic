package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;

public class StudentsPerformanceInfoBean implements Serializable {
    private DomainReference<Person> person;
    private DomainReference<Degree> degree;
    private DomainReference<ExecutionYear> studentsEntryYear;
    private DomainReference<ExecutionYear> currentMonitoringYear;
    private Integer degreeCurricularPeriod = 5; // default
    private boolean activeTutorships;

    private StudentsPerformanceInfoBean() {
	setDegree(null);
	setStudentsEntryYear(null);
	setCurrentMonitoringYear(null);
    }

    public static StudentsPerformanceInfoBean create(Person person) {
	StudentsPerformanceInfoBean bean = new StudentsPerformanceInfoBean();
	bean.setPerson(person);
	bean.initActiveTutorships();
	return bean;
    }

    public Degree getDegree() {
	return (degree == null ? null : degree.getObject());
    }

    public void setDegree(Degree degree) {
	this.degree = new DomainReference<Degree>(degree);
	this.degreeCurricularPeriod = (degree != null ? (int) degree.getDegreeType().getAcademicPeriod().getWeight()
		: getDegreeCurricularPeriod());
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
	if (!studentsEntryYears.contains(this.studentsEntryYear.getObject())) {
	    setStudentsEntryYear(studentsEntryYears.get(0));
	}
    }

    public void setStudentsEntryYearFromSet(Set<ExecutionYear> studentsEntryYears) {
	if (!studentsEntryYears.contains(this.studentsEntryYear.getObject())) {
	    setStudentsEntryYear(studentsEntryYears.iterator().next());
	}
    }

    public void setCurrentMonitoringYearFromList(List<ExecutionYear> monitoringYears) {
	if (!monitoringYears.contains(this.currentMonitoringYear.getObject())) {
	    setCurrentMonitoringYear(monitoringYears.get(0));
	}
    }

    public Integer getDegreeCurricularPeriod() {
	return degreeCurricularPeriod;
    }

    public void setActiveTutorships(boolean activeTutorships) {
	this.activeTutorships = activeTutorships;
    }

    public boolean getActiveTutorships() {
	return this.activeTutorships;
    }

    public List<Tutorship> getTutorships() {
	Teacher teacher = getPerson().getTeacher();
	if (teacher.hasAnyTutorships()) {
	    if (teacher.getNumberOfActiveTutorships() != 0) {
		return Collections.unmodifiableList(getPerson().getTeacher().getActiveTutorships());
	    } else {
		return Collections.unmodifiableList(getPerson().getTeacher().getPastTutorships());
	    }
	} else
	    return new ArrayList<Tutorship>();
    }

    private void initActiveTutorships() {
	if (getPerson().getTeacher().getNumberOfActiveTutorships() != 0) {
	    setActiveTutorships(true);
	} else {
	    setActiveTutorships(false);
	}
    }
}
