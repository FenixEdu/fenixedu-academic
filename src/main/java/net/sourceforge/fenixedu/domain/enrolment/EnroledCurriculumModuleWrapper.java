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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnroledCurriculumModuleWrapper implements Serializable, IDegreeModuleToEvaluate {

    private static final long serialVersionUID = 8730987603988026373L;

    private CurriculumModule curriculumModule;

    protected Context context;

    private ExecutionSemester executionSemester;

    public EnroledCurriculumModuleWrapper(final CurriculumModule curriculumModule, final ExecutionSemester executionSemester) {
        setCurriculumModule(curriculumModule);
        setExecutionPeriod(executionSemester);
    }

    public CurriculumModule getCurriculumModule() {
        return this.curriculumModule;
    }

    public void setCurriculumModule(CurriculumModule curriculumModule) {
        this.curriculumModule = curriculumModule;
    }

    @Override
    public Context getContext() {
        if (context == null) {
            if (!getCurriculumModule().isRoot()) {
                findContext();
            }
        }
        return context;
    }

    private void findContext() {
        Context result = null;

        final CurriculumGroup parent = getCurriculumModule().getCurriculumGroup();
        if (parent.hasDegreeModule()) {
            for (final Context context : parent.getDegreeModule().getValidChildContexts(getExecutionPeriod())) {
                if (context.getChildDegreeModule() == getDegreeModule()) {
                    if (result == null || context.getCurricularYear().intValue() < result.getCurricularYear().intValue()) {
                        result = context;
                    }
                }
            }
        }

        setContext(result);
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
    public CurriculumGroup getCurriculumGroup() {
        return getCurriculumModule().getCurriculumGroup();
    }

    @Override
    public DegreeModule getDegreeModule() {
        return getCurriculumModule().getDegreeModule();
    }

    public boolean hasDegreeModule() {
        return getDegreeModule() != null;
    }

    @Override
    public boolean isLeaf() {
        if (!getCurriculumModule().isLeaf()) {
            return false;
        }
        final CurriculumLine curriculumLine = (CurriculumLine) getCurriculumModule();
        return curriculumLine.isEnrolment();
    }

    @Override
    final public boolean isEnroled() {
        return true;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean isDissertation() {
        return false;
    }

    @Override
    public boolean canCollectRules() {
        if (getCurriculumModule().isLeaf()) {
            return true;
        } else {
            final CurriculumGroup curriculumGroup = (CurriculumGroup) getCurriculumModule();
            return !curriculumGroup.hasAnyCurriculumModules();
        }
    }

    @Override
    public Double getEctsCredits(final ExecutionSemester executionSemester) {
        return getCurriculumModule().getEctsCredits();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EnroledCurriculumModuleWrapper) {
            final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) obj;
            return getCurriculumModule() == moduleEnroledWrapper.getCurriculumModule();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getCurriculumModule().hashCode();
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionSemester executionSemester) {
        return hasDegreeModule() ? getDegreeModule().getCurricularRules(getContext(), executionSemester) : Collections.EMPTY_LIST;
    }

    @Override
    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(ExecutionSemester executionSemester) {
        return getCurriculumModule().isRoot() ? Collections.EMPTY_SET : getCurriculumGroup()
                .getCurricularRules(executionSemester);
    }

    @Override
    public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester) {
        if (getCurriculumModule().isEnrolment()) {
            return ((Enrolment) getCurriculumModule()).getAccumulatedEctsCredits(executionSemester);
        } else {
            return 0d;
        }
    }

    @Override
    public String getName() {
        return getCurriculumModule().getName().getContent();
    }

    @Override
    public String getYearFullLabel() {
        if (getExecutionPeriod() != null) {
            return getExecutionPeriod().getQualifiedName();
        }
        return "";
    }

    @Override
    public boolean isOptionalCurricularCourse() {
        return false;
    }

    @Override
    public Double getEctsCredits() {
        return getCurriculumModule().getEctsCredits();
    }

    @Override
    public String getKey() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getCurriculumModule().getClass().getName()).append(":")
                .append(this.getCurriculumModule().getExternalId()).append(",")
                .append(this.getExecutionPeriod().getClass().getName()).append(":")
                .append(this.getExecutionPeriod().getExternalId());
        return stringBuilder.toString();
    }

    @Override
    final public boolean isEnroling() {
        return false;
    }

    @Override
    public boolean isFor(final DegreeModule degreeModule) {
        return getDegreeModule() == degreeModule;
    }

    @Override
    public boolean isAnnualCurricularCourse(final ExecutionYear executionYear) {
        if (getDegreeModule().isLeaf()) {
            return ((CurricularCourse) getDegreeModule()).isAnual(executionYear);
        }
        return false;
    }
}
