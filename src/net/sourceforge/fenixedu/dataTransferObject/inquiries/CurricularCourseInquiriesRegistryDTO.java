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

    private DomainReference<CurricularCourse> curricularCourse;

    private DomainReference<InquiriesRegistry> inquiriesRegistry;

    private Integer weeklyHoursSpentPercentage;

    private Double studyDaysSpentInExamsSeason;

    public CurricularCourseInquiriesRegistryDTO(final CurricularCourse curricularCourse, final InquiriesRegistry inquiriesRegistry) {
	super();
	setCurricularCourse(curricularCourse);
	setInquiriesRegistry(inquiriesRegistry);
    }

    public CurricularCourse getCurricularCourse() {
	return curricularCourse.getObject();
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
	this.curricularCourse = new DomainReference<CurricularCourse>(curricularCourse);
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

	DecimalFormat format = new DecimalFormat("#0.00");

	// ((%*NHTA + NHC)*14+ NDE*8) / 28
	final double result = ((getWeeklyHoursSpentPercentage() * weeklyHoursSpentInClassesSeason + getCurricularCourse()
		.getCompetenceCourse().getContactLoad()) * 14 + getStudyDaysSpentInExamsSeason() * 8)
		/ (28 * 100);

	return Double.valueOf(format.format(result));
    }
    
    

}
