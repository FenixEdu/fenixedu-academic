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
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.spaces.domain.Space;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class SearchCourseResponsiblesParametersBean implements Serializable {

    private ExecutionSemester executionSemester;

    private Person responsible;

    private CurricularCourse curricularCourse;

    private CompetenceCourse competenceCourse;

    private final Space campus;

    private final Degree degree;

    public SearchCourseResponsiblesParametersBean(CurricularCourse curricularCourse, CompetenceCourse competenceCourse,
            Person responsible, ExecutionSemester executionSemester, Space campus, Degree degree) {
        this.executionSemester = executionSemester;
        this.curricularCourse = curricularCourse;
        this.responsible = responsible;
        this.competenceCourse = competenceCourse;
        this.campus = campus;
        this.degree = degree;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setResponsible(Person responsible) {
        this.responsible = responsible;
    }

    public Person getResponsible() {
        return responsible;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }

    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }

    public Space getCampus() {
        return campus;
    }

    public Degree getDegree() {
        return degree;
    }

}
