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

public class StudentCurricularPlanPropaeudeuticsEnrolmentManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanPropaeudeuticsEnrolmentManager(final EnrolmentContext enrolmentContext) {
        super(enrolmentContext);
    }

    @Override
    protected void assertEnrolmentPreConditions() {
        if (!isResponsiblePersonAllowedToEnrolStudents()) {
            throw new DomainException("error.StudentCurricularPlan.cannot.enrol.in.propaedeutic");
        }

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
                            "error.StudentCurricularPlanPropaeudeuticsEnrolmentManager.can.only.enrol.in.curricularCourses");
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
        for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesToEnrolMap.entrySet()) {
            for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {
                if (degreeModuleToEvaluate.isEnroling() && degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
                    final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();

                    checkIDegreeModuleToEvaluate(curricularCourse);
                    new Enrolment(getStudentCurricularPlan(), degreeModuleToEvaluate.getCurriculumGroup(), curricularCourse,
                            getExecutionSemester(), EnrollmentCondition.VALIDATED, getResponsiblePerson().getUsername());
                }
            }
        }
        getRegistration().updateEnrolmentDate(getExecutionYear());
    }

    @Override
    protected void unEnrol() {
        for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
            if (curriculumModule.isLeaf()) {
                curriculumModule.delete();
            }
        }
    }
}
