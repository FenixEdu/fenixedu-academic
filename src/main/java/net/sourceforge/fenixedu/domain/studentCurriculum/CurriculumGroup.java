package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.predicates.ResultCollection;

public class CurriculumGroup extends CurriculumGroup_Base {

    static final public Comparator<CurriculumGroup> COMPARATOR_BY_CHILD_ORDER_AND_ID = new Comparator<CurriculumGroup>() {
        @Override
        public int compare(CurriculumGroup o1, CurriculumGroup o2) {
            int result = o1.getChildOrder().compareTo(o2.getChildOrder());
            return (result != 0) ? result : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    protected CurriculumGroup() {
        super();
    }

    public CurriculumGroup(final CurriculumGroup curriculumGroup, final CourseGroup courseGroup) {
        this();
        init(curriculumGroup, courseGroup);
    }

    protected void init(final CurriculumGroup curriculumGroup, final CourseGroup courseGroup) {
        if (courseGroup == null || curriculumGroup == null) {
            throw new DomainException("error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
        }
        checkInitConstraints(curriculumGroup, courseGroup);
        setDegreeModule(courseGroup);
        setCurriculumGroup(curriculumGroup);
    }

    protected void checkInitConstraints(final CurriculumGroup parent, final CourseGroup courseGroup) {
        if (parent.getRootCurriculumGroup().hasCourseGroup(courseGroup)) {
            throw new DomainException("error.studentCurriculum.CurriculumGroup.duplicate.courseGroup", courseGroup.getName());
        }
    }

    protected void checkParameters(CourseGroup courseGroup, ExecutionSemester executionSemester) {
        if (courseGroup == null) {
            throw new DomainException("error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
        }
        if (executionSemester == null) {
            throw new DomainException("error.studentCurriculum.curriculumGroup.executionPeriod.cannot.be.null");
        }
    }

    public CurriculumGroup(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup, ExecutionSemester executionSemester) {
        super();
        init(parentCurriculumGroup, courseGroup, executionSemester);
    }

    protected void init(final CurriculumGroup curriculumGroup, final CourseGroup courseGroup,
            final ExecutionSemester executionSemester) {

        checkInitConstraints(curriculumGroup, courseGroup);
        checkParameters(curriculumGroup, courseGroup, executionSemester);

        setDegreeModule(courseGroup);
        setCurriculumGroup(curriculumGroup);
        addChildCurriculumGroups(courseGroup, executionSemester);
    }

    private void checkParameters(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup,
            ExecutionSemester executionSemester) {

        if (parentCurriculumGroup == null) {
            throw new DomainException("error.studentCurriculum.curriculumGroup.parentCurriculumGroup.cannot.be.null");
        }

        checkParameters(courseGroup, executionSemester);
    }

    protected void addChildCurriculumGroups(final CourseGroup courseGroup, final ExecutionSemester executionSemester) {
        for (final CourseGroup childCourseGroup : courseGroup.getNotOptionalChildCourseGroups(executionSemester)) {
            CurriculumGroupFactory.createGroup(this, childCourseGroup, executionSemester);
        }
    }

    @Override
    final public boolean isLeaf() {
        return false;
    }

    final public boolean canBeDeleted() {
        return !hasAnyCurriculumModules();
    }

    @Override
    public void delete() {
        if (canBeDeleted()) {
            super.delete();
        } else {
            throw new DomainException("error.studentCurriculum.CurriculumGroup.notEmptyCurriculumGroupModules", getName()
                    .getContent());
        }
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Override
    public void deleteRecursive() {
        for (final CurriculumModule child : getCurriculumModules()) {
            child.deleteRecursive();
        }

        delete();
    }

    @Override
    final public StringBuilder print(String tabs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tabs);
        builder.append("[CG ").append(getName().getContent()).append(" ]\n");
        final String tab = tabs + "\t";
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            builder.append(curriculumModule.print(tab));
        }
        return builder;
    }

    @Override
    public CourseGroup getDegreeModule() {
        return (CourseGroup) super.getDegreeModule();
    }

    @Override
    final public List<Enrolment> getEnrolments() {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getEnrolments());
        }
        return result;
    }

    final public Set<Enrolment> getEnrolmentsSet() {
        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getEnrolments());
        }
        return result;
    }

    @Override
    final public boolean hasAnyEnrolments() {
        return hasAnyCurriculumModules(new CurriculumModulePredicateByType(Enrolment.class));
    }

    @Override
    public boolean hasAnyCurriculumModules(final Predicate<CurriculumModule> predicate) {
        if (super.hasAnyCurriculumModules(predicate)) {
            return true;
        }

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.hasAnyCurriculumModules(predicate)) {
                return true;
            }
        }

        return false;
    }

    @Override
    final public void collectDismissals(final List<Dismissal> result) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            curriculumModule.collectDismissals(result);
        }
    }

    public List<Dismissal> getChildDismissals() {
        final List<Dismissal> result = new ArrayList<Dismissal>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isDismissal()) {
                result.add((Dismissal) curriculumModule);
            }
        }
        return result;
    }

    public double getChildCreditsDismissalEcts() {
        double total = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCreditsDismissal()) {
                total += curriculumModule.getEctsCredits();
            }
        }

        return total;
    }

    public List<Enrolment> getChildEnrolments() {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                result.add((Enrolment) curriculumModule);
            }
        }

        return result;
    }

    public List<CurriculumLine> getChildCurriculumLines() {
        final List<CurriculumLine> result = new ArrayList<CurriculumLine>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isLeaf()) {
                result.add((CurriculumLine) curriculumModule);
            }
        }

        return result;
    }

    public List<CurriculumGroup> getChildCurriculumGroups() {
        final List<CurriculumGroup> result = new ArrayList<CurriculumGroup>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                result.add((CurriculumGroup) curriculumModule);
            }
        }

        return result;
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return getCurriculumGroup().getStudentCurricularPlan();
    }

    private Collection<Context> getDegreeModulesFor(ExecutionSemester executionSemester) {
        return this.getDegreeModule().getValidChildContexts(executionSemester);
    }

    public List<Context> getCurricularCourseContextsToEnrol(ExecutionSemester executionSemester) {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getDegreeModulesFor(executionSemester)) {
            if (context.getChildDegreeModule().isLeaf()) {
                CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
                if (!this.getStudentCurricularPlan().isApproved(curricularCourse, executionSemester)
                        && !this.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionSemester)) {
                    result.add(context);
                }
            }
        }
        return result;
    }

    public List<Context> getCourseGroupContextsToEnrol(ExecutionSemester executionSemester) {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getDegreeModulesFor(executionSemester)) {
            if (!context.getChildDegreeModule().isLeaf()) {
                if (!this.getStudentCurricularPlan().getRoot().hasDegreeModule(context.getChildDegreeModule())) {
                    result.add(context);
                }
            }
        }
        return result;
    }

    public Collection<CurricularCourse> getCurricularCoursesToDismissal(final ExecutionSemester executionSemester) {
        final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
        for (final Context context : getDegreeModule().getOpenChildContexts(CurricularCourse.class, executionSemester)) {
            final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
            if (!getStudentCurricularPlan().getRoot().isApproved(curricularCourse, null)) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    @Override
    final public boolean isApproved(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
        for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
            if (curriculumModule.isApproved(curricularCourse, executionSemester)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
        for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
            if (curriculumModule.isEnroledInExecutionPeriod(curricularCourse, executionSemester)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
            if (curriculumModule.hasEnrolmentWithEnroledState(curricularCourse, executionSemester)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public ExecutionYear getIEnrolmentsLastExecutionYear() {
        ExecutionYear result = null;

        for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
            final ExecutionYear lastExecutionYear = curriculumModule.getIEnrolmentsLastExecutionYear();
            if (result == null || result.isBefore(lastExecutionYear)) {
                result = lastExecutionYear;
            }
        }

        return result;
    }

    @Override
    public boolean hasDegreeModule(DegreeModule degreeModule) {
        if (super.hasDegreeModule(degreeModule)) {
            return true;
        } else {
            for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
                if (curriculumModule.hasDegreeModule(degreeModule)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    final public boolean hasCurriculumModule(CurriculumModule curriculumModule) {
        if (super.hasCurriculumModule(curriculumModule)) {
            return true;
        }
        for (final CurriculumModule module : getCurriculumModulesSet()) {
            if (module.hasCurriculumModule(curriculumModule)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            final Enrolment search = curriculumModule.findEnrolmentFor(curricularCourse, executionSemester);
            if (search != null) {
                return search;
            }
        }
        return null;
    }

    @Override
    final public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            final Enrolment enrolment = curriculumModule.getApprovedEnrolment(curricularCourse);
            if (enrolment != null) {
                return enrolment;
            }
        }
        return null;
    }

    @Override
    final public Dismissal getDismissal(final CurricularCourse curricularCourse) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            final Dismissal dismissal = curriculumModule.getDismissal(curricularCourse);
            if (dismissal != null) {
                return dismissal;
            }
        }
        return null;
    }

    @Override
    final public CurriculumLine getApprovedCurriculumLine(final CurricularCourse curricularCourse) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            final CurriculumLine curriculumLine = curriculumModule.getApprovedCurriculumLine(curricularCourse);
            if (curriculumLine != null) {
                return curriculumLine;
            }
        }
        return null;
    }

    public CurriculumGroup findCurriculumGroupFor(final CourseGroup courseGroup) {
        if (getDegreeModule() == courseGroup) {
            return this;
        }
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                final CurriculumGroup searchCurriculumGroup = curriculumGroup.findCurriculumGroupFor(courseGroup);
                if (searchCurriculumGroup != null) {
                    return searchCurriculumGroup;
                }
            }
        }
        return null;
    }

    @Override
    public void getCurriculumModules(final ResultCollection<CurriculumModule> collection) {
        collection.condicionalAdd(this);
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            curriculumModule.getCurriculumModules(collection);
        }
    }

    final public Set<CurriculumLine> getCurriculumLines() {
        Set<CurriculumLine> result = new TreeSet<CurriculumLine>(CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (curriculumModule.isLeaf()) {
                result.add((CurriculumLine) curriculumModule);
            }
        }

        return result;
    }

    final public boolean hasCurriculumLines() {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isLeaf()) {
                return true;
            }
        }

        return false;
    }

    @Override
    final public void addApprovedCurriculumLines(final Collection<CurriculumLine> result) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            curriculumModule.addApprovedCurriculumLines(result);
        }
    }

    @Override
    final public boolean hasAnyApprovedCurriculumLines() {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(CurriculumLine.class));
        andPredicate.add(new CurriculumModulePredicateByApproval());

        return hasAnyCurriculumModules(andPredicate);
    }

    final public Set<CurriculumGroup> getCurriculumGroups() {
        Set<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (!curriculumModule.isLeaf()) {
                result.add((CurriculumGroup) curriculumModule);
            }
        }

        return result;
    }

    public Set<CurriculumGroup> getCurriculumGroupsToEnrolmentProcess() {
        final Set<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (!curriculumModule.isLeaf() && !curriculumModule.isNoCourseGroupCurriculumGroup()) {
                result.add((CurriculumGroup) curriculumModule);
            }
        }

        return result;
    }

    public Set<BranchCurriculumGroup> getBranchCurriculumGroups() {
        final Set<BranchCurriculumGroup> result = new HashSet<BranchCurriculumGroup>(1);

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (curriculumModule instanceof CurriculumGroup) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                result.addAll(curriculumGroup.getBranchCurriculumGroups());
            }
        }

        return result;
    }

    public Set<BranchCurriculumGroup> getBranchCurriculumGroups(final BranchType branchType) {
        final Set<BranchCurriculumGroup> result = new HashSet<BranchCurriculumGroup>(1);

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (curriculumModule instanceof CurriculumGroup) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                result.addAll(curriculumGroup.getBranchCurriculumGroups(branchType));
            }
        }

        return result;
    }

    public boolean hasBranchCurriculumGroup(final BranchType type) {
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (curriculumModule instanceof CurriculumGroup) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                if (curriculumGroup.hasBranchCurriculumGroup(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<BranchCurriculumGroup> getMajorBranchCurriculumGroups() {
        return getBranchCurriculumGroups(BranchType.MAJOR);
    }

    public Set<BranchCurriculumGroup> getMinorBranchCurriculumGroups() {
        return getBranchCurriculumGroups(BranchType.MINOR);
    }

    public Set<BranchCourseGroup> getBranchCourseGroups(BranchType branchType) {
        final Set<BranchCourseGroup> result = new HashSet<BranchCourseGroup>();
        for (final BranchCurriculumGroup group : getBranchCurriculumGroups(branchType)) {
            result.add(group.getDegreeModule());
        }
        return result;
    }

    public Set<BranchCourseGroup> getMajorBranchCourseGroups() {
        return getBranchCourseGroups(BranchType.MAJOR);
    }

    public Set<BranchCourseGroup> getMinorBranchCourseGroups() {
        return getBranchCourseGroups(BranchType.MINOR);
    }

    @Override
    public Set<CurriculumGroup> getAllCurriculumGroups() {
        Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();
        result.add(this);

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            result.addAll(curriculumModule.getAllCurriculumGroups());
        }
        return result;
    }

    @Override
    public Set<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();
        result.add(this);

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            result.addAll(curriculumModule.getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups());
        }
        return result;
    }

    @Override
    public Set<CurriculumLine> getAllCurriculumLines() {
        Set<CurriculumLine> result = new HashSet<CurriculumLine>();
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            result.addAll(curriculumModule.getAllCurriculumLines());
        }
        return result;
    }

    public Integer getChildOrder() {
        return getChildOrder(null);
    }

    public Integer getChildOrder(final ExecutionSemester executionSemester) {
        final Integer childOrder = getParentCurriculumGroup().searchChildOrderForChild(this, executionSemester);
        return childOrder != null ? childOrder : Integer.MAX_VALUE;
    }

    private CurriculumGroup getParentCurriculumGroup() {
        return getCurriculumGroup();
    }

    protected Integer searchChildOrderForChild(final CurriculumGroup child, final ExecutionSemester executionSemester) {
        for (final Context context : getDegreeModule().getValidChildContexts(executionSemester)) {
            if (context.getChildDegreeModule() == child.getDegreeModule()) {
                return context.getChildOrder();
            }
        }
        return null;
    }

    public boolean hasCourseGroup(final CourseGroup courseGroup) {
        if (getDegreeModule().equals(courseGroup)) {
            return true;
        }

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (!curriculumModule.isLeaf()) {
                CurriculumGroup group = (CurriculumGroup) curriculumModule;
                if (group.hasCourseGroup(courseGroup)) {
                    return true;
                }
            }
        }

        return false;
    }

    final public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType groupType) {
        for (final CurriculumGroup curriculumGroup : getCurriculumGroups()) {
            if (curriculumGroup.isNoCourseGroupCurriculumGroup()) {
                NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup = (NoCourseGroupCurriculumGroup) curriculumGroup;
                if (noCourseGroupCurriculumGroup.getNoCourseGroupCurriculumGroupType().equals(groupType)) {
                    return noCourseGroupCurriculumGroup;
                }
            }
        }

        return null;
    }

    @Override
    final public Double getEctsCredits() {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getEctsCredits()));
        }
        return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    public Double getAprovedEctsCredits() {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getAprovedEctsCredits()));
        }
        return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionSemester executionSemester) {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getEnroledEctsCredits(executionSemester)));
        }
        return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double getCreditsConcluded(ExecutionYear executionYear) {
        final CreditsLimit creditsLimit =
                (CreditsLimit) getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, executionYear);

        Double creditsConcluded = 0d;
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            creditsConcluded += curriculumModule.getCreditsConcluded(executionYear);
        }

        if (creditsLimit == null) {
            return creditsConcluded;
        } else {
            return Math.min(creditsLimit.getMaximumCredits(), creditsConcluded);
        }
    }

    final public int getNumberOfChildCurriculumGroupsWithCourseGroup() {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                if (!curriculumGroup.isNoCourseGroupCurriculumGroup()) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * This method returns the number of approved child CurriculumLines
     */
    final public int getNumberOfApprovedChildCurriculumLines() {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.isDismissal() && curriculumLine.hasCurricularCourse()) {
                    result++;
                } else if (curriculumLine.isEnrolment() && ((Enrolment) curriculumLine).isApproved()) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * This method makes a deep search to count number of all enrolled
     * CurriculumLines (except NoCourseGroupCurriculumGroups)
     */
    public int getNumberOfAllEnroledCurriculumLines() {
        int result = 0;
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.isEnrolment()) {
                    result++;
                }
            } else {
                result += ((CurriculumGroup) curriculumModule).getNumberOfAllEnroledCurriculumLines();
            }
        }
        return result;
    }

    /**
     * This method makes a deep search to count number of all approved
     * CurriculumLines (except NoCourseGroupCurriculumGroups)
     */
    public int getNumberOfAllApprovedCurriculumLines() {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.isDismissal() && curriculumLine.hasCurricularCourse()) {
                    result++;
                } else if (curriculumLine.isEnrolment() && ((Enrolment) curriculumLine).isApproved()) {
                    result++;
                }
            } else {
                result += ((CurriculumGroup) curriculumModule).getNumberOfAllApprovedCurriculumLines();
            }
        }
        return result;
    }

    final public int getNumberOfChildEnrolments(final ExecutionSemester executionSemester) {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule instanceof Enrolment) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.isValid(executionSemester) && enrolment.isEnroled()) {
                    result++;
                }
            }
        }
        return result;
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(final ExecutionSemester executionSemester) {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result += curriculumModule.getNumberOfAllApprovedEnrolments(executionSemester);
        }
        return result;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionSemester executionSemester) {
        final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>();
        result.add(new EnroledCurriculumModuleWrapper(this, executionSemester));

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            result.addAll(curriculumModule.getDegreeModulesToEvaluate(executionSemester));
        }
        return result;
    }

    @Override
    public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionYear executionYear) {
        final Collection<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getSpecialSeasonEnrolments(executionYear));
        }
        return result;
    }

    @Override
    public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionSemester executionSemester) {
        final Collection<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getSpecialSeasonEnrolments(executionSemester));
        }
        return result;
    }

    @Override
    final public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
        degreeModules.add(getDegreeModule());
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            curriculumModule.getAllDegreeModules(degreeModules);
        }
    }

    @Override
    public ConclusionValue isConcluded(final ExecutionYear executionYear) {
        if (isToCheckCreditsLimits(executionYear)) {
            return ConclusionValue.create(checkCreditsLimits(executionYear));
        } else if (isToCheckDegreeModulesSelectionLimit(executionYear)) {
            return ConclusionValue.create(checkDegreeModulesSelectionLimit(executionYear));
        } else {
            return ConclusionValue.UNKNOWN;
        }
    }

    public void assertCorrectStructure(final Collection<CurriculumGroup> result) {
        for (final CurriculumGroup curriculumGroup : getCurriculumGroups()) {
            if (curriculumGroup.getCurriculumGroups().isEmpty() && curriculumGroup.hasUnexpectedCredits()) {
                result.add(curriculumGroup);
            } else {
                curriculumGroup.assertCorrectStructure(result);
            }
        }
    }

    public boolean hasUnexpectedCredits() {
        return getAprovedEctsCredits().doubleValue() != getCreditsConcluded().doubleValue();
    }

    public boolean hasInsufficientCredits() {
        return getAprovedEctsCredits().doubleValue() < getCreditsConcluded().doubleValue();
    }

    private boolean isToCheckDegreeModulesSelectionLimit(ExecutionYear executionYear) {
        return getMostRecentActiveCurricularRule(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, executionYear) != null;
    }

    private boolean isToCheckCreditsLimits(ExecutionYear executionYear) {
        return getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, executionYear) != null;
    }

    @SuppressWarnings("unchecked")
    private boolean checkCreditsLimits(ExecutionYear executionYear) {
        final CreditsLimit creditsLimit =
                (CreditsLimit) getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, executionYear);

        if (creditsLimit == null) {
            return false;
        } else {
            Double creditsConcluded = 0d;
            for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
                if (curriculumModule.isConcluded(executionYear).isValid()) {
                    creditsConcluded += curriculumModule.getCreditsConcluded(executionYear);
                }
            }

            return creditsConcluded >= creditsLimit.getMinimumCredits();
        }
    }

    @SuppressWarnings("unchecked")
    private boolean checkDegreeModulesSelectionLimit(ExecutionYear executionYear) {
        final DegreeModulesSelectionLimit degreeModulesSelectionLimit =
                (DegreeModulesSelectionLimit) getMostRecentActiveCurricularRule(
                        CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, executionYear);

        if (degreeModulesSelectionLimit == null) {
            return false;
        } else {
            int modulesConcluded = 0;
            for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
                if (curriculumModule.isConcluded(executionYear).value()) {
                    modulesConcluded++;
                }
            }

            return modulesConcluded >= degreeModulesSelectionLimit.getMinimumLimit();
        }
    }

    @Override
    public boolean hasConcluded(final DegreeModule degreeModule, final ExecutionYear executionYear) {
        if (getDegreeModule() == degreeModule) {
            return isConcluded(executionYear).value();
        }

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.hasConcluded(degreeModule, executionYear)) {
                return true;
            }
        }

        return false;

    }

    @Override
    public YearMonthDay calculateConclusionDate() {
        final Collection<CurriculumModule> curriculumModules = new HashSet<CurriculumModule>(getCurriculumModulesSet());
        YearMonthDay result = null;
        for (final CurriculumModule curriculumModule : curriculumModules) {
            if (curriculumModule.isConcluded(getApprovedCurriculumLinesLastExecutionYear()).isValid()
                    && curriculumModule.hasAnyApprovedCurriculumLines()) {
                final YearMonthDay curriculumModuleConclusionDate = curriculumModule.calculateConclusionDate();
                if (curriculumModuleConclusionDate != null && (result == null || curriculumModuleConclusionDate.isAfter(result))) {
                    result = curriculumModuleConclusionDate;
                }
            }
        }

        return result;
    }

    public void assertConclusionDate(final Collection<CurriculumModule> result) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isConcluded(getApprovedCurriculumLinesLastExecutionYear()).isValid()
                    && curriculumModule.hasAnyApprovedCurriculumLines()) {
                final YearMonthDay curriculumModuleConclusionDate = curriculumModule.calculateConclusionDate();
                if (curriculumModuleConclusionDate == null) {
                    result.add(curriculumModule);
                }
            }
        }
    }

    @Override
    public Curriculum getCurriculum(final DateTime when, final ExecutionYear executionYear) {
        final Curriculum curriculum = Curriculum.createEmpty(this, executionYear);
        if (!wasCreated(when)) {
            return curriculum;
        }

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            curriculum.add(curriculumModule.getCurriculum(when, executionYear));
        }

        return curriculum;
    }

    @Override
    public boolean isPropaedeutic() {
        return hasCurriculumGroup() && getCurriculumGroup().isPropaedeutic();
    }

    public boolean isExtraCurriculum() {
        return false;
    }

    public boolean isStandalone() {
        return false;
    }

    public boolean canAdd(final CurriculumLine curriculumLine) {
        if (!curriculumLine.hasCurricularCourse() || !curriculumLine.isBolonhaDegree()) {
            return true;
        }

        if (curriculumLine.isEnrolment()) {
            if (curriculumLine instanceof OptionalEnrolment) {
                return getDegreeModule().hasDegreeModuleOnChilds(
                        ((OptionalEnrolment) curriculumLine).getOptionalCurricularCourse());
            }
        }

        return getDegreeModule().hasDegreeModuleOnChilds(curriculumLine.getCurricularCourse());
    }

    public Collection<CurriculumGroup> getCurricularCoursePossibleGroups(final CurricularCourse curricularCourse) {
        Collection<CurriculumGroup> result = new HashSet<CurriculumGroup>();
        if (getDegreeModule().hasDegreeModuleOnChilds(curricularCourse)) {
            result.add(this);
        }

        for (CurriculumGroup curriculumGroup : this.getCurriculumGroups()) {
            result.addAll(curriculumGroup.getCurricularCoursePossibleGroups(curricularCourse));
        }

        return result;
    }

    public Collection<CurriculumGroup> getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(
            final CurricularCourse curricularCourse) {
        Collection<CurriculumGroup> result = new HashSet<CurriculumGroup>();
        if (getDegreeModule().hasDegreeModuleOnChilds(curricularCourse)) {
            result.add(this);
        }

        for (CurriculumGroup curriculumGroup : this.getCurriculumGroups()) {
            result.addAll(curriculumGroup.getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(curricularCourse));
        }

        return result;
    }

    public Collection<NoCourseGroupCurriculumGroup> getNoCourseGroupCurriculumGroups() {
        Collection<NoCourseGroupCurriculumGroup> res = new HashSet<NoCourseGroupCurriculumGroup>();
        for (CurriculumGroup curriculumGroup : getCurriculumGroups()) {
            res.addAll(curriculumGroup.getNoCourseGroupCurriculumGroups());
        }
        return res;
    }

    public int getNoCourseGroupCurriculumGroupsCount() {
        return getNoCourseGroupCurriculumGroups().size();
    }

    public boolean hasChildDegreeModule(final DegreeModule degreeModule) {
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (curriculumModule.getDegreeModule() == degreeModule) {
                return true;
            }
        }

        return false;
    }

    public CurriculumModule getChildCurriculumModule(final DegreeModule degreeModule) {
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (curriculumModule.getDegreeModule() == degreeModule) {
                return curriculumModule;
            }
        }
        return null;
    }

    @Override
    public boolean hasEnrolment(ExecutionYear executionYear) {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(Enrolment.class));
        andPredicate.add(new CurriculumModulePredicateByExecutionYear(executionYear));

        return hasAnyCurriculumModules(andPredicate);
    }

    @Override
    public boolean hasEnrolment(ExecutionSemester executionSemester) {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(Enrolment.class));
        andPredicate.add(new CurriculumModulePredicateByExecutionSemester(executionSemester));

        return hasAnyCurriculumModules(andPredicate);
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionSemester executionSemester) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnroledInSpecialSeason(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionYear executionYear) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnroledInSpecialSeason(executionYear)) {
                return true;
            }
        }
        return false;
    }

    public Set<Enrolment> getEnrolmentsBy(final ExecutionYear executionYear) {
        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionYear() == executionYear) {
                result.add(enrolment);
            }
        }

        return result;
    }

    public Set<Enrolment> getEnrolmentsBy(final ExecutionSemester executionSemester) {
        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionPeriod() == executionSemester) {
                result.add(enrolment);
            }
        }

        return result;
    }

}
