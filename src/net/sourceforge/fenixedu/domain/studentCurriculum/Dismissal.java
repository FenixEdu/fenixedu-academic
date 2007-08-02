package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class Dismissal extends Dismissal_Base {

    public Dismissal() {
	super();
    }

    public Dismissal(Credits credits, CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
	init(credits, curriculumGroup, curricularCourse);
    }

    final protected void init(Credits credits, CurriculumGroup curriculumGroup) {
	if (credits == null || curriculumGroup == null) {
	    throw new DomainException("error.dismissal.wrong.arguments");
	}
	setCredits(credits);
	setCurriculumGroup(curriculumGroup);
    }

    final protected void init(Credits credits, CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
	if (curricularCourse == null) {
	    throw new DomainException("error.dismissal.wrong.arguments");
	}
	checkCurriculumGroupCurricularCourse(curriculumGroup, curricularCourse);
	init(credits, curriculumGroup);
	setCurricularCourse(curricularCourse);
    }

    private void checkCurriculumGroupCurricularCourse(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
	if (!(curriculumGroup instanceof NoCourseGroupCurriculumGroup)) {
	    if (!curriculumGroup.getCurricularCoursesToDismissal().contains(curricularCourse)) {
		throw new DomainException("error.dismissal.invalid.curricular.course.to.dismissal");
	    }
	}
    }

    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan,
	    final CourseGroup courseGroup) {
	return new CreditsDismissal(credits, findCurriculumGroupForCourseGroup(studentCurricularPlan, courseGroup));
    }

    static private CurriculumGroup findCurriculumGroupForCourseGroup(final StudentCurricularPlan studentCurricularPlan,
	    final CourseGroup courseGroup) {
	final CurriculumGroup curriculumGroup = studentCurricularPlan.findCurriculumGroupFor(courseGroup);
	if (curriculumGroup != null) {
	    return curriculumGroup;
	}
	return new CurriculumGroup(getOrCreateExtraCurricularCurriculumGroup(studentCurricularPlan), courseGroup);
    }

    static private NoCourseGroupCurriculumGroup getOrCreateExtraCurricularCurriculumGroup(
	    final StudentCurricularPlan studentCurricularPlan) {
	final NoCourseGroupCurriculumGroup result = studentCurricularPlan
		.getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR);
	return (result == null) ? studentCurricularPlan
		.createNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR) : result;
    }

    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan,
	    final CurricularCourse curricularCourse) {
	return new Dismissal(credits, findCurriculumGroupForCurricularCourse(studentCurricularPlan, curricularCourse),
		curricularCourse);
    }

    static protected OptionalDismissal createNewOptionalDismissal(final Credits credits,
	    final StudentCurricularPlan studentCurricularPlan, final OptionalCurricularCourse optionalCurricularCourse,
	    final Double ectsCredits) {
	return new OptionalDismissal(credits, findCurriculumGroupForCurricularCourse(studentCurricularPlan,
		optionalCurricularCourse), optionalCurricularCourse, ectsCredits);
    }

    static private CurriculumGroup findCurriculumGroupForCurricularCourse(final StudentCurricularPlan studentCurricularPlan,
	    final CurricularCourse curricularCourse) {
	final Set<CurriculumGroup> curriculumGroups = new HashSet<CurriculumGroup>(curricularCourse.getParentContextsCount());
	for (final Context context : curricularCourse.getParentContexts()) {
	    final CurriculumGroup curriculumGroup = studentCurricularPlan.findCurriculumGroupFor(context.getParentCourseGroup());
	    if (curriculumGroup != null && !curriculumGroup.parentCurriculumGroupIsNoCourseGroupCurriculumGroup()) {
		curriculumGroups.add(curriculumGroup);
	    }
	}
	return curriculumGroups.size() == 1 ? curriculumGroups.iterator().next()
		: getOrCreateExtraCurricularCurriculumGroup(studentCurricularPlan);
    }

    @Override
    public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[D ").append(hasDegreeModule() ? getDegreeModule().getName() : "").append(" ");
	builder.append(getEctsCredits()).append(" ects ]\n");
	return builder;
    }

    @Override
    final public boolean isApproved() {
	return getCredits().isSubstitution();
    }

    final public Collection<IEnrolment> getSourceIEnrolments() {
	return getCredits().getIEnrolments();
    }

    @Override
    public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return (executionPeriod == null || getExecutionPeriod().isBeforeOrEquals(executionPeriod))
		&& getCurricularCourse().isEquivalent(curricularCourse);
    }

    @Override
    public Double getEctsCredits() {
	return getCurricularCourse().isOptionalCurricularCourse() ? getEnrolmentsEcts() : getCurricularCourse().getEctsCredits(
		getExecutionPeriod());
    }

    final protected Double getEnrolmentsEcts() {
	return getCredits().getEnrolmentsEcts();
    }

    @Override
    final public Double getAprovedEctsCredits() {
	return getEctsCredits();
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionPeriod executionPeriod) {
	return Double.valueOf(0d);
    }

    @Override
    final public void collectDismissals(final List<Dismissal> result) {
	result.add(this);
    }

    @Override
    final public boolean isDismissal() {
	return true;
    }

    @Override
    final public Dismissal getDismissal(final CurricularCourse curricularCourse) {
	return (getCurricularCourse() == curricularCourse) ? this : null;
    }

    @Override
    public void delete() {
	final Credits credits = getCredits();
	removeCredits();
	if (!credits.hasAnyDismissals()) {
	    credits.delete();
	}
	super.delete();
    }

    @Override
    final public boolean isConcluded(final ExecutionYear executionYear) {
	return executionYear == null || getExecutionPeriod().getExecutionYear().isBeforeOrEquals(executionYear);
    }

    @Override
    final public Double getCreditsConcluded(ExecutionYear executionYear) {
	if (isConcluded(executionYear)) {
	    return getEctsCredits();
	}
	return Double.valueOf(0d);
    }

    @Override
    final public YearMonthDay getConclusionDate() {
	if (!isConcluded((ExecutionYear) null)) {
	    throw new DomainException("Dismissal.is.not.concluded");
	}

	final SortedSet<IEnrolment> iEnrolments = new TreeSet<IEnrolment>(IEnrolment.COMPARATOR_BY_APPROVEMENT_DATE);
	iEnrolments.addAll(getSourceIEnrolments());

	return iEnrolments.isEmpty() ? getExecutionPeriod().getBeginDateYearMonthDay() : iEnrolments.last().getApprovementDate();
    }

    @Override
    final public ExecutionPeriod getExecutionPeriod() {
	return getCredits().getExecutionPeriod();
    }
    
        @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionPeriod executionPeriod) {
	if (executionPeriod != null && executionPeriod != getExecutionPeriod()) {
	    return Collections.EMPTY_SET;
	}

	final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>(1);
	result.add(new CurriculumModuleEnroledWrapper(this, executionPeriod));
	return result;
    }
    

}
