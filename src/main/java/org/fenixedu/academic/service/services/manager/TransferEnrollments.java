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
package org.fenixedu.academic.service.services.manager;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class TransferEnrollments {

    @Atomic
    public static void run(final String destinationStudentCurricularPlanId, final String[] enrollmentIDsToTransfer,
            final String destinationCurriculumGroupID) {
        if (!StringUtils.isEmpty(destinationCurriculumGroupID)) {

            CurriculumGroup curriculumGroup = (CurriculumGroup) FenixFramework.getDomainObject(destinationCurriculumGroupID);
            StudentCurricularPlan studentCurricularPlan = curriculumGroup.getStudentCurricularPlan();

            for (final String enrollmentIDToTransfer : enrollmentIDsToTransfer) {
                Enrolment enrolment = (Enrolment) FenixFramework.getDomainObject(enrollmentIDToTransfer);

                fixEnrolmentCurricularCourse(studentCurricularPlan, enrolment);

                enrolment.setCurriculumGroup(curriculumGroup);
                enrolment.setStudentCurricularPlan(null);
            }

        } else {

            final StudentCurricularPlan studentCurricularPlan =
                    FenixFramework.getDomainObject(destinationStudentCurricularPlanId);
            for (final String enrollmentIDToTransfer : enrollmentIDsToTransfer) {
                final Enrolment enrollment = (Enrolment) FenixFramework.getDomainObject(enrollmentIDToTransfer);

                fixEnrolmentCurricularCourse(studentCurricularPlan, enrollment);

                if (enrollment.getStudentCurricularPlan() != studentCurricularPlan) {
                    enrollment.setStudentCurricularPlan(studentCurricularPlan);
                    enrollment.setCurriculumGroup(null);
                }

            }
        }
    }

    private static void fixEnrolmentCurricularCourse(final StudentCurricularPlan studentCurricularPlan, final Enrolment enrollment) {
        if (enrollment.getCurricularCourse().getDegreeCurricularPlan() != studentCurricularPlan.getDegreeCurricularPlan()) {
            CurricularCourse curricularCourse =
                    studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(
                            enrollment.getCurricularCourse().getCode());
            if (curricularCourse != null) {
                enrollment.setCurricularCourse(curricularCourse);
            }
        }
    }

}