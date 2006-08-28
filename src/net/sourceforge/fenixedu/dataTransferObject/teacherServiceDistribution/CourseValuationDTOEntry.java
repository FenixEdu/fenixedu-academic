package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCurricularCourse;
import pt.ist.utl.fenix.utils.Pair;

public class CourseValuationDTOEntry extends DataTranferObject {
	private CourseValuation courseValuation;
	private List<ExecutionPeriod> executionPeriodList = null;
	
	public CourseValuationDTOEntry(CourseValuation _courseValuation, List<ExecutionPeriod> executionPeriodList) {
		this.courseValuation = _courseValuation;
		this.executionPeriodList = executionPeriodList;
	}
	
	public List<ProfessorshipValuationDTOEntry> getProfessorshipValuationDTOEntries() {
		List<ProfessorshipValuationDTOEntry> professorshipValuationDTOEntryList = new ArrayList<ProfessorshipValuationDTOEntry>();
		
		for (ProfessorshipValuation professorshipValuation : courseValuation.getProfessorshipValuations()) {
			professorshipValuationDTOEntryList.add(new ProfessorshipValuationDTOEntry(professorshipValuation, executionPeriodList));
		}
		
		return professorshipValuationDTOEntryList;
	}
	
	public CourseValuation getCourseValuation() {
		return courseValuation;
	}
	
	public List<Pair<String, List<String>>> getCurricularCoursesInformation() {
		List<Pair<String, List<String>>> curricularCourseInformation = new ArrayList<Pair<String, List<String>>>();
		
		List<ValuationCurricularCourse> valuationCurricularCourseList = courseValuation.getAssociatedValuationCurricularCourses();
		
		for (ValuationCurricularCourse valuationCurricularCourse : valuationCurricularCourseList) {
			Set<String> curricularYearsSet = buildCurricularYearsSet(valuationCurricularCourse, courseValuation.getExecutionPeriod());
			
			List<String> curricularYearsList = new ArrayList<String>();
			curricularYearsList.addAll(curricularYearsSet);
			
			curricularCourseInformation.add(new Pair<String, List<String>>(valuationCurricularCourse.getDegreeCurricularPlan().getDegree().getSigla(), curricularYearsList));
			
		}
		
		return curricularCourseInformation;
	}
	
	public String getAcronym() {		
		ValuationCurricularCourse valuationCurricularCourse = null;
		if(!courseValuation.getAssociatedValuationCurricularCourses().isEmpty()) {
			valuationCurricularCourse = courseValuation.getAssociatedValuationCurricularCourses().get(0);
		}
		
		if(valuationCurricularCourse != null) {
			return valuationCurricularCourse.getAcronym();
		} else {
			return courseValuation.getName();
		}
	}
	
	private Set<String> buildCurricularYearsSet(ValuationCurricularCourse valuationCurricularCourseEntry, ExecutionPeriod executionPeriodEntry) {
		Set<String> curricularYearsSet = new LinkedHashSet<String>();
		
		for (Integer year : valuationCurricularCourseEntry.getCurricularIntYears()) {
			curricularYearsSet.add(year.toString());
		}
		return curricularYearsSet;
	}	
}
