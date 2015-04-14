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
package org.fenixedu.academic.service.services.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.studentCurricularPlan.Specialization;
import org.fenixedu.academic.dto.InfoStudentCurricularPlanWithFirstTimeEnrolment;

import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListMasterDegreeStudents {

    @Atomic
    public static Collection run(String executionYearName) {
        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearName);

        final Collection<InfoStudentCurricularPlanWithFirstTimeEnrolment> infoStudentCurricularPlans = new ArrayList();
        final Collection<StudentCurricularPlan> studentCurricularPlans = new ArrayList();
        final Collection<DegreeCurricularPlan> masterDegreeCurricularPlans = readByDegreeTypeAndState();
        CollectionUtils.filter(masterDegreeCurricularPlans, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) arg0;
                for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                    if (executionDegree.getExecutionYear().equals(executionYear)) {
                        return true;
                    }
                }
                return false;
            }

        });

        for (DegreeCurricularPlan degreeCurricularPlan : masterDegreeCurricularPlans) {
            studentCurricularPlans.addAll(degreeCurricularPlan.getStudentCurricularPlansSet());
        }

        for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {

            if (!studentCurricularPlan.isActive()) {
                continue;
            }

            boolean firstTimeEnrolment = true;
            if (studentCurricularPlan.getSpecialization() != null
                    && studentCurricularPlan.getSpecialization().equals(Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {

                Collection<StudentCurricularPlan> previousStudentCurricularPlans =
                        studentCurricularPlan.getRegistration().getStudentCurricularPlansBySpecialization(
                                Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE);

                previousStudentCurricularPlans.remove(studentCurricularPlan);
                for (StudentCurricularPlan previousStudentCurricularPlan : previousStudentCurricularPlans) {
                    if (previousStudentCurricularPlan.getDegreeCurricularPlan().getDegree()
                            .equals(studentCurricularPlan.getDegreeCurricularPlan().getDegree())) {
                        firstTimeEnrolment = false;
                        break;
                    }
                }
            } else if (studentCurricularPlan.getSpecialization() != null
                    && studentCurricularPlan.getSpecialization().equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {
                if (!studentCurricularPlan.getDegreeCurricularPlan().getFirstExecutionDegree().getExecutionYear()
                        .equals(executionYear)) {
                    continue;
                }
            }

            if (firstTimeEnrolment) {
                if (!studentCurricularPlan.getDegreeCurricularPlan().getFirstExecutionDegree().getExecutionYear()
                        .equals(executionYear)) {
                    firstTimeEnrolment = false;
                }
            }

            InfoStudentCurricularPlanWithFirstTimeEnrolment infoStudentCurricularPlan =
                    InfoStudentCurricularPlanWithFirstTimeEnrolment.newInfoFromDomain(studentCurricularPlan);
            infoStudentCurricularPlan.setFirstTimeEnrolment(firstTimeEnrolment);
            infoStudentCurricularPlans.add(infoStudentCurricularPlan);
        }

        return infoStudentCurricularPlans;

    }

    private static List<DegreeCurricularPlan> readByDegreeTypeAndState() {
        List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
        for (DegreeCurricularPlan degreeCurricularPlan : DegreeCurricularPlan.readNotEmptyDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getDegree().getDegreeType().isPreBolonhaMasterDegree()
                    && degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE) {
                result.add(degreeCurricularPlan);
            }
        }
        return result;
    }
}