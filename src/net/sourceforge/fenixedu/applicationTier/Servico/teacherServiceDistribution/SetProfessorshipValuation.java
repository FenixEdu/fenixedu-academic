package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;

public class SetProfessorshipValuation extends Service {
	public ProfessorshipValuation run(
		Integer courseValuationId, 
		Integer valuationTeacherId, 
		Map<String, Object> courseValuationParameters) {
		
		CourseValuation courseValuation = rootDomainObject.readCourseValuationByOID(courseValuationId);
		ValuationTeacher valuationTeacher = rootDomainObject.readValuationTeacherByOID(valuationTeacherId);
		
		ProfessorshipValuation professorshipValuation = courseValuation.getProfessorshipValuationByValuationTeacher(valuationTeacher);
		
		if(professorshipValuation == null) {
			professorshipValuation = new ProfessorshipValuation(courseValuation, valuationTeacher);
		}
		
		professorshipValuation.setTheoreticalHoursManual((Double) courseValuationParameters.get("theoreticalHoursManual"));
		professorshipValuation.setTheoreticalHoursType(ValuationValueType.valueOf((String) courseValuationParameters.get("theoreticalHoursType")));
		professorshipValuation.setPraticalHoursManual((Double) courseValuationParameters.get("praticalHoursManual"));
		professorshipValuation.setPraticalHoursType(ValuationValueType.valueOf((String) courseValuationParameters.get("praticalHoursType")));
		professorshipValuation.setTheoPratHoursManual((Double) courseValuationParameters.get("theoPratHoursManual"));
		professorshipValuation.setTheoPratHoursType(ValuationValueType.valueOf((String) courseValuationParameters.get("theoPratHoursType")));
		professorshipValuation.setLaboratorialHoursManual((Double) courseValuationParameters.get("laboratorialHoursManual"));
		professorshipValuation.setLaboratorialHoursType(ValuationValueType.valueOf((String) courseValuationParameters.get("laboratorialHoursType")));	
		
		return professorshipValuation;
	}
}
