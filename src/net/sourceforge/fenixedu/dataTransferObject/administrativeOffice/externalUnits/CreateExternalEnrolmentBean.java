package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.Grade;

import org.joda.time.YearMonthDay;

public class CreateExternalEnrolmentBean implements Serializable {

    private DomainReference<ExternalCurricularCourse> externalCurricularCourse;
    private DomainReference<ExecutionSemester> executionSemester;

    private Integer studentNumber;
    private Grade grade;
    private YearMonthDay evaluationDate;
    private Double ectsCredits;

    public CreateExternalEnrolmentBean() {
    }

    public CreateExternalEnrolmentBean(final ExternalCurricularCourse externalCurricularCourse) {
	setExternalCurricularCourse(externalCurricularCourse);
    }

    public ExternalCurricularCourse getExternalCurricularCourse() {
	return (this.externalCurricularCourse != null) ? this.externalCurricularCourse.getObject() : null;
    }

    public void setExternalCurricularCourse(ExternalCurricularCourse externalCurricularCourse) {
	this.externalCurricularCourse = (externalCurricularCourse != null) ? new DomainReference<ExternalCurricularCourse>(
		externalCurricularCourse) : null;
    }

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester != null) ? this.executionSemester.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
    }

    public YearMonthDay getEvaluationDate() {
	return evaluationDate;
    }

    public void setEvaluationDate(YearMonthDay evaluationDate) {
	this.evaluationDate = evaluationDate;
    }

    public Grade getGrade() {
	return grade;
    }

    public void setGrade(Grade grade) {
	this.grade = grade;
    }

    public Double getEctsCredits() {
	return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
	this.ectsCredits = ectsCredits;
    }
}
