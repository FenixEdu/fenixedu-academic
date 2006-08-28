package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuationGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;

public class CopyLastYearRealDataToValuationPhase extends Service {
	public void run(Integer valuationPhaseId) {
		ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
				
		for (CourseValuation courseValuation : valuationPhase.getCourseValuations()) {
			courseValuation.delete();
		}
		
		copyLastYearRealDataToValuationPhase(valuationPhase);
	}
	
	private void copyLastYearRealDataToValuationPhase(ValuationPhase valuationPhase) {
		List<ExecutionPeriod> executionPeriods = valuationPhase.getTeacherServiceDistribution().getExecutionPeriods();
		
		
		for(ExecutionPeriod executionPeriod : executionPeriods){
			Set<ExecutionCourse> executionCourses = new HashSet<ExecutionCourse>();
			
			for (CompetenceCourse competenceCourse : valuationPhase.getRootValuationGrouping().getCompetenceCoursesByExecutionPeriods(executionPeriod)) {
				executionCourses.addAll(competenceCourse.getExecutionCoursesByExecutionPeriod(executionPeriod.getPreviousExecutionPeriod().getPreviousExecutionPeriod()));
			}	
		
			for (ExecutionCourse executionCourse : executionCourses) {
				CurricularCourseValuationGroup curricularCourseValuationGroup = createCurricularCourseValuationGroupByExecutionCourse(executionCourse, executionPeriod, valuationPhase);	
				fillProfessorshipValuations(curricularCourseValuationGroup, executionCourse, valuationPhase);
			}		
		}
	}


	@SuppressWarnings("unchecked")
	private CurricularCourseValuationGroup createCurricularCourseValuationGroupByExecutionCourse(ExecutionCourse executionCourse, final ExecutionPeriod executionPeriod, ValuationPhase valuationPhase) {
		
		final Department tsdDepartment = valuationPhase.getTeacherServiceDistribution().getDepartment();
		List<CurricularCourse> curricularCourseList = (List<CurricularCourse>) CollectionUtils.select(executionCourse.getAssociatedCurricularCourses(), new Predicate(){
			public boolean evaluate(Object arg0) {
				CurricularCourse curricularCourse = (CurricularCourse) arg0;
				
				if(curricularCourse.getCompetenceCourse() == null || !curricularCourse.getCompetenceCourse().hasDepartments(tsdDepartment)) {
					return false;
				}
				if(curricularCourse.getActiveScopesInExecutionPeriod(executionPeriod).isEmpty()){
					return false;
				}
				return true;
			}
		});
		
		List<CurricularCourseValuation> curricularCourseValuationList = createCurricularCourseValuationsByCurricularCourses(curricularCourseList, executionPeriod, valuationPhase);
		
		if(!curricularCourseValuationList.isEmpty()){	
			CurricularCourseValuationGroup curricularCourseValuationGroup = new CurricularCourseValuationGroup(curricularCourseValuationList);
			fillCourseValuationData(curricularCourseValuationGroup, Boolean.TRUE);
			
			return curricularCourseValuationGroup;
		}
		
		return null;
	}

	private List<CurricularCourseValuation> createCurricularCourseValuationsByCurricularCourses(List<CurricularCourse> curricularCourseList, ExecutionPeriod executionPeriod, ValuationPhase valuationPhase) {
		List<CurricularCourseValuation> curricularCourseValuationList = new ArrayList<CurricularCourseValuation>();
		
		for(CurricularCourse curricularCourse : curricularCourseList) {
			CurricularCourseValuation curricularCourseValuation = createCurricularCourseValuationByCurricularCourse(valuationPhase, curricularCourse, executionPeriod);
			
			if(curricularCourseValuation != null) 
				curricularCourseValuationList.add(curricularCourseValuation);
		}
		
		return curricularCourseValuationList;
	}

