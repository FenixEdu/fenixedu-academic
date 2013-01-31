package net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionDegree;

public class ImportContentBean implements Serializable, HasExecutionDegree {

	private ExecutionSemester executionPeriodReference;

	private ExecutionDegree executionDegreeReference;

	private CurricularYear curricularYearReference;

	private ExecutionCourse executionCourseReference;

	public ImportContentBean() {
		setExecutionPeriod(null);
		setExecutionDegree(null);
		setCurricularYear(null);
		setExecutionCourse(null);
	}

	public ExecutionCourse getExecutionCourse() {
		return this.executionCourseReference;
	}

	public void setExecutionCourse(ExecutionCourse executionCourse) {
		this.executionCourseReference = executionCourse;
	}

	public ExecutionSemester getExecutionPeriod() {
		return this.executionPeriodReference;
	}

	public void setExecutionPeriod(ExecutionSemester executionSemester) {
		this.executionPeriodReference = executionSemester;
	}

	@Override
	public ExecutionDegree getExecutionDegree() {
		return this.executionDegreeReference;
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
		this.executionDegreeReference = executionDegree;
	}

	public CurricularYear getCurricularYear() {
		return this.curricularYearReference;
	}

	public void setCurricularYear(CurricularYear curricularYear) {
		this.curricularYearReference = curricularYear;
	}

}
