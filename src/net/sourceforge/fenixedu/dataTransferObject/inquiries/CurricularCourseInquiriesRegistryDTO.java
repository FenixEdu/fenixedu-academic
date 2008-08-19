/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.text.DecimalFormat;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularCourseInquiriesRegistryDTO implements Serializable {

    private DomainReference<InquiriesRegistry> inquiriesRegistry;

    private Integer weeklyHoursSpentPercentage;

    private Double studyDaysSpentInExamsSeason;

    public CurricularCourseInquiriesRegistryDTO(final InquiriesRegistry inquiriesRegistry) {
	super();
	setInquiriesRegistry(inquiriesRegistry);
    }

    public CurricularCourse getCurricularCourse() {
	return getInquiriesRegistry().getCurricularCourse();
    }

    public InquiriesRegistry getInquiriesRegistry() {
	return inquiriesRegistry.getObject();
    }

    public void setInquiriesRegistry(InquiriesRegistry inquiriesRegistry) {
	this.inquiriesRegistry = new DomainReference<InquiriesRegistry>(inquiriesRegistry);
	setWeeklyHoursSpentPercentage(inquiriesRegistry.getWeeklyHoursSpentPercentage());
	setStudyDaysSpentInExamsSeason(inquiriesRegistry.getStudyDaysSpentInExamsSeason());
    }

    public Integer getWeeklyHoursSpentPercentage() {
	return weeklyHoursSpentPercentage;
    }

    public void setWeeklyHoursSpentPercentage(Integer weeklyHoursSpentPercentage) {
	this.weeklyHoursSpentPercentage = weeklyHoursSpentPercentage;
    }

    public Double getStudyDaysSpentInExamsSeason() {
	return studyDaysSpentInExamsSeason;
    }

    public double getWeeklyContactLoad() {
	return getCurricularCourse().getCompetenceCourse().getContactLoad() / 14;
    }

    public void setStudyDaysSpentInExamsSeason(Double studyDaysSpentInExamsSeason) {
	this.studyDaysSpentInExamsSeason = studyDaysSpentInExamsSeason;
    }

    public Double getCalculatedECTSCredits() {
	Integer weeklyHoursSpentInClassesSeason = getInquiriesRegistry().getInquiriesStudentExecutionPeriod()
		.getWeeklyHoursSpentInClassesSeason();

	DecimalFormat format = new DecimalFormat("#0.0");

	// ((%*NHTA + NHC)*14+ NDE*8) / 28
	final double result = (((getWeeklyHoursSpentPercentage() / 100d) * weeklyHoursSpentInClassesSeason * 14)
		+ getCurricularCourse().getCompetenceCourse().getContactLoad() + getStudyDaysSpentInExamsSeason() * 8) / 28;

	return Double.valueOf(format.format(result));
    }

}
