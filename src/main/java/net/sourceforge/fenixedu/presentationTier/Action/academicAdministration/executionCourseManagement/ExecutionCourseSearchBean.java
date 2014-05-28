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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class ExecutionCourseSearchBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionSemester semester;
    private DegreeCurricularPlan degreeCurricularPlan;
    private CurricularCourse curricularCourse;

    public ExecutionCourseSearchBean() {

    }

    public ExecutionSemester getSemester() {
        return semester;
    }

    public void setSemester(ExecutionSemester semester) {
        this.semester = semester;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

}
