package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RootCurriculumGroup extends RootCurriculumGroup_Base {

    public RootCurriculumGroup() {
	super();
	createExtraCurriculumGroup();
	createPropaedeuticsCurriculumGroup();
    }

    public RootCurriculumGroup(StudentCurricularPlan studentCurricularPlan, RootCourseGroup rootCourseGroup,
	    ExecutionSemester executionSemester, CycleType cycleType) {
	this();
	init(studentCurricularPlan, rootCourseGroup, executionSemester, cycleType);
    }

    private void init(StudentCurricularPlan studentCurricularPlan, RootCourseGroup courseGroup,
	    ExecutionSemester executionSemester, CycleType cycleType) {
	checkParameters(studentCurricularPlan, courseGroup, executionSemester);
	checkInitConstraints(studentCurricularPlan, courseGroup);

	setParentStudentCurricularPlan(studentCurricularPlan);
	setDegreeModule(courseGroup);
	addChildCurriculumGroups(courseGroup, executionSemester, cycleType);
    }

    private void checkParameters(final StudentCurricularPlan studentCurricularPlan, final RootCourseGroup courseGroup,
	    final ExecutionSemester executionSemester) {
	checkParameters(studentCurricularPlan, courseGroup);
	if (executionSemester == null) {
	    throw new DomainException("error.studentCurriculum.executionPeriod.cannot.be.null");
	}
    }

    public RootCurriculumGroup(StudentCurricularPlan studentCurricularPlan, RootCourseGroup rootCourseGroup, CycleType cycleType) {
	this();
	init(studentCurricularPlan, rootCourseGroup, cycleType);
    }

    private void init(final StudentCurricularPlan studentCurricularPlan, final RootCourseGroup rootCourseGroup,
	    final CycleType cycleType) {
	checkParameters(studentCurricularPlan, rootCourseGroup);
	checkInitConstraints(studentCurricularPlan, rootCourseGroup);

	setParentStudentCurricularPlan(studentCurricularPlan);
	setDegreeModule(rootCourseGroup);
	addChildCurriculumGroups(rootCourseGroup, cycleType);
    }

    private void checkParameters(final StudentCurricularPlan studentCurricularPlan, final RootCourseGroup rootCourseGroup) {
	if (studentCurricularPlan == null) {
	    throw new DomainException("error.studentCurriculum.studentCurricularPlan.cannot.be.null");
	}
	if (rootCourseGroup == null) {
	    throw new DomainException("error.studentCurriculum.rootCourseGroup.cannot.be.null");
	}
    }

    private void addChildCurriculumGroups(final RootCourseGroup rootCourseGroup, final ExecutionSemester executionSemester,
	    CycleType cycle) {
	if (rootCourseGroup.hasCycleGroups()) {
	    createCycle(rootCourseGroup, executionSemester, cycle);
	} else {
	    super.addChildCurriculumGroups(rootCourseGroup, executionSemester);
	}
    }

    private void addChildCurriculumGroups(final RootCourseGroup rootCourseGroup, CycleType cycle) {
	if (rootCourseGroup.hasCycleGroups()) {
	    createCycle(rootCourseGroup, null, cycle);
	}
    }

    private void createCycle(final RootCourseGroup rootCourseGroup, final ExecutionSemester executionSemester, CycleType cycle) {
	if (cycle == null) {
	    cycle = rootCourseGroup.getDegree().getDegreeType().getFirstCycleType();
	}
	if (cycle != null) {
	    if (executionSemester != null) {
		new CycleCurriculumGroup(this, rootCourseGroup.getCycleCourseGroup(cycle), executionSemester);
	    } else {
		new CycleCurriculumGroup(this, rootCourseGroup.getCycleCourseGroup(cycle));
	    }
	}
    }

    private void checkInitConstraints(final StudentCurricularPlan studentCurricularPlan, final RootCourseGroup rootCourseGroup) {
	if (studentCurricularPlan.getDegreeCurricularPlan() != rootCourseGroup.getParentDegreeCurricularPlan()) {
	    throw new DomainException("error.rootCurriculumGroup.scp.and.root.have.different.degreeCurricularPlan");
	}
    }

    public void setRootCourseGroup(final RootCourseGroup rootCourseGroup) {
	setDegreeModule(rootCourseGroup);
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
	if (degreeModule != null && !(degreeModule instanceof RootCourseGroup)) {
	    throw new DomainException("error.curriculumGroup.RootCurriculumGroup.degreeModuleMustBeRootCourseGroup");
	}
	super.setDegreeModule(degreeModule);
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	if (curriculumGroup != null) {
	    throw new DomainException("error.curriculumGroup.RootCurriculumGroupCannotHaveParent");
	}
    }

    @Override
    public boolean isRoot() {
	return true;
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
	return getParentStudentCurricularPlan();
    }

    private void createExtraCurriculumGroup() {
	NoCourseGroupCurriculumGroup.createNewNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR,
		this);
    }

    private void createPropaedeuticsCurriculumGroup() {
	NoCourseGroupCurriculumGroup.createNewNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.PROPAEDEUTICS, this);
    }

    public CycleCurriculumGroup getCycleCurriculumGroup(CycleType cycleType) {
	for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule.isCycleCurriculumGroup()) {
		CycleCurriculumGroup cycleCurriculumGroup = (CycleCurriculumGroup) curriculumModule;
		if (cycleCurriculumGroup.isCycle(cycleType)) {
		    return cycleCurriculumGroup;
		}
	    }
	}
	return null;
    }

    public CycleCurriculumGroup getFirstOrderedCycleCurriculumGroup() {
	for (final CycleType cycleType : getDegreeType().getOrderedCycleTypes()) {
	    CycleCurriculumGroup cycleCurriculumGroup = getCycleCurriculumGroup(cycleType);
	    if (cycleCurriculumGroup != null) {
		return cycleCurriculumGroup;
	    }
	}

	return null;
    }

    public CycleCurriculumGroup getLastOrderedCycleCurriculumGroup() {
	final SortedSet<CycleCurriculumGroup> cycleCurriculumGroups = new TreeSet<CycleCurriculumGroup>(
		CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
	cycleCurriculumGroups.addAll(getInternalCycleCurriculumGroups());

	return cycleCurriculumGroups.isEmpty() ? null : cycleCurriculumGroups.last();
    }

    public CycleCurriculumGroup getLastConcludedCycleCurriculumGroup() {
	final SortedSet<CycleCurriculumGroup> cycleCurriculumGroups = new TreeSet<CycleCurriculumGroup>(new ReverseComparator(
		CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID));
	cycleCurriculumGroups.addAll(getInternalCycleCurriculumGroups());

	for (final CycleCurriculumGroup curriculumGroup : cycleCurriculumGroups) {
	    if (curriculumGroup.isConcluded()) {
		return curriculumGroup;
	    }
	}

	return null;
    }

    public Collection<CycleCurriculumGroup> getCycleCurriculumGroups() {
	Collection<CycleCurriculumGroup> cycleCurriculumGroups = new HashSet<CycleCurriculumGroup>();
	for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule.isCycleCurriculumGroup()) {
		cycleCurriculumGroups.add((CycleCurriculumGroup) curriculumModule);
	    }
	}
	return cycleCurriculumGroups;
    }

    public DegreeType getDegreeType() {
	return getStudentCurricularPlan().getDegreeType();
    }

    public boolean hasConcludedCycle(CycleType cycleType) {
	for (CycleType degreeCycleType : getDegreeType().getCycleTypes()) {
	    if (cycleType == null || degreeCycleType == cycleType) {
		if (!isConcluded(degreeCycleType)) {
		    return false;
		}
	    }
	}

	return cycleType == null || getDegreeType().getCycleTypes().contains(cycleType);
    }

    private boolean isConcluded(CycleType cycleType) {
	final CycleCurriculumGroup cycleCurriculumGroup = getCycleCurriculumGroup(cycleType);
	if (cycleCurriculumGroup != null) {
	    return cycleCurriculumGroup.isConcluded();
	} else {
	    return false;
	}
    }

    /**
     * Only the DegreeType's CycleTypes should be inspected.
     * CycleCurriculumGroups of other CycleType might exist and shouldn't be
     * taken into account.
     * 
     */
    @Override
    public Curriculum getCurriculum(final ExecutionYear executionYear) {
	final Curriculum curriculum = Curriculum.createEmpty(this, executionYear);

	final DegreeType degreeType = getDegreeType();
	if (degreeType.hasAnyCycleTypes()) {
	    for (final CycleCurriculumGroup cycleCurriculumGroup : getInternalCycleCurriculumGroups()) {
		curriculum.add(cycleCurriculumGroup.getCurriculum(executionYear));
	    }
	} else {
	    curriculum.add(super.getCurriculum(executionYear));
	}

	return curriculum;
    }

    @Override
    public void delete() {
	removeParentStudentCurricularPlan();
	super.delete();
    }

    @Override
    public RootCourseGroup getDegreeModule() {
	return (RootCourseGroup) super.getDegreeModule();
    }

    public boolean hasExternalCycles() {
	for (final CycleCurriculumGroup cycleCurriculumGroup : getCycleCurriculumGroups()) {
	    if (cycleCurriculumGroup.isExternal()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType, final ExecutionYear executionYear) {
	return getDegreeModule().getMostRecentActiveCurricularRule(ruleType, null, executionYear);
    }

    public CycleCurriculumGroup getCycleCurriculumGroupFor(final CurriculumModule curriculumModule) {
	for (final CycleCurriculumGroup cycleCurriculumGroup : getCycleCurriculumGroups()) {
	    if (cycleCurriculumGroup.hasCurriculumModule(curriculumModule)) {
		return cycleCurriculumGroup;
	    }
	}

	return null;
    }

    public CycleCourseGroup getCycleCourseGroupFor(final CurriculumModule curriculumModule) {
	final CycleCurriculumGroup cycleCurriculumGroup = getCycleCurriculumGroupFor(curriculumModule);
	return cycleCurriculumGroup != null ? cycleCurriculumGroup.getDegreeModule() : null;
    }

    public List<CycleCurriculumGroup> getInternalCycleCurriculumGroups() {
	final List<CycleCurriculumGroup> result = new ArrayList<CycleCurriculumGroup>();

	for (final CycleCurriculumGroup cycleCurriculumGroup : getCycleCurriculumGroups()) {
	    if (!cycleCurriculumGroup.isExternal()) {
		result.add(cycleCurriculumGroup);
	    }
	}

	return result;
    }

    public List<CycleCurriculumGroup> getExternalCycleCurriculumGroups() {
	final List<CycleCurriculumGroup> result = new ArrayList<CycleCurriculumGroup>();

	for (final CycleCurriculumGroup cycleCurriculumGroup : getCycleCurriculumGroups()) {
	    if (cycleCurriculumGroup.isExternal()) {
		result.add(cycleCurriculumGroup);
	    }
	}

	return result;
    }

    public void createNoCourseGroupCurriculumGroupEnrolment(final StudentCurricularPlan studentCurricularPlan,
	    final CurricularCourse curricularCourse, final ExecutionSemester executionSemester,
	    final NoCourseGroupCurriculumGroupType groupType) {
	
	CurriculumGroup extraCurricularGroup = getNoCourseGroupCurriculumGroup(groupType);
	if (extraCurricularGroup == null) {
	    extraCurricularGroup = NoCourseGroupCurriculumGroup.createNewNoCourseGroupCurriculumGroup(groupType, this);
	}

	new Enrolment(studentCurricularPlan, extraCurricularGroup, curricularCourse, executionSemester,
		EnrollmentCondition.VALIDATED, AccessControl.getUserView().getUtilizador());
    }
}
