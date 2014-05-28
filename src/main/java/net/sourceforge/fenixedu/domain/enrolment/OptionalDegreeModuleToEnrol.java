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
package net.sourceforge.fenixedu.domain.enrolment;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class OptionalDegreeModuleToEnrol extends DegreeModuleToEnrol {

    static private final long serialVersionUID = -7335154953414429996L;

    private CurricularCourse curricularCourse;

    public OptionalDegreeModuleToEnrol(CurriculumGroup curriculumGroup, Context context, ExecutionSemester executionSemester,
            CurricularCourse curricularCourse) {
        super(curriculumGroup, context, executionSemester);
        setCurricularCourse(curricularCourse);

    }

    public CurricularCourse getCurricularCourse() {
        return this.curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OptionalDegreeModuleToEnrol) {
            return super.equals(obj) && ((OptionalDegreeModuleToEnrol) obj).getCurricularCourse() == getCurricularCourse();
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + getContext().hashCode();
        result = 37 * result + getCurriculumGroup().hashCode();
        result = 37 * result + getCurricularCourse().hashCode();
        return result;
    }

    @Override
    public String getKey() {
        return super.getKey() + getCurricularCourse().getClass().getName() + ":" + getCurricularCourse().getExternalId();
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public Double getEctsCredits(final ExecutionSemester executionSemester) {
        return getCurricularCourse().getEctsCredits(executionSemester);
    }

    @Override
    public boolean isFor(DegreeModule degreeModule) {
        return getDegreeModule() == degreeModule || getCurricularCourse() == degreeModule;
    }

    @Override
    public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester) {
        if (isLeaf()) {
            return getCurriculumGroup().getStudentCurricularPlan().getAccumulatedEctsCredits(executionSemester,
                    getCurricularCourse());
        } else {
            return 0d;
        }
    }
}
