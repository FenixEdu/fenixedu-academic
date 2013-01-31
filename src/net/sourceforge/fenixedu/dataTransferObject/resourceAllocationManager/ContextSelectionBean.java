package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class ContextSelectionBean implements Serializable {
	private AcademicInterval academicInterval;
	private CurricularYear curricularYear;
	private ExecutionDegree executionDegree;
	private String courseName;

	public ContextSelectionBean(AcademicInterval academicInterval) {
		this.academicInterval = academicInterval;
	}

	public ContextSelectionBean() {
		this.academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
	}

	public ContextSelectionBean(AcademicInterval academicInterval, ExecutionDegree executionDegree, CurricularYear curricularYear) {
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
		return this.curricularYear;
	}

	public void setCurricularYear(CurricularYear curricularYear) {
		this.curricularYear = curricularYear;
	}

	public ExecutionDegree getExecutionDegree() {
		return this.executionDegree;
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

}
