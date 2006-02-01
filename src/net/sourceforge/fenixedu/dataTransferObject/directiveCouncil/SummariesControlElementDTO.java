/*
 * Created on Jan 20, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.dataTransferObject.directiveCouncil;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

public class SummariesControlElementDTO extends DataTranferObject {

    String teacherName, executionCourseName, categoryName, executionPeriodName, departmentName, siglas;

    Double lessonHours, summaryHours, courseSummaryHours, shiftDifference, courseDifference;
    
    Integer teacherNumber;
       
    public SummariesControlElementDTO(String teacherName, String executionCourseName,
            Integer teacherNumber, String categoryName, Double lessonHours, Double summaryHours,
            Double totalSummaryHours, Double shiftDifference, Double courseDifference, String siglas) {
       
        this.siglas = siglas;
        this.shiftDifference = shiftDifference;
        this.courseDifference = courseDifference;
        this.executionCourseName = executionCourseName;
        this.lessonHours = lessonHours;
        this.summaryHours = summaryHours;
        this.teacherName = teacherName;
        this.teacherNumber = teacherNumber;
        this.courseSummaryHours = totalSummaryHours;
        this.categoryName = categoryName;
    }

    public Double getShiftDifference() {
        return shiftDifference;
    }

    public String getExecutionCourseName() {
        return executionCourseName;
    }

    public Double getLessonHours() {
        return lessonHours;
    }

    public Double getSummaryHours() {
        return summaryHours;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Integer getTeacherNumber() {
        return teacherNumber;
    }

    public Double getCourseSummaryHours() {
        return courseSummaryHours;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Double getCourseDifference() {
        return courseDifference;
    }

    public String getSiglas() {
        return siglas;
    }
}
