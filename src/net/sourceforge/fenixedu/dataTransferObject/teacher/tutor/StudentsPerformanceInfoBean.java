package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipEntryExecutionYearProvider;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipMonitoringExecutionYearProvider;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipEntryExecutionYearProvider.TutorshipEntryExecutionYearProviderForSingleStudent;

public class StudentsPerformanceInfoBean implements Serializable {
    private Teacher teacher;
    private Student student;
    private Degree degree;
    private ExecutionYear studentsEntryYear;
    private ExecutionYear currentMonitoringYear;
    private Integer degreeCurricularPeriod = 5; // default
    private boolean activeTutorships;

    public StudentsPerformanceInfoBean() {
	degree = null;
	studentsEntryYear = null;
	currentMonitoringYear = null;
    }

    public static StudentsPerformanceInfoBean create(Teacher teacher) {
	StudentsPerformanceInfoBean bean = new StudentsPerformanceInfoBean();
	bean.setTeacher(teacher);
	bean.initActiveTutorships();
	return bean;
    }

    public static StudentsPerformanceInfoBean create(Student student) {
	StudentsPerformanceInfoBean bean = new StudentsPerformanceInfoBean();
	bean.setStudent(student);
	return bean;
    }

    public Student getStudent() {
	return student;
    }

    public void setStudent(Student student) {
	this.student = student;
	refreshStudentsEntryYear();
    }

    public Teacher getTeacher() {
	return teacher;
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = teacher;
	refreshDegree();
    }

    protected void refreshDegree() {
	Set<Degree> degrees = getFilteredDegrees();
	if (!checkDegreeMatchesTeacher(getDegree())) {
	    setDegree(degrees.iterator().next());
	}
    }

    protected boolean checkDegreeMatchesTeacher(Degree degree) {
	return ((degree != null) && (getFilteredDegrees().contains(degree)));
    }

    public Degree getDegree() {
	return (degree);
    }

    public void setDegree(Degree degree) {
	if (!checkDegreeMatchesTeacher(degree)) {
	    return;
	}
	this.degree = degree;
	this.degreeCurricularPeriod = (degree != null ? (int) degree.getDegreeType().getAcademicPeriod().getWeight()
		: getDegreeCurricularPeriod());
	refreshStudentsEntryYear();
    }

    protected void refreshStudentsEntryYear() {
	List<ExecutionYear> entryYears;
	if (getStudent() != null) {
	    if ((!getTutorshipsFromStudent().isEmpty()) && (!checkStudentsEntryYearMatchesStudent(getStudentsEntryYear()))) {
		entryYears = TutorshipEntryExecutionYearProviderForSingleStudent.getExecutionYears(this);
		setStudentsEntryYear(entryYears.get(0));
	    }
	} else {
	    if ((!getTutorships().isEmpty()) && (!checkStudentsEntryYearMatchesDegree(getStudentsEntryYear()))) {
		entryYears = TutorshipEntryExecutionYearProvider.getExecutionYears(this);
		setStudentsEntryYear(entryYears.get(0));
	    }
	}
    }

    protected boolean checkStudentsEntryYearMatchesStudent(ExecutionYear studentsEntryYear) {
	List<ExecutionYear> entryYears = TutorshipEntryExecutionYearProviderForSingleStudent.getExecutionYears(this);
	return ((studentsEntryYear != null) && entryYears.contains(studentsEntryYear));
    }

    protected boolean checkStudentsEntryYearMatchesDegree(ExecutionYear studentsEntryYear) {
	List<ExecutionYear> entryYears = TutorshipEntryExecutionYearProvider.getExecutionYears(this);
	return ((studentsEntryYear != null) && entryYears.contains(studentsEntryYear));
    }

    public ExecutionYear getStudentsEntryYear() {
	return (studentsEntryYear);
    }

    public void setStudentsEntryYear(ExecutionYear studentsEntryYear) {
	if (getStudent() != null) {
	    if (!checkStudentsEntryYearMatchesStudent(studentsEntryYear)) {
		return;
	    }
	} else {
	    if (!checkStudentsEntryYearMatchesDegree(studentsEntryYear)) {
		return;
	    }
	}
	this.studentsEntryYear = studentsEntryYear;
	refreshCurrentMonitoringYear();
    }

    protected void refreshCurrentMonitoringYear() {
	List<ExecutionYear> monitoringYears = TutorshipMonitoringExecutionYearProvider.getExecutionYears(this);
	if (!checkCurrentMonitoringYearMatchesStudentsEntryYear(getCurrentMonitoringYear())) {
	    setCurrentMonitoringYear(monitoringYears.get(0));
	}
    }

    protected boolean checkCurrentMonitoringYearMatchesStudentsEntryYear(ExecutionYear currentMonitoringYear) {
	List<ExecutionYear> monitoringYears = TutorshipMonitoringExecutionYearProvider.getExecutionYears(this);
	return ((currentMonitoringYear != null) && monitoringYears.contains(currentMonitoringYear));
    }

    public ExecutionYear getCurrentMonitoringYear() {
	return (currentMonitoringYear);
    }

    public void setCurrentMonitoringYear(ExecutionYear currentMonitoringYear) {
	if (!checkCurrentMonitoringYearMatchesStudentsEntryYear(currentMonitoringYear)) {
	    return;
	}
	this.currentMonitoringYear = currentMonitoringYear;
    }

    private Set<Degree> getFilteredDegrees() {
	Set<Degree> degrees = new HashSet<Degree>();
	for (Tutorship tutorship : getTutorships()) {
	    StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
	    degrees.add(studentCurricularPlan.getRegistration().getDegree());
	}
	return degrees;
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
	List<Tutorship> result = new ArrayList<Tutorship>();
	result.addAll(getTeacher().getActiveTutorships());
	result.addAll(getTeacher().getPastTutorships());
	return result;
    }

    public List<Tutorship> getTutorshipsFromStudent() {
	List<Tutorship> result = new ArrayList<Tutorship>();
	result.addAll(getStudent().getActiveTutorships());
	return result;
    }

    private void initActiveTutorships() {
	if (getTeacher().getNumberOfActiveTutorships() != 0) {
	    setActiveTutorships(true);
	} else {
	    setActiveTutorships(false);
	}
    }
}
