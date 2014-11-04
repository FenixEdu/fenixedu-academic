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
package net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionDegree;

public class ImportContentBean implements Serializable, HasExecutionDegree {

    private ExecutionSemester executionPeriodReference;

    private ExecutionDegree executionDegreeReference;

    private CurricularYear curricularYearReference;

    private ExecutionCourse executionCourseReference;

    public ImportContentBean() {
        setExecutionPeriod(null);
        setExecutionDegree(null);
        setCurricularYear(null);
        setExecutionCourse(null);
    }

    public ExecutionCourse getExecutionCourse() {
        return this.executionCourseReference;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourseReference = executionCourse;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionPeriodReference;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionPeriodReference = executionSemester;
    }

    @Override
    public ExecutionDegree getExecutionDegree() {
        return this.executionDegreeReference;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegreeReference = executionDegree;
    }

    public CurricularYear getCurricularYear() {
        return this.curricularYearReference;
    }

    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYearReference = curricularYear;
    }

}
