package net.sourceforge.fenixedu.domain.enrolmentPeriods;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.joda.time.DateTime;

public class EnrolmentPeriodManagementBean implements java.io.Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private ExecutionSemester executionSemester;
	private DateTime begin;
	private DateTime end;

	private DegreeType degreeType;
	private EnrolmentPeriodType type;

	private List<DegreeCurricularPlan> degreeCurricularPlanList;

	public EnrolmentPeriodManagementBean(final ExecutionSemester semester) {
		this.executionSemester = semester;

		this.degreeCurricularPlanList = new ArrayList<DegreeCurricularPlan>();
	}

	public EnrolmentPeriodManagementBean(EnrolmentPeriod enrolmentPeriod, ExecutionSemester semester) {
		this.degreeType = enrolmentPeriod.getDegree().getDegreeType();
		this.type = EnrolmentPeriodType.readTypeByClass(enrolmentPeriod.getClass());
		this.begin = enrolmentPeriod.getStartDateDateTime();
		this.end = enrolmentPeriod.getEndDateDateTime();
		this.degreeCurricularPlanList = new ArrayList<DegreeCurricularPlan>();
		this.degreeCurricularPlanList.add(enrolmentPeriod.getDegreeCurricularPlan());
		this.executionSemester = semester;
	}

	public ExecutionSemester getExecutionSemester() {
		return executionSemester;
	}

	public void setExecutionSemester(ExecutionSemester executionSemester) {
		this.executionSemester = executionSemester;
	}

	public DateTime getBegin() {
		return begin;
	}

	public void setBegin(DateTime begin) {
		this.begin = begin;
	}

	public DateTime getEnd() {
		return end;
	}

	public void setEnd(DateTime end) {
		this.end = end;
	}

	public DegreeType getDegreeType() {
		return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
		this.degreeType = degreeType;
	}

	public EnrolmentPeriodType getType() {
		return type;
	}

	public void setType(EnrolmentPeriodType type) {
		this.type = type;
	}

	public List<DegreeCurricularPlan> getDegreeCurricularPlanList() {
		return degreeCurricularPlanList;
	}

	public void setDegreeCurricularPlanList(List<DegreeCurricularPlan> degreeCurricularPlanList) {
		this.degreeCurricularPlanList = degreeCurricularPlanList;
	}

}
