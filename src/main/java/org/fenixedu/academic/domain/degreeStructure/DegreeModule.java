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
package org.fenixedu.academic.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.DegreeModulesSelectionLimit;
import org.fenixedu.academic.domain.curricularRules.Exclusiveness;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

abstract public class DegreeModule extends DegreeModule_Base {

    static final public Comparator<DegreeModule> COMPARATOR_BY_NAME = Comparator
            .<DegreeModule, LocalizedString> comparing(DegreeModule::getNameI18N).thenComparing(DegreeModule::getExternalId);

    public static class ComparatorByMinEcts implements Comparator<DegreeModule> {

        private final ExecutionInterval executionInterval;

        public ComparatorByMinEcts(final ExecutionInterval executionSemester) {
            this.executionInterval = executionSemester;

        }

        @Override
        public int compare(DegreeModule leftDegreeModule, DegreeModule rightDegreeModule) {
            int comparationResult = leftDegreeModule.getMinEctsCredits(this.executionInterval)
                    .compareTo(rightDegreeModule.getMinEctsCredits(this.executionInterval));
            return (comparationResult == 0) ? leftDegreeModule.getExternalId()
                    .compareTo(rightDegreeModule.getExternalId()) : comparationResult;
        }

    }

    public DegreeModule() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    /**
     * We need a method to return a full name of a course group - from it's
     * parent course group to the degree curricular plan's root course group.
     * 
     * Given this is impossible, for there are many routes from the root course
     * group to one particular course group, we choose (for now...) to get one
     * possible full name, always visiting the first element of every list of
     * contexts on our way to the root course group.
     * 
     * @param result builder for with one possible full name of this course group
     * @param executionInterval
     */
    protected void getOneFullName(final StringBuilder result, final ExecutionInterval executionInterval) {
        final String selfName = getNameI18N(executionInterval).getContent();

        if (isRoot()) {
            result.append(selfName);
        } else {
            Collection<Context> parentContextsByExecutionPeriod = getParentContextsByExecutionSemester(executionInterval);
            if (parentContextsByExecutionPeriod.isEmpty()) {
                // if not existing, just return all (as previous implementation
                // of method
                parentContextsByExecutionPeriod = getParentContextsSet();
            }

            final CourseGroup parentCourseGroup = parentContextsByExecutionPeriod.iterator().next().getParentCourseGroup();

            parentCourseGroup.getOneFullName(result, executionInterval);
            result.append(FULL_NAME_SEPARATOR);
            result.append(selfName);
        }
    }

    private static final String FULL_NAME_SEPARATOR = " > ";

    public String getOneFullName(final ExecutionInterval executionInterval) {
        final StringBuilder result = new StringBuilder();
        getOneFullName(result, executionInterval);
        return result.toString();
    }

    public String getOneFullName() {
        return getOneFullName(null);
    }

    public String getOneFullNameI18N(final Locale language) {
        return getOneFullNameI18N(null, language);
    }

    public String getOneFullNameI18N(final ExecutionSemester executionSemester, Locale language) {
        final StringBuilder result = new StringBuilder();
        getOneFullNameI18N(result, executionSemester, language);
        return result.toString();
    }

    protected void getOneFullNameI18N(final StringBuilder result, final ExecutionSemester executionSemester,
            final Locale language) {
        final String selfName = getNameI18N(executionSemester).getContent(language);

        if (isRoot()) {
            result.append(selfName);
        } else {
            Collection<Context> parentContextsByExecutionPeriod = getParentContextsByExecutionSemester(executionSemester);
            if (parentContextsByExecutionPeriod.isEmpty()) {
                // if not existing, just return all (as previous implementation
                // of method
                parentContextsByExecutionPeriod = getParentContextsSet();
            }

            final CourseGroup parentCourseGroup = parentContextsByExecutionPeriod.iterator().next().getParentCourseGroup();

            parentCourseGroup.getOneFullNameI18N(result, executionSemester, language);
            result.append(FULL_NAME_SEPARATOR);
            result.append(selfName);
        }
    }

    private LocalizedString getNameI18N(final ExecutionSemester executionSemester) {
        LocalizedString LocalizedString = new LocalizedString();

        String name = getName(executionSemester);
        if (name != null && name.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.PT, name);
        }

        String nameEn = getNameEn(executionSemester);
        if (nameEn != null && nameEn.length() > 0) {
            LocalizedString = LocalizedString.with(org.fenixedu.academic.util.LocaleUtils.EN, nameEn);
        }

