package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;

public class CreateCurricularCourseValuations extends Service {
	public void run(
			Integer valuationCompetenceCourseId,
			Integer valuationPhaseId,
			Integer executionPeriodId,
			Map<String, Object> courseValuationParameters) {
		ValuationCompetenceCourse valuationCompetenceCourse = rootDomainObject.readValuationCompetenceCourseByOID(valuationCompetenceCourseId);
		ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		
		List<ValuationCurricularCourse> valuationCurricularCourseList = valuationCompetenceCourse.getValuationCurricularCourses(executionPeriod);
		
		for (ValuationCurricularCourse valuationCurricularCourse : valuationCurricularCourseList) {
			CurricularCourseValuation curricularCourseValuation = valuationCurricularCourse.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(valuationPhase, executionPeriod);
			
			if(curricularCourseValuation == null) {
				curricularCourseValuation = new CurricularCourseValuation(valuationCurricularCourse, valuationPhase, executionPeriod);

				curricularCourseValuation.setFirstTimeEnrolledStudentsManual((Integer) courseValuationParameters.get("firstTimeEnrolledStudentsManual"));
				curricularCourseValuation.setFirstTimeEnrolledStudentsType(ValuationValueType.valueOf((String) courseValuationParameters.get("firstTimeEnrolledStudentsType")));
				curricularCourseValuation.setSecondTimeEnrolledStudentsManual((Integer) courseValuationParameters.get("secondTimeEnrolledStudentsManual"));
				curricularCourseValuation.setSecondTimeEnrolledStudentsType(ValuationValueType.valueOf((String)  courseValuationParameters.get("secondTimeEnrolledStudentsType")));
				curricularCourseValuation.setStudentsPerTheoreticalShiftManual((Integer) courseValuationParameters.get("studentsPerTheoreticalShiftManual"));
				curricularCourseValuation.setStudentsPerTheoreticalShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("studentsPerTheoreticalShiftType")));
				curricularCourseValuation.setStudentsPerPraticalShiftManual((Integer) courseValuationParameters.get("studentsPerPraticalShiftManual"));
				curricularCourseValuation.setStudentsPerPraticalShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("studentsPerPraticalShiftType")));
				curricularCourseValuation.setStudentsPerTheoPratShiftManual((Integer) courseValuationParameters.get("studentsPerTheoPratShiftManual"));
				curricularCourseValuation.setStudentsPerTheoPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("studentsPerTheoPratShiftType")));
				curricularCourseValuation.setStudentsPerLaboratorialShiftManual((Integer) courseValuationParameters.get("studentsPerLaboratorialShiftManual"));
				curricularCourseValuation.setStudentsPerLaboratorialShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("studentsPerLaboratorialShiftType")));
				curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftManual((Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoShiftManual"));
				curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoShiftType")));
				curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftManual((Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerPratShiftManual"));
				curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerPratShiftType")));
				curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual((Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftManual"));
				curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftType")));
				curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftManual((Double) courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerLabShiftManual"));
				curricularCourseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightFirstTimeEnrolledStudentsPerLabShiftType")));
				curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftManual((Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoShiftManual"));
				curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoShiftType")));
				curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftManual((Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerPratShiftManual"));
				curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerPratShiftType")));
				curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual((Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftManual"));
				curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftType")));
				curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftManual((Double) courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerLabShiftManual"));
				curricularCourseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftType(ValuationValueType.valueOf((String)  courseValuationParameters.get("weightSecondTimeEnrolledStudentsPerLabShiftType")));
				curricularCourseValuation.setIsActive(false);
				curricularCourseValuation.setTheoreticalHoursManual((Double) courseValuationParameters.get("theoreticalHoursManual"));
				curricularCourseValuation.setTheoreticalHoursType(ValuationValueType.valueOf((String)  courseValuationParameters.get("theoreticalHoursType")));
				curricularCourseValuation.setPraticalHoursManual((Double) courseValuationParameters.get("praticalHoursManual"));
				curricularCourseValuation.setPraticalHoursType(ValuationValueType.valueOf((String)  courseValuationParameters.get("praticalHoursType")));
				curricularCourseValuation.setTheoPratHoursManual((Double) courseValuationParameters.get("theoPratHoursManual"));
				curricularCourseValuation.setTheoPratHoursType(ValuationValueType.valueOf((String)  courseValuationParameters.get("theoPratHoursType")));
				curricularCourseValuation.setLaboratorialHoursManual((Double) courseValuationParameters.get("laboratorialHoursManual"));
				curricularCourseValuation.setLaboratorialHoursType(ValuationValueType.valueOf((String)  courseValuationParameters.get("laboratorialHoursType")));	
			}
		}
	}
}
