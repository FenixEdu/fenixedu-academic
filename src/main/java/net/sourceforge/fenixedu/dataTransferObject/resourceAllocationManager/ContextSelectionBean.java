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
package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class ContextSelectionBean implements Serializable {
    private AcademicInterval academicInterval;
    private CurricularYear curricularYear;
    private ExecutionDegree executionDegree;
    private String courseName;

    public ContextSelectionBean(AcademicInterval academicInterval) {
        this.academicInterval = academicInterval;
    }

    public ContextSelectionBean() {
        this.academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
    }

    public ContextSelectionBean(AcademicInterval academicInterval, ExecutionDegree executionDegree, CurricularYear curricularYear) {
        this.academicInterval = academicInterval;
        setExecutionDegree(executionDegree);
        setCurricularYear(curricularYear);
    }

    public AcademicInterval getAcademicInterval() {
        return academicInterval;
    }

    public void setAcademicInterval(AcademicInterval academicInterval) {
        this.academicInterval = academicInterval;
    }

    public CurricularYear getCurricularYear() {
        return this.curricularYear;
    }

    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    public ExecutionDegree getExecutionDegree() {
        return this.executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}
