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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;

public class ExternalCurricularCourseToEnrol extends DegreeModuleToEnrol {

    private CurricularCourse curricularCourse;

    public ExternalCurricularCourseToEnrol(final CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval) {
        super(curriculumGroup, null, executionInterval);
        this.curricularCourse = curricularCourse;
    }

    @Override
    public boolean canCollectRules() {
        return false;
    }

    @Override
    public double getAccumulatedEctsCredits(ExecutionInterval executionInterval) {
        return getStudentCurricularPlan().getAccumulatedEctsCredits(executionInterval, getDegreeModule());
    }

    @Override
    public Context getContext() {
        throw new DomainException("error.ExternalCurricularCourseToEnrol.doesnot.have.context");
    }

    @Override
    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(ExecutionInterval executionInterval) {
        return Collections.emptySet();
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionInterval executionInterval) {
        return Collections.emptyList();
    }

    @Override
    public CurricularCourse getDegreeModule() {
        return this.curricularCourse;
    }

    @Override
    public Double getEctsCredits(ExecutionInterval executionInterval) {
        return getDegreeModule().getEctsCredits(executionInterval);
    }

    @Override
    public String getKey() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getCurriculumGroup().getClass().getName()).append(":").append(getCurriculumGroup().getExternalId())
                .append(",").append(this.getDegreeModule().getClass().getName()).append(":").append(getName()).append(",")
                .append(getExecutionInterval().getClass().getName()).append(":").append(getExecutionInterval().getExternalId());
        return stringBuilder.toString();
    }

    @Override
    public String getName() {
        return getDegreeModule().getName(getExecutionInterval());
    }

    @Override
    public String getYearFullLabel() {
        throw new DomainException("error.ExternalCurricularCourseToEnrol.doesnot.have.context");
    }

    @Override
    public boolean isAnnualCurricularCourse(final ExecutionYear executionYear) {
        return getDegreeModule().isAnual(executionYear);
    }

    @Override
    public boolean isOptionalCurricularCourse() {
        return getDegreeModule().isOptionalCurricularCourse();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ExternalCurricularCourseToEnrol) {
            final ExternalCurricularCourseToEnrol degreeModuleToEnrol = (ExternalCurricularCourseToEnrol) obj;
            return getDegreeModule().equals(degreeModuleToEnrol.getDegreeModule())
                    && getCurriculumGroup().equals(degreeModuleToEnrol.getCurriculumGroup());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getDegreeModule().hashCode() + getCurriculumGroup().hashCode();
    }

}
