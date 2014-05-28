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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class NoCourseGroupCurriculumGroup extends NoCourseGroupCurriculumGroup_Base {

    protected NoCourseGroupCurriculumGroup() {
        super();
    }

    protected void init(final RootCurriculumGroup curriculumGroup) {
        checkParameters(curriculumGroup);
        this.setCurriculumGroup(curriculumGroup);
    }

    private void checkParameters(final RootCurriculumGroup curriculumGroup) {
        if (curriculumGroup == null) {
            throw new DomainException("error.NoCourseGroupCurriculumGroup.invalid.curriculumGroup");
        }
    }

    public static NoCourseGroupCurriculumGroup create(final NoCourseGroupCurriculumGroupType groupType,
            final RootCurriculumGroup curriculumGroup) {
        switch (groupType) {

        case PROPAEDEUTICS:
            return new PropaedeuticsCurriculumGroup(curriculumGroup);

        case EXTRA_CURRICULAR:
            return new ExtraCurriculumGroup(curriculumGroup);

        case STANDALONE:
            return new StandaloneCurriculumGroup(curriculumGroup);

        case INTERNAL_CREDITS_SOURCE_GROUP:
            return new InternalCreditsSourceCurriculumGroup(curriculumGroup);

        default:
            throw new DomainException("error.unknown.NoCourseGroupCurriculumGroupType");
        }
    }

    @Override
    public boolean isNoCourseGroupCurriculumGroup() {
        return true;
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString(MultiLanguageString.pt, getNoCourseGroupCurriculumGroupType().getLocalizedName(
                MultiLanguageString.pt)).with(MultiLanguageString.en,
                getNoCourseGroupCurriculumGroupType().getLocalizedName(MultiLanguageString.en));
    }

    @Override
    public List<Context> getCurricularCourseContextsToEnrol(ExecutionSemester executionSemester) {
        return Collections.emptyList();
    }

    @Override
    public List<Context> getCourseGroupContextsToEnrol(ExecutionSemester executionSemester) {
        return Collections.emptyList();
    }

    @Override
    public Collection<CurricularCourse> getCurricularCoursesToDismissal(final ExecutionSemester executionSemester) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasDegreeModule(DegreeModule degreeModule) {
        for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
            if (curriculumModule.hasDegreeModule(degreeModule)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCourseGroup(CourseGroup courseGroup) {
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (!curriculumModule.isLeaf()) {
                final CurriculumGroup group = (CurriculumGroup) curriculumModule;
                if (group.hasCourseGroup(courseGroup)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Flat structure below NoCourseGroupCurriculumGroup
     */
    @Override
    public CurriculumGroup findCurriculumGroupFor(final CourseGroup courseGroup) {
        for (final CurriculumModule each : getCurriculumModulesSet()) {
            if (!each.isLeaf() && each.getDegreeModule() == courseGroup) {
                return (CurriculumGroup) each;
            }
        }
        return null;
    }

    @Override
    public Integer getChildOrder(final ExecutionSemester executionSemester) {
        return Integer.MAX_VALUE;
    }

    @Override
    protected Integer searchChildOrderForChild(final CurriculumGroup child, final ExecutionSemester executionSemester) {
        final List<CurriculumModule> result = new ArrayList<CurriculumModule>(getCurriculumModulesSet());
        Collections.sort(result, CurriculumModule.COMPARATOR_BY_NAME_AND_ID);
        return result.indexOf(child);
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionSemester executionSemester) {
        return Collections.emptySet();
    }

    @Override
    public Set<ICurricularRule> getCurricularRules(ExecutionSemester executionSemester) {
        return Collections.emptySet();
    }

    @Override
    public ConclusionValue isConcluded(ExecutionYear executionYear) {
        return ConclusionValue.CONCLUDED;
    }

    @Override
    final public Curriculum getCurriculum(final DateTime when, final ExecutionYear executionYear) {
        return Curriculum.createEmpty(this, executionYear);
    }

    @Override
    public Double getCreditsConcluded(ExecutionYear executionYear) {
        return Double.valueOf(0d);
    }

    @Override
    public boolean canAdd(CurriculumLine curriculumLine) {
        return false;
    }

    @Override
    public Collection<CurriculumGroup> getCurricularCoursePossibleGroups(CurricularCourse curricularCourse) {
        return Collections.singleton((CurriculumGroup) this);
    }

    @Override
    public Collection<CurriculumGroup> getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(
            CurricularCourse curricularCourse) {
        return Collections.emptyList();
    }

    @Override
    public Double getAprovedEctsCredits() {
        return Double.valueOf(0d);
    }

    @Override
    public Collection<NoCourseGroupCurriculumGroup> getNoCourseGroupCurriculumGroups() {
        Collection<NoCourseGroupCurriculumGroup> res = new HashSet<NoCourseGroupCurriculumGroup>();
        res.add(this);
        res.addAll(super.getNoCourseGroupCurriculumGroups());
        return res;
    }

    @Override
    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType, final ExecutionYear executionYear) {
        return null;
    }

    abstract public NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType();

    @Override
    public int getNumberOfAllApprovedCurriculumLines() {
        return 0;
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(ExecutionSemester executionSemester) {
        return 0;
    }

    /**
     * Used to check if enrolment childs can give accumulated ects credits
     * correct value
     */
    public boolean allowAccumulatedEctsCredits() {
        return false;
    }

    @Override
    public Set<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        return Collections.emptySet();
    }

    public boolean isVisible() {
        return true;
    }
}
