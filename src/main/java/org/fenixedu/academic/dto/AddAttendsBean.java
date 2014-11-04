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
package org.fenixedu.academic.dto;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.interfaces.HasExecutionDegree;
import org.fenixedu.academic.domain.interfaces.HasExecutionSemester;
import org.fenixedu.academic.domain.interfaces.HasExecutionYear;

public class AddAttendsBean implements Serializable, HasExecutionYear, HasExecutionSemester, HasExecutionDegree {

    private ExecutionYear executionYear;
    private ExecutionSemester executionPeriod;
    private ExecutionDegree executionDegree;
    private ExecutionCourse executionCourse;

    @Override
    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    @Override
    public ExecutionSemester getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(ExecutionSemester executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    @Override
    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(final ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(final ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

}
