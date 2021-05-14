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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;

public class ImprovementStudentCurriculumGroupBean extends StudentCurriculumGroupBean {

    static private final long serialVersionUID = 1L;

    public ImprovementStudentCurriculumGroupBean(final CurriculumGroup curriculumGroup, final ExecutionSemester executionSemester) {
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
                if (enrolment.isImprovementEnroled() && !enrolment.getExecutionPeriod().isAfter(executionSemester)) {
                    result.add(new StudentCurriculumEnrolmentBean(enrolment));
                }
            }
        }

        return result;
    }

    @Override
    protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
            ExecutionSemester executionSemester) {
        List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
        for (CurriculumModule curriculumModule : group.getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.canBeImproved() && !enrolment.getExecutionPeriod().isAfter(executionSemester)) {
                    result.add(new EnroledCurriculumModuleWrapper(enrolment, enrolment.getExecutionPeriod()));
                }
            }
        }

        return result;
    }

    @Override
    protected List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(CurriculumGroup parentGroup,
            ExecutionSemester executionSemester, int[] curricularYears) {
        final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();
        for (final CurriculumGroup curriculumGroup : parentGroup.getCurriculumGroupsToEnrolmentProcess()) {
            result.add(new ImprovementStudentCurriculumGroupBean(curriculumGroup, executionSemester));
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

}
