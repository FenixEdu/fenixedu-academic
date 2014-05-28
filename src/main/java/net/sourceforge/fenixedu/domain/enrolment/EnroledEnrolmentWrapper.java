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

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;

public class EnroledEnrolmentWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 8766503234444669518L;

    public EnroledEnrolmentWrapper(final Enrolment enrolment, final ExecutionSemester executionSemester) {
        super(enrolment, executionSemester);
    }

    @Override
    public boolean canCollectRules() {
        return getCurriculumModule().parentCurriculumGroupIsNoCourseGroupCurriculumGroup() ? false : super.canCollectRules();
    }

    @Override
    public Enrolment getCurriculumModule() {
        return (Enrolment) super.getCurriculumModule();
    }

    private boolean isApproved() {
        return getCurriculumModule().isApproved();
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionSemester executionSemester) {
        return isApproved() ? Collections.EMPTY_LIST : super.getCurricularRulesFromDegreeModule(executionSemester);
    }

    @Override
    public boolean isDissertation() {
        return getCurriculumModule().getDegreeModule().isDissertation();
    }
}
