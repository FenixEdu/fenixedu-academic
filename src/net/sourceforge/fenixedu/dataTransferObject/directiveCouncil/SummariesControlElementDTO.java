/*
 * Created on Jan 20, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.dataTransferObject.directiveCouncil;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

public class SummariesControlElementDTO extends DataTranferObject {

    String teacherName, executionCourseName, categoryName, executionPeriodName, departmentName, siglas;

    BigDecimal lessonHours, summaryHours, courseSummaryHours, shiftDifference, courseDifference;
    
    Integer teacherNumber;
       
    public SummariesControlElementDTO(String teacherName, String executionCourseName,
            Integer teacherNumber, String categoryName, BigDecimal lessonHours, BigDecimal summaryHours,
            BigDecimal courseSummaryHours, BigDecimal shiftDifference, BigDecimal courseDifference, String siglas) {
       
        this.siglas = siglas;
        this.shiftDifference = shiftDifference;
        this.courseDifference = courseDifference;
        this.executionCourseName = executionCourseName;
        this.lessonHours = lessonHours;
        this.summaryHours = summaryHours;
        this.teacherName = teacherName;
        this.teacherNumber = teacherNumber;
        this.courseSummaryHours = courseSummaryHours;
        this.categoryName = categoryName;
    }

    public BigDecimal getShiftDifference() {
        return shiftDifference;
    }

    public String getExecutionCourseName() {
        return executionCourseName;
    }

    public BigDecimal getLessonHours() {
        return lessonHours;
    }

    public BigDecimal getSummaryHours() {
        return summaryHours;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Integer getTeacherNumber() {
        return teacherNumber;
    }

    public BigDecimal getCourseSummaryHours() {
        return courseSummaryHours;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public BigDecimal getCourseDifference() {
        return courseDifference;
    }

    public String getSiglas() {
        return siglas;
    }
}
