package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation;

public class ProfessorshipValuationDTOEntry extends DataTranferObject {
	private ProfessorshipValuation professorshipValuation;
	private List<ExecutionPeriod> executionPeriodList = null;
	
	public ProfessorshipValuationDTOEntry(ProfessorshipValuation professorshipValuation, List<ExecutionPeriod> executionPeriodList) {
		this.professorshipValuation = professorshipValuation;
		this.executionPeriodList = executionPeriodList;
	}
	
	public CourseValuationDTOEntry getCourseValuationDTOEntry() {
		return new CourseValuationDTOEntry(professorshipValuation.getCourseValuation(), executionPeriodList);
	}
	
	public ValuationTeacherDTOEntry getValuationTeacherDTOEntry() {
		return new ValuationTeacherDTOEntry(professorshipValuation.getValuationTeacher(), executionPeriodList);
	}
	
	public ProfessorshipValuation getProfessorshipValuation() {
		return professorshipValuation;
	}
}
