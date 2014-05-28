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
package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;

public class StudentCurriculumGroupBean extends StudentCurriculumModuleBean {

    private static final long serialVersionUID = 1L;

    static protected class ComparatorByCurriculumGroupOrder implements Comparator<StudentCurriculumGroupBean> {

        private ExecutionSemester executionSemester;

        public ComparatorByCurriculumGroupOrder(ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
        }

        @Override
        public int compare(StudentCurriculumGroupBean o1, StudentCurriculumGroupBean o2) {
            CurriculumGroup c1 = o1.getCurriculumModule();
            CurriculumGroup c2 = o2.getCurriculumModule();
            return c1.getChildOrder(executionSemester).compareTo(c2.getChildOrder(executionSemester));
        }

    }

    // Student Structure
    private List<StudentCurriculumEnrolmentBean> enrolledCurriculumCourses;

    private List<StudentCurriculumGroupBean> enrolledCurriculumGroups;

    // Curriculum Structure
    private List<IDegreeModuleToEvaluate> courseGroupsToEnrol;

    private List<IDegreeModuleToEvaluate> curricularCoursesToEnrol;

    public StudentCurriculumGroupBean(final CurriculumGroup curriculumGroup, final ExecutionSemester executionSemester,
            int[] curricularYears) {
        super(curriculumGroup);

        setCourseGroupsToEnrol(buildCourseGroupsToEnrol(curriculumGroup, executionSemester));

        if (curricularYears != null) {
            setCurricularCoursesToEnrol(buildCurricularCoursesToEnrol(curriculumGroup, executionSemester, curricularYears));
        } else {
            setCurricularCoursesToEnrol(buildCurricularCoursesToEnrol(curriculumGroup, executionSemester));
        }

        setEnrolledCurriculumGroups(buildCurriculumGroupsEnroled(curriculumGroup, executionSemester, curricularYears));
        setEnrolledCurriculumCourses(buildCurricularCoursesEnroled(curriculumGroup, executionSemester));
    }

    protected List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(CurriculumGroup parentGroup,
            ExecutionSemester executionSemester, int[] curricularYears) {
        final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();
        for (final CurriculumGroup curriculumGroup : parentGroup.getCurriculumGroupsToEnrolmentProcess()) {
            result.add(createEnroledCurriculumGroupBean(executionSemester, curricularYears, curriculumGroup));
        }

        return result;
    }

    protected StudentCurriculumGroupBean createEnroledCurriculumGroupBean(ExecutionSemester executionSemester,
            int[] curricularYears, final CurriculumGroup curriculumGroup) {
        return new StudentCurriculumGroupBean(curriculumGroup, executionSemester, curricularYears);
    }

    protected List<IDegreeModuleToEvaluate> buildCourseGroupsToEnrol(CurriculumGroup group, ExecutionSemester executionSemester) {
        final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
        final List<Context> courseGroupContextsToEnrol = group.getCourseGroupContextsToEnrol(executionSemester);

        for (final Context context : courseGroupContextsToEnrol) {
            result.add(new DegreeModuleToEnrol(group, context, executionSemester));

        }

        return result;
    }

    protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
            ExecutionSemester executionSemester, int[] curricularYears) {
        final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
        final List<Context> curricularCoursesToEnrol = group.getCurricularCourseContextsToEnrol(executionSemester);

        for (final Context context : curricularCoursesToEnrol) {
            // NOTE: Temporary solution until first degree completes
            final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
            for (final int curricularYear : curricularYears) {
                if (context.containsSemesterAndCurricularYear(executionSemester.getSemester(), curricularYear,
                        curricularCourse.getRegime())) {
                    result.add(new DegreeModuleToEnrol(group, context, executionSemester));
                    break;
                }
            }
        }

        return result;
    }

    protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
            ExecutionSemester executionSemester) {

        final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
        for (final Context context : group.getCurricularCourseContextsToEnrol(executionSemester)) {
            result.add(new DegreeModuleToEnrol(group, context, executionSemester));
        }

        return result;

    }

    protected List<StudentCurriculumEnrolmentBean> buildCurricularCoursesEnroled(CurriculumGroup group,
            ExecutionSemester executionSemester) {
        final List<StudentCurriculumEnrolmentBean> result = new ArrayList<StudentCurriculumEnrolmentBean>();

        for (final CurriculumLine curriculumLine : group.getCurriculumLines()) {
            if (curriculumLine.isEnrolment()) {
                Enrolment enrolment = (Enrolment) curriculumLine;
                if (enrolment.getExecutionPeriod().equals(executionSemester) && enrolment.isEnroled()) {
                    result.add(new StudentCurriculumEnrolmentBean((Enrolment) curriculumLine));
                }
            }
        }

        return result;
    }

    @Override
    public CurriculumGroup getCurriculumModule() {
        return (CurriculumGroup) super.getCurriculumModule();
    }

    public List<IDegreeModuleToEvaluate> getCourseGroupsToEnrol() {
        return courseGroupsToEnrol;
    }

    public List<IDegreeModuleToEvaluate> getCourseGroupsToEnrolSortedByContext() {
        final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>(courseGroupsToEnrol);
        Collections.sort(result, IDegreeModuleToEvaluate.COMPARATOR_BY_CONTEXT);

        return result;
    }

    public void setCourseGroupsToEnrol(List<IDegreeModuleToEvaluate> courseGroupsToEnrol) {
        this.courseGroupsToEnrol = courseGroupsToEnrol;
    }

    public List<IDegreeModuleToEvaluate> getSortedDegreeModulesToEvaluate() {
        final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>(curricularCoursesToEnrol);
        Collections.sort(result, IDegreeModuleToEvaluate.COMPARATOR_BY_CONTEXT);

        return result;
    }

    public List<IDegreeModuleToEvaluate> getCurricularCoursesToEnrol() {
        return curricularCoursesToEnrol;
    }

    public void setCurricularCoursesToEnrol(List<IDegreeModuleToEvaluate> curricularCoursesToEnrol) {
        this.curricularCoursesToEnrol = curricularCoursesToEnrol;
    }

    public List<StudentCurriculumGroupBean> getEnrolledCurriculumGroups() {
        return enrolledCurriculumGroups;
    }

    public List<StudentCurriculumGroupBean> getEnrolledCurriculumGroupsSortedByOrder(final ExecutionSemester executionSemester) {
        final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>(enrolledCurriculumGroups);
        Collections.sort(result, new ComparatorByCurriculumGroupOrder(executionSemester));

        return result;
    }

    public boolean isEnrolledInAnyCurriculumGroups() {
        return !enrolledCurriculumGroups.isEmpty();
    }

    public void setEnrolledCurriculumGroups(List<StudentCurriculumGroupBean> enrolledCurriculumGroups) {
        this.enrolledCurriculumGroups = enrolledCurriculumGroups;
    }

    public List<StudentCurriculumEnrolmentBean> getEnrolledCurriculumCourses() {
        return enrolledCurriculumCourses;
    }

    public boolean isEnrolledInAnyCurriculumCourses() {
        return !enrolledCurriculumCourses.isEmpty();
    }

    public void setEnrolledCurriculumCourses(List<StudentCurriculumEnrolmentBean> enrolledCurriculumCourses) {
        this.enrolledCurriculumCourses = enrolledCurriculumCourses;
    }

    public boolean isRoot() {
        return getCurriculumModule().isRoot();
    }

    public boolean isNoCourseGroupCurriculumGroup() {
        return getCurriculumModule().isNoCourseGroupCurriculumGroup();
    }

    public boolean isToBeDisabled() {
        return isRoot() || isEnrolledInAnyCurriculumCourses() || isEnrolledInAnyCurriculumGroups()
                || isNoCourseGroupCurriculumGroup();
    }

}
