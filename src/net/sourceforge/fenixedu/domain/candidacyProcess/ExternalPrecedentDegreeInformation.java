package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.LocalDate;

public class ExternalPrecedentDegreeInformation extends ExternalPrecedentDegreeInformation_Base {

    private ExternalPrecedentDegreeInformation() {
	super();
    }

    public ExternalPrecedentDegreeInformation(final IndividualCandidacy candidacy, final String degreeDesignation,
	    final LocalDate conclusionDate, final Unit institution, final String conclusionGrade) {
	this();
	checkParameters(candidacy, degreeDesignation, institution, conclusionDate, conclusionGrade);
	setCandidacy(candidacy);
	setDegreeDesignation(degreeDesignation);
	setConclusionDate(conclusionDate);
	setInstitution(institution);
	setConclusionGrade(conclusionGrade);
    }

    private void checkParameters(final IndividualCandidacy candidacy, final String degreeDesignation, final Unit institution,
	    final LocalDate conclusionDate, final String conclusionGrade) {

	if (candidacy == null) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.candidacy");
	}

	if (degreeDesignation == null || degreeDesignation.length() == 0) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.degreeDesignation");
	}

	if (institution == null) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.institution");
	}

	if (conclusionGrade != null && conclusionGrade.length() != 0 && !conclusionGrade.matches("[0-9]+(\\.[0-9]+)?")) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.conclusionGrade");
	}
    }

    @Override
    public boolean isExternal() {
	return true;
    }

    @Override
    public void edit(final CandidacyPrecedentDegreeInformationBean bean) {
	checkParameters(getCandidacy(), bean.getDegreeDesignation(), bean.getInstitution(), bean.getConclusionDate(), bean
		.getConclusionGrade());
	setDegreeDesignation(bean.getDegreeDesignation());
	setConclusionDate(bean.getConclusionDate());
	setInstitution(bean.getInstitution());
	setConclusionGrade(bean.getConclusionGrade());
    }

    public void init(final Integer numberOfApprovedCurricularCourses, final BigDecimal gradeSum, final BigDecimal approvedEcts,
	    final BigDecimal enroledEcts) {
	checkParameters(numberOfApprovedCurricularCourses, gradeSum, approvedEcts, enroledEcts);
	setNumberOfApprovedCurricularCourses(numberOfApprovedCurricularCourses);
	setGradeSum(gradeSum);
	setApprovedEcts(approvedEcts);
	setEnroledEcts(enroledEcts);
    }

    private void checkParameters(final Integer numberOfApprovedCurricularCourses, final BigDecimal gradeSum,
	    final BigDecimal approvedEcts, final BigDecimal enroledEcts) {
	if (numberOfApprovedCurricularCourses != null && numberOfApprovedCurricularCourses.intValue() == 0) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.numberOfApprovedCurricularCourses");
	}
	checkBigDecimal(gradeSum, "gradeSum");
	checkBigDecimal(approvedEcts, "approvedEcts");
	checkBigDecimal(enroledEcts, "enroledEcts");
    }

    private void checkBigDecimal(final BigDecimal value, final String property) {
	if (value != null && value.signum() == 0) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid." + property);
	}
    }
}
