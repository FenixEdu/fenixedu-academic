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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class StandaloneCurriculumGroup extends StandaloneCurriculumGroup_Base {

    private StandaloneCurriculumGroup() {
        super();
    }

    protected StandaloneCurriculumGroup(final RootCurriculumGroup curriculumGroup) {
        this();
        init(curriculumGroup);
    }

    @Override
    public NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType() {
        return NoCourseGroupCurriculumGroupType.STANDALONE;
    }

    @Override
    public boolean canAdd(CurriculumLine curriculumLine) {
        return curriculumLine.isEnrolment();
    }

    @Override
    public Integer getChildOrder() {
        return super.getChildOrder() - 4;
    }

    @Override
    public boolean isStandalone() {
        return true;
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(final ExecutionSemester executionSemester) {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result += curriculumModule.getNumberOfAllApprovedEnrolments(executionSemester);
        }
        return result;
    }

    @Override
    public boolean allowAccumulatedEctsCredits() {
        return true;
    }
}
