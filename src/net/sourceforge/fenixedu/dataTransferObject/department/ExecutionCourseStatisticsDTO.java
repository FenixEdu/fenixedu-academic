package net.sourceforge.fenixedu.dataTransferObject.department;

import java.util.List;

import net.sourceforge.fenixedu.domain.curriculum.IGrade;

public class ExecutionCourseStatisticsDTO extends CourseStatisticsDTO {

    private String executionPeriod;

    private String executionYear;

    private String teacher;

    private List<String> degrees;

    public ExecutionCourseStatisticsDTO() {
	super();
    }

    public ExecutionCourseStatisticsDTO(int idInternal, String name, int firstEnrolledCount, int firstApprovedCount,
	    IGrade firstApprovedAverage, IGrade firstApprovedSum, int restEnrolledCount, int restApprovedCount,
	    IGrade restApprovedAverage, IGrade restApprovedSum, int totalEnrolledCount, int totalApprovedCount,
	    IGrade totalApprovedAverage, IGrade totalApprovedSum, List<String> degrees, String executionPeriod,
	    String executionYear, String teacher) {
	super(idInternal, name, firstEnrolledCount, firstApprovedCount, firstApprovedAverage, restEnrolledCount,
		restApprovedCount, restApprovedAverage, totalEnrolledCount, totalApprovedCount, totalApprovedAverage);
	this.executionPeriod = executionPeriod;
	this.teacher = teacher;
	this.executionYear = executionYear;
	this.degrees = degrees;
    }

    public String getExecutionPeriod() {
	return executionPeriod;
    }

    public void setExecutionPeriod(String executionPeriod) {
	this.executionPeriod = executionPeriod;
    }

    public String getTeacher() {
	return teacher;
    }

    public void setTeacher(String teacher) {
	this.teacher = teacher;
    }

    public String getExecutionYear() {
	return executionYear;
    }

    public void setExecutionYear(String executionYear) {
	this.executionYear = executionYear;
    }

    public List<String> getDegrees() {
	return degrees;
    }

    public void setDegrees(List<String> degrees) {
	this.degrees = degrees;
    }
}
