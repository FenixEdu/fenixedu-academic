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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;

public class DegreeModuleToEnrol implements Serializable, IDegreeModuleToEvaluate {

    private static final long serialVersionUID = -6337658191828772384L;

    private CurriculumGroup curriculumGroup;

    private Context context;

    private ExecutionInterval executionInterval;

    protected DegreeModuleToEnrol() {
    }

    public DegreeModuleToEnrol(final CurriculumGroup curriculumGroup, final Context context,
            final ExecutionInterval executionInterval) {
        this.curriculumGroup = curriculumGroup;
        this.context = context;
        this.executionInterval = executionInterval;
    }

    @Override
    public CurriculumGroup getCurriculumGroup() {
        return this.curriculumGroup;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
        this.curriculumGroup = curriculumGroup;
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

//    @Override
//    public ExecutionSemester getExecutionPeriod() {
//        return this.executionInterval;
//    }

//    public void setExecutionPeriod(ExecutionSemester executionSemester) {
//        this.executionSemester = executionSemester;
//    }

    @Override
    public ExecutionInterval getExecutionInterval() {
        return this.executionInterval;
    }

    @Override
    public String getKey() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getContext().getClass().getName()).append(":").append(this.getContext().getExternalId())
                .append(",").append(this.getCurriculumGroup().getClass().getName()).append(":")
                .append(this.getCurriculumGroup().getExternalId()).append(",")
                .append(this.getExecutionInterval().getClass().getName()).append(":")
                .append(this.getExecutionInterval().getExternalId());
        return stringBuilder.toString();
    }

    @Override
    public boolean isLeaf() {
        return getDegreeModule().isLeaf();
    }

    @Override
    final public boolean isEnroling() {
        return true;
    }

    @Override
    final public boolean isEnroled() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean isDissertation() {
        return getDegreeModule().isDissertation();
    }

    @Override
    public boolean canCollectRules() {
        return true;
    }

    @Override
    public DegreeModule getDegreeModule() {
        return getContext().getChildDegreeModule();
    }

    @Override
    public Double getEctsCredits(final ExecutionInterval executionInterval) {
        return isLeaf() ? ((CurricularCourse) getDegreeModule()).getEctsCredits(executionInterval) : Double.valueOf(0d);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DegreeModuleToEnrol) {
            final DegreeModuleToEnrol degreeModuleToEnrol = (DegreeModuleToEnrol) obj;
            return (this.getContext().equals(degreeModuleToEnrol.getContext())
                    && (this.getCurriculumGroup().equals(degreeModuleToEnrol.getCurriculumGroup())));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getContext().hashCode() + getCurriculumGroup().hashCode();
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionInterval executionInterval) {
        return getDegreeModule().getCurricularRules(getContext(), executionInterval);
    }

    @Override
    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(final ExecutionInterval executionInterval) {
        return getCurriculumGroup().getCurricularRules(executionInterval);
    }

    @Override
    public double getAccumulatedEctsCredits(final ExecutionInterval executionInterval) {
        if (isLeaf()) {
            return getCurriculumGroup().getStudentCurricularPlan().getAccumulatedEctsCredits(executionInterval,
                    (CurricularCourse) getDegreeModule());
        } else {
            return 0d;
        }
    }

    @Override
    public String getName() {
        return getDegreeModule().getName();
    }

    @Override
    public String getYearFullLabel() {
        return getContext().getCurricularPeriod().getFullLabel();
    }

    @Override
    public boolean isOptionalCurricularCourse() {
        if (getDegreeModule().isLeaf()) {
            CurricularCourse curricularCourse = (CurricularCourse) getDegreeModule();
            return curricularCourse.isOptionalCurricularCourse();
        }
        return false;
    }

    @Override
    public Double getEctsCredits() {
        return getEctsCredits(getExecutionInterval());
    }

    @Override
    public boolean isFor(DegreeModule degreeModule) {
        return getDegreeModule() == degreeModule;
    }

    @Override
    public boolean isAnnualCurricularCourse(ExecutionYear executionYear) {
        if (getDegreeModule().isLeaf()) {
            return ((CurricularCourse) getDegreeModule()).isAnual(executionYear);
        }
        return false;
    }

    protected StudentCurricularPlan getStudentCurricularPlan() {
        return getCurriculumGroup().getStudentCurricularPlan();
    }
}
