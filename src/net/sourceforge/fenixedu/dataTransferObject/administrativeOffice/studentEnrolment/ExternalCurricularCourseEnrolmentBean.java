package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.Grade;

public class ExternalCurricularCourseEnrolmentBean implements Serializable {
    
    private ExternalCurricularCourseResultBean externalCurricularCourseResultBean;
    private DomainReference<ExecutionPeriod> executionPeriod;
    private YearMonthDay evaluationDate;
    private Grade grade;
    
    public ExternalCurricularCourseEnrolmentBean(final ExternalCurricularCourse externalCurricularCourse) {
	setExternalCurricularCourseResultBean(new ExternalCurricularCourseResultBean(externalCurricularCourse));
    }

    public ExternalCurricularCourseResultBean getExternalCurricularCourseResultBean() {
	return externalCurricularCourseResultBean;
    }

    public void setExternalCurricularCourseResultBean(ExternalCurricularCourseResultBean externalCurricularCourseResultBean) {
	this.externalCurricularCourseResultBean = externalCurricularCourseResultBean;
    }
    
    public ExecutionPeriod getExecutionPeriod() {
	return (this.executionPeriod != null) ? this.executionPeriod.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
    }

    public ExternalCurricularCourse getExternalCurricularCourse() {
	return getExternalCurricularCourseResultBean().getExternalCurricularCourse();
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
