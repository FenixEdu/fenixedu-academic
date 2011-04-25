/*
 * Created on Apr 14, 2010
 *	by rcro
 */
package net.sourceforge.fenixedu.dataTransferObject.directiveCouncil;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetailSummaryElement implements Serializable {

    String teacherName;
    String executionCourseName;
    String categoryName;
    String executionPeriodName;
    String departmentName;
    String siglas;
    BigDecimal declaredLessons;
    BigDecimal givenSummaries;
    BigDecimal givenSummariesPercentage;
    BigDecimal givenNotTaughtSummaries;
    BigDecimal givenNotTaughtSummariesPercentage;
    Integer teacherNumber;
    String teacherEmail;

    public DetailSummaryElement(String teacherName, String executionCourseName, Integer teacherNumber, String teacherEmail,
	    String categoryName, BigDecimal declaredLessons, BigDecimal givenSummaries, BigDecimal givenSummariesPercentage,
	    BigDecimal givenNotTaughtSummaries, BigDecimal givenNotTaughtSummariesPercentage,
	    String siglas) {

	setExecutionCourseName(executionCourseName);
	setTeacherName(teacherName);
	setTeacherNumber(teacherNumber);
	setTeacherEmail(teacherEmail);
	setCategoryName(categoryName);
	setSiglas(siglas);
	setDeclaredLessons(declaredLessons);
	setGivenSummaries(givenSummaries);
	setGivenSummariesPercentage(givenSummariesPercentage);
	setGivenNotTaughtSummaries(givenNotTaughtSummaries);
	setGivenNotTaughtSummariesPercentage(givenNotTaughtSummariesPercentage);
    }

    public String getTeacherEmail() {
	return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
	this.teacherEmail = teacherEmail;
    }

    public String getTeacherName() {
	return teacherName;
    }

    public void setTeacherName(String teacherName) {
	this.teacherName = teacherName;
    }

    public String getExecutionCourseName() {
	return executionCourseName;
    }

    public void setExecutionCourseName(String executionCourseName) {
	this.executionCourseName = executionCourseName;
    }

    public String getCategoryName() {
	return categoryName;
    }

    public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
    }

    public String getExecutionPeriodName() {
	return executionPeriodName;
    }

    public void setExecutionPeriodName(String executionPeriodName) {
	this.executionPeriodName = executionPeriodName;
    }

    public String getDepartmentName() {
	return departmentName;
    }

    public void setDepartmentName(String departmentName) {
	this.departmentName = departmentName;
    }

    public String getSiglas() {
	return siglas;
    }

    public void setSiglas(String siglas) {
	this.siglas = siglas;
    }

    public BigDecimal getDeclaredLessons() {
	return declaredLessons;
    }

    public void setDeclaredLessons(BigDecimal declaredLessons) {
	this.declaredLessons = declaredLessons;
    }

    public BigDecimal getGivenSummaries() {
	return givenSummaries;
    }

    public void setGivenSummaries(BigDecimal givenSummaries) {
	this.givenSummaries = givenSummaries;
    }

    public BigDecimal getGivenSummariesPercentage() {
	if (declaredLessons.intValue() == 0) {
	    return null;
	}
	return givenSummariesPercentage;
    }

    public void setGivenNotTaughtSummaries(BigDecimal givenNotTaughtSummaries) {
	this.givenNotTaughtSummaries = givenNotTaughtSummaries;
    }

    public BigDecimal getGivenNotTaughtSummaries() {
	return givenNotTaughtSummaries;
    }

    public BigDecimal getGivenNotTaughtSummariesPercentage() {
	if (declaredLessons.intValue() == 0) {
	    return null;
	}
	return givenNotTaughtSummariesPercentage;
    }

    public void setGivenNotTaughtSummariesPercentage(BigDecimal givenNotTaughtSummariesPercentage) {
	this.givenNotTaughtSummariesPercentage = givenNotTaughtSummariesPercentage;
    }

    public void setGivenSummariesPercentage(BigDecimal givenSummariesPercentage) {
	this.givenSummariesPercentage = givenSummariesPercentage;
    }

    public Integer getTeacherNumber() {
	return teacherNumber;
    }

    public void setTeacherNumber(Integer teacherNumber) {
	this.teacherNumber = teacherNumber;
    }

}
