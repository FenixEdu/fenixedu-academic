package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import pt.ist.utl.fenix.utils.Pair;

public class TSDCourseDTOEntry extends DataTranferObject {
	private TSDCourse tsdCourse;
	private List<ExecutionSemester> executionPeriodList = null;
	
	public TSDCourseDTOEntry(TSDCourse _tsdCourse, List<ExecutionSemester> executionPeriodList) {
		this.tsdCourse = _tsdCourse;
		this.executionPeriodList = executionPeriodList;
	}
	
	public List<TSDProfessorshipDTOEntry> getTSDProfessorshipDTOEntries() {
		List<TSDProfessorshipDTOEntry> tsdProfessorshipDTOEntryList = new ArrayList<TSDProfessorshipDTOEntry>();
		
		for (TSDTeacher teacher : tsdCourse.getAssociatedTSDTeachers()) {
			tsdProfessorshipDTOEntryList.add(new TSDProfessorshipDTOEntry(tsdCourse.getTSDProfessorshipByTSDTeacher(teacher), executionPeriodList));
		}
		
		return tsdProfessorshipDTOEntryList;
	}
	
	public TSDCourse getTSDCourse() {
		return tsdCourse;
	}
	
	public List<Pair<String, List<String>>> getCurricularCoursesInformation() {
		List<Pair<String, List<String>>> curricularCourseInformation = new ArrayList<Pair<String, List<String>>>();
		
		List<CurricularCourse> tsdCurricularCourseList = tsdCourse.getAssociatedCurricularCourses();
		
		for (CurricularCourse tsdCurricularCourse : tsdCurricularCourseList) {
			Set<String> curricularYearsSet = buildCurricularYearsSet(tsdCurricularCourse, tsdCourse.getExecutionPeriod());
			
			List<String> curricularYearsList = new ArrayList<String>();
			curricularYearsList.addAll(curricularYearsSet);
			
			curricularCourseInformation.add(new Pair<String, List<String>>(tsdCurricularCourse.getDegreeCurricularPlan().getDegree().getSigla(), curricularYearsList));
			
		}
		
		return curricularCourseInformation;
	}
	
	public String getAcronym() {		
		CurricularCourse tsdCurricularCourse = null;
		if(!tsdCourse.getAssociatedCurricularCourses().isEmpty()) {
			tsdCurricularCourse = tsdCourse.getAssociatedCurricularCourses().get(0);
		}
		
		if(tsdCurricularCourse != null) {
			return tsdCurricularCourse.getAcronym();
		} else {
			return tsdCourse.getCompetenceName();
		}
	}
	
	private Set<String> buildCurricularYearsSet(CurricularCourse tsdCurricularCourseEntry, ExecutionSemester executionPeriodEntry) {
		Set<String> curricularYearsSet = new LinkedHashSet<String>();
		
		for (Integer year : getCurricularIntYears(tsdCurricularCourseEntry)) {
			curricularYearsSet.add(year.toString());
		}
		return curricularYearsSet;
	}	
	
	private List<Integer> getCurricularIntYears(CurricularCourse course) {
		List<Integer> curricularYearList = new ArrayList<Integer>();
		
		for (DegreeModuleScope degreeModuleScope : course.getDegreeModuleScopes()) {
			curricularYearList.add(degreeModuleScope.getCurricularYear());
		}
		
		return curricularYearList;
	}
    
}
