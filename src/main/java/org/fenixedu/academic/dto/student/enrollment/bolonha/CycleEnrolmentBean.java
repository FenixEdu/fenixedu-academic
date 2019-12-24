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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.bennu.core.security.Authenticate;

public class CycleEnrolmentBean implements Serializable {

    private static final long serialVersionUID = -7926077929745839701L;

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionInterval executionInterval;

    private CycleCourseGroup cycleCourseGroupToEnrol;

    private CycleType sourceCycleAffinity;

    private CycleType cycleTypeToEnrol;

    public CycleEnrolmentBean(final StudentCurricularPlan studentCurricularPlan, final ExecutionInterval executionInterval,
            final CycleType sourceCycleAffinity, final CycleType cycleTypeToEnrol) {
        setStudentCurricularPlan(studentCurricularPlan);
        setExecutionPeriod(executionInterval);
        setSourceCycleAffinity(sourceCycleAffinity);
        setCycleTypeToEnrol(cycleTypeToEnrol);
    }

    public CycleEnrolmentBean(final StudentCurricularPlan studentCurricularPlan, final ExecutionInterval executionInterval,
            final CycleCourseGroup cycleCourseGroup) {
        this(studentCurricularPlan, executionInterval, CycleType.FIRST_CYCLE, cycleCourseGroup.getCycleType());
        setCycleCourseGroupToEnrol(cycleCourseGroup);
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public ExecutionInterval getExecutionPeriod() {
        return this.executionInterval;
    }

    public void setExecutionPeriod(ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
    }

    public CycleCourseGroup getCycleCourseGroupToEnrol() {
        return this.cycleCourseGroupToEnrol;
    }

    public void setCycleCourseGroupToEnrol(CycleCourseGroup cycleCourseGroup) {
        this.cycleCourseGroupToEnrol = cycleCourseGroup;
    }

    public CycleType getSourceCycleAffinity() {
        return sourceCycleAffinity;
    }

    public void setSourceCycleAffinity(CycleType sourceCycleAffinity) {
        this.sourceCycleAffinity = sourceCycleAffinity;
    }

    public CycleType getCycleTypeToEnrol() {
        return cycleTypeToEnrol;
    }

    public void setCycleTypeToEnrol(CycleType cycleTypeToEnrol) {
        this.cycleTypeToEnrol = cycleTypeToEnrol;
    }

    public Collection<CycleCourseGroup> getCycleDestinationAffinities() {
        final Collection<CycleCourseGroup> affinities =
                getDegreeCurricularPlan().getDestinationAffinities(getSourceCycleAffinity());

        if (affinities.isEmpty()) {
            return Collections.emptyList();
        }

        if (!isStudent()) {
            return affinities;
        }

        final List<CycleCourseGroup> result = new ArrayList<CycleCourseGroup>();
        return result;
    }

    private boolean isStudent() {
        return RoleType.STUDENT.isMember(Authenticate.getUser().getPerson().getUser());
    }

    private DegreeCurricularPlan getDegreeCurricularPlan() {
        return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public CycleCurriculumGroup getSourceCycle() {
        return getStudentCurricularPlan().getCycle(getSourceCycleAffinity());
    }

}
