package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.Grade;

import org.joda.time.YearMonthDay;

public class CreateExternalEnrolmentBean implements Serializable {

    private DomainReference<ExternalCurricularCourse> externalCurricularCourse;
    private DomainReference<ExecutionPeriod> executionPeriod;

    private Integer studentNumber;
    private Grade grade;
    private YearMonthDay evaluationDate;
    
    public CreateExternalEnrolmentBean() {
    }
    
    public CreateExternalEnrolmentBean(final ExternalCurricularCourse externalCurricularCourse) {
	setExternalCurricularCourse(externalCurricularCourse);
    }
    
    public ExternalCurricularCourse getExternalCurricularCourse() {
	return (this.externalCurricularCourse != null) ? this.externalCurricularCourse.getObject() : null;
    }

    public void setExternalCurricularCourse(ExternalCurricularCourse externalCurricularCourse) {
	this.externalCurricularCourse = (externalCurricularCourse != null) ? new DomainReference<ExternalCurricularCourse>(externalCurricularCourse) : null;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public ExecutionPeriod getExecutionPeriod() {
	return (this.executionPeriod != null) ? this.executionPeriod.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
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
}
