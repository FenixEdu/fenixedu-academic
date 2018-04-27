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
package org.fenixedu.academic.domain.studentCurriculum;

import java.util.Collection;
import java.util.Collections;

import org.fenixedu.academic.domain.CurricularCourse;

public class InternalCreditsSourceCurriculumGroup extends InternalCreditsSourceCurriculumGroup_Base {

    protected InternalCreditsSourceCurriculumGroup(final RootCurriculumGroup curriculumGroup) {
        super();
        init(curriculumGroup);
    }

    @Override
    public boolean isInternalCreditsSourceGroup() {
        return true;
    }

    @Override
    public NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType() {
        return NoCourseGroupCurriculumGroupType.INTERNAL_CREDITS_SOURCE_GROUP;
    }

    @Override
    public Integer getChildOrder() {
        return super.getChildOrder() - 1;
    }

    @Override
    public Collection<CurriculumGroup> getCurricularCoursePossibleGroups(final CurricularCourse curricularCourse) {
        return Collections.emptyList();
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
