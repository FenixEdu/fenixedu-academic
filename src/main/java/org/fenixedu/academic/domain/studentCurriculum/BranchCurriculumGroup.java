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
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.degreeStructure.BranchCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.BranchType;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class BranchCurriculumGroup extends BranchCurriculumGroup_Base {

    protected BranchCurriculumGroup() {
        super();
    }

    public BranchCurriculumGroup(CurriculumGroup parentNode, BranchCourseGroup branch) {
        this();
        init(parentNode, branch);
    }

    public BranchCurriculumGroup(CurriculumGroup parentNode, BranchCourseGroup branch, ExecutionSemester executionSemester) {
        super();
        init(parentNode, branch, executionSemester);
    }

    @Override
    protected void checkInitConstraints(CurriculumGroup parent, CourseGroup courseGroup) {
        super.checkInitConstraints(parent, courseGroup);

        final BranchCourseGroup branchCourseGroup = (BranchCourseGroup) courseGroup;

        final CycleCurriculumGroup cycle = parent.getParentCycleCurriculumGroup();
        if (cycle != null && cycle.hasBranchCurriculumGroup(branchCourseGroup.getBranchType())) {
            throw new DomainException("error.BranchCurriculumGroup.parent.cycle.cannot.have.another.branch.with.same.type");
        }
    }

    @Override
    public boolean isBranchCurriculumGroup() {
        return true;
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
        if (degreeModule != null && !(degreeModule instanceof BranchCourseGroup)) {
            throw new DomainException("error.curriculumGroup.BranchParentDegreeModuleCanOnlyBeBranchCourseGroup");
        }
        super.setDegreeModule(degreeModule);
    }

    @Override
    public BranchCourseGroup getDegreeModule() {
        return (BranchCourseGroup) super.getDegreeModule();
    }

    public BranchType getBranchType() {
        return this.getDegreeModule().getBranchType();
    }

    @Override
    public Set<BranchCurriculumGroup> getBranchCurriculumGroups() {
        return Collections.singleton(this);
    }

    @Override
    public Set<BranchCurriculumGroup> getBranchCurriculumGroups(BranchType branchType) {
        return getBranchType() == branchType ? Collections.<BranchCurriculumGroup> singleton(this) : Collections
                .<BranchCurriculumGroup> emptySet();
    }

    @Override
    public BranchCurriculumGroup getParentBranchCurriculumGroup() {
        return this;
    }

    @Override
    public boolean hasBranchCurriculumGroup(final BranchType type) {
        return getBranchType() == type;
    }

    public boolean isMajor() {
        return getDegreeModule().isMajor();
    }

    public boolean isMinor() {
        return getDegreeModule().isMinor();
    }

}
