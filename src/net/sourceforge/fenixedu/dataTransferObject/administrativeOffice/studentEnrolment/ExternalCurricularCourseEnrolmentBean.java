package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.Grade;

import org.joda.time.YearMonthDay;

public class ExternalCurricularCourseEnrolmentBean implements Serializable {

    private ExternalCurricularCourseResultBean externalCurricularCourseResultBean;
    private DomainReference<ExecutionSemester> executionSemester;
    private YearMonthDay evaluationDate;
    private Grade grade;
    private Double ectsCredits;

    public ExternalCurricularCourseEnrolmentBean(final ExternalCurricularCourse externalCurricularCourse) {
	setExternalCurricularCourseResultBean(new ExternalCurricularCourseResultBean(externalCurricularCourse));
    }

    public ExternalCurricularCourseResultBean getExternalCurricularCourseResultBean() {
	return externalCurricularCourseResultBean;
    }

    public void setExternalCurricularCourseResultBean(ExternalCurricularCourseResultBean externalCurricularCourseResultBean) {
	this.externalCurricularCourseResultBean = externalCurricularCourseResultBean;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester != null) ? this.executionSemester.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
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

    public Double getEctsCredits() {
	return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
	this.ectsCredits = ectsCredits;
    }
}
