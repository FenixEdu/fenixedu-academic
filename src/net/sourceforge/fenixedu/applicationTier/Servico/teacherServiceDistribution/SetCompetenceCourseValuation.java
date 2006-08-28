package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CompetenceCourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;

public class SetCompetenceCourseValuation extends Service {
	public CompetenceCourseValuation run(
		Integer competenceCourseId, 
		Integer valuationPhaseId, 
		Integer executionPeriodId,
		Map<String, Object> courseValuationParameters) {
		
		ValuationCompetenceCourse valuationCompetenceCourse = rootDomainObject.readValuationCompetenceCourseByOID(competenceCourseId);
		ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		
		CompetenceCourseValuation competenceCourseValuation = valuationCompetenceCourse.getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(valuationPhase, executionPeriod);
		
		if(competenceCourseValuation == null)
			competenceCourseValuation = new CompetenceCourseValuation(valuationCompetenceCourse, valuationPhase, executionPeriod);

			
		
		ValuationValueType firstTimeEnrolledStudentsType = ValuationValueType.valueOf((String) courseValuationParameters.get("firstTimeEnrolledStudentsType"));		
		ValuationValueType secondTimeEnrolledStudentsType = ValuationValueType.valueOf((String) courseValuationParameters.get("secondTimeEnrolledStudentsType"));		
		ValuationValueType studentsPerTheoreticalShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("studentsPerTheoreticalShiftType"));		
		ValuationValueType studentsPerPraticalShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("studentsPerPraticalShiftType"));		
		ValuationValueType studentsPerTheoPratShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("studentsPerTheoPratShiftType"));		
		ValuationValueType studentsPerLaboratorialShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("studentsPerLaboratorialShiftType"));		
		ValuationValueType weightFirstTimeEnrolledStudentsPerTheoShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoShiftType"));		
		ValuationValueType weightFirstTimeEnrolledStudentsPerPratShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerPratShiftType"));		
		ValuationValueType weightFirstTimeEnrolledStudentsPerTheoPratShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftType"));		
		ValuationValueType weightFirstTimeEnrolledStudentsPerLabShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerLabShiftType"));		
		ValuationValueType weightSecondTimeEnrolledStudentsPerTheoShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoShiftType"));		
		ValuationValueType weightSecondTimeEnrolledStudentsPerPratShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerPratShiftType"));		
		ValuationValueType weightSecondTimeEnrolledStudentsPerTheoPratShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftType"));		
		ValuationValueType weightSecondTimeEnrolledStudentsPerLabShiftType = ValuationValueType.valueOf((String) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerLabShiftType"));		
		ValuationValueType theoreticalHoursType = ValuationValueType.valueOf((String) courseValuationParameters.get("theoreticalHoursType"));	
		ValuationValueType praticalHoursType = ValuationValueType.valueOf((String) courseValuationParameters.get("praticalHoursType"));		
		ValuationValueType theoPratHoursType = ValuationValueType.valueOf((String) courseValuationParameters.get("theoPratHoursType"));	
		ValuationValueType laboratorialHoursType = ValuationValueType.valueOf((String) courseValuationParameters.get("laboratorialHoursType"));	

		Integer firstTimeEnrolledStudentsManual = (firstTimeEnrolledStudentsType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("firstTimeEnrolledStudentsManual") : competenceCourseValuation.getFirstTimeEnrolledStudentsManual();
		
		Integer secondTimeEnrolledStudentsManual = (secondTimeEnrolledStudentsType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("secondTimeEnrolledStudentsManual") : competenceCourseValuation.getSecondTimeEnrolledStudentsManual();
		
		Integer studentsPerTheoreticalShiftManual = (studentsPerTheoreticalShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerTheoreticalShiftManual") : competenceCourseValuation.getStudentsPerTheoreticalShiftManual();
		
		Integer studentsPerPraticalShiftManual = (studentsPerPraticalShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerPraticalShiftManual") : competenceCourseValuation.getStudentsPerPraticalShiftManual();
		
		Integer studentsPerTheoPratShiftManual = (studentsPerTheoPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerTheoPratShiftManual") : competenceCourseValuation.getStudentsPerTheoPratShiftManual();
		
		Integer studentsPerLaboratorialShiftManual = (studentsPerLaboratorialShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerLaboratorialShiftManual") : competenceCourseValuation.getStudentsPerLaboratorialShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerTheoShiftManual = (weightFirstTimeEnrolledStudentsPerTheoShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoShiftManual") : competenceCourseValuation.getWeightFirstTimeEnrolledStudentsPerTheoShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerPratShiftManual = (weightFirstTimeEnrolledStudentsPerPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerPratShiftManual") : competenceCourseValuation.getWeightFirstTimeEnrolledStudentsPerPratShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerTheoPratShiftManual = (weightFirstTimeEnrolledStudentsPerTheoPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftManual") : competenceCourseValuation.getWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerLabShiftManual = (weightFirstTimeEnrolledStudentsPerLabShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerLabShiftManual") : competenceCourseValuation.getWeightFirstTimeEnrolledStudentsPerLabShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerTheoShiftManual = (weightSecondTimeEnrolledStudentsPerTheoShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoShiftManual") : competenceCourseValuation.getWeightSecondTimeEnrolledStudentsPerTheoShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerPratShiftManual = (weightSecondTimeEnrolledStudentsPerPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerPratShiftManual") : competenceCourseValuation.getWeightSecondTimeEnrolledStudentsPerPratShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerTheoPratShiftManual = (weightSecondTimeEnrolledStudentsPerTheoPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftManual") : competenceCourseValuation.getWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerLabShiftManual = (weightSecondTimeEnrolledStudentsPerLabShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerLabShiftManual") : competenceCourseValuation.getWeightSecondTimeEnrolledStudentsPerLabShiftManual();
		
		Double theoreticalHoursManual = (theoreticalHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("theoreticalHoursManual") : competenceCourseValuation.getTheoreticalHoursManual();
		
		Double praticalHoursManual = (praticalHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("praticalHoursManual") : competenceCourseValuation.getPraticalHoursManual();
		
		Double theoPratHoursManual = (theoPratHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("theoPratHoursManual") : competenceCourseValuation.getTheoPratHoursManual();
		
		Double laboratorialHoursManual = (laboratorialHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("laboratorialHoursManual") : competenceCourseValuation.getLaboratorialHoursManual();
		
		
		

		competenceCourseValuation.setFirstTimeEnrolledStudentsManual(firstTimeEnrolledStudentsManual);
		competenceCourseValuation.setFirstTimeEnrolledStudentsType(firstTimeEnrolledStudentsType);
		competenceCourseValuation.setSecondTimeEnrolledStudentsManual(secondTimeEnrolledStudentsManual);
		competenceCourseValuation.setSecondTimeEnrolledStudentsType(secondTimeEnrolledStudentsType);
		competenceCourseValuation.setStudentsPerTheoreticalShiftManual(studentsPerTheoreticalShiftManual);
		competenceCourseValuation.setStudentsPerTheoreticalShiftType(studentsPerTheoreticalShiftType);
		competenceCourseValuation.setStudentsPerPraticalShiftManual(studentsPerPraticalShiftManual);
		competenceCourseValuation.setStudentsPerPraticalShiftType(studentsPerPraticalShiftType);
		competenceCourseValuation.setStudentsPerTheoPratShiftManual(studentsPerTheoPratShiftManual);
		competenceCourseValuation.setStudentsPerTheoPratShiftType(studentsPerTheoPratShiftType);
		competenceCourseValuation.setStudentsPerLaboratorialShiftManual(studentsPerLaboratorialShiftManual);
		competenceCourseValuation.setStudentsPerLaboratorialShiftType(studentsPerLaboratorialShiftType);
		competenceCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftManual(weightFirstTimeEnrolledStudentsPerTheoShiftManual);
		competenceCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftType(weightFirstTimeEnrolledStudentsPerTheoShiftType);
		competenceCourseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftManual(weightFirstTimeEnrolledStudentsPerPratShiftManual);
		competenceCourseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftType(weightFirstTimeEnrolledStudentsPerPratShiftType);
		competenceCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual(weightFirstTimeEnrolledStudentsPerTheoPratShiftManual);
		competenceCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftType(weightFirstTimeEnrolledStudentsPerTheoPratShiftType);
		competenceCourseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftManual(weightFirstTimeEnrolledStudentsPerLabShiftManual);
		competenceCourseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftType(weightFirstTimeEnrolledStudentsPerLabShiftType);
		competenceCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftManual(weightSecondTimeEnrolledStudentsPerTheoShiftManual);
		competenceCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftType(weightSecondTimeEnrolledStudentsPerTheoShiftType);
		competenceCourseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftManual(weightSecondTimeEnrolledStudentsPerPratShiftManual);
		competenceCourseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftType(weightSecondTimeEnrolledStudentsPerPratShiftType);
		competenceCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual(weightSecondTimeEnrolledStudentsPerTheoPratShiftManual);
		competenceCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftType(weightSecondTimeEnrolledStudentsPerTheoPratShiftType);
		competenceCourseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftManual(weightSecondTimeEnrolledStudentsPerLabShiftManual);
		competenceCourseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftType(weightSecondTimeEnrolledStudentsPerLabShiftType);
		competenceCourseValuation.setIsActive((Boolean) courseValuationParameters.get("isActive"));
		competenceCourseValuation.setTheoreticalHoursManual(theoreticalHoursManual);
		competenceCourseValuation.setTheoreticalHoursType(theoreticalHoursType);
		competenceCourseValuation.setPraticalHoursManual(praticalHoursManual);
		competenceCourseValuation.setPraticalHoursType(praticalHoursType);
		competenceCourseValuation.setTheoPratHoursManual(theoPratHoursManual);
		competenceCourseValuation.setTheoPratHoursType(theoPratHoursType);
		competenceCourseValuation.setLaboratorialHoursManual(laboratorialHoursManual);
		competenceCourseValuation.setLaboratorialHoursType(laboratorialHoursType);	

		return competenceCourseValuation;
	}
}
