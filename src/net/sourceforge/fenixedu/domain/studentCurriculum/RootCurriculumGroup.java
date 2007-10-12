package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RootCurriculumGroup extends RootCurriculumGroup_Base {

    public RootCurriculumGroup() {
	super();
	createExtraCurriculumGroup();
    }

    public RootCurriculumGroup(StudentCurricularPlan studentCurricularPlan, RootCourseGroup rootCourseGroup,
	    ExecutionPeriod executionPeriod, CycleType cycleType) {
	this();

	if (studentCurricularPlan == null) {
	    throw new DomainException("error.studentCurriculum.curriculumGroup.studentCurricularPlan.cannot.be.null");
	}

	setParentStudentCurricularPlan(studentCurricularPlan);
	init(rootCourseGroup, executionPeriod, cycleType);
    }

    private void init(RootCourseGroup courseGroup, ExecutionPeriod executionPeriod, CycleType cycleType) {
	checkParameters(courseGroup, executionPeriod);
	setDegreeModule(courseGroup);
	addChildCurriculumGroups(courseGroup, executionPeriod, cycleType);
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

    private void addChildCurriculumGroups(RootCourseGroup rootCourseGroup, ExecutionPeriod executionPeriod, CycleType cycle) {

	if (rootCourseGroup.hasCycleGroups()) {
	    if (cycle == null) {
		cycle = rootCourseGroup.getDegree().getDegreeType().getFirstCycleType();
	    }

	    if (cycle != null) {
		new CycleCurriculumGroup(this, rootCourseGroup.getCycleCourseGroup(cycle), executionPeriod);
	    }

	} else {
	    super.addChildCurriculumGroups(rootCourseGroup, executionPeriod);
	}

    }

    private void createExtraCurriculumGroup() {
	NoCourseGroupCurriculumGroup.createNewNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR, this);
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

    public boolean hasConcludedCycle(CycleType cycleType, ExecutionYear executionYear) {
	for (CycleType degreeCycleType : getDegreeType().getCycleTypes()) {
	    if (cycleType == null || degreeCycleType == cycleType) {
		if (!isConcluded(degreeCycleType, executionYear)) {
		    return false;
		}
	    }
	}

	return cycleType == null || getDegreeType().getCycleTypes().contains(cycleType);
    }

    private boolean isConcluded(CycleType cycleType, ExecutionYear executionYear) {
	CycleCurriculumGroup cycleCurriculumGroup = getCycleCurriculumGroup(cycleType);
	if (cycleCurriculumGroup != null) {
	    return cycleCurriculumGroup.isConcluded(executionYear);
	} else {
	    return false;
	}
    }

    final public YearMonthDay getConclusionDate(final CycleType cycleType) {
	if (!getDegreeType().hasAnyCycleTypes()) {
	    return null;
	}

	if (!hasConcludedCycle(cycleType, (ExecutionYear) null)) {
	    throw new DomainException("RootCurriculumGroup.hasnt.concluded.cicle");
	}

	return getCycleCurriculumGroup(cycleType).getConclusionDate();
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
}
