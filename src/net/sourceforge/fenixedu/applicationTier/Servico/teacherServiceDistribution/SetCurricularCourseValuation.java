package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;

public class SetCurricularCourseValuation extends Service {
	public CurricularCourseValuation run(
		Integer valuationCurricularCourseId, 
		Integer valuationPhaseId,
		Integer executionPeriodId,
		Map<String, Object> courseValuationParameters) {
		
		ValuationCurricularCourse valuationCurricularCourse = rootDomainObject.readValuationCurricularCourseByOID(valuationCurricularCourseId);
		ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		
		CurricularCourseValuation curricularCourseValuation = valuationCurricularCourse.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(valuationPhase, executionPeriod);
		
		if(curricularCourseValuation == null)
			curricularCourseValuation = new CurricularCourseValuation(valuationCurricularCourse, valuationPhase, executionPeriod);

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

		Integer firstTimeEnrolledStudentsManual = (firstTimeEnrolledStudentsType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("firstTimeEnrolledStudentsManual") : curricularCourseValuation.getFirstTimeEnrolledStudentsManual();
		
		Integer secondTimeEnrolledStudentsManual = (secondTimeEnrolledStudentsType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("secondTimeEnrolledStudentsManual") : curricularCourseValuation.getSecondTimeEnrolledStudentsManual();
		
		Integer studentsPerTheoreticalShiftManual = (studentsPerTheoreticalShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerTheoreticalShiftManual") : curricularCourseValuation.getStudentsPerTheoreticalShiftManual();
		
		Integer studentsPerPraticalShiftManual = (studentsPerPraticalShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerPraticalShiftManual") : curricularCourseValuation.getStudentsPerPraticalShiftManual();
		
		Integer studentsPerTheoPratShiftManual = (studentsPerTheoPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerTheoPratShiftManual") : curricularCourseValuation.getStudentsPerTheoPratShiftManual();
		
		Integer studentsPerLaboratorialShiftManual = (studentsPerLaboratorialShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerLaboratorialShiftManual") : curricularCourseValuation.getStudentsPerLaboratorialShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerTheoShiftManual = (weightFirstTimeEnrolledStudentsPerTheoShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoShiftManual") : curricularCourseValuation.getWeightFirstTimeEnrolledStudentsPerTheoShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerPratShiftManual = (weightFirstTimeEnrolledStudentsPerPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerPratShiftManual") : curricularCourseValuation.getWeightFirstTimeEnrolledStudentsPerPratShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerTheoPratShiftManual = (weightFirstTimeEnrolledStudentsPerTheoPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftManual") : curricularCourseValuation.getWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerLabShiftManual = (weightFirstTimeEnrolledStudentsPerLabShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerLabShiftManual") : curricularCourseValuation.getWeightFirstTimeEnrolledStudentsPerLabShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerTheoShiftManual = (weightSecondTimeEnrolledStudentsPerTheoShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoShiftManual") : curricularCourseValuation.getWeightSecondTimeEnrolledStudentsPerTheoShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerPratShiftManual = (weightSecondTimeEnrolledStudentsPerPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerPratShiftManual") : curricularCourseValuation.getWeightSecondTimeEnrolledStudentsPerPratShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerTheoPratShiftManual = (weightSecondTimeEnrolledStudentsPerTheoPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftManual") : curricularCourseValuation.getWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerLabShiftManual = (weightSecondTimeEnrolledStudentsPerLabShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerLabShiftManual") : curricularCourseValuation.getWeightSecondTimeEnrolledStudentsPerLabShiftManual();
		
		Double theoreticalHoursManual = (theoreticalHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("theoreticalHoursManual") : curricularCourseValuation.getTheoreticalHoursManual();
		
		Double praticalHoursManual = (praticalHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("praticalHoursManual") : curricularCourseValuation.getPraticalHoursManual();
		
		Double theoPratHoursManual = (theoPratHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("theoPratHoursManual") : curricularCourseValuation.getTheoPratHoursManual();
		
		Double laboratorialHoursManual = (laboratorialHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("laboratorialHoursManual") : curricularCourseValuation.getLaboratorialHoursManual();
		
		
		

		curricularCourseValuation.setFirstTimeEnrolledStudentsManual(firstTimeEnrolledStudentsManual);
		curricularCourseValuation.setFirstTimeEnrolledStudentsType(firstTimeEnrolledStudentsType);
		curricularCourseValuation.setSecondTimeEnrolledStudentsManual(secondTimeEnrolledStudentsManual);
		curricularCourseValuation.setSecondTimeEnrolledStudentsType(secondTimeEnrolledStudentsType);
		curricularCourseValuation.setStudentsPerTheoreticalShiftManual(studentsPerTheoreticalShiftManual);
		curricularCourseValuation.setStudentsPerTheoreticalShiftType(studentsPerTheoreticalShiftType);
		curricularCourseValuation.setStudentsPerPraticalShiftManual(studentsPerPraticalShiftManual);
		curricularCourseValuation.setStudentsPerPraticalShiftType(studentsPerPraticalShiftType);
		curricularCourseValuation.setStudentsPerTheoPratShiftManual(studentsPerTheoPratShiftManual);
		curricularCourseValuation.setStudentsPerTheoPratShiftType(studentsPerTheoPratShiftType);
		curricularCourseValuation.setStudentsPerLaboratorialShiftManual(studentsPerLaboratorialShiftManual);
		curricularCourseValuation.setStudentsPerLaboratorialShiftType(studentsPerLaboratorialShiftType);
		curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftManual(weightFirstTimeEnrolledStudentsPerTheoShiftManual);
		curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftType(weightFirstTimeEnrolledStudentsPerTheoShiftType);
		curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftManual(weightFirstTimeEnrolledStudentsPerPratShiftManual);
		curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftType(weightFirstTimeEnrolledStudentsPerPratShiftType);
		curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual(weightFirstTimeEnrolledStudentsPerTheoPratShiftManual);
		curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftType(weightFirstTimeEnrolledStudentsPerTheoPratShiftType);
		curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftManual(weightFirstTimeEnrolledStudentsPerLabShiftManual);
		curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftType(weightFirstTimeEnrolledStudentsPerLabShiftType);
		curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftManual(weightSecondTimeEnrolledStudentsPerTheoShiftManual);
		curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftType(weightSecondTimeEnrolledStudentsPerTheoShiftType);
		curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftManual(weightSecondTimeEnrolledStudentsPerPratShiftManual);
		curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftType(weightSecondTimeEnrolledStudentsPerPratShiftType);
		curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual(weightSecondTimeEnrolledStudentsPerTheoPratShiftManual);
		curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftType(weightSecondTimeEnrolledStudentsPerTheoPratShiftType);
		curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftManual(weightSecondTimeEnrolledStudentsPerLabShiftManual);
		curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftType(weightSecondTimeEnrolledStudentsPerLabShiftType);
		curricularCourseValuation.setIsActive((Boolean) courseValuationParameters.get("isActive"));
		curricularCourseValuation.setTheoreticalHoursManual(theoreticalHoursManual);
		curricularCourseValuation.setTheoreticalHoursType(theoreticalHoursType);
		curricularCourseValuation.setPraticalHoursManual(praticalHoursManual);
		curricularCourseValuation.setPraticalHoursType(praticalHoursType);
		curricularCourseValuation.setTheoPratHoursManual(theoPratHoursManual);
		curricularCourseValuation.setTheoPratHoursType(theoPratHoursType);
		curricularCourseValuation.setLaboratorialHoursManual(laboratorialHoursManual);
		curricularCourseValuation.setLaboratorialHoursType(laboratorialHoursType);	
				
		return curricularCourseValuation;
	}
}
