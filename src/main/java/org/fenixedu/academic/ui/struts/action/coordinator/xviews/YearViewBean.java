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
package org.fenixedu.academic.ui.struts.action.coordinator.xviews;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.BranchType;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.interfaces.HasDegreeCurricularPlan;
import org.fenixedu.academic.domain.interfaces.HasExecutionYear;

public class YearViewBean implements Serializable, HasExecutionYear, HasDegreeCurricularPlan {

    private DegreeCurricularPlan degreeCurricularPlan;
    private ExecutionYear executionYear;
    private Set<Enrolment> enrolments;

    private boolean hasMajorBranches;
    private Set<CourseGroup> majorBranches;
    private Map<CourseGroup, Inar> inarByMajorBranches;
    private Map<CourseGroup, String> averageByMajorBranches;

    private boolean hasMinorBranches;
    private Set<CourseGroup> minorBranches;
    private Map<CourseGroup, Inar> inarByMinorBranches;
    private Map<CourseGroup, String> averageByMinorBranches;

    private String resumedQUC;

    @Override
    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    @Override
    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public Set<Enrolment> getEnrolments() {
        return enrolments;
    }

    public void setEnrolments(Set<Enrolment> enrolments) {
        this.enrolments = enrolments;
    }

    public void setEnrolments() {
        this.enrolments = generateEnrolmentSample();
    }

    public boolean isHasMajorBranches() {
        return hasMajorBranches;
    }

    public void setHasMajorBranches(boolean hasMajorBranches) {
        this.hasMajorBranches = hasMajorBranches;
    }

    public Set<CourseGroup> getMajorBranches() {
        return majorBranches;
    }

    public void setMajorBranches(Set<CourseGroup> majorBranches) {
        this.majorBranches = majorBranches;
    }

    public Map<CourseGroup, Inar> getInarByMajorBranches() {
        return inarByMajorBranches;
    }

    public void setInarByMajorBranches(Map<CourseGroup, Inar> inarByMajorBranches) {
        this.inarByMajorBranches = inarByMajorBranches;
    }

    public Map<CourseGroup, String> getAverageByMajorBranches() {
        return averageByMajorBranches;
    }

    public void setAverageByMajorBranches(Map<CourseGroup, String> averageByMajorBranches) {
        this.averageByMajorBranches = averageByMajorBranches;
    }

    public boolean isHasMinorBranches() {
        return hasMinorBranches;
    }

    public void setHasMinorBranches(boolean hasMinorBranches) {
        this.hasMinorBranches = hasMinorBranches;
    }

    public Set<CourseGroup> getMinorBranches() {
        return minorBranches;
    }

    public void setMinorBranches(Set<CourseGroup> minorBranches) {
        this.minorBranches = minorBranches;
    }

    public Map<CourseGroup, Inar> getInarByMinorBranches() {
        return inarByMinorBranches;
    }

    public void setInarByMinorBranches(Map<CourseGroup, Inar> inarByMinorBranches) {
        this.inarByMinorBranches = inarByMinorBranches;
    }

    public Map<CourseGroup, String> getAverageByMinorBranches() {
        return averageByMinorBranches;
    }

    public void setAverageByMinorBranches(Map<CourseGroup, String> averageByMinorBranches) {
        this.averageByMinorBranches = averageByMinorBranches;
    }

    public String getResumedQUC() {
        return resumedQUC;
    }

    public void setResumedQUC(String resumedQUC) {
        this.resumedQUC = resumedQUC;
    }

    public YearViewBean() {
        super();
        this.hasMajorBranches = false;
        this.hasMinorBranches = false;
    }

    public YearViewBean(DegreeCurricularPlan degreeCurricularPlan) {
        this();
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    private Set<Enrolment> generateEnrolmentSample() {
        Set<Enrolment> enrolments = new HashSet<Enrolment>();
        for (StudentCurricularPlan scp : this.degreeCurricularPlan.getStudentCurricularPlansSet()) {
            for (Enrolment enrol : scp.getEnrolmentsSet()) {
                if (enrol.getExecutionPeriod().getExecutionYear() == this.executionYear
                        && enrol.getParentCycleCurriculumGroup() != null
                        && degreeCurricularPlan.getCycleCourseGroup(enrol.getParentCycleCurriculumGroup().getCycleType()) != null) {
                    enrolments.add(enrol);
                }
            }
        }
        return enrolments;
    }

    public boolean hasBranches() {
        return degreeCurricularPlan.hasBranches();
    }

    public boolean hasBranchesByType(BranchType branchType) {
        return degreeCurricularPlan.hasBranchesByType(branchType);
    }

}
