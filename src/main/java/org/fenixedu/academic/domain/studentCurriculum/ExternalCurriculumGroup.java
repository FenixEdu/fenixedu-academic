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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.CreditsLimitInExternalCycle;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;

import java.util.Set;
import java.util.function.BiPredicate;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalCurriculumGroup extends ExternalCurriculumGroup_Base {

    public static BiPredicate<StudentCurricularPlan, CourseGroup> CAN_HAVE_EXTERNAL_GROUP = (scp, group) -> false;

    public ExternalCurriculumGroup() {
        super();
    }

    public ExternalCurriculumGroup(final RootCurriculumGroup rootCurriculumGroup, final CycleCourseGroup cycleCourseGroup) {
        this();
        init(rootCurriculumGroup, cycleCourseGroup);
    }

    public ExternalCurriculumGroup(final RootCurriculumGroup rootCurriculumGroup, final CycleCourseGroup cycleCourseGroup,
            final ExecutionSemester executionSemester) {
        this();
        init(rootCurriculumGroup, cycleCourseGroup, executionSemester);
    }

    @Override
    protected void checkInitConstraints(final CurriculumGroup parent, final CourseGroup courseGroup) {
        super.checkInitConstraints(parent, courseGroup);

        if (parent.getDegreeCurricularPlanOfStudent() == courseGroup.getParentDegreeCurricularPlan()) {
            throw new DomainException(
                    "error.studentCurriculum.CurriculumGroup.courseGroup.must.have.different.degreeCurricularPlan");
        }

        checkIfCycleCourseGroupIsInDestinationAffinitiesOfSource(parent.getStudentCurricularPlan(), courseGroup);

        if (!CAN_HAVE_EXTERNAL_GROUP.test(parent.getStudentCurricularPlan(), courseGroup)) {
            throw new DomainException("error.studentCurriculum.CurriculumGroup.courseGroup.not.allowed",
                    courseGroup.getName() + " - " +  courseGroup.getParentDegreeCurricularPlan().getName());
        }
    }

    private void checkIfCycleCourseGroupIsInDestinationAffinitiesOfSource(final StudentCurricularPlan studentCurricularPlan,
            final CourseGroup courseGroup) {

        final CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) courseGroup;
        final CycleCourseGroup sourceAffinityCycleCourseGroup =
                studentCurricularPlan.getDegreeCurricularPlan().getCycleCourseGroup(
                        cycleCourseGroup.getCycleType().getSourceCycleAffinity());

        if (!sourceAffinityCycleCourseGroup.getDestinationAffinitiesSet().contains(cycleCourseGroup)) {
            throw new DomainException(
                    "error.studentCurriculum.ExternalCurriculumGroup.cycle.course.group.does.not.belong.to.afinity.of.source");
        }
    }

    @Override
    public Integer getChildOrder() {
        return Integer.MAX_VALUE - 5;
    }

    @Override
    public Integer getChildOrder(ExecutionSemester executionSemester) {
        return getChildOrder();
    }

    @Override
    public LocalizedString getName() {
        LocalizedString LocalizedString = new LocalizedString();

        if (!StringUtils.isEmpty(getDegreeModule().getName())) {
            LocalizedString =
                    LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.PT, getDegreeModule().getName() + " ("
                            + getDegreeCurricularPlanOfDegreeModule().getName() + ")");
        }

        if (!StringUtils.isEmpty(getDegreeModule().getNameEn())) {
            LocalizedString =
                    LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.EN, getDegreeModule().getNameEn() + " ("
                            + getDegreeCurricularPlanOfDegreeModule().getName() + ")");
        }

        return LocalizedString;
    }

    @Override
    public Set<ICurricularRule> getCurricularRules(ExecutionSemester executionSemester) {
        final Set<ICurricularRule> result = super.getCurricularRules(executionSemester);
        result.add(new CreditsLimitInExternalCycle(getRootCurriculumGroup().getCycleCurriculumGroup(
                getCycleType().getSourceCycleAffinity()), this));

        return result;
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    @Override
    final public Curriculum getCurriculum(final DateTime when, final ExecutionYear executionYear) {
        return Curriculum.createEmpty(this, executionYear);
    }

    final public Curriculum getCurriculumInAdvance() {
        return getCurriculum(new DateTime());
    }

    final public Curriculum getCurriculumInAdvance(final DateTime when) {
        return super.getCurriculum(when, (ExecutionYear) null);
    }

    @Override
    public void conclude() {
        throw new DomainException("error.ExternalCurriculumGroup.cannot.conclude.external.curriculumGroups");
    }

    @Override
    public boolean isConclusionProcessed() {
        return false;
    }

}
