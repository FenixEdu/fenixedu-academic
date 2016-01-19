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
package org.fenixedu.academic.dto.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curriculum.EnrolmentEvaluationContext;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;

@SuppressWarnings("serial")
public class SpecialSeasonStudentCurriculumGroupBean implements Serializable {

    private EvaluationSeason evaluationSeason;

    protected SpecialSeasonStudentCurriculumGroupBean(final EvaluationSeason evaluationSeason) {
        setEvaluationSeason(evaluationSeason);
    }

    public StudentCurriculumGroupBean create(final CurriculumGroup curriculumGroup, final ExecutionSemester executionSemester) {
        return new StudentCurriculumGroupBean(curriculumGroup, executionSemester, null) {

            @Override
            protected List<IDegreeModuleToEvaluate> buildCourseGroupsToEnrol(CurriculumGroup group,
                    ExecutionSemester executionSemester) {
                return Collections.emptyList();
            }

            @Override
            protected List<StudentCurriculumEnrolmentBean> buildCurricularCoursesEnroled(CurriculumGroup group,
                    ExecutionSemester executionSemester) {

                List<StudentCurriculumEnrolmentBean> result = new ArrayList<StudentCurriculumEnrolmentBean>();
                for (CurriculumModule curriculumModule : group.getCurriculumModulesSet()) {
                    if (curriculumModule.isEnrolment()) {
                        Enrolment enrolment = (Enrolment) curriculumModule;

                        if (enrolment.isEnroledInSeason(getEvaluationSeason(), executionSemester)) {
                            result.add(new StudentCurriculumEnrolmentBean(enrolment));
                        }
                    }
                }

                return result;
            }

            @Override
            protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
                    ExecutionSemester executionSemester) {

                final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();

                for (CurriculumModule curriculumModule : group.getCurriculumModulesSet()) {
                    if (curriculumModule.isEnrolment()) {
                        final Enrolment enrolment = (Enrolment) curriculumModule;

                        if (Enrolment.getPredicateSpecialSeason()
                                .fill(getEvaluationSeason(), executionSemester, EnrolmentEvaluationContext.MARK_SHEET_EVALUATION)
                                .testExceptionless(enrolment)) {

                            if (enrolment.parentCurriculumGroupIsNoCourseGroupCurriculumGroup()) {
                                result.add(new NoCourseGroupEnroledCurriculumModuleWrapper(enrolment,
                                        enrolment.getExecutionPeriod()));
                            } else {
                                result.add(new EnroledCurriculumModuleWrapper(enrolment, enrolment.getExecutionPeriod()));
                            }
                        }
                    }
                }

                return result;
            }

            @Override
            protected List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(CurriculumGroup parentGroup,
                    ExecutionSemester executionSemester, int[] curricularYears) {

                final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();
                
                final Set<CurriculumGroup> curriculumGroupsToEnrolmentProcess = parentGroup.getCurriculumGroupsToEnrolmentProcess();
                if (!parentGroup.isNoCourseGroupCurriculumGroup()) {
                    for (final NoCourseGroupCurriculumGroup curriculumGroup : parentGroup.getNoCourseGroupCurriculumGroups()) {
                        if (curriculumGroup.isVisible()) {
                            curriculumGroupsToEnrolmentProcess.add(curriculumGroup);
                        }
                    }
                }

                for (final CurriculumGroup curriculumGroup : curriculumGroupsToEnrolmentProcess) {
                    result.add(create(curriculumGroup, executionSemester));
                }

                return result;
            }

            @Override
            public List<IDegreeModuleToEvaluate> getSortedDegreeModulesToEvaluate() {
                final List<IDegreeModuleToEvaluate> result =
                        new ArrayList<IDegreeModuleToEvaluate>(getCurricularCoursesToEnrol());
                Collections.sort(result, IDegreeModuleToEvaluate.COMPARATOR_BY_EXECUTION_PERIOD);
                return result;
            }

            @Override
            public boolean isToBeDisabled() {
                return true;
            }
        };
    }

    public static StudentCurriculumGroupBean create(final CurriculumGroup curriculumGroup,
            final ExecutionSemester executionSemester, final EvaluationSeason evaluationSeason) {
        return new SpecialSeasonStudentCurriculumGroupBean(evaluationSeason).create(curriculumGroup, executionSemester);
    }

    public EvaluationSeason getEvaluationSeason() {
        return evaluationSeason;
    }

    public void setEvaluationSeason(EvaluationSeason evaluationSeason) {
        this.evaluationSeason = evaluationSeason;
    }

}
