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
package org.fenixedu.academic.domain.enrolment;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;

public class OptionalDegreeModuleToEnrol extends DegreeModuleToEnrol {

    static private final long serialVersionUID = -7335154953414429996L;

    private CurricularCourse curricularCourse;

    public OptionalDegreeModuleToEnrol(CurriculumGroup curriculumGroup, Context context, ExecutionInterval executionInterval,
            CurricularCourse curricularCourse) {
        super(curriculumGroup, context, executionInterval);
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
    public Double getEctsCredits(final ExecutionInterval executionInterval) {
        return getCurricularCourse().getEctsCredits(executionInterval);
    }

    @Override
    public boolean isFor(DegreeModule degreeModule) {
        return getDegreeModule() == degreeModule || getCurricularCourse() == degreeModule;
    }

    @Override
    public double getAccumulatedEctsCredits(final ExecutionInterval executionInterval) {
        if (isLeaf()) {
            return getCurriculumGroup().getStudentCurricularPlan().getAccumulatedEctsCredits(executionInterval,
                    getCurricularCourse());
        } else {
            return 0d;
        }
    }
}
