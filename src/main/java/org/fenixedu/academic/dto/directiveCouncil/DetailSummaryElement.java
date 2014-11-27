/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Apr 14, 2010
 *	by rcro
 */
package org.fenixedu.academic.dto.directiveCouncil;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetailSummaryElement implements Serializable {

    String teacherName;
    String executionCourseName;
    String categoryName;
    String executionPeriodName;
    String departmentName;
    String siglas;
    BigDecimal givenSummaries;
    BigDecimal givenNotTaughtSummaries;
    String teacherId;
    String teacherEmail;

    public DetailSummaryElement(String teacherName, String executionCourseName, String teacherId, String teacherEmail,
            String categoryName, BigDecimal givenSummaries, BigDecimal givenNotTaughtSummaries, String siglas) {

        setExecutionCourseName(executionCourseName);
        setTeacherName(teacherName);
        setTeacherId(teacherId);
        setTeacherEmail(teacherEmail);
        setCategoryName(categoryName);
        setSiglas(siglas);
        setGivenSummaries(givenSummaries);
        setGivenNotTaughtSummaries(givenNotTaughtSummaries);
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

    public BigDecimal getGivenSummaries() {
        return givenSummaries;
    }

    public void setGivenSummaries(BigDecimal givenSummaries) {
        this.givenSummaries = givenSummaries;
    }

    public void setGivenNotTaughtSummaries(BigDecimal givenNotTaughtSummaries) {
        this.givenNotTaughtSummaries = givenNotTaughtSummaries;
    }

    public BigDecimal getGivenNotTaughtSummaries() {
        return givenNotTaughtSummaries;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

}
