package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class StandByCandidacySituation extends StandByCandidacySituation_Base {

    public StandByCandidacySituation(Candidacy candidacy) {
	this(candidacy, AccessControl.getPerson());
    }

    public StandByCandidacySituation(Candidacy candidacy, Person person) {
	super();
	init(candidacy, person);
    }

    @Override
    public void checkConditionsToForward() {
	if (!checkIfDataIsFilled()) {
	    throw new DomainException("error.mandatory.data.not.filled.yet");
	}
	if (getCandidacy() instanceof DFACandidacy) {
	    if (!checkIfPrecedenceDataIsFilled()) {
		throw new DomainException("error.mandatory.data.not.filled.yet");
	    }
	}
    }

    @Override
    public void nextState() {
	new StandByFilledDataCandidacySituation(getCandidacy());
    }

    private boolean checkIfPrecedenceDataIsFilled() {
	PrecedentDegreeInformation precedentDegreeInformation = ((DFACandidacy) getCandidacy())
		.getPrecedentDegreeInformation();
	return (precedentDegreeInformation.getConclusionGrade() != null
		&& precedentDegreeInformation.getConclusionYear() != null
		&& precedentDegreeInformation.getCountry() != null
		&& precedentDegreeInformation.getDegreeDesignation() != null && precedentDegreeInformation
		.getInstitution() != null);
    }

    private boolean checkIfDataIsFilled() {
	Person person = getCandidacy().getPerson();
	return (person.getGender() != null && person.getEmissionDateOfDocumentIdYearMonthDay() != null
		&& person.getEmissionLocationOfDocumentId() != null
		&& person.getExpirationDateOfDocumentIdYearMonthDay() != null
		&& person.getSocialSecurityNumber() != null && person.getProfession() != null
		&& person.getMaritalStatus() != null && person.getDateOfBirthYearMonthDay() != null
		&& person.getNationality() != null && person.getParishOfBirth() != null
		&& person.getDistrictSubdivisionOfBirth() != null && person.getDistrictOfBirth() != null
		&& person.getCountryOfBirth() != null && person.getNameOfFather() != null
		&& person.getNameOfMother() != null && person.hasDefaultPhysicalAddress() && person
		.getEmail() != null);
    }

    @Override
    public boolean canChangePersonalData() {
	return true;
    }

    @Override
    public void checkConditionsToForward(String nextState) {
	checkConditionsToForward();
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.STAND_BY;
    }

    @Override
    public Set<String> getValidNextStates() {
	Set<String> nextStates = new HashSet<String>();
	nextStates.add(CandidacySituationType.STAND_BY_FILLED_DATA.toString());
	nextStates.add(CandidacySituationType.STAND_BY_CONFIRMED_DATA.toString());
	return nextStates;
    }

    @Override
    public void nextState(String nextState) {
	CandidacySituationType situationType = CandidacySituationType.valueOf(nextState);
	switch (situationType) {
	case STAND_BY_FILLED_DATA:
	    nextState();
	    break;
	case STAND_BY_CONFIRMED_DATA:
	    new StandByConfirmedDataCandidacySituation(getCandidacy());
	    break;
	default:
	    throw new DomainException("invalid next state");
	}
    }

    @Override
    public boolean getCanCandidacyDataBeValidated() {
	return true;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	return false;
    }
}