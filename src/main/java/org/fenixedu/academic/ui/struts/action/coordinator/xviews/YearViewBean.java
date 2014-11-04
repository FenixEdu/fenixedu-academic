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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.interfaces.HasDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionYear;

public class YearViewBean implements Serializable, HasExecutionYear, HasDegreeCurricularPlan {

    private DegreeCurricularPlan degreeCurricularPlan;
    private ExecutionYear executionYear;
    private Set<Enrolment> enrolments;

    private boolean hasMajorBranches;
    private Set<BranchCourseGroup> majorBranches;
    private Map<BranchCourseGroup, Inar> inarByMajorBranches;
    private Map<BranchCourseGroup, String> averageByMajorBranches;

    private boolean hasMinorBranches;
    private Set<BranchCourseGroup> minorBranches;
    private Map<BranchCourseGroup, Inar> inarByMinorBranches;
    private Map<BranchCourseGroup, String> averageByMinorBranches;

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

    public Set<BranchCourseGroup> getMajorBranches() {
        return majorBranches;
    }

    public void setMajorBranches(Set<BranchCourseGroup> majorBranches) {
        this.majorBranches = majorBranches;
    }

    public Map<BranchCourseGroup, Inar> getInarByMajorBranches() {
        return inarByMajorBranches;
    }

    public void setInarByMajorBranches(Map<BranchCourseGroup, Inar> inarByMajorBranches) {
        this.inarByMajorBranches = inarByMajorBranches;
    }

    public Map<BranchCourseGroup, String> getAverageByMajorBranches() {
        return averageByMajorBranches;
    }

    public void setAverageByMajorBranches(Map<BranchCourseGroup, String> averageByMajorBranches) {
        this.averageByMajorBranches = averageByMajorBranches;
    }

    public boolean isHasMinorBranches() {
        return hasMinorBranches;
    }

    public void setHasMinorBranches(boolean hasMinorBranches) {
        this.hasMinorBranches = hasMinorBranches;
    }

    public Set<BranchCourseGroup> getMinorBranches() {
        return minorBranches;
    }

    public void setMinorBranches(Set<BranchCourseGroup> minorBranches) {
        this.minorBranches = minorBranches;
    }

    public Map<BranchCourseGroup, Inar> getInarByMinorBranches() {
        return inarByMinorBranches;
    }

    public void setInarByMinorBranches(Map<BranchCourseGroup, Inar> inarByMinorBranches) {
        this.inarByMinorBranches = inarByMinorBranches;
    }

    public Map<BranchCourseGroup, String> getAverageByMinorBranches() {
        return averageByMinorBranches;
    }

    public void setAverageByMinorBranches(Map<BranchCourseGroup, String> averageByMinorBranches) {
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
