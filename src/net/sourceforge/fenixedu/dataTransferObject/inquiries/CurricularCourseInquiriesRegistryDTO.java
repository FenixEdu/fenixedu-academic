/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularCourseInquiriesRegistryDTO implements Serializable {

    private DomainReference<InquiriesRegistry> inquiriesRegistry;

    private Integer weeklyHoursSpentPercentage;

    private Double studyDaysSpentInExamsSeason;

    private Integer autonomousWorkHoursForSimulation;

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
	BigDecimal result = new BigDecimal(getCurricularCourse().getCompetenceCourse().getContactLoad(getExecutionSemester()));
	return result.divide(new BigDecimal(14), 1, RoundingMode.UP).doubleValue();
    }

    public void setStudyDaysSpentInExamsSeason(Double studyDaysSpentInExamsSeason) {
	this.studyDaysSpentInExamsSeason = studyDaysSpentInExamsSeason;
    }

    public Double getCalculatedECTSCredits() {
	return calculateECTSCredits(getInquiriesRegistry().getInquiriesStudentExecutionPeriod()
		.getWeeklyHoursSpentInClassesSeason());
    }

    public Double getSimulatedECTSCredits() {
	return calculateECTSCredits(getAutonomousWorkHoursForSimulation());
    }

    public Double calculateECTSCredits(final Integer weeklyHoursSpentInClassesSeason) {

	if (getWeeklyHoursSpentPercentage() == null || getStudyDaysSpentInExamsSeason() == null
		|| weeklyHoursSpentInClassesSeason == null) {
	    return 0d;
	}

	// ((%*NHTA + NHC)*14+ NDE*8) / 28
	final double result = (((getWeeklyHoursSpentPercentage() / 100d) * weeklyHoursSpentInClassesSeason * 14)
		+ getCurricularCourse().getCompetenceCourse().getContactLoad(getExecutionSemester()) + getStudyDaysSpentInExamsSeason() * 8) / 28;

	return new BigDecimal(result).setScale(1, BigDecimal.ROUND_UP).doubleValue();
    }

    public double getCourseEctsCredits() {
	return getCurricularCourse().getCompetenceCourse().getEctsCredits(getExecutionSemester().getSemester(),
		getExecutionSemester());
    }

    private ExecutionSemester getExecutionSemester() {
	return getInquiriesRegistry().getExecutionPeriod();
    }

    public Double getSimulatedSpentHours() {
	if (getWeeklyHoursSpentPercentage() == null || getAutonomousWorkHoursForSimulation() == null) {
	    return 0d;
	}

	final double result = (getWeeklyHoursSpentPercentage() / 100d) * getAutonomousWorkHoursForSimulation();
	return new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Integer getAutonomousWorkHoursForSimulation() {
	return autonomousWorkHoursForSimulation;
    }

    public void setAutonomousWorkHoursForSimulation(Integer autonomousWorkHoursForSimulation) {
	this.autonomousWorkHoursForSimulation = autonomousWorkHoursForSimulation;
    }

}
