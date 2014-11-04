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

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnroledOptionalEnrolment extends EnroledCurriculumModuleWrapper {

    /**
     * 
     */
    private static final long serialVersionUID = 3085559707039631255L;

    private OptionalCurricularCourse optionalCurricularCourse;

    public EnroledOptionalEnrolment(CurriculumModule curriculumModule, OptionalCurricularCourse optionalCurricularCourse,
            ExecutionSemester executionSemester) {
        super(curriculumModule, executionSemester);
        setOptionalCurricularCourse(optionalCurricularCourse);

    }

    public OptionalCurricularCourse getOptionalCurricularCourse() {
        return this.optionalCurricularCourse;
    }

    public void setOptionalCurricularCourse(OptionalCurricularCourse optionalCurricularCourse) {
        this.optionalCurricularCourse = optionalCurricularCourse;
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionSemester executionSemester) {
        final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) getCurriculumModule();
        return optionalEnrolment.isApproved() ? Collections.EMPTY_LIST : getOptionalCurricularCourse().getCurricularRules(
                getContext(), executionSemester);
    }

    @Override
    public Context getContext() {
        if (this.context == null) {
            if (!getCurriculumModule().isRoot()) {
                final CurriculumGroup parentCurriculumGroup = getCurriculumModule().getCurriculumGroup();
                for (final Context context : parentCurriculumGroup.getDegreeModule().getValidChildContexts(getExecutionPeriod())) {
                    if (context.getChildDegreeModule() == getOptionalCurricularCourse()) {
                        setContext(context);
                        break;
                    }
                }
            }

        }

        return context;
    }

    @Override
    public boolean isFor(DegreeModule degreeModule) {
        return getDegreeModule() == degreeModule || getOptionalCurricularCourse() == degreeModule;
    }

    @Override
    public boolean isAnnualCurricularCourse(ExecutionYear executionYear) {
        return getOptionalCurricularCourse().isAnual(executionYear);
    }

}
