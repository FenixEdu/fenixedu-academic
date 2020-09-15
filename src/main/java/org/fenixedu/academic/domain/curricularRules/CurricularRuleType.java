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
/*
 * Created on Feb 2, 2006
 */
package org.fenixedu.academic.domain.curricularRules;

public enum CurricularRuleType {

    PRECEDENCY_APPROVED_DEGREE_MODULE,

    PRECEDENCY_ENROLED_DEGREE_MODULE,

    RESTRICTION_NOT_ENROLED_DEGREE_MODULE,

    PRECEDENCY_BETWEEN_DEGREE_MODULES,

    CREDITS_LIMIT,

    DEGREE_MODULES_SELECTION_LIMIT,

    ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR,

    EXCLUSIVENESS,

    ANY_CURRICULAR_COURSE,

    MINIMUM_NUMBER_OF_CREDITS_TO_ENROL,

    MAXIMUM_NUMBER_OF_CREDITS_FOR_ENROLMENT_PERIOD,

    PREVIOUS_YEARS_ENROLMENT,

    ASSERT_UNIQUE_APPROVAL_IN_CURRICULAR_COURSE_CONTEXTS,

    IMPROVEMENT_OF_APPROVED_ENROLMENT,

    ENROLMENT_IN_SPECIAL_SEASON_EVALUATION,

    MAXIMUM_NUMBER_OF_ECTS_IN_SPECIAL_SEASON_EVALUATION,

    CREDITS_LIMIT_IN_EXTERNAL_CYCLE,

    EVEN_ODD,

    MAXIMUM_NUMBER_OF_ECTS_IN_STANDALONE_CURRICULUM_GROUP,

    PHD_VALID_CURRICULAR_COURSES,

    SENIOR_STATUTE_SCOPE,
    
    LAST_DIGIT_SPLIT;

    public String getName() {
        return name();
    }

}
