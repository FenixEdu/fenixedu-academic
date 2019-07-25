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
package org.fenixedu.academic.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.dto.GenericPair;

public class AnyCurricularCourseExceptions extends AnyCurricularCourseExceptions_Base {

    public AnyCurricularCourseExceptions(final OptionalCurricularCourse degreeModuleToApplyRule,
            final CourseGroup contextCourseGroup, final ExecutionInterval begin, final ExecutionInterval end,
            final Boolean optionalsConfiguration) {

        super();
        init(degreeModuleToApplyRule, contextCourseGroup, begin, end, CurricularRuleType.ANY_CURRICULAR_COURSE_EXCEPTIONS);
        setOptionalsConfiguration(optionalsConfiguration);
    }

    @Override
    protected void removeOwnParameters() {
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> result = new ArrayList<GenericPair<Object, Boolean>>();

        String key = "label.anyCurricularCourseExceptions";

        final Boolean optionalsConfiguration = getOptionalsConfiguration();
        if (optionalsConfiguration != null) {
            key =
                    optionalsConfiguration ? "label.anyCurricularCourseExceptions.only.optional" : "label.anyCurricularCourseExceptions.only.mandatory";
        }

        result.add(new GenericPair<Object, Boolean>(key, true));
        return result;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
