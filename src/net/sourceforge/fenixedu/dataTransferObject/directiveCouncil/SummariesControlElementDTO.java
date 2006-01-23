/*
 * Created on Jan 20, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.dataTransferObject.directiveCouncil;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

public class SummariesControlElementDTO extends DataTranferObject {

    String teacherName, executionCourseName, categoryName;

    Integer teacherNumber;

    Double lessonHours, summaryHours, totalSummaryHours, difference;

    public SummariesControlElementDTO(String teacherName, String executionCourseName,
            Integer teacherNumber, String categoryName, Double lessonHours, Double summaryHours,
            Double totalSummaryHours, Double difference) {
       
        this.difference = difference;
        this.executionCourseName = executionCourseName;
        this.lessonHours = lessonHours;
        this.summaryHours = summaryHours;
        this.teacherName = teacherName;
        this.teacherNumber = teacherNumber;
        this.totalSummaryHours = totalSummaryHours;
        this.categoryName = categoryName;
    }

    public Double getDifference() {
        return difference;
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

    public Double getTotalSummaryHours() {
        return totalSummaryHours;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