	private CurricularCourseValuation createCurricularCourseValuationByCurricularCourse(ValuationPhase valuationPhase, CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {			
		if(curricularCourse.getCompetenceCourse().getValuationCompetenceCourse() == null){
			new ValuationCompetenceCourse(curricularCourse.getCompetenceCourse());
		}
		
		CurricularCourseValuation curricularCourseValuation = new CurricularCourseValuation(curricularCourse.getValuationCurricularCourse(),
				valuationPhase, executionPeriod);
		
		fillCourseValuationData(curricularCourseValuation, Boolean.FALSE);
		
		return curricularCourseValuation;
	}


	private void fillCourseValuationData(CourseValuation courseValuation, Boolean isActive) {
		Integer zeroInteger = 0;
		Double zeroDouble = 0d;
		
		courseValuation.setFirstTimeEnrolledStudentsManual(zeroInteger);		
		courseValuation.setSecondTimeEnrolledStudentsManual(zeroInteger);
		courseValuation.setStudentsPerTheoreticalShiftManual(zeroInteger);	
		courseValuation.setStudentsPerPraticalShiftManual(zeroInteger);		
		courseValuation.setStudentsPerTheoPratShiftManual(zeroInteger);		
		courseValuation.setStudentsPerLaboratorialShiftManual(zeroInteger);		
		courseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftManual(zeroDouble);		
		courseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftManual(zeroDouble);
		courseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual(zeroDouble);		
		courseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftManual(zeroDouble);		
		courseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftManual(zeroDouble);		
		courseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftManual(zeroDouble);		
		courseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual(zeroDouble);		
		courseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftManual(zeroDouble);	
		courseValuation.setTheoreticalHoursManual(zeroDouble);		
		courseValuation.setPraticalHoursManual(zeroDouble);		
		courseValuation.setTheoPratHoursManual(zeroDouble);		
		courseValuation.setLaboratorialHoursManual(zeroDouble);			
		
		courseValuation.setFirstTimeEnrolledStudentsType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		courseValuation.setSecondTimeEnrolledStudentsType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		courseValuation.setStudentsPerTheoreticalShiftType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		courseValuation.setStudentsPerPraticalShiftType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		courseValuation.setStudentsPerTheoPratShiftType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		courseValuation.setStudentsPerLaboratorialShiftType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		courseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftType(ValuationValueType.OMISSION_VALUE);
		courseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftType(ValuationValueType.OMISSION_VALUE);
		courseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftType(ValuationValueType.OMISSION_VALUE);
		courseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftType(ValuationValueType.OMISSION_VALUE);
		courseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftType(ValuationValueType.OMISSION_VALUE);
		courseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftType(ValuationValueType.OMISSION_VALUE);
		courseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftType(ValuationValueType.OMISSION_VALUE);
		courseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftType(ValuationValueType.OMISSION_VALUE);
		courseValuation.setIsActive(isActive);
		courseValuation.setTheoreticalHoursType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		courseValuation.setPraticalHoursType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		courseValuation.setTheoPratHoursType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		courseValuation.setLaboratorialHoursType(ValuationValueType.LAST_YEAR_REAL_VALUE);
		
		if(courseValuation instanceof CurricularCourseValuationGroup){
			((CurricularCourseValuationGroup)courseValuation).setUsingCurricularCourseValuations(Boolean.FALSE);	
		}	
	}
	
	private void fillProfessorshipValuations(CurricularCourseValuationGroup curricularCourseValuationGroup, ExecutionCourse executionCourse, ValuationPhase valuationPhase) {
		
		if(curricularCourseValuationGroup == null){
			return;
		}
		
		for(Professorship professorship : executionCourse.getProfessorships()) {
			Teacher teacher = professorship.getTeacher();
			ValuationTeacher valuationTeacher = valuationPhase.getRootValuationGrouping().getValuationTeacherByTeacher(teacher);
						
			if(valuationTeacher != null) {
				ProfessorshipValuation professorshipValuation = curricularCourseValuationGroup.getProfessorshipValuationByValuationTeacher(valuationTeacher);
				
				if(professorshipValuation == null) {
					professorshipValuation = new ProfessorshipValuation(curricularCourseValuationGroup, valuationTeacher);					
				}
				professorshipValuation.setTheoreticalHoursManual(0d);
				professorshipValuation.setTheoreticalHoursType(ValuationValueType.LAST_YEAR_REAL_VALUE);
				professorshipValuation.setPraticalHoursManual(0d);
				professorshipValuation.setPraticalHoursType(ValuationValueType.LAST_YEAR_REAL_VALUE);
				professorshipValuation.setTheoPratHoursManual(0d);
				professorshipValuation.setTheoPratHoursType(ValuationValueType.LAST_YEAR_REAL_VALUE);
				professorshipValuation.setLaboratorialHoursManual(0d);
				professorshipValuation.setLaboratorialHoursType(ValuationValueType.LAST_YEAR_REAL_VALUE);					
			}
		}
	}
	
}
