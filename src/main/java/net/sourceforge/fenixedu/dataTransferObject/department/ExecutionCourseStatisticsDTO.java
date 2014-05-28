/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
