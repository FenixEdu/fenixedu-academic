/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

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

    private Integer studyDaysSpentInExamsSeason;

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

    public Integer getStudyDaysSpentInExamsSeason() {
	return studyDaysSpentInExamsSeason;
    }

    public void setStudyDaysSpentInExamsSeason(Integer studyDaysSpentInExamsSeason) {
	this.studyDaysSpentInExamsSeason = studyDaysSpentInExamsSeason;
    }

}
