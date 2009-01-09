package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class ContextSelectionBean implements Serializable {
    private AcademicInterval academicInterval;
    private DomainReference<CurricularYear> curricularYear;
    private DomainReference<ExecutionDegree> executionDegree;
    private String courseName;

    public ContextSelectionBean(AcademicInterval academicInterval) {
	this.academicInterval = academicInterval;
    }

    public ContextSelectionBean() {
	this.academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
    }

    public ContextSelectionBean(AcademicInterval academicInterval, ExecutionDegree executionDegree,
	    CurricularYear curricularYear) {
	this.academicInterval = academicInterval;
	setExecutionDegree(executionDegree);
	setCurricularYear(curricularYear);
    }

    public AcademicInterval getAcademicInterval() {
	return academicInterval;
    }

    public void setAcademicInterval(AcademicInterval academicInterval) {
	this.academicInterval = academicInterval;
    }

    public CurricularYear getCurricularYear() {
	return (this.curricularYear != null) ? this.curricularYear.getObject() : null;
    }

    public void setCurricularYear(CurricularYear curricularYear) {
	this.curricularYear = (curricularYear != null) ? new DomainReference<CurricularYear>(curricularYear) : null;
    }

    public ExecutionDegree getExecutionDegree() {
	return (this.executionDegree != null) ? this.executionDegree.getObject() : null;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = (executionDegree != null) ? new DomainReference<ExecutionDegree>(executionDegree) : null;
    }

    public String getCourseName() {
	return courseName;
    }

    public void setCourseName(String courseName) {
	this.courseName = courseName;
    }

}
