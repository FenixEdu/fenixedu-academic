package net.sourceforge.fenixedu.domain.degreeStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

abstract public class DegreeModule extends DegreeModule_Base {

    static final public Comparator<DegreeModule> COMPARATOR_BY_NAME = new Comparator<DegreeModule>() {

        @Override
        public int compare(DegreeModule o1, DegreeModule o2) {
            String name1;
            String name2;
            if (Language.getLocale().toString().equalsIgnoreCase("pt")) {
                name1 = o1.getName();
                name2 = o2.getName();
            } else {
                name1 = o1.getNameEn();
                name2 = o2.getNameEn();
            }

            final int c = Collator.getInstance().compare(name1, name2);
            return c == 0 ? COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    public static class ComparatorByMinEcts implements Comparator<DegreeModule> {

        private final ExecutionSemester executionSemester;

        public ComparatorByMinEcts(final ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;

        }

        @Override
        public int compare(DegreeModule leftDegreeModule, DegreeModule rightDegreeModule) {
            int comparationResult =
                    leftDegreeModule.getMinEctsCredits(this.executionSemester).compareTo(
                            rightDegreeModule.getMinEctsCredits(this.executionSemester));
            return (comparationResult == 0) ? leftDegreeModule.getExternalId().compareTo(rightDegreeModule.getExternalId()) : comparationResult;
        }

    }

    public DegreeModule() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
     * @return A string with one possible full name of this course group
     */
    protected void getOneFullName(final StringBuilder result, final ExecutionSemester executionSemester) {
        final String selfName = getNameI18N(executionSemester).getContent(Language.getUserLanguage());

        if (isRoot()) {
            result.append(selfName);
        } else {
            List<Context> parentContextsByExecutionPeriod = getParentContextsByExecutionSemester(executionSemester);
            if (parentContextsByExecutionPeriod.isEmpty()) {
                // if not existing, just return all (as previous implementation
                // of method
                parentContextsByExecutionPeriod = getParentContexts();
            }

            final CourseGroup parentCourseGroup = parentContextsByExecutionPeriod.get(0).getParentCourseGroup();

            parentCourseGroup.getOneFullName(result, executionSemester);
            result.append(FULL_NAME_SEPARATOR);
            result.append(selfName);
        }
    }

    private static final String FULL_NAME_SEPARATOR = " > ";

    public String getOneFullName(final ExecutionSemester executionSemester) {
        final StringBuilder result = new StringBuilder();
        getOneFullName(result, executionSemester);
        return result.toString();
    }

    public String getOneFullName() {
        return getOneFullName(null);
    }

    public String getOneFullNameI18N(final Language language) {
        return getOneFullNameI18N(null, language);
    }

    public String getOneFullNameI18N(final ExecutionSemester executionSemester, Language language) {
        final StringBuilder result = new StringBuilder();
        getOneFullNameI18N(result, executionSemester, language);
        return result.toString();
    }

    protected void getOneFullNameI18N(final StringBuilder result, final ExecutionSemester executionSemester,
            final Language language) {
        final String selfName = getNameI18N(executionSemester).getContent(language);

        if (isRoot()) {
            result.append(selfName);
        } else {
            List<Context> parentContextsByExecutionPeriod = getParentContextsByExecutionSemester(executionSemester);
            if (parentContextsByExecutionPeriod.isEmpty()) {
                // if not existing, just return all (as previous implementation
                // of method
                parentContextsByExecutionPeriod = getParentContexts();
            }

            final CourseGroup parentCourseGroup = parentContextsByExecutionPeriod.get(0).getParentCourseGroup();

            parentCourseGroup.getOneFullNameI18N(result, executionSemester, language);
            result.append(FULL_NAME_SEPARATOR);
            result.append(selfName);
        }
    }

    public MultiLanguageString getNameI18N(final ExecutionSemester executionSemester) {
        MultiLanguageString multiLanguageString = new MultiLanguageString();

        String name = getName(executionSemester);
        if (name != null && name.length() > 0) {
            multiLanguageString = multiLanguageString.with(Language.pt, name);
        }

        String nameEn = getNameEn(executionSemester);
        if (nameEn != null && nameEn.length() > 0) {
            multiLanguageString = multiLanguageString.with(Language.en, nameEn);
        }

        return multiLanguageString;
    }

    public MultiLanguageString getNameI18N(final ExecutionYear executionYear) {
        return getNameI18N((executionYear == null) ? null : executionYear.getLastExecutionPeriod());
    }

    public MultiLanguageString getNameI18N() {
        return getNameI18N((ExecutionSemester) null);
    }

    protected String getName(final ExecutionSemester executionSemester) {
        return getName();
    }

    protected String getNameEn(final ExecutionSemester executionSemester) {
        return getNameEn();
    }

    public void delete() {
        if (getCanBeDeleted()) {
            for (; !getParentContexts().isEmpty(); getParentContexts().get(0).delete()) {
                ;
            }
            for (; !getCurricularRules().isEmpty(); getCurricularRules().get(0).delete()) {
                ;
            }
            for (; !getParticipatingPrecedenceCurricularRules().isEmpty(); getParticipatingPrecedenceCurricularRules().get(0)
                    .delete()) {
                ;
            }
            for (; !getParticipatingExclusivenessCurricularRules().isEmpty(); getParticipatingExclusivenessCurricularRules().get(
                    0).delete()) {
                ;
            }
        } else {
            throw new DomainException("courseGroup.notEmptyCurriculumModules");
        }
    }

    protected Boolean getCanBeDeleted() {
        return !hasAnyCurriculumModules();
    }

    public void deleteContext(Context context) {
        if (hasParentContexts(context)) {
            context.delete();
        }
        if (!hasAnyParentContexts()) {
            delete();
        }
    }

    public Set<CourseGroup> getAllParentCourseGroups() {
        Set<CourseGroup> result = new HashSet<CourseGroup>();
        collectParentCourseGroups(result, this);
        return result;
    }

    private void collectParentCourseGroups(Set<CourseGroup> result, DegreeModule module) {
        for (Context parent : module.getParentContexts()) {
            if (!parent.getParentCourseGroup().isRoot()) {
                result.add(parent.getParentCourseGroup());
                collectParentCourseGroups(result, parent.getParentCourseGroup());
            }
        }
    }

    public List<CurricularRule> getParticipatingCurricularRules() {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        result.addAll(getParticipatingPrecedenceCurricularRules());
        result.addAll(getParticipatingExclusivenessCurricularRules());
        return result;
    }

    public List<CurricularRule> getCurricularRules(final ExecutionYear executionYear) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : this.getCurricularRules()) {
            if (isCurricularRuleValid(curricularRule, executionYear)) {
                result.add(curricularRule);
            }
        }

        return result;
    }

