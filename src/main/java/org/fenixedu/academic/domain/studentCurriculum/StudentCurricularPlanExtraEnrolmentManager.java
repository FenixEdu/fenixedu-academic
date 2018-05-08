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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.treasury.ITreasuryBridgeAPI;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;

import com.google.common.collect.Sets;

public class StudentCurricularPlanExtraEnrolmentManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanExtraEnrolmentManager(final EnrolmentContext enrolmentContext) {
        super(enrolmentContext);
    }

    @Override
    protected void assertEnrolmentPreConditions() {
        if (getRegistration().isRegistrationConclusionProcessed()) {
            checkUpdateRegistrationAfterConclusion();
        }

        checkEnrolingDegreeModules();
    }

    private void checkEnrolingDegreeModules() {
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isEnroling()) {
                if (!degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
                    throw new DomainException(
                            "error.StudentCurricularPlanExtraEnrolmentManager.can.only.enrol.in.curricularCourses");
                }
                checkIDegreeModuleToEvaluate((CurricularCourse) degreeModuleToEvaluate.getDegreeModule());
            }
        }
    }

    private void checkIDegreeModuleToEvaluate(final CurricularCourse curricularCourse) {
        if (getStudentCurricularPlan().isApproved(curricularCourse, getExecutionSemester())) {
            throw new DomainException("error.already.aproved", curricularCourse.getName());
        }

        if (getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, getExecutionSemester())) {
            throw new DomainException("error.already.enroled.in.executionPeriod", curricularCourse.getName(),
                    getExecutionSemester().getQualifiedName());
        }
    }

    @Override
    protected void addEnroled() {
        // nothing to be done
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
        final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result =
                new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isEnroling() && degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
                result.put(degreeModuleToEvaluate, Collections.<ICurricularRule> emptySet());
            }
        }
        return result;
    }

    @Override
    protected void performEnrolments(Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap) {

        final Set<Enrolment> enrolmentsToNotify = Sets.newHashSet();

        for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesToEnrolMap.entrySet()) {
            for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {
                if (degreeModuleToEvaluate.isEnroling() && degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
                    final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();

                    checkIDegreeModuleToEvaluate(curricularCourse);
                    final Enrolment enrolment =
                            new Enrolment(getStudentCurricularPlan(), degreeModuleToEvaluate.getCurriculumGroup(),
                                    curricularCourse, getExecutionSemester(), EnrollmentCondition.VALIDATED,
                                    getResponsiblePerson().getUsername());

                    enrolmentsToNotify.add(enrolment);
                }
            }
        }

        for (final Enrolment enrolment : enrolmentsToNotify) {
            Signal.emit(ITreasuryBridgeAPI.EXTRACURRICULAR_ENROLMENT, new DomainObjectEvent<Enrolment>(enrolment));
        }
        getRegistration().updateEnrolmentDate(getExecutionYear());
    }

    @Override
    protected void unEnrol() {
        for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
            if (curriculumModule.isLeaf()) {
                TreasuryBridgeAPIFactory.implementation().extracurricularUnenrolment((Enrolment) curriculumModule);

                curriculumModule.delete();
            }
        }
    }

}
