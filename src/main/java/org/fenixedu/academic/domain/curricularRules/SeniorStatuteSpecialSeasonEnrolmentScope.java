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
package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class SeniorStatuteSpecialSeasonEnrolmentScope extends CurricularRuleNotPersistent {

    private Enrolment enrolment;
    private Registration registration;

    public SeniorStatuteSpecialSeasonEnrolmentScope(final Enrolment enrolment, final Registration registration) {
        if (enrolment == null || registration == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        } else {
            this.enrolment = enrolment;
            this.registration = registration;
        }
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections
                .singletonList(new GenericPair<Object, Boolean>("label.seniorStatuteSpecialSeasonEnrolmentScope", true));
    }

    public Enrolment getEnrolment() {
        return enrolment;
    }

    public Registration getRegistration() {
        return registration;
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return getEnrolment().getDegreeModule();
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return null;
    }

    @Override
    public CompositeRule getParentCompositeRule() {
        return null;
    }

    @Override
    public CurricularRuleType getCurricularRuleType() {
        return CurricularRuleType.SENIOR_STATUTE_SCOPE;
    }

    @Override
    public ExecutionSemester getBegin() {
        return ExecutionSemester.readActualExecutionSemester();
    }

    @Override
    public ExecutionSemester getEnd() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SeniorStatuteSpecialSeasonEnrolmentScope) {
            SeniorStatuteSpecialSeasonEnrolmentScope seniorStatuteSpecialSeasonEnrolmentScope =
                    (SeniorStatuteSpecialSeasonEnrolmentScope) obj;

            return (enrolment == seniorStatuteSpecialSeasonEnrolmentScope.getEnrolment() && registration == seniorStatuteSpecialSeasonEnrolmentScope
                    .getRegistration());
        }

        return false;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
