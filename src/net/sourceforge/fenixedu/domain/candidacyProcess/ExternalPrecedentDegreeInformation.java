package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.YearMonthDay;

public class ExternalPrecedentDegreeInformation extends ExternalPrecedentDegreeInformation_Base {

    private ExternalPrecedentDegreeInformation() {
	super();
    }

    public ExternalPrecedentDegreeInformation(final IndividualCandidacy candidacy, final String degreeDesignation,
	    final YearMonthDay conclusionDate, final Unit institution, final String conclusionGrade) {
	this();
	checkParameters(candidacy, degreeDesignation, conclusionDate, institution, conclusionGrade);
	setCandidacy(candidacy);
	setDegreeDesignation(degreeDesignation);
	setConclusionDate(conclusionDate);
	setInstitution(institution);
	setConclusionGrade(conclusionGrade);
    }

    private void checkParameters(final IndividualCandidacy candidacy, final String degreeDesignation,
	    final YearMonthDay conclusionDate, final Unit institution, final String conclusionGrade) {

	if (candidacy == null) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.candidacy");
	}

	if (degreeDesignation == null || degreeDesignation.length() == 0) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.degreeDesignation");
	}

	if (conclusionDate == null) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.conclusionDate");
	}

	if (institution == null) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.institution");
	}

	if (conclusionGrade == null || conclusionGrade.length() == 0 || !conclusionGrade.matches("[0-9]+(\\.[0-9]+)?")) {
	    throw new DomainException("error.ExternalPrecedentDegreeInformation.invalid.conclusionGrade");
	}
    }

    @Override
    public boolean isExternal() {
	return true;
    }

    @Override
    public void edit(final CandidacyPrecedentDegreeInformationBean bean) {
	checkParameters(getCandidacy(), bean.getDegreeDesignation(), bean.getConclusionDate(), bean.getInstitution(), bean
		.getConclusionGrade());
	setDegreeDesignation(bean.getDegreeDesignation());
	setConclusionDate(bean.getConclusionDate());
	setInstitution(bean.getInstitution());
	setConclusionGrade(bean.getConclusionGrade());
    }
    
}
