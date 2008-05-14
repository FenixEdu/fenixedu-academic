package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ExecutionDegreeListBean implements Serializable {

    
	/**
	 * Serial version id.
	 */
	private static final long serialVersionUID = 1L;

	private DomainReference<Degree> degree;

	private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

	private DomainReference<ExecutionDegree> executionDegree;

	private DomainReference<ExecutionYear> executionYear;

	private DomainReference<CurricularCourse> curricularCourse;
	
	private DomainReference<ExecutionSemester> executionSemester;
	

	public ExecutionDegreeListBean() {
		super();

		this.degree = new DomainReference<Degree>(null);
		this.degreeCurricularPlan = new DomainReference<DegreeCurricularPlan>(
				null);
		this.executionYear = new DomainReference<ExecutionYear>(null);
		this.curricularCourse = new DomainReference<CurricularCourse>(null);
		this.executionDegree = new DomainReference<ExecutionDegree>(null);

	}

	public ExecutionDegreeListBean(Degree degree) {
		this();

		setDegree(degree);
	}

	public ExecutionDegreeListBean(DegreeCurricularPlan degreeCurricularPlan) {
		this(degreeCurricularPlan == null ? null : degreeCurricularPlan
				.getDegree());

		setDegreeCurricularPlan(degreeCurricularPlan);
	}

	public ExecutionDegreeListBean(ExecutionDegree executionDegree) {
		this(executionDegree == null ? null : executionDegree
				.getDegreeCurricularPlan());

		setExecutionDegree(executionDegree);
	}

	public Degree getDegree() {
		return (this.degree == null) ? null : this.degree.getObject();
	}

	public void setDegree(Degree degree) {
		this.degree = (degree != null) ? new DomainReference<Degree>(degree)
				: null;
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
		return (this.degreeCurricularPlan == null) ? null
				: this.degreeCurricularPlan.getObject();
	}

	public void setDegreeCurricularPlan(
			DegreeCurricularPlan degreeCurricularPlan) {
		this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(
				degreeCurricularPlan)
				: null;
	}

	public ExecutionDegree getExecutionDegree() {
		return this.executionDegree.getObject();
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
		this.executionDegree = new DomainReference<ExecutionDegree>(
				executionDegree);
	}

	public ExecutionYear getExecutionYear() {
		return (executionYear == null) ? null : this.executionYear.getObject();
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(
				executionYear)
				: null;
	}

	public CurricularCourse getCurricularCourse() {
		return (this.curricularCourse == null) ? null : this.curricularCourse.getObject();
	}

	public void setCurricularCourse(CurricularCourse curricularCourse) {
		this.curricularCourse = (curricularCourse == null) ? null
				: new DomainReference<CurricularCourse>(curricularCourse);
	}

	public ExecutionSemester getExecutionPeriod() {
		return (this.executionSemester == null) ? null : this.executionSemester.getObject();
	}

	public void setExecutionPeriod( ExecutionSemester executionSemester) {
		this.executionSemester = (executionSemester == null) ? null
				: new DomainReference<ExecutionSemester>(executionSemester);
	}

}
