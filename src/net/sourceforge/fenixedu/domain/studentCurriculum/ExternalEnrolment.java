package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class ExternalEnrolment extends ExternalEnrolment_Base implements IEnrolment {

    static public final Comparator<ExternalEnrolment> COMPARATOR_BY_NAME = new Comparator<ExternalEnrolment>() {
	public int compare(ExternalEnrolment o1, ExternalEnrolment o2) {
	    int result = o1.getName().compareTo(o2.getName());
	    return (result != 0) ? result : o1.getIdInternal().compareTo(o2.getIdInternal());
	}
    };

    static final public Comparator<ExternalEnrolment> COMPARATOR_BY_EXECUTION_PERIOD_AND_EVALUATION_DATE = new Comparator<ExternalEnrolment>() {
	public int compare(ExternalEnrolment o1, ExternalEnrolment o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(IEnrolment.COMPARATOR_BY_EXECUTION_PERIOD);
	    comparatorChain.addComparator(IEnrolment.COMPARATOR_BY_APPROVEMENT_DATE);

	    return comparatorChain.compare(o1, o2);
	}
    };


    protected ExternalEnrolment() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDateDateTime(new DateTime());
	if (AccessControl.getPerson() != null) {
	    setCreatedBy(AccessControl.getPerson().getUsername());
	}
    }

    public ExternalEnrolment(final Registration registration,
	    final ExternalCurricularCourse externalCurricularCourse, final Grade grade,
	    final ExecutionPeriod executionPeriod, final YearMonthDay evaluationDate,
	    final Double ectsCredits) {
	this();

	checkConstraints(registration, externalCurricularCourse, executionPeriod, grade, ectsCredits);
	checkIfCanCreateExternalEnrolment(registration, externalCurricularCourse);

	setRegistration(registration);
	setExternalCurricularCourse(externalCurricularCourse);
	setGrade(grade);
	setExecutionPeriod(executionPeriod);
	setEvaluationDate(evaluationDate);
	setEctsCredits(ectsCredits);
    }

    private void checkIfCanCreateExternalEnrolment(final Registration registration, final ExternalCurricularCourse externalCurricularCourse) {
	for (final ExternalEnrolment externalEnrolment : registration.getExternalEnrolmentsSet()) {
	    if (externalEnrolment.getExternalCurricularCourse() == externalCurricularCourse) {
		throw new DomainException("error.studentCurriculum.ExternalEnrolment.already.exists.externalEnrolment.for.externalCurricularCourse", externalCurricularCourse.getName());
	    }
	}
    }

    private void checkConstraints(final Registration registration, final ExternalCurricularCourse externalCurricularCourse, final ExecutionPeriod executionPeriod, final Grade grade, final Double ectsCredits) {
	if (registration == null) {
	    throw new DomainException("error.externalEnrolment.student.cannot.be.null");
	}
	if (externalCurricularCourse == null) {
	    throw new DomainException("error.externalEnrolment.externalCurricularCourse.cannot.be.null");
	}
	if (executionPeriod == null) {
	    throw new DomainException("error.externalEnrolment.executionPeriod.cannot.be.null");
	}
	if (grade == null || grade.isEmpty()) {
	    throw new DomainException("error.externalEnrolment.invalid.grade");
	}
	if (ectsCredits == null) {
	    throw new DomainException("error.externalEnrolment.ectsCredits.cannot.be.null");
	}
    }

    public void edit(final Registration registration, final Grade grade,
	    final ExecutionPeriod executionPeriod, final YearMonthDay evaluationDate,
	    final Double ectsCredits) {

	if (registration != getRegistration()) {
	    checkIfCanCreateExternalEnrolment(registration, getExternalCurricularCourse());
	}

	checkConstraints(grade, ectsCredits);

	setRegistration(registration);
	setGrade(grade);
	setExecutionPeriod(executionPeriod);
	setEvaluationDate(evaluationDate);
	setEctsCredits(ectsCredits);
    }

    public MultiLanguageString getName() {
	MultiLanguageString multiLanguageString = new MultiLanguageString();
	multiLanguageString.setContent(getExternalCurricularCourse().getName());
	return multiLanguageString;
    }

    public String getCode() {
	return getExternalCurricularCourse().getCode();
    }

    public String getFullPathName() {
	return getExternalCurricularCourse().getFullPathName();
    }

    public String getDescription() {
	return getFullPathName();
    }

    public void delete() {
	removeExecutionPeriod();
	removeExternalCurricularCourse();
	removeRegistration();
	removeRootDomainObject();
	getNotNeedToEnrollCurricularCourses().clear();
	super.deleteDomainObject();
    }

    final public boolean isApproved() {
	return true;
    }

    final public boolean isEnroled() {
	return true;
    }

    final public boolean isExternalEnrolment() {
	return true;
    }

    final public boolean isEnrolment() {
	return false;
    }

    public Integer getFinalGrade() {
	final String grade = getGradeValue();
	return (StringUtils.isEmpty(grade) || !StringUtils.isNumeric(grade)) ? null : Integer
		.valueOf(grade);
    }

    public Double getWeigth() {
	return getEctsCredits();
    }

    final public ExecutionYear getExecutionYear() {
	return getExecutionPeriod() != null ? getExecutionPeriod().getExecutionYear() : null;
    }

    final public YearMonthDay getApprovementDate() {
        return getEvaluationDate();
    }
    
    public Unit getAcademicUnit() {
	return getExternalCurricularCourse().getAcademicUnit();
    }

    public String getGradeValue() {
	return getGrade().getValue();
    }

    /**
     * There is no thesis associated to an external enrolment.
     * 
     * @return <code>null</code>
     */
    public Thesis getThesis() {
        return null;
    }
    
}
