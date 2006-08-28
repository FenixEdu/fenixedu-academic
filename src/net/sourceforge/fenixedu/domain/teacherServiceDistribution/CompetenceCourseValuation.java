package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class CompetenceCourseValuation extends CompetenceCourseValuation_Base {
    
    private  CompetenceCourseValuation() {
        super();
    }
    
    public CompetenceCourseValuation(ValuationCompetenceCourse valuationCompetenceCourse, ValuationPhase valuationPhase, ExecutionPeriod executionPeriod) {
    	this();
    	
    	this.setValuationCompetenceCourse(valuationCompetenceCourse);
    	this.setValuationPhase(valuationPhase);
    	this.setExecutionPeriod(executionPeriod);
    	setOjbConcreteClass(getClass().getName());
    }

	@Override
	public List<ValuationCurricularCourse> getAssociatedValuationCurricularCourses() {
		return getValuationCompetenceCourse().getValuationCurricularCourses(getExecutionPeriod());
	}

	public static CompetenceCourseValuation createAndCopyFromCompetenceCourseValuation(ValuationPhase valuationPhase, ValuationCompetenceCourse valuationCompetenceCourse, ExecutionPeriod executionPeriod, CompetenceCourseValuation oldCompetenceCourseValuation) {
		CompetenceCourseValuation newCompetenceCourseValuation = new CompetenceCourseValuation(valuationCompetenceCourse, valuationPhase, executionPeriod);
		fillCourseValuationFromAnotherCourseValuation(oldCompetenceCourseValuation, newCompetenceCourseValuation);
		
		return newCompetenceCourseValuation;
	}
	
	@Override
	public void delete(){		
		removeValuationCompetenceCourse();
		super.delete();
	}
	
}
