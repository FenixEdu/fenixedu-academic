/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseInquiriesRegistryDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesStudentExecutionPeriod;
import net.sourceforge.fenixedu.domain.student.Student;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SubmitStudentSpentTimeInPeriod extends Service {

    public void run(Student student, List<CurricularCourseInquiriesRegistryDTO> courses, Integer weeklySpentHours, ExecutionSemester executionSemester) {

	if (!checkTotalPercentageDistribution(courses)) {
	    throw new DomainException("error.weeklyHoursSpentPercentage.is.not.100.percent");
	}

	if (!checkTotalStudyDaysSpentInExamsSeason(courses)) {
	    throw new DomainException("error.studyDaysSpentInExamsSeason.exceedsMaxDaysLimit");
	}

	InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod = student.getInquiriesStudentExecutionPeriod(executionSemester);
	if (inquiriesStudentExecutionPeriod == null) {
	    inquiriesStudentExecutionPeriod = new InquiriesStudentExecutionPeriod(student, executionSemester);
	}
	inquiriesStudentExecutionPeriod.setWeeklyHoursSpentInClassesSeason(weeklySpentHours);

	for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
	    InquiriesRegistry inquiriesRegistry = curricularCourseInquiriesRegistryDTO.getInquiriesRegistry();
	    inquiriesRegistry.setStudyDaysSpentInExamsSeason(curricularCourseInquiriesRegistryDTO
		    .getStudyDaysSpentInExamsSeason());
	    inquiriesRegistry.setWeeklyHoursSpentPercentage(curricularCourseInquiriesRegistryDTO.getWeeklyHoursSpentPercentage());
	}

    }

    private boolean checkTotalPercentageDistribution(List<CurricularCourseInquiriesRegistryDTO> courses) {
	Integer totalPercentage = 0;
	for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
	    totalPercentage += curricularCourseInquiriesRegistryDTO.getWeeklyHoursSpentPercentage();
	}

	return totalPercentage == 100;
    }
    
    private boolean checkTotalStudyDaysSpentInExamsSeason(List<CurricularCourseInquiriesRegistryDTO> courses) {
	double totalDays = 0;
	for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
	    totalDays += curricularCourseInquiriesRegistryDTO.getStudyDaysSpentInExamsSeason();
	}

	return totalDays <= 42;
    }
}
