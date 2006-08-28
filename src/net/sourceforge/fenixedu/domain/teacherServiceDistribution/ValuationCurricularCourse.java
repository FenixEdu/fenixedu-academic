package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ValuationCurricularCourse extends ValuationCurricularCourse_Base {

	private ValuationCurricularCourse() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public ValuationCurricularCourse(
			CurricularCourse curricularCourse,
			ValuationCompetenceCourse valuationCompetenceCourse) {
		this();

		if (curricularCourse == null || valuationCompetenceCourse == null) {
			throw new NullPointerException();
		}

		this.setCurricularCourse(curricularCourse);
	}

	public ValuationCurricularCourse(
			ValuationCompetenceCourse valuationCompetenceCourse,
			List<CurricularYear> curricularYearList,
			DegreeCurricularPlan degreeCurricularPlan,
			ExecutionPeriod executionPeriod,
			String acronym,
			Double theoreticalHours,
			Double praticalHours,
			Double theoPratHours,
			Double laboratorialHours) {
		this();
		
		if(valuationCompetenceCourse == null || curricularYearList == null || curricularYearList.isEmpty() 
				|| degreeCurricularPlan == null || executionPeriod == null){
			throw new NullPointerException();
		}
		if(acronym == null || acronym.equals("")){
			acronym = valuationCompetenceCourse.getName();
		}
		
		this.setValuationCompetenceCourse(valuationCompetenceCourse);
		this.getCurricularYears().addAll(curricularYearList);
		this.setDegreeCurricularPlan(degreeCurricularPlan);
		this.setExecutionPeriod(executionPeriod);
		this.setAcronym(acronym);
		this.setTheoreticalHours(theoreticalHours);
		this.setPraticalHours(praticalHours);
		this.setTheoPratHours(theoPratHours);
		this.setLaboratorialHours(laboratorialHours);
	}
	
	public ValuationCompetenceCourse getValuationCompetenceCourse() {
		if(getIsRealCurricularCourse()) {
			return getCurricularCourse().getCompetenceCourse().getValuationCompetenceCourse();
		}
		
		return super.getValuationCompetenceCourse();
	}

	@SuppressWarnings("unchecked")
	public CurricularCourseValuation getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(
			final ValuationPhase valuationPhase,
			final ExecutionPeriod executionPeriod) {
		return (CurricularCourseValuation) CollectionUtils.find(getCurricularCourseValuations(), new Predicate() {

			public boolean evaluate(Object arg0) {
				CurricularCourseValuation curricularCourseValuation = (CurricularCourseValuation) arg0;
				return (curricularCourseValuation.getValuationPhase() == valuationPhase && curricularCourseValuation.getExecutionPeriod() == executionPeriod);
			}
		});
	}

	public CurricularCourseValuationGroup getCurricularCourseValuationGroupByValuationPhaseAndExecutionPeriod(
			final ValuationPhase valuationPhase,
			final ExecutionPeriod executionPeriod) {
		CurricularCourseValuation curricularCourseValuation = getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(
				valuationPhase,
				executionPeriod);

		if (curricularCourseValuation != null) {
			return curricularCourseValuation.getCurricularCourseValuationGroup();
		}

		return null;
	}

	public Boolean getIsRealCurricularCourse() {
		return getCurricularCourse() != null;
	}

	public Integer getFirstTimeEnrolmentStudentNumber(final ExecutionPeriod executionPeriod) {
		if (getIsRealCurricularCourse()) {
			return getCurricularCourse().getFirstTimeEnrolmentStudentNumber(executionPeriod);
		} else {
			return 0;
		}
	}

	public Integer getSecondTimeEnrolmentStudentNumber(final ExecutionPeriod executionPeriod) {
		if (getIsRealCurricularCourse()) {
			return getCurricularCourse().getSecondTimeEnrolmentStudentNumber(executionPeriod);
		} else {
			return 0;
		}
	}

	public Integer getTotalEnrolmentStudentNumber(final ExecutionPeriod executionPeriod) {
		if (getIsRealCurricularCourse()) {
			return getCurricularCourse().getTotalEnrolmentStudentNumber(executionPeriod);
		} else {
			return 0;
		}
	}

	public List<ExecutionCourse> getExecutionCoursesByExecutionPeriod(final ExecutionPeriod executionPeriod) {
		if (getIsRealCurricularCourse()) {
			return getCurricularCourse().getExecutionCoursesByExecutionPeriod(executionPeriod);
		} else {
			return new ArrayList<ExecutionCourse>();
		}
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
		if (getIsRealCurricularCourse()) {
			return getCurricularCourse().getDegreeCurricularPlan();
		} else {
			return super.getDegreeCurricularPlan();
		}
	}

	public String getAcronym() {
		if (getIsRealCurricularCourse()) {
			return getCurricularCourse().getAcronym();
		} else {
			return super.getAcronym();
		}
	}

	public Double getTheoreticalHours() {
		if (getIsRealCurricularCourse()) {
			return getCurricularCourse().getTheoreticalHours();
		} else {
			return super.getTheoreticalHours();
		}
	}

	public Double getPraticalHours() {
		if (getIsRealCurricularCourse()) {
			if(getCurricularCourse().getPraticalHours() != null && getCurricularCourse().getPraticalHours() > 0d) {
				return getCurricularCourse().getPraticalHours();
			} else {
				return getCurricularCourse().getProblemsHours();
			}
			
		} else {
			return super.getPraticalHours();
		}
	}

	public Double getTheoPratHours() {
		if (getIsRealCurricularCourse()) {
			if(getCurricularCourse().getTheoPratHours() != null && getCurricularCourse().getTheoPratHours() > 0d) {
				return getCurricularCourse().getTheoPratHours();
			} else {
				return getCurricularCourse().getSeminaryHours();
			}
		} else {
			return super.getTheoPratHours();
		}
	}

	public Double getLaboratorialHours() {
		if (getIsRealCurricularCourse()) {
			if(getCurricularCourse().getLabHours() != null && getCurricularCourse().getLabHours() > 0d) {
				return getCurricularCourse().getLabHours();
			} else {
				return getCurricularCourse().getLaboratorialHours();
			}
			
		} else {
			return super.getLaboratorialHours();
		}
	}

	public List<Integer> getCurricularIntYears() {
		List<Integer> curricularYearList = new ArrayList<Integer>();
		if (getIsRealCurricularCourse()) {
			List<DegreeModuleScope> degreeModuleList = getCurricularCourse().getDegreeModuleScopes();
			for (DegreeModuleScope degreeModuleScope : degreeModuleList) {
				curricularYearList.add(degreeModuleScope.getCurricularYear());
			}
		} else {
			for (CurricularYear curricularYear : super.getCurricularYears()) {
				curricularYearList.add(curricularYear.getYear());
			}
		}

		return curricularYearList;
	}

	public void delete() {
		for (CurricularCourseValuation courseValuation : getCurricularCourseValuations()) {
			removeCurricularCourseValuations(courseValuation);
			
			courseValuation.delete();
		}
		for (CurricularYear curricularYear : getCurricularYears()) {
			removeCurricularYears(curricularYear);
		}
		removeCurricularCourse();
		removeDegreeCurricularPlan();
		removeExecutionPeriod();
		removeValuationCompetenceCourse();
		removeRootDomainObject();
		
		super.deleteDomainObject();
	}

	
	
}
