package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Registration;

public abstract class StudentCycleInquiryTemplate extends StudentCycleInquiryTemplate_Base {

	public StudentCycleInquiryTemplate() {
		super();
	}

	public static StudentCycleInquiryTemplate getStudentCycleInquiryTemplate(Registration registration) {
		CycleType cycleType = registration.getCycleType(ExecutionYear.readCurrentExecutionYear());
		switch (cycleType) {
		case FIRST_CYCLE:
			return Student1rstCycleInquiryTemplate.getCurrentTemplate();
		case SECOND_CYCLE:
			return Student2ndCycleInquiryTemplate.getCurrentTemplate();
		default:
			return StudentOtherCycleInquiryTemplate.getCurrentTemplate();
		}
	}

	public static StudentCycleInquiryTemplate getStudentCycleInquiryTemplate(PhdIndividualProgramProcess phdProcess) {
		return StudentOtherCycleInquiryTemplate.getCurrentTemplate();
	}
}
