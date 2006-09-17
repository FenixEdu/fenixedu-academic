package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CurriculumGroup extends CurriculumGroup_Base {

    public CurriculumGroup(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	super();
	init(studentCurricularPlan, courseGroup, executionPeriod);
    }

    private void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	checkParameters(studentCurricularPlan, courseGroup, executionPeriod);
	setDegreeModule(courseGroup);
	setParentStudentCurricularPlan(studentCurricularPlan);
	addChildCurriculumGroups(courseGroup, executionPeriod);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	if (studentCurricularPlan == null) {
	    throw new DomainException("error.studentCurriculum.curriculumGroup.studentCurricularPlan.cannot.be.null");
	}
	checkParameters(courseGroup, executionPeriod);
    }

    private void checkParameters(CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	if (courseGroup == null) {
	    throw new DomainException("error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
	}
	if (executionPeriod == null) {
	    throw new DomainException("error.studentCurriculum.curriculumGroup.executionPeriod.cannot.be.null");
	}
    }

    public CurriculumGroup(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	super();
	init(parentCurriculumGroup, courseGroup, executionPeriod);
    }

    private void init(CurriculumGroup curriculumGroup, CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	checkParameters(curriculumGroup, courseGroup, executionPeriod);
	setDegreeModule(courseGroup);
	setCurriculumGroup(curriculumGroup);
	addChildCurriculumGroups(courseGroup, executionPeriod);
    }

    private void checkParameters(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	if (parentCurriculumGroup == null) {
	    throw new DomainException("error.studentCurriculum.curriculumGroup.parentCurriculumGroup.cannot.be.null");
	}
	checkParameters(courseGroup, executionPeriod);
    }

    private void addChildCurriculumGroups(final CourseGroup courseGroup, final ExecutionPeriod executionPeriod) {
	for (final CourseGroup childCourseGroup : courseGroup.getNotOptionalChildCourseGroups(executionPeriod)) {
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
	    throw new DomainException("error.studentCurriculum.curriculumGroup.notEmptyCurriculumGroupModules");
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
    
    public boolean isRoot() {
	return hasParentStudentCurricularPlan();
    }
    
    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return isRoot() ? getParentStudentCurricularPlan() : getCurriculumGroup().getStudentCurricularPlan();
    }

}
