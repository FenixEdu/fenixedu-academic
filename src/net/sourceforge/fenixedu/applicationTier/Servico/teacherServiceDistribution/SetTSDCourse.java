package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularLoad;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType;

public class SetTSDCourse extends Service {
	public void run(
		Integer tsdCourseId, 
		Map<String, Object> tsdCourseParameters) {
		
		TSDCourse tsdCourse = rootDomainObject.readTSDCourseByOID(tsdCourseId);
				
		String firstTimeEnrolledStudentsTypeStr = (String) tsdCourseParameters.get("firstTimeEnrolledStudentsType");
		//Object removeTSDLoad = tsdCourseParameters.get("removeTSDLoad");
		String weightFirstTimeEnrolledStudentsPerShiftTypeStr = (String) tsdCourseParameters.get("weightFirstTimeEnrolledStudentsPerShiftType");
		String hoursTypeStr = (String) tsdCourseParameters.get("hoursType");
		
		// Students Valuation
		if(firstTimeEnrolledStudentsTypeStr != null) {
			String secondTimeEnrolledStudentsTypeStr = (String) tsdCourseParameters.get("secondTimeEnrolledStudentsType");
			
			TSDValueType firstTimeEnrolledStudentsType = (firstTimeEnrolledStudentsTypeStr == null || firstTimeEnrolledStudentsTypeStr.equals("")) ? 
					TSDValueType.MANUAL_VALUE : TSDValueType.valueOf(firstTimeEnrolledStudentsTypeStr);
			TSDValueType secondTimeEnrolledStudentsType = (secondTimeEnrolledStudentsTypeStr == null || secondTimeEnrolledStudentsTypeStr.equals("")) ? 
					TSDValueType.MANUAL_VALUE : TSDValueType.valueOf(secondTimeEnrolledStudentsTypeStr);
					
			Integer firstTimeEnrolledStudentsManual = (firstTimeEnrolledStudentsType == TSDValueType.MANUAL_VALUE) ? 
					(Integer) tsdCourseParameters.get("firstTimeEnrolledStudentsManual") : tsdCourse.getFirstTimeEnrolledStudentsManual();
			Integer secondTimeEnrolledStudentsManual = (secondTimeEnrolledStudentsType == TSDValueType.MANUAL_VALUE) ? 
					(Integer) tsdCourseParameters.get("secondTimeEnrolledStudentsManual") : tsdCourse.getSecondTimeEnrolledStudentsManual();
					
			tsdCourse.setFirstTimeEnrolledStudentsManual(firstTimeEnrolledStudentsManual);
			tsdCourse.setFirstTimeEnrolledStudentsType(firstTimeEnrolledStudentsType);
			tsdCourse.setSecondTimeEnrolledStudentsManual(secondTimeEnrolledStudentsManual);
			tsdCourse.setSecondTimeEnrolledStudentsType(secondTimeEnrolledStudentsType);
		}
		
		// Remove Load
		/*if(removeTSDLoad != null) {
			
			String shiftTypeStr = (String) tsdCourseParameters.get("shiftType");
			ShiftType shiftType = ShiftType.valueOf(shiftTypeStr);
			
			TSDCurricularLoad tsdLoad = tsdCourse.getTSDCurricularLoadByShiftType(shiftType);
			
			if(tsdLoad != null){
				tsdLoad.delete();		
			} 
		}*/
		
		// Weights Valuation
		if(weightFirstTimeEnrolledStudentsPerShiftTypeStr != null) {
			String weightSecondTimeEnrolledStudentsPerShiftTypeStr = (String) tsdCourseParameters.get("weightSecondTimeEnrolledStudentsPerShiftType");
			
			TSDValueType weightFirstTimeEnrolledStudentsPerShiftType = weightFirstTimeEnrolledStudentsPerShiftTypeStr.equals("") ? 
					TSDValueType.MANUAL_VALUE : TSDValueType.valueOf(weightFirstTimeEnrolledStudentsPerShiftTypeStr);
			TSDValueType weightSecondTimeEnrolledStudentsPerShiftType = 
				(weightSecondTimeEnrolledStudentsPerShiftTypeStr == null || weightSecondTimeEnrolledStudentsPerShiftTypeStr.equals("")) ? 
					TSDValueType.MANUAL_VALUE : TSDValueType.valueOf(weightSecondTimeEnrolledStudentsPerShiftTypeStr);
					
			String shiftTypeStr = (String) tsdCourseParameters.get("shiftType");
			ShiftType shiftType = ShiftType.valueOf(shiftTypeStr);
			TSDCurricularLoad tsdLoad = tsdCourse.getTSDCurricularLoadByShiftType(shiftType);
					
			Double weightFirstTimeEnrolledStudentsPerShiftManual = (weightFirstTimeEnrolledStudentsPerShiftType == TSDValueType.MANUAL_VALUE) ? 
					(Double) tsdCourseParameters.get("weightFirstTimeEnrolledStudentsPerShiftManual") : tsdLoad.getWeightFirstTimeEnrolledStudentsPerShiftManual();
			Double weightSecondTimeEnrolledStudentsPerShiftManual = (weightSecondTimeEnrolledStudentsPerShiftType == TSDValueType.MANUAL_VALUE) ? 
					(Double) tsdCourseParameters.get("weightSecondTimeEnrolledStudentsPerShiftManual") : tsdLoad.getWeightSecondTimeEnrolledStudentsPerShiftManual();
					
			tsdLoad.setWeightFirstTimeEnrolledStudentsPerShiftManual(weightFirstTimeEnrolledStudentsPerShiftManual);
			tsdLoad.setWeightFirstTimeEnrolledStudentsPerShiftType(weightFirstTimeEnrolledStudentsPerShiftType);
			tsdLoad.setWeightSecondTimeEnrolledStudentsPerShiftManual(weightSecondTimeEnrolledStudentsPerShiftManual);
			tsdLoad.setWeightSecondTimeEnrolledStudentsPerShiftType(weightSecondTimeEnrolledStudentsPerShiftType);
		}
		
		// Hours Valuation
		if(hoursTypeStr != null) {
						
			TSDValueType hoursType = hoursTypeStr.equals("") ? TSDValueType.MANUAL_VALUE : TSDValueType.valueOf(hoursTypeStr);
			
			String shiftTypeStr = (String) tsdCourseParameters.get("shiftType");
			String studentsPerShiftTypeStr = (String) tsdCourseParameters.get("studentsPerShiftType");
			
			ShiftType shiftType = ShiftType.valueOf(shiftTypeStr);
			TSDValueType studentsPerShiftType = TSDValueType.valueOf(studentsPerShiftTypeStr);
			
			TSDCurricularLoad tsdLoad = tsdCourse.getTSDCurricularLoadByShiftType(shiftType);
			
			Double hoursManual = (hoursType == TSDValueType.MANUAL_VALUE) ? 
					(Double) tsdCourseParameters.get("hoursManual") : tsdLoad.getHoursManual();
			Double hoursPerShift = (Double) tsdCourseParameters.get("hoursPerShift");
			//Integer timeTableSlots = (Integer) tsdCourseParameters.get("timeTableSlots");
						
			Integer studentsPerShiftManual = (studentsPerShiftType == TSDValueType.MANUAL_VALUE) ? 
					(Integer) tsdCourseParameters.get("studentsPerShiftManual") : tsdCourse.getStudentsPerShiftManual(shiftType);
			Double shiftFrequency = (Double) tsdCourseParameters.get("shiftFrequency");
														
			tsdLoad.setHoursManual(hoursManual);
			tsdLoad.setHoursType(hoursType);
			tsdLoad.setHoursPerShiftManual(hoursPerShift);
			//tsdLoad.setTimeTableSlots(timeTableSlots);
			tsdLoad.setStudentsPerShiftManual(studentsPerShiftManual);
			tsdLoad.setStudentsPerShiftType(studentsPerShiftType);
			tsdLoad.setFrequency(shiftFrequency);
		}
	}
}
