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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DegreeModuleToEnrol implements Serializable, IDegreeModuleToEvaluate {

    private static final long serialVersionUID = -6337658191828772384L;

    private CurriculumGroup curriculumGroup;

    private Context context;

    private ExecutionSemester executionSemester;

    protected DegreeModuleToEnrol() {
    }

    public DegreeModuleToEnrol(final CurriculumGroup curriculumGroup, final Context context,
            final ExecutionSemester executionSemester) {
        this.curriculumGroup = curriculumGroup;
        this.context = context;
        this.executionSemester = executionSemester;
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

    @Override
    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    @Override
    public String getKey() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getContext().getClass().getName()).append(":").append(this.getContext().getExternalId())
                .append(",").append(this.getCurriculumGroup().getClass().getName()).append(":")
                .append(this.getCurriculumGroup().getExternalId()).append(",")
                .append(this.getExecutionPeriod().getClass().getName()).append(":")
                .append(this.getExecutionPeriod().getExternalId());
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
    public Double getEctsCredits(final ExecutionSemester executionSemester) {
        return isLeaf() ? ((CurricularCourse) getDegreeModule()).getEctsCredits(executionSemester) : Double.valueOf(0d);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DegreeModuleToEnrol) {
            final DegreeModuleToEnrol degreeModuleToEnrol = (DegreeModuleToEnrol) obj;
            return (this.getContext().equals(degreeModuleToEnrol.getContext()) && (this.getCurriculumGroup()
                    .equals(degreeModuleToEnrol.getCurriculumGroup())));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getContext().hashCode() + getCurriculumGroup().hashCode();
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionSemester executionSemester) {
        return getDegreeModule().getCurricularRules(getContext(), executionSemester);
    }

    @Override
    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(final ExecutionSemester executionSemester) {
        return getCurriculumGroup().getCurricularRules(executionSemester);
    }

    @Override
    public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester) {
        if (isLeaf()) {
            return getCurriculumGroup().getStudentCurricularPlan().getAccumulatedEctsCredits(executionSemester,
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
        return getEctsCredits(getExecutionPeriod());
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
