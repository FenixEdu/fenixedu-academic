package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.AverageType;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;

import org.joda.time.LocalDate;

public class InstitutionPrecedentDegreeInformation extends InstitutionPrecedentDegreeInformation_Base {

    private InstitutionPrecedentDegreeInformation() {
	super();
    }

    public InstitutionPrecedentDegreeInformation(final IndividualCandidacy candidacy,
	    final StudentCurricularPlan studentCurricularPlan) {
	this(candidacy, studentCurricularPlan, null);
    }

    public InstitutionPrecedentDegreeInformation(final IndividualCandidacy candidacy,
	    final StudentCurricularPlan studentCurricularPlan, final CycleType cycleType) {
	this();
	checkParameters(candidacy, studentCurricularPlan, cycleType);
	setCandidacy(candidacy);
	setStudentCurricularPlan(studentCurricularPlan);
	setCycleType(cycleType);
    }

    private void checkParameters(final IndividualCandidacy candidacy, final StudentCurricularPlan studentCurricularPlan,
	    final CycleType cycleType) {
	if (candidacy == null) {
	    throw new DomainException("error.InstitutionPrecedentDegreeInformation.invalid.candidacy");
	}
	if (studentCurricularPlan == null) {
	    throw new DomainException("error.InstitutionPrecedentDegreeInformation.invalid.studentCurricularPlan");
	}
	if (studentCurricularPlan.isBolonhaDegree() && cycleType == null) {
	    throw new DomainException("error.InstitutionPrecedentDegreeInformation.invalid.cycleType");
	}
    }

    private boolean isBolonha() {
	return getStudentCurricularPlan().isBolonhaDegree();
    }

    @Override
    public LocalDate getConclusionDate() {
	return new LocalDate(isBolonha() ? getStudentCurricularPlan().getConclusionDate(getCycleType()) : getRegistration()
		.getConclusionDate());
    }

    @Override
    public String getConclusionGrade() {
	final Integer result = isBolonha() ? getStudentCurricularPlan().getCycle(getCycleType()).getFinalAverage()
		: getRegistration().getFinalAverage();
	return (result == null) ? null : String.valueOf(result);
    }

    @Override
    public String getDegreeDesignation() {
	return getStudentCurricularPlan().getName();
    }

    @Override
    public Unit getInstitution() {
	return getRootDomainObject().getInstitutionUnit();
    }

    private Registration getRegistration() {
	return getStudentCurricularPlan().getRegistration();
    }

    @Override
    public void edit(CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {
	throw new DomainException("error.InstitutionPrecedentDegreeInformation.cannot.edit");
    }

    @Override
    public Integer getNumberOfApprovedCurricularCourses() {
	return getStudentCurricularPlan().getRoot().getNumberOfAllApprovedCurriculumLines();
    }

    @Override
    public BigDecimal getGradeSum() {
	final Curriculum curriculum = getStudentCurricularPlan().getRoot().getCurriculum();
	curriculum.setAverageType(AverageType.SIMPLE);
	return curriculum.getSumPiCi();
    }

    @Override
    public BigDecimal getApprovedEcts() {
	return BigDecimal.valueOf(getStudentCurricularPlan().getRoot().getAprovedEctsCredits());
    }

    @Override
    public BigDecimal getEnroledEcts() {
	return BigDecimal.valueOf(getStudentCurricularPlan().getRoot().getEctsCredits());
    }

}
