package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class BolonhaStudentOptionalEnrollmentBean implements Serializable {

    private DegreeType degreeType;

    private DomainReference<Degree> degree;

    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

    private DomainReference<ExecutionPeriod> executionPeriod;

    private DomainReference<CurricularCourse> selectedOptionalCurricularCourse;

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;

    private IDegreeModuleToEvaluate selectedDegreeModuleToEnrol;

    public BolonhaStudentOptionalEnrollmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod, final IDegreeModuleToEvaluate degreeModuleToEnrol) {
	super();

	setExecutionPeriod(executionPeriod);
	setSelectedDegreeModuleToEnrol(degreeModuleToEnrol);
	setStudentCurricularPlan(studentCurricularPlan);

    }

    public DegreeType getDegreeType() {
	return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
	this.degreeType = degreeType;
    }

    public boolean hasDegreeType() {
	return getDegreeType() != null;
    }

    public Degree getDegree() {
	return (this.degree != null) ? this.degree.getObject() : null;
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public boolean hasDegree() {
	return getDegree() != null;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return (this.degreeCurricularPlan != null) ? this.degreeCurricularPlan.getObject() : null;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(
		degreeCurricularPlan)
		: null;
    }

    public ExecutionPeriod getExecutionPeriod() {
	return (this.executionPeriod != null) ? this.executionPeriod.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(
		executionPeriod) : null;
    }

    public CurricularCourse getSelectedOptionalCurricularCourse() {
	return (this.selectedOptionalCurricularCourse != null) ? this.selectedOptionalCurricularCourse
		.getObject() : null;
    }

    public void setSelectedOptionalCurricularCourse(CurricularCourse selectedOptionalCurricularCourse) {
	this.selectedOptionalCurricularCourse = (selectedOptionalCurricularCourse != null) ? new DomainReference<CurricularCourse>(
		selectedOptionalCurricularCourse)
		: null;
    }

    public IDegreeModuleToEvaluate getSelectedDegreeModuleToEnrol() {
	return selectedDegreeModuleToEnrol;
    }

    public void setSelectedDegreeModuleToEnrol(IDegreeModuleToEvaluate selectedDegreeModuleToEnrol) {
	this.selectedDegreeModuleToEnrol = selectedDegreeModuleToEnrol;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return (this.studentCurricularPlan != null) ? this.studentCurricularPlan.getObject() : null;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		studentCurricularPlan)
		: null;
    }

}
