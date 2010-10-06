package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Student;

public class StudentsPerformanceInfoBean implements Serializable {
    private Person person;
    private Degree degree;
    private ExecutionYear studentsEntryYear;
    private ExecutionYear currentMonitoringYear;
    private Integer degreeCurricularPeriod = 5; // default
    private boolean activeTutorships;

    public StudentsPerformanceInfoBean() {
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
	return (degree);
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
	this.degreeCurricularPeriod = (degree != null ? (int) degree.getDegreeType().getAcademicPeriod().getWeight()
		: getDegreeCurricularPeriod());
    }

    public ExecutionYear getCurrentMonitoringYear() {
	return (currentMonitoringYear);
    }

    public void setCurrentMonitoringYear(ExecutionYear currentMonitoringYear) {
	this.currentMonitoringYear = currentMonitoringYear;
    }

    public ExecutionYear getStudentsEntryYear() {
	return (studentsEntryYear);
    }

    public void setStudentsEntryYear(ExecutionYear studentsEntryYear) {
	this.studentsEntryYear = studentsEntryYear;
    }

    public Person getPerson() {
	return (person);
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public void setStudentsEntryYearFromList(List<ExecutionYear> studentsEntryYears) {
	if (!studentsEntryYears.contains(this.studentsEntryYear)) {
	    setStudentsEntryYear(studentsEntryYears.get(0));
	}
    }

    public void setStudentsEntryYearFromSet(Set<ExecutionYear> studentsEntryYears) {
	if (!studentsEntryYears.contains(this.studentsEntryYear)) {
	    setStudentsEntryYear(studentsEntryYears.iterator().next());
	}
    }

    public void setCurrentMonitoringYearFromList(List<ExecutionYear> monitoringYears) {
	if (!monitoringYears.contains(this.currentMonitoringYear)) {
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
	List<Tutorship> result = new ArrayList<Tutorship>();
	result.addAll(teacher.getActiveTutorships());
	result.addAll(teacher.getPastTutorships());
	return result;
    }

    public List<Tutorship> getTutorshipsFromStudent() {
	Student student = getPerson().getStudent();
	List<Tutorship> result = new ArrayList<Tutorship>();
	result.addAll(student.getActiveTutorships());
	return result;
    }

    private void initActiveTutorships() {
	if (getPerson().getTeacher().getNumberOfActiveTutorships() != 0) {
	    setActiveTutorships(true);
	} else {
	    setActiveTutorships(false);
	}
    }
}