    public List<CurricularRule> getCurricularRules(final ExecutionSemester executionSemester) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : this.getCurricularRules()) {
            if (isCurricularRuleValid(curricularRule, executionSemester)) {
                result.add(curricularRule);
            }
        }

        return result;
    }

    public List<CurricularRule> getVisibleCurricularRules(final ExecutionYear executionYear) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : this.getCurricularRules()) {
            if (curricularRule.isVisible() && (executionYear == null || curricularRule.isValid(executionYear))) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    public List<CurricularRule> getVisibleCurricularRules(final ExecutionSemester executionSemester) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : this.getCurricularRules()) {
            if (curricularRule.isVisible() && isCurricularRuleValid(curricularRule, executionSemester)) {
                result.add(curricularRule);
            }
        }

        return result;
    }

    public List<CurricularRule> getCurricularRules(final Context context, final ExecutionSemester executionSemester) {
        final List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (final CurricularRule curricularRule : getCurricularRules()) {
            if (isCurricularRuleValid(curricularRule, executionSemester) && curricularRule.appliesToContext(context)) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    private boolean isCurricularRuleValid(final ICurricularRule curricularRule, final ExecutionSemester executionSemester) {
        return executionSemester == null || curricularRule.isValid(executionSemester);
    }

    private boolean isCurricularRuleValid(final ICurricularRule curricularRule, final ExecutionYear executionYear) {
        return executionYear == null || curricularRule.isValid(executionYear);
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

    public List<Context> getParentContextsByExecutionSemester(final ExecutionSemester executionSemester) {
        final List<Context> result = new ArrayList<Context>();
        for (final Context context : getParentContextsSet()) {
            if (executionSemester == null || context.isValid(executionSemester)) {
                result.add(context);
            }
        }
        return result;
    }

    public List<Context> getParentContextsBy(final ExecutionSemester executionSemester, final CourseGroup parentCourseGroup) {
        final List<Context> result = new ArrayList<Context>();
        for (final Context context : getParentContextsSet()) {
            if (context.isValid(executionSemester) && context.getParentCourseGroup() == parentCourseGroup) {
                result.add(context);
            }
        }

        return result;
    }

    public boolean hasAnyParentContexts(final ExecutionSemester executionSemester) {
        for (final Context context : getParentContextsSet()) {
            if (executionSemester == null || context.isValid(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyOpenParentContexts(final ExecutionSemester executionSemester) {
        for (final Context context : getParentContextsSet()) {
            if (executionSemester == null || context.isOpen(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    private Boolean bolonhaDegree = null;

    public boolean isBolonhaDegree() {
        if (bolonhaDegree == null) {
            bolonhaDegree = Boolean.valueOf(getParentDegreeCurricularPlan().isBolonhaDegree());
        }
        return bolonhaDegree.booleanValue();
    }

    public boolean isOptional() {
        return false;
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

    public boolean hasOnlyOneParentCourseGroup(final ExecutionSemester executionSemester) {
        DegreeModule degreeModule = null;
        for (final Context context : getParentContextsByExecutionSemester(executionSemester)) {
            if (degreeModule == null) {
                degreeModule = context.getParentCourseGroup();

            } else if (degreeModule != context.getParentCourseGroup()) {
                return false;
            }
        }
        return true;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
            final ExecutionSemester executionSemester) {
        final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
        for (final ICurricularRule curricularRule : getCurricularRules()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionSemester)) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    public boolean hasAnyCurricularRules(final CurricularRuleType ruleType, final ExecutionSemester executionSemester) {
        for (final ICurricularRule curricularRule : getCurricularRules()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionSemester)) {
                return true;
            }
        }

        return false;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType, final ExecutionYear executionYear) {
        final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
        for (final ICurricularRule curricularRule : getCurricularRules()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionYear)) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
            final CourseGroup parentCourseGroup, final ExecutionYear executionYear) {

        final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
        for (final ICurricularRule curricularRule : getCurricularRules()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionYear)
                    && curricularRule.appliesToCourseGroup(parentCourseGroup)) {
                result.add(curricularRule);
            }
        }
        return result;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
            final CourseGroup parentCourseGroup, final ExecutionSemester executionSemester) {

        final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
        for (final ICurricularRule curricularRule : getCurricularRules()) {
            if (curricularRule.hasCurricularRuleType(ruleType) && isCurricularRuleValid(curricularRule, executionSemester)
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
            final CourseGroup parentCourseGroup, final ExecutionSemester executionSemester) {

        final List<ICurricularRule> curricularRules =
                new ArrayList<ICurricularRule>(getCurricularRules(ruleType, parentCourseGroup, executionSemester));

        if (curricularRules.isEmpty()) {
            return null;
        }

        ICurricularRule result = null;
        for (final ICurricularRule curricularRule : curricularRules) {
            if (curricularRule.isValid(executionSemester)) {
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
        return getMaxEctsCredits(ExecutionSemester.readActualExecutionSemester());
    }

    public Double getMinEctsCredits() {
        return getMinEctsCredits(ExecutionSemester.readActualExecutionSemester());
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
        return this.equals(degreeModule);
    }

    public ExecutionSemester getMinimumExecutionPeriod() {
        if (isRoot()) {
            return isBolonhaDegree() ? getBeginBolonhaExecutionPeriod() : getFirstExecutionPeriodOfFirstExecutionDegree();
        }
        final SortedSet<ExecutionSemester> executionSemesters = new TreeSet<ExecutionSemester>();
        for (final Context context : getParentContextsSet()) {
            executionSemesters.add(context.getBeginExecutionPeriod());
        }
        return executionSemesters.first();
    }

    public ExecutionSemester getBeginBolonhaExecutionPeriod() {
        return ExecutionSemester.readFirstBolonhaExecutionPeriod();
    }

    private ExecutionSemester getFirstExecutionPeriodOfFirstExecutionDegree() {
        final ExecutionDegree executionDegree = getParentDegreeCurricularPlan().getFirstExecutionDegree();
        return executionDegree != null ? executionDegree.getExecutionYear().getFirstExecutionPeriod() : getBeginBolonhaExecutionPeriod();
    }

    public DegreeModulesSelectionLimit getDegreeModulesSelectionLimitRule(final ExecutionSemester executionSemester) {
        final List<DegreeModulesSelectionLimit> result =
                (List<DegreeModulesSelectionLimit>) getCurricularRules(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT,
                        executionSemester);
        return result.isEmpty() ? null : (DegreeModulesSelectionLimit) result.get(0);
    }

    public CreditsLimit getCreditsLimitRule(final ExecutionSemester executionSemester) {
        final List<? extends ICurricularRule> result = getCurricularRules(CurricularRuleType.CREDITS_LIMIT, executionSemester);
        return result.isEmpty() ? null : (CreditsLimit) result.get(0);
    }

    public List<Exclusiveness> getExclusivenessRules(final ExecutionSemester executionSemester) {
        return (List<Exclusiveness>) getCurricularRules(CurricularRuleType.EXCLUSIVENESS, executionSemester);
    }

    public Set<EquivalencePlanEntry> getNewDegreeModuleEquivalencePlanEntries(final EquivalencePlan equivalencePlan) {
        final Set<EquivalencePlanEntry> equivalencePlanEntries =
                new TreeSet<EquivalencePlanEntry>(EquivalencePlanEntry.COMPARATOR);
        for (final EquivalencePlanEntry equivalencePlanEntry : getNewEquivalencePlanEntriesSet()) {
            if (equivalencePlanEntry.getEquivalencePlan() == equivalencePlan) {
                equivalencePlanEntries.add(equivalencePlanEntry);
            }
        }
        return equivalencePlanEntries;
    }

    public Collection<CycleCourseGroup> getParentCycleCourseGroups() {
        Collection<CycleCourseGroup> res = new HashSet<CycleCourseGroup>();
        for (CourseGroup courseGroup : getParentCourseGroups()) {
            res.addAll(courseGroup.getParentCycleCourseGroups());
        }
        return res;
    }

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

    abstract public Double getMaxEctsCredits(final ExecutionSemester executionSemester);

    abstract public Double getMinEctsCredits(final ExecutionSemester executionSemester);

    abstract public void getAllDegreeModules(final Collection<DegreeModule> degreeModules);

    abstract public Set<CurricularCourse> getAllCurricularCourses(final ExecutionSemester executionSemester);

    abstract public Set<CurricularCourse> getAllCurricularCourses();

    abstract public void doForAllCurricularCourses(final CurricularCourseFunctor curricularCourseFunctor);

    abstract public void applyToCurricularCourses(final ExecutionYear executionYear, final Predicate predicate);

}
