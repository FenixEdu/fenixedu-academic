package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CycleCurriculumGroup extends CycleCurriculumGroup_Base {

    static final private Comparator<CycleCurriculumGroup> COMPARATOR_BY_CYCLE_TYPE = new Comparator<CycleCurriculumGroup>() {
	final public int compare(final CycleCurriculumGroup o1, final CycleCurriculumGroup o2) {
	    return CycleType.COMPARATOR_BY_LESS_WEIGHT.compare(o1.getCycleType(), o2.getCycleType());
	}
    };

    static final public Comparator<CycleCurriculumGroup> COMPARATOR_BY_CYCLE_TYPE_AND_ID = new Comparator<CycleCurriculumGroup>() {
	final public int compare(final CycleCurriculumGroup o1, final CycleCurriculumGroup o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE);
	    comparatorChain.addComparator(CycleCurriculumGroup.COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    protected CycleCurriculumGroup() {
	super();
    }

    public CycleCurriculumGroup(RootCurriculumGroup rootCurriculumGroup, CycleCourseGroup cycleCourseGroup,
	    ExecutionPeriod executionPeriod) {
	this();
	init(rootCurriculumGroup, cycleCourseGroup, executionPeriod);
    }

    public CycleCurriculumGroup(RootCurriculumGroup rootCurriculumGroup, CycleCourseGroup cycleCourseGroup) {
	this();
	init(rootCurriculumGroup, cycleCourseGroup);
    }

    @Override
    protected void init(CurriculumGroup curriculumGroup, CourseGroup courseGroup) {
	checkInitConstraints((RootCurriculumGroup) curriculumGroup, (CycleCourseGroup) courseGroup);
	super.init(curriculumGroup, courseGroup);
    }

    @Override
    protected void init(CurriculumGroup curriculumGroup, CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	checkInitConstraints((RootCurriculumGroup) curriculumGroup, (CycleCourseGroup) courseGroup);
	super.init(curriculumGroup, courseGroup, executionPeriod);
    }

    private void checkInitConstraints(final RootCurriculumGroup rootCurriculumGroup, final CycleCourseGroup cycleCourseGroup) {
	if (rootCurriculumGroup.getCycleCurriculumGroup(cycleCourseGroup.getCycleType()) != null) {
	    throw new DomainException(
		    "error.studentCurriculum.RootCurriculumGroup.cycle.course.group.already.exists.in.curriculum",
		    cycleCourseGroup.getName());
	}
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	if (curriculumGroup != null && !(curriculumGroup instanceof RootCurriculumGroup)) {
	    throw new DomainException("error.curriculumGroup.CycleParentCanOnlyBeRootCurriculumGroup");
	}
	super.setCurriculumGroup(curriculumGroup);
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
	if (degreeModule != null && !(degreeModule instanceof CycleCourseGroup)) {
	    throw new DomainException("error.curriculumGroup.CycleParentDegreeModuleCanOnlyBeCycleCourseGroup");
	}
	super.setDegreeModule(degreeModule);
    }

    @Override
    public CycleCourseGroup getDegreeModule() {
	return (CycleCourseGroup) super.getDegreeModule();
    }

    @Override
    public boolean isCycleCurriculumGroup() {
	return true;
    }

    public boolean isCycle(final CycleType cycleType) {
	return getCycleType() == cycleType;
    }

    public boolean isFirstCycle() {
	return isCycle(CycleType.FIRST_CYCLE);
    }

    public CycleCourseGroup getCycleCourseGroup() {
	return (CycleCourseGroup) getDegreeModule();
    }

    public CycleType getCycleType() {
	return getCycleCourseGroup().getCycleType();
    }

    @Override
    public RootCurriculumGroup getCurriculumGroup() {
	return (RootCurriculumGroup) super.getCurriculumGroup();
    }

    @Override
    public void delete() {
	checkRulesToDelete();

	super.delete();
    }

    @Override
    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void deleteRecursive() {
	for (final CurriculumModule child : getCurriculumModules()) {
	    child.deleteRecursive();
	}

	super.delete();
    }

    private void checkRulesToDelete() {
	if (!getCurriculumGroup().getDegreeType().canRemoveEnrolmentIn(getCycleType())) {
	    throw new DomainException("error.studentCurriculum.CycleCurriculumGroup.degree.type.requires.this.cycle.to.exist",
		    getName().getContent());
	}

    }

    public boolean isExternal() {
	return false;
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    public void conclude() {
	if (hasFinalAverage()) {
	    throw new DomainException("error.CycleCurriculumGroup.cycle.is.already.concluded", getCycleCourseGroup().getName());
	}
	if (!isConcluded()) {
	    throw new DomainException("error.CycleCurriculumGroup.cycle.is.not.concluded");
	}

	super.setFinalAverage(getCurriculum().getRoundedAverage());
	super.setConclusionDate(calculateConclusionDate());
	super.setConclusionProcessResponsible(AccessControl.getPerson());
    }

    @Override
    protected ConclusionValue isConcluded(final ExecutionYear executionYear) {
	return hasFinalAverage() ? ConclusionValue.CONCLUDED : super.isConcluded(executionYear);
    }

    final public BigDecimal getAverage() {
	return getAverage((ExecutionYear) null);
    }

    final public BigDecimal getAverage(final ExecutionYear executionYear) {
	return executionYear == null && isConcluded() && isConclusionProcessed() ? BigDecimal
		.valueOf(getFinalAverage()) : getCurriculum(executionYear).getAverage();
    }

    public boolean hasFinalAverage() {
	return super.getFinalAverage() != null;
    }

    public boolean isConclusionProcessed() {
	return hasFinalAverage();
    }

    @Override
    public void setFinalAverage(Integer finalAverage) {
	throw new DomainException("error.CycleCurriculumGroup.cannot.modify.final.average");
    }

    @Override
    public void setConclusionDate(YearMonthDay conclusionDate) {
	throw new DomainException("error.CycleCurriculumGroup.cannot.modify.conclusion.date");
    }

    @Override
    public void setConclusionProcessResponsible(Person responsibleForConclusionProcess) {
	throw new DomainException("error.CycleCurriculumGroup.cannot.modify.responsibleForConclusionProcess");
    }
    
    public String getConclusionProcessResponsibleIstUsername() {
	return hasConclusionProcessResponsible() ? getConclusionProcessResponsible().getIstUsername() : null;
    }
    
    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    public void removeConcludedInformation() {
	// checkRulesToRemoveConcludedInformation();

	super.setFinalAverage(null);
	super.setConclusionDate(null);
	super.setConclusionProcessResponsible(null);
    }

    private void checkRulesToRemoveConcludedInformation() {
	// TODO: Hack until requests are migrated to cycles instead of
	// registrations
	final DegreeType degreeType = getStudentCurricularPlan().getDegreeType();
	if (degreeType.getCycleTypes().size() == 1) {
	    if (!getStudentCurricularPlan().getRegistration().getSucessfullyFinishedDocumentRequests(
		    DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE).isEmpty()) {
		throw new DomainException(
			"cannot.delete.concluded.state.of.registration.with.concluded.degree.finalization.request");
	    }

	    if (!getStudentCurricularPlan().getRegistration().getSucessfullyFinishedDocumentRequests(
		    DocumentRequestType.DIPLOMA_REQUEST).isEmpty()) {
		throw new DomainException("cannot.delete.concluded.state.of.registration.with.concluded.diploma.request");
	    }
	} else {

	}
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void editConclusionInformation(final Integer finalAverage, final YearMonthDay conclusionDate) {
	if (!isConclusionProcessed()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup.its.only.possible.to.edit.after.conclusion.process.has.been.performed");
	}

	super.setFinalAverage(finalAverage);
	super.setConclusionDate(conclusionDate);
    }

}
