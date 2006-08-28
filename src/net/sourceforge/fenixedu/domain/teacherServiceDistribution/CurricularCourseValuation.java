package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class CurricularCourseValuation extends CurricularCourseValuation_Base {
    
    private  CurricularCourseValuation() {
        super();
    }

	public CurricularCourseValuation(ValuationCurricularCourse valuationCurricularCourse, ValuationPhase valuationPhase, ExecutionPeriod executionPeriod) {
		this();
		
		this.setValuationCurricularCourse(valuationCurricularCourse);
		this.setValuationPhase(valuationPhase);
		this.setExecutionPeriod(executionPeriod);
		this.setOjbConcreteClass(getClass().getName());
	}

	@Override
	public List<ValuationCurricularCourse> getAssociatedValuationCurricularCourses() {
		List<ValuationCurricularCourse> valuationCurricularCourse = new ArrayList<ValuationCurricularCourse>();
		
		valuationCurricularCourse.add(getValuationCurricularCourse());
		
		return valuationCurricularCourse;
	}

	public static CurricularCourseValuation createAndCopyFromCurricularCourseValuation(ValuationPhase valuationPhase, ValuationCurricularCourse newValuationCurricularCourse, ExecutionPeriod newExecutionPeriod, CurricularCourseValuation oldCurricularCourseValuation) {
		CurricularCourseValuation newCourseValuation = new CurricularCourseValuation(newValuationCurricularCourse, valuationPhase, newExecutionPeriod);
		fillCourseValuationFromAnotherCourseValuation(oldCurricularCourseValuation, newCourseValuation);
		
		return newCourseValuation;
	}

	@Override
	public void delete(){
		CurricularCourseValuationGroup valuationGroup  = getCurricularCourseValuationGroup();
		removeCurricularCourseValuationGroup();
		
		if(valuationGroup != null &&  valuationGroup.getCurricularCourseValuationsCount() == 0) {
			valuationGroup.delete();			
		}
			
		removeValuationCurricularCourse();
		super.delete();
	}
	
	public void deleteValuationOnly() {
		removeCurricularCourseValuationGroup();
		removeValuationCurricularCourse();
		super.delete();
		
	}
}