        return LocalizedString;
    }

    private LocalizedString getNameI18N(final ExecutionYear executionYear) {
        return getNameI18N((executionYear == null) ? null : executionYear.getLastExecutionPeriod());
    }

    public LocalizedString getNameI18N() {
        return getNameI18N((ExecutionSemester) null);
    }

    protected String getName(final ExecutionInterval executionInterval) {
        return getName();
    }

    protected String getNameEn(final ExecutionInterval executionInterval) {
        return getNameEn();
    }

    public LocalizedString getNameI18N(ExecutionInterval executionInterval) {
        if (executionInterval instanceof ExecutionSemester) {
            return getNameI18N((ExecutionSemester) executionInterval);
        }
        if (executionInterval instanceof ExecutionYear) {
            return getNameI18N((ExecutionYear) executionInterval);
        }

        if (executionInterval == null) {
            return getNameI18N((ExecutionSemester) null);
        }

        throw new DomainException("error.DegreeModule.getNameI18N.does.not.support.provided.executionInterval.type",
                executionInterval.getClass().getName());
    }

    public void delete() {
        if (getCanBeDeleted()) {
            for (; !getParentContextsSet().isEmpty(); getParentContextsSet().iterator().next().delete()) {
                ;
            }
            for (; !getCurricularRulesSet().isEmpty(); getCurricularRulesSet().iterator().next().delete()) {
                ;
            }
            for (; !getParticipatingPrecedenceCurricularRulesSet().isEmpty(); getParticipatingPrecedenceCurricularRulesSet()
                    .iterator().next().delete()) {
                ;
            }
            for (; !getParticipatingExclusivenessCurricularRulesSet().isEmpty(); getParticipatingExclusivenessCurricularRulesSet()
                    .iterator().next().delete()) {
                ;
            }
        } else {
            throw new DomainException("courseGroup.notEmptyCurriculumModules");
        }
    }

    protected Boolean getCanBeDeleted() {
        return getCurriculumModulesSet().isEmpty();
    }

    public void deleteContext(Context context) {
        if (getParentContextsSet().contains(context)) {
            context.delete();
        }
        if (getParentContextsSet().isEmpty()) {
            delete();
        }
    }

    public Set<CourseGroup> getAllParentCourseGroups() {
        Set<CourseGroup> result = new HashSet<CourseGroup>();
        collectParentCourseGroups(result, this);
        return result;
    }

    private void collectParentCourseGroups(Set<CourseGroup> result, DegreeModule module) {
        for (Context parent : module.getParentContextsSet()) {
            if (!parent.getParentCourseGroup().isRoot()) {
                result.add(parent.getParentCourseGroup());
                collectParentCourseGroups(result, parent.getParentCourseGroup());
            }
        }
    }

    public List<CurricularRule> getParticipatingCurricularRules() {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        result.addAll(getParticipatingPrecedenceCurricularRulesSet());
        result.addAll(getParticipatingExclusivenessCurricularRulesSet());
        return result;
    }

    public List<CurricularRule> getCurricularRules(final ExecutionYear executionYear) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : this.getCurricularRulesSet()) {
            if (isCurricularRuleValid(curricularRule, executionYear)) {
                result.add(curricularRule);
            }
        }

        return result;
    }

    public List<CurricularRule> getCurricularRules(final ExecutionInterval executionInterval) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : this.getCurricularRulesSet()) {
            if (isCurricularRuleValid(curricularRule, executionInterval)) {
                result.add(curricularRule);
            }
        }

        return result;
    }

    public List<CurricularRule> getVisibleCurricularRules(final ExecutionYear executionYear) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : this.getCurricularRulesSet()) {
            if (curricularRule.isVisible() && (executionYear == null || curricularRule.isValid(executionYear))) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    public List<CurricularRule> getVisibleCurricularRules(final ExecutionInterval executionInterval) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : this.getCurricularRulesSet()) {
            if (curricularRule.isVisible() && isCurricularRuleValid(curricularRule, executionInterval)) {
                result.add(curricularRule);
            }
        }

        return result;
    }

    public List<CurricularRule> getCurricularRules(final Context context, final ExecutionInterval executionInterval) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : getCurricularRulesSet()) {
            if (isCurricularRuleValid(curricularRule, executionInterval) && curricularRule.appliesToContext(context)) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    private boolean isCurricularRuleValid(final ICurricularRule curricularRule, final ExecutionInterval executionInterval) {
        return executionInterval == null || curricularRule.isValid(executionInterval);
    }

    private boolean isCurricularRuleValid(final ICurricularRule curricularRule, final ExecutionYear executionYear) {
        return executionYear == null || curricularRule.isValid(executionYear);
    }

    public List<Context> getParentContexts(final ExecutionInterval interval) {
        return getParentContextsSet().stream().filter(c -> interval == null || c.isValid(interval.getAcademicInterval()))
                .collect(Collectors.toList());
    }

    public List<Context> getParentContextsByExecutionYear(final ExecutionYear executionYear) {
        final List<Context> result = new ArrayList<Context>();
        for (final Context context : getParentContextsSet()) {
            if (executionYear == null || context.isValid(executionYear)) {
                result.add(context);
            }
        }
        return result;
    }

    public List<Context> getParentContextsByExecutionSemester(final ExecutionInterval executionInterval) {
        final List<Context> result = new ArrayList<Context>();
        for (final Context context : getParentContextsSet()) {
            if (executionInterval == null || context.isValid(executionInterval)) {
                result.add(context);
            }
        }
        return result;
    }

    public List<Context> getParentContextsBy(final ExecutionInterval executionInterval, final CourseGroup parentCourseGroup) {
        final List<Context> result = new ArrayList<Context>();
        for (final Context context : getParentContextsSet()) {
            if (context.isValid(executionInterval) && context.getParentCourseGroup() == parentCourseGroup) {
                result.add(context);
            }
        }

        return result;
    }

    public boolean hasAnyParentContexts(final ExecutionInterval executionInterval) {
        for (final Context context : getParentContextsSet()) {
            if (executionInterval == null || context.isValid(executionInterval)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyOpenParentContexts(final ExecutionInterval executionInterval) {
        for (final Context context : getParentContextsSet()) {
            if (executionInterval == null || context.isOpen(executionInterval)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBolonhaDegree() {
        return getParentDegreeCurricularPlan().isBolonhaDegree();
    }

    public boolean isBranchCourseGroup() {
        return false;
    }

    public boolean isCycleCourseGroup() {
        return false;
    }

    public boolean isCurricularCourse() {
        return false;
    }

    public boolean isCourseGroup() {
        return false;
    }

    public Degree getDegree() {
        return getParentDegreeCurricularPlan().getDegree();
    }

    public DegreeType getDegreeType() {
        return getDegree().getDegreeType();
    }

    public boolean hasOnlyOneParentCourseGroup() {
        return hasOnlyOneParentCourseGroup(null);
    }

    public boolean hasOnlyOneParentCourseGroup(final ExecutionInterval executionInterval) {
        DegreeModule degreeModule = null;
        for (final Context context : getParentContextsByExecutionSemester(executionInterval)) {
            if (degreeModule == null) {
                degreeModule = context.getParentCourseGroup();

            } else if (degreeModule != context.getParentCourseGroup()) {
                return false;
            }
        }
        return true;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
            final ExecutionInterval executionInterval) {
        final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
        for (final ICurricularRule curricularRule : getCurricularRulesSet()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionInterval)) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    public boolean hasAnyCurricularRules(final CurricularRuleType ruleType, final ExecutionInterval executionInterval) {
        for (final ICurricularRule curricularRule : getCurricularRulesSet()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionInterval)) {
                return true;
            }
        }

        return false;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
            final ExecutionYear executionYear) {
        final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
        for (final ICurricularRule curricularRule : getCurricularRulesSet()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionYear)) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
            final CourseGroup parentCourseGroup, final ExecutionYear executionYear) {

        final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
        for (final ICurricularRule curricularRule : getCurricularRulesSet()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionYear)
                    && curricularRule.appliesToCourseGroup(parentCourseGroup)) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
            final CourseGroup parentCourseGroup, final ExecutionInterval executionInterval) {

        final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
        for (final ICurricularRule curricularRule : getCurricularRulesSet()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionInterval)
                    && curricularRule.appliesToCourseGroup(parentCourseGroup)) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType,
            final CourseGroup parentCourseGroup, final ExecutionYear executionYear) {
        final List<ICurricularRule> curricularRules =
                new ArrayList<ICurricularRule>(getCurricularRules(ruleType, parentCourseGroup, (ExecutionYear) null));
        Collections.sort(curricularRules, ICurricularRule.COMPARATOR_BY_BEGIN);

        if (curricularRules.isEmpty()) {
            return null;
        }

        if (executionYear == null) {
            final ListIterator<ICurricularRule> iter = curricularRules.listIterator(curricularRules.size());
            while (iter.hasPrevious()) {
                final ICurricularRule curricularRule = iter.previous();
                if (curricularRule.isActive()) {
                    return curricularRule;
                }
            }

            return null;
        }

        ICurricularRule result = null;
        for (final ICurricularRule curricularRule : curricularRules) {
            if (curricularRule.isValid(executionYear)) {
                if (result != null) {
                    // TODO: remove this throw when curricular rule ensures
                    // that it can be only one active for execution period
                    // and replace by: return curricularRule
                    throw new DomainException("error.degree.module.has.more.than.one.credits.limit.for.executionYear", getName());
                }
                result = curricularRule;
            }
        }

        return result;
    }

    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType,
            final CourseGroup parentCourseGroup, final ExecutionInterval executionInterval) {

        final List<ICurricularRule> curricularRules =
                new ArrayList<ICurricularRule>(getCurricularRules(ruleType, parentCourseGroup, executionInterval));

        if (curricularRules.isEmpty()) {
            return null;
        }

        ICurricularRule result = null;
        for (final ICurricularRule curricularRule : curricularRules) {
            if (curricularRule.isValid(executionInterval)) {
                if (result != null) {
                    // TODO: remove this throw when curricular rule ensures
                    // that it can be only one active for execution period
                    // and replace by: return curricularRule
                    throw new DomainException("error.degree.module.has.more.than.one.credits.limit.for.executionPeriod",
                            getName());
                }
                result = curricularRule;
            }
        }

        return result;
    }

    public Double getMaxEctsCredits() {
        return getMaxEctsCredits(ExecutionSemester.findCurrent(getDegree().getCalendar()));
    }

    public Double getMinEctsCredits() {
        return getMinEctsCredits(ExecutionSemester.findCurrent(getDegree().getCalendar()));
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
        return this.equals(degreeModule);
    }

    public ExecutionInterval getMinimumExecutionPeriod() {
        if (isRoot()) {
            return isBolonhaDegree() ? ExecutionSemester
                    .readFirstExecutionSemester() : getFirstExecutionPeriodOfFirstExecutionDegree();
        }
        final SortedSet<ExecutionInterval> executionIntervals = new TreeSet<ExecutionInterval>();
        for (final Context context : getParentContextsSet()) {
            executionIntervals.add(context.getBeginExecutionInterval());
        }
        return executionIntervals.first().convert(ExecutionSemester.class);
    }

    private ExecutionInterval getFirstExecutionPeriodOfFirstExecutionDegree() {
        final ExecutionDegree executionDegree = getParentDegreeCurricularPlan().getFirstExecutionDegree();
        return executionDegree != null ? executionDegree.getExecutionYear().getFirstExecutionPeriod() : ExecutionSemester
                .readFirstExecutionSemester();
    }

    public DegreeModulesSelectionLimit getDegreeModulesSelectionLimitRule(final ExecutionInterval executionInterval) {
        final List<DegreeModulesSelectionLimit> result =
                (List<DegreeModulesSelectionLimit>) getCurricularRules(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT,
                        executionInterval);
        return result.isEmpty() ? null : (DegreeModulesSelectionLimit) result.iterator().next();
    }

    public CreditsLimit getCreditsLimitRule(final ExecutionInterval executionInterval) {
        final List<? extends ICurricularRule> result = getCurricularRules(CurricularRuleType.CREDITS_LIMIT, executionInterval);
        return result.isEmpty() ? null : (CreditsLimit) result.iterator().next();
    }

    public List<Exclusiveness> getExclusivenessRules(final ExecutionInterval executionInterval) {
        return (List<Exclusiveness>) getCurricularRules(CurricularRuleType.EXCLUSIVENESS, executionInterval);
    }

    public Collection<CycleCourseGroup> getParentCycleCourseGroups() {
        final Collection<CycleCourseGroup> res = new HashSet<CycleCourseGroup>();
        getParentContextsSet().forEach(c -> res.addAll(c.getParentCourseGroup().getParentCycleCourseGroups()));
        return res;
    }

    /**
     * @deprecated use getParentContextsSet instead
     */
    @Deprecated
    public Set<CourseGroup> getParentCourseGroups() {
        Set<CourseGroup> res = new HashSet<CourseGroup>();
        for (Context context : getParentContextsSet()) {
            res.add(context.getParentCourseGroup());
        }
        return res;
    }

    public boolean isDissertation() {
        return false;
    }

    abstract public DegreeCurricularPlan getParentDegreeCurricularPlan();

    abstract public void print(StringBuilder stringBuffer, String tabs, Context previousContext);

    abstract public boolean isLeaf();

    abstract public boolean isRoot();

    abstract public Double getMaxEctsCredits(final ExecutionInterval executionInterval);

    abstract public Double getMinEctsCredits(final ExecutionInterval executionInterval);

    abstract public void getAllDegreeModules(final Collection<DegreeModule> degreeModules);

    abstract public Set<CurricularCourse> getAllCurricularCourses(final ExecutionInterval executionInterval);

    abstract public Set<CurricularCourse> getAllCurricularCourses();

    abstract public void applyToCurricularCourses(final ExecutionYear executionYear, final Predicate predicate);

}
