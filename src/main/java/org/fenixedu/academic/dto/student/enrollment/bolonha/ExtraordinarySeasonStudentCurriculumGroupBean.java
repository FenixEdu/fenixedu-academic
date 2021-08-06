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

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.util.predicates.InlinePredicate;
import org.fenixedu.bennu.core.security.Authenticate;

import java.util.*;
import java.util.function.Predicate;

public class ExtraordinarySeasonStudentCurriculumGroupBean extends StudentCurriculumGroupBean {

    private static final long serialVersionUID = 8504847305104217989L;

    public ExtraordinarySeasonStudentCurriculumGroupBean(final CurriculumGroup curriculumGroup,
                                                         final ExecutionSemester executionSemester) {
        super(curriculumGroup, executionSemester, null);
    }

    @Override
    protected List<IDegreeModuleToEvaluate> buildCourseGroupsToEnrol(CurriculumGroup group, ExecutionSemester executionSemester) {
        return Collections.emptyList();
    }

    @Override
    protected List<StudentCurriculumEnrolmentBean> buildCurricularCoursesEnroled(CurriculumGroup group,
            ExecutionSemester executionSemester) {
        List<StudentCurriculumEnrolmentBean> result = new ArrayList<StudentCurriculumEnrolmentBean>();
        for (CurriculumModule curriculumModule : group.getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.isExtraordinarySeasonEnroled(executionSemester)) {
                    result.add(new StudentCurriculumEnrolmentBean(enrolment));
                }
            }
        }

        return result;
    }

    @Override
    protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
            ExecutionSemester executionSemester) {

        final Collection<Enrolment> extraordinarySeasonEnrolments = group.getExtraordinarySeasonEnrolments(executionSemester);
        final Predicate<Enrolment> alreadyHasExtraordinarySeasonEnrolment =
                new InlinePredicate<Enrolment, Collection<Enrolment>>(extraordinarySeasonEnrolments) {

                    @Override
                    public boolean test(Enrolment enrolment) {
                        for (final Enrolment extraordinarySeasonEnrolment : getValue()) {
                            if (extraordinarySeasonEnrolment.getDegreeModule().equals(enrolment.getDegreeModule())) {
                                return true;
                            }
                        }
                        return false;
                    }
                };

        final Map<CurricularCourse, Enrolment> enrolmentsMap = new HashMap<CurricularCourse, Enrolment>();
        boolean isServices =
                AcademicAuthorizationGroup.get(AcademicOperationType.STUDENT_ENROLMENTS).isMember(Authenticate.getUser());

        for (final CurriculumModule curriculumModule : group.getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {

                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (!considerThisEnrolmentGeneralRule(enrolment, executionSemester, alreadyHasExtraordinarySeasonEnrolment)) {
                    continue;
                }

                if (considerThisEnrolmentNormalEnrolments(enrolment)
                        || considerThisEnrolmentPropaedeuticEnrolments(enrolment, isServices)
                        || considerThisEnrolmentExtraCurricularEnrolments(enrolment, isServices)
                        || considerThisEnrolmentStandaloneEnrolments(enrolment, isServices)) {

                    if (enrolmentsMap.get(enrolment.getCurricularCourse()) != null) {
                        Enrolment enrolmentMap = enrolmentsMap.get(enrolment.getCurricularCourse());
                        if (enrolment.getExecutionPeriod().isAfter(enrolmentMap.getExecutionPeriod())) {
                            enrolmentsMap.put(enrolment.getCurricularCourse(), enrolment);
                        }
                    } else {
                        enrolmentsMap.put(enrolment.getCurricularCourse(), enrolment);
                    }

                }
            }
        }

        final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
        for (Enrolment enrolment : enrolmentsMap.values()) {
            if (enrolment.parentCurriculumGroupIsNoCourseGroupCurriculumGroup()) {
                result.add(new NoCourseGroupEnroledCurriculumModuleWrapper(enrolment, enrolment.getExecutionPeriod()));
            } else {
                result.add(new EnroledCurriculumModuleWrapper(enrolment, enrolment.getExecutionPeriod()));
            }
        }

        return result;
    }

    @Override
    protected List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(CurriculumGroup parentGroup,
            ExecutionSemester executionSemester, int[] curricularYears) {

        final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();
        for (final CurriculumGroup curriculumGroup : parentGroup.getCurriculumGroupsToEnrolmentProcess()) {
            result.add(new ExtraordinarySeasonStudentCurriculumGroupBean(curriculumGroup, executionSemester));
        }

        if (!parentGroup.isNoCourseGroupCurriculumGroup()) {
            for (final NoCourseGroupCurriculumGroup curriculumGroup : parentGroup.getNoCourseGroupCurriculumGroups()) {

                if (!curriculumGroup.isVisible()) {
                    continue;
                }

                result.add(new ExtraordinarySeasonStudentCurriculumGroupBean(curriculumGroup, executionSemester));
            }
        }

        return result;
    }

    @Override
    public List<IDegreeModuleToEvaluate> getSortedDegreeModulesToEvaluate() {
        final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>(getCurricularCoursesToEnrol());
        Collections.sort(result, IDegreeModuleToEvaluate.COMPARATOR_BY_EXECUTION_PERIOD);
        return result;
    }

    @Override
    public boolean isToBeDisabled() {
        return true;
    }

    private boolean considerThisEnrolmentGeneralRule(Enrolment enrolment, ExecutionSemester executionSemester,
            Predicate<Enrolment> alreadyHasExtraordinarySeasonEnrolment) {
        return enrolment.canBeExtraordinarySeasonEnroled(executionSemester) && !alreadyHasExtraordinarySeasonEnrolment.test(enrolment);
    }

    private boolean considerThisEnrolmentNormalEnrolments(Enrolment enrolment) {
        if (enrolment.isBolonhaDegree() && !enrolment.isExtraCurricular() && !enrolment.isPropaedeutic()
                && !enrolment.isStandalone()) {
            if (enrolment.getParentCycleCurriculumGroup().isConclusionProcessed()) {
                return false;
            }
        }
        return !enrolment.parentCurriculumGroupIsNoCourseGroupCurriculumGroup() || enrolment.isPropaedeutic();
    }

    private boolean considerThisEnrolmentPropaedeuticEnrolments(Enrolment enrolment, boolean isServices) {
        return enrolment.isPropaedeutic() && isServices;
    }

    private boolean considerThisEnrolmentExtraCurricularEnrolments(Enrolment enrolment, boolean isServices) {
        return enrolment.isExtraCurricular() && isServices;
    }

    private boolean considerThisEnrolmentStandaloneEnrolments(Enrolment enrolment, boolean isServices) {
        return enrolment.isStandalone() && isServices;
    }
}
