package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuationGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;

public class SetCurricularCourseValuationGroup extends Service {
	public CurricularCourseValuationGroup run(
		Integer curricularCourseValuationGroupId, 
		Map<String, Object> courseValuationParameters) {
		
		CurricularCourseValuationGroup curricularCourseValuationGroup = (CurricularCourseValuationGroup) rootDomainObject.readCourseValuationByOID(curricularCourseValuationGroupId);
		
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

		Integer firstTimeEnrolledStudentsManual = (firstTimeEnrolledStudentsType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("firstTimeEnrolledStudentsManual") : curricularCourseValuationGroup.getFirstTimeEnrolledStudentsManual();
		
		Integer secondTimeEnrolledStudentsManual = (secondTimeEnrolledStudentsType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("secondTimeEnrolledStudentsManual") : curricularCourseValuationGroup.getSecondTimeEnrolledStudentsManual();
		
		Integer studentsPerTheoreticalShiftManual = (studentsPerTheoreticalShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerTheoreticalShiftManual") : curricularCourseValuationGroup.getStudentsPerTheoreticalShiftManual();
		
		Integer studentsPerPraticalShiftManual = (studentsPerPraticalShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerPraticalShiftManual") : curricularCourseValuationGroup.getStudentsPerPraticalShiftManual();
		
		Integer studentsPerTheoPratShiftManual = (studentsPerTheoPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerTheoPratShiftManual") : curricularCourseValuationGroup.getStudentsPerTheoPratShiftManual();
		
		Integer studentsPerLaboratorialShiftManual = (studentsPerLaboratorialShiftType == ValuationValueType.MANUAL_VALUE) ? (Integer) courseValuationParameters.get("studentsPerLaboratorialShiftManual") : curricularCourseValuationGroup.getStudentsPerLaboratorialShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerTheoShiftManual = (weightFirstTimeEnrolledStudentsPerTheoShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoShiftManual") : curricularCourseValuationGroup.getWeightFirstTimeEnrolledStudentsPerTheoShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerPratShiftManual = (weightFirstTimeEnrolledStudentsPerPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerPratShiftManual") : curricularCourseValuationGroup.getWeightFirstTimeEnrolledStudentsPerPratShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerTheoPratShiftManual = (weightFirstTimeEnrolledStudentsPerTheoPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftManual") : curricularCourseValuationGroup.getWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual();
		
		Double weightFirstTimeEnrolledStudentsPerLabShiftManual = (weightFirstTimeEnrolledStudentsPerLabShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerLabShiftManual") : curricularCourseValuationGroup.getWeightFirstTimeEnrolledStudentsPerLabShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerTheoShiftManual = (weightSecondTimeEnrolledStudentsPerTheoShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoShiftManual") : curricularCourseValuationGroup.getWeightSecondTimeEnrolledStudentsPerTheoShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerPratShiftManual = (weightSecondTimeEnrolledStudentsPerPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerPratShiftManual") : curricularCourseValuationGroup.getWeightSecondTimeEnrolledStudentsPerPratShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerTheoPratShiftManual = (weightSecondTimeEnrolledStudentsPerTheoPratShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftManual") : curricularCourseValuationGroup.getWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual();
		
		Double weightSecondTimeEnrolledStudentsPerLabShiftManual = (weightSecondTimeEnrolledStudentsPerLabShiftType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerLabShiftManual") : curricularCourseValuationGroup.getWeightSecondTimeEnrolledStudentsPerLabShiftManual();
		
		Double theoreticalHoursManual = (theoreticalHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("theoreticalHoursManual") : curricularCourseValuationGroup.getTheoreticalHoursManual();
		
		Double praticalHoursManual = (praticalHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("praticalHoursManual") : curricularCourseValuationGroup.getPraticalHoursManual();
		
		Double theoPratHoursManual = (theoPratHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("theoPratHoursManual") : curricularCourseValuationGroup.getTheoPratHoursManual();
		
		Double laboratorialHoursManual = (laboratorialHoursType == ValuationValueType.MANUAL_VALUE) ? (Double) courseValuationParameters.get("laboratorialHoursManual") : curricularCourseValuationGroup.getLaboratorialHoursManual();
		
		
		

		curricularCourseValuationGroup.setFirstTimeEnrolledStudentsManual(firstTimeEnrolledStudentsManual);
		curricularCourseValuationGroup.setFirstTimeEnrolledStudentsType(firstTimeEnrolledStudentsType);
		curricularCourseValuationGroup.setSecondTimeEnrolledStudentsManual(secondTimeEnrolledStudentsManual);
		curricularCourseValuationGroup.setSecondTimeEnrolledStudentsType(secondTimeEnrolledStudentsType);
		curricularCourseValuationGroup.setStudentsPerTheoreticalShiftManual(studentsPerTheoreticalShiftManual);
		curricularCourseValuationGroup.setStudentsPerTheoreticalShiftType(studentsPerTheoreticalShiftType);
		curricularCourseValuationGroup.setStudentsPerPraticalShiftManual(studentsPerPraticalShiftManual);
		curricularCourseValuationGroup.setStudentsPerPraticalShiftType(studentsPerPraticalShiftType);
		curricularCourseValuationGroup.setStudentsPerTheoPratShiftManual(studentsPerTheoPratShiftManual);
		curricularCourseValuationGroup.setStudentsPerTheoPratShiftType(studentsPerTheoPratShiftType);
		curricularCourseValuationGroup.setStudentsPerLaboratorialShiftManual(studentsPerLaboratorialShiftManual);
		curricularCourseValuationGroup.setStudentsPerLaboratorialShiftType(studentsPerLaboratorialShiftType);
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerTheoShiftManual(weightFirstTimeEnrolledStudentsPerTheoShiftManual);
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerTheoShiftType(weightFirstTimeEnrolledStudentsPerTheoShiftType);
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerPratShiftManual(weightFirstTimeEnrolledStudentsPerPratShiftManual);
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerPratShiftType(weightFirstTimeEnrolledStudentsPerPratShiftType);
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual(weightFirstTimeEnrolledStudentsPerTheoPratShiftManual);
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftType(weightFirstTimeEnrolledStudentsPerTheoPratShiftType);
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerLabShiftManual(weightFirstTimeEnrolledStudentsPerLabShiftManual);
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerLabShiftType(weightFirstTimeEnrolledStudentsPerLabShiftType);
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerTheoShiftManual(weightSecondTimeEnrolledStudentsPerTheoShiftManual);
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerTheoShiftType(weightSecondTimeEnrolledStudentsPerTheoShiftType);
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerPratShiftManual(weightSecondTimeEnrolledStudentsPerPratShiftManual);
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerPratShiftType(weightSecondTimeEnrolledStudentsPerPratShiftType);
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual(weightSecondTimeEnrolledStudentsPerTheoPratShiftManual);
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftType(weightSecondTimeEnrolledStudentsPerTheoPratShiftType);
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerLabShiftManual(weightSecondTimeEnrolledStudentsPerLabShiftManual);
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerLabShiftType(weightSecondTimeEnrolledStudentsPerLabShiftType);
		curricularCourseValuationGroup.setIsActive((Boolean) courseValuationParameters.get("isActive"));
		curricularCourseValuationGroup.setTheoreticalHoursManual(theoreticalHoursManual);
		curricularCourseValuationGroup.setTheoreticalHoursType(theoreticalHoursType);
		curricularCourseValuationGroup.setPraticalHoursManual(praticalHoursManual);
		curricularCourseValuationGroup.setPraticalHoursType(praticalHoursType);
		curricularCourseValuationGroup.setTheoPratHoursManual(theoPratHoursManual);
		curricularCourseValuationGroup.setTheoPratHoursType(theoPratHoursType);
		curricularCourseValuationGroup.setLaboratorialHoursManual(laboratorialHoursManual);
		curricularCourseValuationGroup.setLaboratorialHoursType(laboratorialHoursType);	
		
		curricularCourseValuationGroup.setUsingCurricularCourseValuations((Boolean) courseValuationParameters.get("usingCurricularCourseValuations"));
		
		return curricularCourseValuationGroup;
	}
}
