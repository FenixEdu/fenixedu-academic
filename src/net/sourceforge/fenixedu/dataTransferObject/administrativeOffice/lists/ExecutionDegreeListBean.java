package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

public class ExecutionDegreeListBean implements Serializable {

	/**
	 * Serial version id.
	 */
	private static final long serialVersionUID = 1L;

	private Degree degree;

	private DegreeCurricularPlan degreeCurricularPlan;

	private ExecutionDegree executionDegree;

	private ExecutionYear executionYear;

	private CurricularCourse curricularCourse;

	private ExecutionSemester executionSemester;

	private AcademicInterval academicInterval;

	public ExecutionDegreeListBean() {
		super();

		degree = null;
		degreeCurricularPlan = null;
		executionYear = null;
		curricularCourse = null;
		executionDegree = null;

	}

	public ExecutionDegreeListBean(Degree degree) {
		this();

		setDegree(degree);
	}

	public ExecutionDegreeListBean(DegreeCurricularPlan degreeCurricularPlan) {
		this(degreeCurricularPlan == null ? null : degreeCurricularPlan.getDegree());

		setDegreeCurricularPlan(degreeCurricularPlan);
	}

	public ExecutionDegreeListBean(ExecutionDegree executionDegree) {
		this(executionDegree == null ? null : executionDegree.getDegreeCurricularPlan());

		setExecutionDegree(executionDegree);
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
		this.degreeCurricularPlan = degreeCurricularPlan;
	}

	public ExecutionDegree getExecutionDegree() {
		return executionDegree;
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}

	@Deprecated
	public ExecutionYear getExecutionYear() {
		return executionYear;
	}

	@Deprecated
	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	public CurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	public void setCurricularCourse(CurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}

	@Deprecated
	public ExecutionSemester getExecutionPeriod() {
		return executionSemester;
	}

	@Deprecated
	public void setExecutionPeriod(ExecutionSemester executionSemester) {
		this.executionSemester = executionSemester;
	}

	public AcademicInterval getAcademicInterval() {
		return academicInterval;
	}

	public void setAcademicInterval(AcademicInterval academicInterval) {
		this.academicInterval = academicInterval;
	}
}
