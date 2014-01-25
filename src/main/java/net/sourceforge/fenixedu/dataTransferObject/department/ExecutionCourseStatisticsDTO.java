package net.sourceforge.fenixedu.dataTransferObject.department;

import java.util.List;

import net.sourceforge.fenixedu.domain.curriculum.IGrade;

public class ExecutionCourseStatisticsDTO extends CourseStatisticsDTO {

    private String executionPeriod;

    private String executionYear;

    private List<String> teachers;

    private List<String> degrees;

    public ExecutionCourseStatisticsDTO() {
        super();
    }

    public ExecutionCourseStatisticsDTO(String externalId, String name, int firstEnrolledCount, int firstApprovedCount,
            IGrade firstApprovedAverage, IGrade firstApprovedSum, int restEnrolledCount, int restApprovedCount,
            IGrade restApprovedAverage, IGrade restApprovedSum, int totalEnrolledCount, int totalApprovedCount,
            IGrade totalApprovedAverage, IGrade totalApprovedSum, List<String> degrees, String executionPeriod,
            String executionYear, List<String> teachers) {
        super(externalId, name, firstEnrolledCount, firstApprovedCount, firstApprovedAverage, restEnrolledCount,
                restApprovedCount, restApprovedAverage, totalEnrolledCount, totalApprovedCount, totalApprovedAverage);
        this.executionPeriod = executionPeriod;
        this.teachers = teachers;
        this.executionYear = executionYear;
        this.degrees = degrees;
    }

    public String getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(String executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
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
