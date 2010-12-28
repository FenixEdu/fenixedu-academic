/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularCourseInquiriesRegistryDTO implements Serializable {

    private StudentInquiryRegistry inquiryRegistry;

    private Integer weeklyHoursSpentPercentage;

    private Double studyDaysSpentInExamsSeason;

    private Integer autonomousWorkHoursForSimulation;

    private Integer attendenceClassesPercentage;

    public CurricularCourseInquiriesRegistryDTO(final StudentInquiryRegistry inquiryRegistry) {
	super();
	setInquiryRegistry(inquiryRegistry);
	setWeeklyHoursSpentPercentage(inquiryRegistry.getWeeklyHoursSpentPercentage());
	setStudyDaysSpentInExamsSeason(inquiryRegistry.getStudyDaysSpentInExamsSeason());
	setAttendenceClassesPercentage(inquiryRegistry.getAttendenceClassesPercentage());
    }

    public CurricularCourse getCurricularCourse() {
	return getInquiryRegistry().getCurricularCourse();
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
	return calculateECTSCredits(getInquiryRegistry().getStudentInquiryExecutionPeriod().getWeeklyHoursSpentInClassesSeason());
    }

    public Double getSimulatedECTSCredits() {
	return calculateECTSCredits(getAutonomousWorkHoursForSimulation());
    }

    public Double calculateECTSCredits(final Integer weeklyHoursSpentInClassesSeason) {

	if (getWeeklyHoursSpentPercentage() == null || getStudyDaysSpentInExamsSeason() == null
		|| weeklyHoursSpentInClassesSeason == null) {
	    return 0d;
	}

	// a - weeklyHoursSpentInClassesSeason; b1 - attendenceClassesPercentage
	// c - weeklyHoursSpentPercentage; d - studyHoursSpentInExamsSeason
	final double result = ((weeklyHoursSpentInClassesSeason /* a */* (getWeeklyHoursSpentPercentage() / 100d) /* c */+ getWeeklyContactLoad() /* b */
		* (getAttendenceClassesPercentage() / 100d) /* b1 */) * 14 + getStudyDaysSpentInExamsSeason() /* d */* 8) / 28;

	// ((%*NHTA + NHC)*14+ NDE*8) / 28
	// ((a*c+b*b1)*14+d*8)/28
	// (((getWeeklyHoursSpentPercentage() / 100d) *
	// weeklyHoursSpentInClassesSeason * 14) +
	// getCurricularCourse().getCompetenceCourse().getContactLoad(getExecutionSemester())
	// + getStudyDaysSpentInExamsSeason() * 8) / 28;

	return new BigDecimal(result).setScale(1, BigDecimal.ROUND_UP).doubleValue();
    }

    public double getCourseEctsCredits() {
	return getCurricularCourse().getCompetenceCourse().getEctsCredits(getExecutionSemester().getSemester(),
		getExecutionSemester());
    }

    private ExecutionSemester getExecutionSemester() {
	return getInquiryRegistry().getExecutionCourse().getExecutionPeriod();
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

    public StudentInquiryRegistry getInquiryRegistry() {
	return inquiryRegistry;
    }

    public void setInquiryRegistry(StudentInquiryRegistry inquiryRegistry) {
	this.inquiryRegistry = inquiryRegistry;
    }

    public Integer getAttendenceClassesPercentage() {
	return attendenceClassesPercentage;
    }

    public void setAttendenceClassesPercentage(Integer attendenceClassesPercentage) {
	this.attendenceClassesPercentage = attendenceClassesPercentage;
    }
}
