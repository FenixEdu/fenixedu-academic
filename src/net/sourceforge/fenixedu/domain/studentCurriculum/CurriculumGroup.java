package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CurriculumGroup extends CurriculumGroup_Base {

    public CurriculumGroup(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	super();
	init(studentCurricularPlan, courseGroup, executionPeriod);
    }

    public CurriculumGroup(CurriculumGroup curriculumGroup, CourseGroup courseGroup) {
	if (courseGroup == null || curriculumGroup == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
	}
	setDegreeModule(courseGroup);
	setCurriculumGroup(curriculumGroup);
    }

    private void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	checkParameters(studentCurricularPlan, courseGroup, executionPeriod);
	setDegreeModule(courseGroup);
	setParentStudentCurricularPlan(studentCurricularPlan);
	addChildCurriculumGroups(courseGroup, executionPeriod);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	if (studentCurricularPlan == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.studentCurricularPlan.cannot.be.null");
	}
	checkParameters(courseGroup, executionPeriod);
    }

    private void checkParameters(CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	if (courseGroup == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
	}
	if (executionPeriod == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.executionPeriod.cannot.be.null");
	}
    }

    public CurriculumGroup(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	super();
	init(parentCurriculumGroup, courseGroup, executionPeriod);
    }

    private void init(CurriculumGroup curriculumGroup, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	checkParameters(curriculumGroup, courseGroup, executionPeriod);
	setDegreeModule(courseGroup);
	setCurriculumGroup(curriculumGroup);
	addChildCurriculumGroups(courseGroup, executionPeriod);
    }

    private void checkParameters(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	if (parentCurriculumGroup == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.parentCurriculumGroup.cannot.be.null");
	}
	checkParameters(courseGroup, executionPeriod);
    }

    private void addChildCurriculumGroups(final CourseGroup courseGroup,
	    final ExecutionPeriod executionPeriod) {
	for (final CourseGroup childCourseGroup : courseGroup
		.getNotOptionalChildCourseGroups(executionPeriod)) {
	    new CurriculumGroup(this, childCourseGroup, executionPeriod);
	}
    }

    public boolean isLeaf() {
	return false;
    }

    public boolean canBeDeleted() {
	return !hasAnyCurriculumModules();
    }

    @Override
    public void delete() {
	if (canBeDeleted()) {
	    super.delete();
	} else {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.notEmptyCurriculumGroupModules");
	}
    }

    @Override
    public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[CG ").append(getDegreeModule().getName()).append(" ]\n");
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

    public List<Enrolment> getEnrolments() {
	final List<Enrolment> result = new ArrayList<Enrolment>();
	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
	    result.addAll(curriculumModule.getEnrolments());
	}
	return result;
    }

    public Set<Enrolment> getEnrolmentsSet() {
	final Set<Enrolment> result = new HashSet<Enrolment>();
	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
	    result.addAll(curriculumModule.getEnrolments());
	}
	return result;
    }

    public boolean isRoot() {
	return hasParentStudentCurricularPlan();
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
	return isRoot() ? getParentStudentCurricularPlan() : getCurriculumGroup()
		.getStudentCurricularPlan();
    }

    // alterar isto, colocar as regras
    private Collection<Context> getDegreeModulesToEnrol(ExecutionPeriod executionPeriod) {
	Collection<Context> result = new HashSet<Context>();
	for (Context context : this.getDegreeModule().getChildContexts(executionPeriod)) {
	    if (context.getCurricularPeriod() == null
		    || context.getCurricularPeriod().contains(CurricularPeriodType.SEMESTER,
			    executionPeriod.getSemester()) != null) {
		result.add(context);
	    }
	}
	return result;
    }
    
    public List<Context> getCurricularCourseContextsToEnrol(ExecutionPeriod executionPeriod){
	List<Context> result = new ArrayList<Context>();
	for (Context context : this.getDegreeModulesToEnrol(executionPeriod)) {
	    if(context.getChildDegreeModule().isLeaf()) {
		CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
		if(!this.getStudentCurricularPlan().getRoot().isAproved(curricularCourse, executionPeriod) &&
			!this.getStudentCurricularPlan().getRoot().isEnroledInExecutionPeriod(curricularCourse, executionPeriod)) {
		    result.add(context);
		}
	    }
	}
	return result;
    }
    
    public List<Context> getCourseGroupContextsToEnrol(ExecutionPeriod executionPeriod){
	List<Context> result = new ArrayList<Context>();
	for (Context context : this.getDegreeModulesToEnrol(executionPeriod)) {
	    if(!context.getChildDegreeModule().isLeaf()) {
		if(!this.getStudentCurricularPlan().getRoot().hasDegreModule(context.getChildDegreeModule())) {
		    result.add(context);
		}
	    }
	}
	return result;
    }

    @Override
    public boolean isAproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
	    if (curriculumModule.isAproved(curricularCourse, executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
	    if (curriculumModule.isEnroledInExecutionPeriod(curricularCourse, executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean hasDegreModule(DegreeModule degreeModule) {
	if (super.hasDegreModule(degreeModule)) {
	    return true;
	} else {
	    for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
		if (curriculumModule.hasDegreModule(degreeModule)) {
		    return true;
		}
	    }
	    return false;
	}
    }

    public Set<CurriculumLine> getCurriculumLines() {
	Set<CurriculumLine> result = new TreeSet<CurriculumLine>(CurriculumModule.COMPARATOR_BY_NAME);

	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isLeaf()) {
		result.add((CurriculumLine) curriculumModule);
	    }
	}

	return result;
    }

    public Set<CurriculumGroup> getCurriculumGroups() {
	Set<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumModule.COMPARATOR_BY_NAME);

	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (!curriculumModule.isLeaf()) {
		result.add((CurriculumGroup) curriculumModule);
	    }
	}

	return result;
    }
    
    public Integer getChildOrder() {
	final CourseGroup parentCourseGroup = getCurriculumGroup().getDegreeModule();
	final CourseGroup courseGroup = getDegreeModule();
	
	for (final Context context : parentCourseGroup.getChildContexts(ExecutionPeriod.readActualExecutionPeriod())) {
	    if (context.getChildDegreeModule() == courseGroup) {
		return context.getChildOrder();
	    }
	}
	
	return null;
    }

}
