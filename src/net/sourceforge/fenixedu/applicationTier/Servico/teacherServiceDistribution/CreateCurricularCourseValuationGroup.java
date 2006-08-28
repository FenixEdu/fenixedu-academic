package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuationGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;

public class CreateCurricularCourseValuationGroup extends Service {
	public CurricularCourseValuationGroup run(
			Integer[] curricularCourseValuationToGroupArray,
			Map<String, Object> courseValuationParameters) {
		List<CurricularCourseValuation> curricularCourseValuationList = new ArrayList<CurricularCourseValuation>();
		
		for (Integer curricularCourseValuationId : curricularCourseValuationToGroupArray) {
			CurricularCourseValuation curricularCourseValuation = (CurricularCourseValuation) rootDomainObject.readCourseValuationByOID(curricularCourseValuationId);
			
			if(curricularCourseValuation != null) {
				curricularCourseValuationList.add(curricularCourseValuation);
			}
		}
		
		CurricularCourseValuationGroup curricularCourseValuationGroup = new CurricularCourseValuationGroup(curricularCourseValuationList);

		curricularCourseValuationGroup.setFirstTimeEnrolledStudentsManual((Integer) courseValuationParameters.get("firstTimeEnrolledStudentsManual"));
		curricularCourseValuationGroup.setFirstTimeEnrolledStudentsType(ValuationValueType.valueOf((String) courseValuationParameters.get("firstTimeEnrolledStudentsType")));
		curricularCourseValuationGroup.setSecondTimeEnrolledStudentsManual((Integer) courseValuationParameters.get("secondTimeEnrolledStudentsManual"));
		curricularCourseValuationGroup.setSecondTimeEnrolledStudentsType(ValuationValueType.valueOf((String)  courseValuationParameters.get("secondTimeEnrolledStudentsType")));
		curricularCourseValuationGroup.setStudentsPerTheoreticalShiftManual((Integer) courseValuationParameters.get("studentsPerTheoreticalShiftManual"));
		curricularCourseValuationGroup.setStudentsPerTheoreticalShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("studentsPerTheoreticalShiftType")));
		curricularCourseValuationGroup.setStudentsPerPraticalShiftManual((Integer) courseValuationParameters.get("studentsPerPraticalShiftManual"));
		curricularCourseValuationGroup.setStudentsPerPraticalShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("studentsPerPraticalShiftType")));
		curricularCourseValuationGroup.setStudentsPerTheoPratShiftManual((Integer) courseValuationParameters.get("studentsPerTheoPratShiftManual"));
		curricularCourseValuationGroup.setStudentsPerTheoPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("studentsPerTheoPratShiftType")));
		curricularCourseValuationGroup.setStudentsPerLaboratorialShiftManual((Integer) courseValuationParameters.get("studentsPerLaboratorialShiftManual"));
		curricularCourseValuationGroup.setStudentsPerLaboratorialShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("studentsPerLaboratorialShiftType")));
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerTheoShiftManual((Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoShiftManual"));
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerTheoShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoShiftType")));
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerPratShiftManual((Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerPratShiftManual"));
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerPratShiftType")));
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual((Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftManual"));
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftType")));
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerLabShiftManual((Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerLabShiftManual"));
		curricularCourseValuationGroup.setWeightFirstTimeEnrolledStudentsPerLabShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerLabShiftType")));
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerTheoShiftManual((Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoShiftManual"));
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerTheoShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoShiftType")));
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerPratShiftManual((Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerPratShiftManual"));
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerPratShiftType")));
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual((Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftManual"));
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftType")));
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerLabShiftManual((Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerLabShiftManual"));
		curricularCourseValuationGroup.setWeightSecondTimeEnrolledStudentsPerLabShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerLabShiftType")));
		curricularCourseValuationGroup.setIsActive((Boolean) courseValuationParameters.get("isActive"));
		curricularCourseValuationGroup.setTheoreticalHoursManual((Double) courseValuationParameters.get("theoreticalHoursManual"));
		curricularCourseValuationGroup.setTheoreticalHoursType(ValuationValueType.valueOf((String)  courseValuationParameters.get("theoreticalHoursType")));
		curricularCourseValuationGroup.setPraticalHoursManual((Double) courseValuationParameters.get("praticalHoursManual"));
		curricularCourseValuationGroup.setPraticalHoursType(ValuationValueType.valueOf((String)  courseValuationParameters.get("praticalHoursType")));
		curricularCourseValuationGroup.setTheoPratHoursManual((Double) courseValuationParameters.get("theoPratHoursManual"));
		curricularCourseValuationGroup.setTheoPratHoursType(ValuationValueType.valueOf((String)  courseValuationParameters.get("theoPratHoursType")));
		curricularCourseValuationGroup.setLaboratorialHoursManual((Double) courseValuationParameters.get("laboratorialHoursManual"));
		curricularCourseValuationGroup.setLaboratorialHoursType(ValuationValueType.valueOf((String)  courseValuationParameters.get("laboratorialHoursType")));	
		
		curricularCourseValuationGroup.setUsingCurricularCourseValuations((Boolean) courseValuationParameters.get("usingCurricularCourseValuations"));
		
		return curricularCourseValuationGroup;
	}
}
