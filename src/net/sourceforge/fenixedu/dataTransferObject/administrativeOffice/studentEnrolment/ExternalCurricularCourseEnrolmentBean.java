package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;

public class ExternalCurricularCourseEnrolmentBean implements Serializable {
    
    private ExternalCurricularCourseResultBean externalCurricularCourseResultBean;
    private DomainReference<ExecutionPeriod> executionPeriod;
    private String grade;
    
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public ExternalCurricularCourse getExternalCurricularCourse() {
	return getExternalCurricularCourseResultBean().getExternalCurricularCourse();
    }
}
