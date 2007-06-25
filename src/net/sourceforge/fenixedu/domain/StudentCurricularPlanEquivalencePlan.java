package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.EquivalencyPlanEntryCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.EquivalencyPlanEntryWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class StudentCurricularPlanEquivalencePlan extends StudentCurricularPlanEquivalencePlan_Base {

    public StudentCurricularPlanEquivalencePlan(final StudentCurricularPlan studentCurricularPlan) {
	super();

	init(studentCurricularPlan);
    }

    private void init(StudentCurricularPlan oldStudentCurricularPlan) {
	checkParameters(oldStudentCurricularPlan);

	super.setOldStudentCurricularPlan(oldStudentCurricularPlan);
    }

    private void checkParameters(StudentCurricularPlan oldStudentCurricularPlan) {
	if (oldStudentCurricularPlan == null) {
	    throw new DomainException(
		    "error.StudentCurricularPlanEquivalencePlan.oldStudentCurricularPlan.cannot.be.null");
	}
    }

    public Set<EquivalencePlanEntry> getEntriesToRemove(final DegreeCurricularPlan degreeCurricularPlan) {
	final Set<EquivalencePlanEntry> result = new HashSet<EquivalencePlanEntry>();
	for (final EquivalencePlanEntry entry : getEntriesToRemoveSet()) {
	    if (entry.hasAnyDestinationDegreeModuleFor(degreeCurricularPlan)) {
		result.add(entry);
	}
	}
	return result;
    }

    public Set<EquivalencePlanEntry> getEntries(final DegreeCurricularPlan degreeCurricularPlan) {
	final Set<EquivalencePlanEntry> result = new HashSet<EquivalencePlanEntry>();
	for (final EquivalencePlanEntry entry : getEntriesSet()) {
	    if (entry.hasAnyDestinationDegreeModuleFor(degreeCurricularPlan)) {
		result.add(entry);
	    }
	}
	return result;
    }

    public Set<EquivalencePlanEntry> getEquivalencePlanEntries(final DegreeCurricularPlan degreeCurricularPlan) {
	final Set<EquivalencePlanEntry> equivalencePlanEntries = new HashSet<EquivalencePlanEntry>();
	equivalencePlanEntries.addAll(degreeCurricularPlan.getEquivalencePlan().getEntriesSet());
	equivalencePlanEntries.removeAll(getEntriesToRemoveSet());
	for (final EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
	    if (equivalencePlanEntry.isFor(degreeCurricularPlan)) {
		equivalencePlanEntries.add(equivalencePlanEntry);
	    }
	}
	return equivalencePlanEntries;
    }

    public Set<EquivalencyPlanEntryWrapper> getEquivalencePlanEntryWrappers(final DegreeCurricularPlan degreeCurricularPlan, final CurriculumModule curriculumModule) {
	final Set<EquivalencyPlanEntryWrapper> equivalencePlanEntries = new TreeSet<EquivalencyPlanEntryWrapper>(EquivalencyPlanEntryWrapper.COMPARATOR);

	for (final EquivalencePlanEntry equivalencePlanEntry : degreeCurricularPlan.getEquivalencePlan().getEntriesSet()) {
	    if (hasAllEnrolmentsFor(equivalencePlanEntry, getOldStudentCurricularPlan())
		    && (curriculumModule == null || matchOrigin(equivalencePlanEntry, curriculumModule.getDegreeModule()))) {
		if (getEntriesToRemoveSet().contains(equivalencePlanEntry)) {
		    equivalencePlanEntries.add(new EquivalencyPlanEntryWrapper(equivalencePlanEntry, true));
		} else {
		    equivalencePlanEntries.add(new EquivalencyPlanEntryWrapper(equivalencePlanEntry, false));
		}
	    }
	}

	for (final EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
	    if (equivalencePlanEntry.isFor(degreeCurricularPlan)
		    && (curriculumModule == null || equivalencePlanEntry.isFor(curriculumModule.getDegreeModule()))) {
		equivalencePlanEntries.add(new EquivalencyPlanEntryWrapper(equivalencePlanEntry, false));
	    }
	}

	return equivalencePlanEntries;	
    }

    private boolean hasAllEnrolmentsFor(final EquivalencePlanEntry equivalencePlanEntry, final StudentCurricularPlan studentCurricularPlan) {
	if (equivalencePlanEntry.isCourseGroupEntry()) {
	    final CourseGroupEquivalencePlanEntry courseGroupEquivalencePlanEntry = (CourseGroupEquivalencePlanEntry) equivalencePlanEntry;
	    return studentCurricularPlan.hasEnrolmentOrAprovalInCurriculumModule(courseGroupEquivalencePlanEntry.getOldCourseGroup());
	} else if (equivalencePlanEntry.isCurricularCourseEntry()) {
	    final CurricularCourseEquivalencePlanEntry curricularCourseEquivalencePlanEntry = (CurricularCourseEquivalencePlanEntry) equivalencePlanEntry;
	    boolean isApprovedInAll = true;
	    for (final CurricularCourse curricularCourse : curricularCourseEquivalencePlanEntry.getOldCurricularCoursesSet()) {
		isApprovedInAll &= studentCurricularPlan.hasEnrolmentOrAprovalInCurriculumModule(curricularCourse);
	    }
	    return isApprovedInAll;
	} else {
	    throw new Error("unknown.type: " + equivalencePlanEntry.getClass().getName());
	}
    }

    private boolean matchOrigin(final EquivalencePlanEntry equivalencePlanEntry, final DegreeModule degreeModule) {
	if (equivalencePlanEntry.isCourseGroupEntry()) {
	    final CourseGroupEquivalencePlanEntry courseGroupEquivalencePlanEntry = (CourseGroupEquivalencePlanEntry) equivalencePlanEntry;
	    return courseGroupEquivalencePlanEntry.getOldCourseGroup() == degreeModule;
	} else if (equivalencePlanEntry.isCurricularCourseEntry()) {
	    final CurricularCourseEquivalencePlanEntry curricularCourseEquivalencePlanEntry = (CurricularCourseEquivalencePlanEntry) equivalencePlanEntry;
	    for (final CurricularCourse curricularCourse : curricularCourseEquivalencePlanEntry.getOldCurricularCoursesSet()) {
		if (curricularCourse == degreeModule) {
		    return true;
		}
	    }
	    return false;
	} else {
	    throw new Error("unknown.type: " + equivalencePlanEntry.getClass().getName());
	}
    }

//    public Set<EquivalencePlanEntry> getEquivalencePlanEntries(final DegreeCurricularPlan degreeCurricularPlan, final CurriculumModule curriculumModule) {
//	final Set<EquivalencePlanEntry> equivalencePlanEntries = new HashSet<EquivalencePlanEntry>();
//
//	for (final EquivalencePlanEntry equivalencePlanEntry : degreeCurricularPlan.getEquivalencePlan().getEntriesSet()) {
//	    if (!getEntriesToRemoveSet().contains(equivalencePlanEntry) && getOldStudentCurricularPlan().hasEnrolmentOrAprovalInCurriculumModule(curriculumModule)) {
//		equivalencePlanEntries.add(equivalencePlanEntry);
//	    }
//	}
//
//	for (final EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
//	    if (equivalencePlanEntry.isFor(degreeCurricularPlan)
//		    && (curriculumModule == null || equivalencePlanEntry.isFor(curriculumModule.getDegreeModule()))) {
//		equivalencePlanEntries.add(equivalencePlanEntry);
//	    }
//	}
//
//	return equivalencePlanEntries;	
//    }

//    public Set<EquivalencePlanEntry> getRemovedEquivalencePlanEntries(final DegreeCurricularPlan degreeCurricularPlan, final CurriculumModule curriculumModule) {
//	final Set<EquivalencePlanEntry> equivalencePlanEntries = new HashSet<EquivalencePlanEntry>();
//
//	for (final EquivalencePlanEntry equivalencePlanEntry : getEntriesToRemoveSet()) {
//	    if (equivalencePlanEntry.isFor(degreeCurricularPlan)
//		    && (curriculumModule == null || equivalencePlanEntry.isFor(curriculumModule.getDegreeModule()))) {
//		equivalencePlanEntries.add(equivalencePlanEntry);
//	    }
//	}
//
//	return equivalencePlanEntries;	
//    }

    public EquivalencyPlanEntryCurriculumModuleWrapper getRootEquivalencyPlanEntryCurriculumModuleWrapper(final DegreeCurricularPlan degreeCurricularPlan) {
	return getEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan, getOldStudentCurricularPlan().getRoot());
    }

    public EquivalencyPlanEntryCurriculumModuleWrapper getEquivalencyPlanEntryCurriculumModuleWrapper(final DegreeCurricularPlan degreeCurricularPlan, final CurriculumModule curriculumModule) {
	final EquivalencyPlanEntryCurriculumModuleWrapper equivalencyPlanEntryCurriculumModuleWrapper = new EquivalencyPlanEntryCurriculumModuleWrapper(curriculumModule);
	final DegreeModule degreeModule = curriculumModule.getDegreeModule();

	addEquivalencyPlanEntryCurriculumModuleWrappers(equivalencyPlanEntryCurriculumModuleWrapper, degreeCurricularPlan.getEquivalencePlan(), degreeModule);

	for (EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
	    if (equivalencePlanEntry.isFor(degreeModule)) {
		equivalencyPlanEntryCurriculumModuleWrapper.addEquivalencePlanEntriesToApply(equivalencePlanEntry);
	    }
	}

	if (!curriculumModule.isLeaf()) {
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
	    for (final CurriculumModule childCurriculumModule : curriculumGroup.getCurriculumModulesSet()) {
		final EquivalencyPlanEntryCurriculumModuleWrapper childEquivalencyPlanEntryCurriculumModuleWrapper = 
		    getEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan, childCurriculumModule);
		equivalencyPlanEntryCurriculumModuleWrapper.addChildren(childEquivalencyPlanEntryCurriculumModuleWrapper);
	    }
	}

	return equivalencyPlanEntryCurriculumModuleWrapper;
    }

    private void addEquivalencyPlanEntryCurriculumModuleWrappers(final EquivalencyPlanEntryCurriculumModuleWrapper equivalencyPlanEntryCurriculumModuleWrapper,
	    final DegreeCurricularPlanEquivalencePlan equivalencePlan, final DegreeModule degreeModule) {
	final Set<EquivalencePlanEntry> equivalencePlanEntries;
	if (degreeModule.isLeaf()) {
	    final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
	    equivalencePlanEntries = (Set) curricularCourse.getOldEquivalencePlanEntriesSet();
	} else {
	    final CourseGroup courseGroup = (CourseGroup) degreeModule;
	    equivalencePlanEntries = (Set) courseGroup.getOldEquivalencePlanEntriesSet();
	}
	for (final EquivalencePlanEntry equivalencePlanEntry : equivalencePlanEntries) {
	    if (equivalencePlanEntry.getEquivalencePlan() == equivalencePlan) {
		if (getEntriesToRemoveSet().contains(equivalencePlanEntry)) {
		    equivalencyPlanEntryCurriculumModuleWrapper.addRemovedEquivalencePlanEntries(equivalencePlanEntry);
		} else {
		    equivalencyPlanEntryCurriculumModuleWrapper.addEquivalencePlanEntriesToApply(equivalencePlanEntry);
		}
	    }
	}
    }

}
