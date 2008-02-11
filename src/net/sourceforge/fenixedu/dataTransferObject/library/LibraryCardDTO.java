package net.sourceforge.fenixedu.dataTransferObject.library;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearcherContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.LibraryCardUnitsProvider;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.YearMonthDay;

public class LibraryCardDTO implements Serializable {

    private DomainReference<LibraryCard> libraryCard;

    private DomainReference<Person> person;

    private DomainReference<Unit> unit;

    private PartyClassification partyClassification;

    private String userName;

    private String unitName;

    private String chosenUnitName;

    private Integer number;

    private String phone;

    private String mobile;

    private Integer pin;

    private Boolean unlimitedCard;

    private YearMonthDay validUntil;

    public LibraryCardDTO(Person person, PartyClassification partyClassification) {
	this(person, partyClassification, getPersonUnit(person, partyClassification));
    }

    public LibraryCardDTO(Person person, PartyClassification partyClassification, Unit unit) {
	setPerson(person);
	setPartyClassification(partyClassification);
	setUserName(person.getNickname());
	setUnit(unit);
	String unitName = unit != null ? unit.getName() : "";
	if (isStudent(partyClassification)) {
	    unitName = unit != null ? unit.getAcronym() : "";
	} else if (partyClassification.equals(PartyClassification.PERSON)) {
	    if (person.hasExternalContract()) {
		unitName = person.getExternalContract().getInstitutionUnit().getPresentationName();
	    }
	}
	setUnitName(unitName);
	setChosenUnitName(unitName);
	setNumber(getMostSignificantNumber());
	setPhone(person.getPhone());
	setMobile(person.getMobile());
    }

    private Integer getMostSignificantNumber() {
	if (getPartyClassification() == PartyClassification.TEACHER) {
	    return getPerson().getTeacher().getTeacherNumber();
	}
	if (getPartyClassification() == PartyClassification.EMPLOYEE) {
	    return getPerson().getEmployee().getEmployeeNumber();
	}
	if (getPartyClassification() == PartyClassification.RESEARCHER && getPerson().getEmployee() != null) {
	    return getPerson().getEmployee().getEmployeeNumber();
	}
	if (isStudent(getPartyClassification())) {
	    return getPerson().getStudent().getNumber();
	}
	if (getPartyClassification() == PartyClassification.GRANT_OWNER) {
	    return getPerson().getGrantOwner().getNumber();
	}
	return 0;
    }

    public LibraryCardDTO(LibraryCard libraryCard) {
	setLibraryCard(libraryCard);
	setPin(libraryCard.getPin());
	setPerson(libraryCard.getPerson());
	setPartyClassification(libraryCard.getPartyClassification());
	setUserName(libraryCard.getUserName());
	setUnitName(libraryCard.getUnitName());
	setChosenUnitName(libraryCard.getUnitName());
	setNumber(libraryCard.getPerson().getMostSignificantNumber());
	setValidUntil(libraryCard.getValidUntil());
	setPhone(libraryCard.getPerson().getPhone());
	setMobile(libraryCard.getPerson().getMobile());
    }

    private static Unit getPersonUnit(Person person, PartyClassification partyClassification) {
	if (partyClassification == PartyClassification.EMPLOYEE) {
	    return person.getEmployee().getLastWorkingPlace();
	} else if (partyClassification == PartyClassification.TEACHER) {
	    return person.getTeacher().getCurrentWorkingUnit();
	} else if (partyClassification == PartyClassification.RESEARCHER) {
	    YearMonthDay today = new YearMonthDay();
	    for (Accountability accountability : person.getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT,
		    ResearcherContract.class)) {
		ResearcherContract researcherContract = (ResearcherContract) accountability;
		if (researcherContract.isActive(today)) {
		    return researcherContract.getUnit();
		}
	    }
	} else if (isStudent(partyClassification)) {
	    return person.getStudentByType(DegreeType.valueOf(partyClassification.toString())).getDegree().getUnit();
	}
	return null;
    }

    public static boolean isStudent(PartyClassification partyClassification) {
	return partyClassification == PartyClassification.BOLONHA_ADVANCED_FORMATION_DIPLOMA
		|| partyClassification == PartyClassification.BOLONHA_DEGREE
		|| partyClassification == PartyClassification.BOLONHA_INTEGRATED_MASTER_DEGREE
		|| partyClassification == PartyClassification.BOLONHA_MASTER_DEGREE
		|| partyClassification == PartyClassification.BOLONHA_SPECIALIZATION_DEGREE
		|| partyClassification == PartyClassification.DEGREE
		|| partyClassification == PartyClassification.MASTER_DEGREE;
    }

    public String getMailCostCenterCode() {
	if (getPerson().getEmployee() != null && getPerson().getEmployee().getCurrentMailingPlace() != null) {
	    return getPerson().getEmployee().getCurrentMailingPlace().getCostCenterCode().toString();
	}
	return "";
    }

    public Integer getLibraryCardID() {
	return getLibraryCard() != null ? getLibraryCard().getIdInternal() : 0;
    }

    public boolean getIsToGenerate() {
	return getLibraryCard() == null;
    }

    public boolean getIsToViewDetails() {
	return !getIsToGenerate();
    }

    public LibraryCard getLibraryCard() {
	return libraryCard != null ? libraryCard.getObject() : null;
    }

    public void setLibraryCard(LibraryCard libraryCard) {
	this.libraryCard = libraryCard != null ? new DomainReference<LibraryCard>(libraryCard) : null;
    }

    public Person getPerson() {
	return person != null ? person.getObject() : null;
    }

    public void setPerson(Person person) {
	this.person = person != null ? new DomainReference<Person>(person) : null;
    }

    public Unit getUnit() {
	return unit != null ? unit.getObject() : null;
    }

    public void setUnit(Unit unit) {
	this.unit = unit != null ? new DomainReference<Unit>(unit) : null;
    }

    public PartyClassification getPartyClassification() {
	return partyClassification;
    }

    public void setPartyClassification(PartyClassification partyClassification) {
	this.partyClassification = partyClassification;
    }

    public String getCategory() {
	if (getPartyClassification().equals(PartyClassification.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
	    return "DFA";
	}
	if (getPartyClassification().equals(PartyClassification.BOLONHA_DEGREE)) {
	    return "Lic. Bolonha";
	}
	if (getPartyClassification().equals(PartyClassification.BOLONHA_SPECIALIZATION_DEGREE)) {
	    return "Curso Especialização";
	}
	if (getPartyClassification().equals(PartyClassification.PERSON)) {
	    return "Externa";
	}
	final ResourceBundle enumerationBundle = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils
		.getLocale());
	return enumerationBundle.getString(getPartyClassification().name());
    }

    public String getUnitName() {
	return unitName;
    }

    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public Integer getNumber() {
	return number;
    }

    public void setNumber(Integer number) {
	this.number = number;
    }

    public String getMobile() {
	return mobile;
    }

    public void setMobile(String mobile) {
	this.mobile = mobile != null ? mobile : "";
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone != null ? phone : "";
    }

    public String getPinToShow() {
	if (getPin() != null) {
	    String pinToShow = getPin().toString();
	    if (getPin() < 10000) {
		pinToShow = "0" + pinToShow;
	    }
	    return pinToShow;
	}
	return "";
    }

    public Integer getPin() {
	return pin;
    }

    public void setPin(Integer pin) {
	this.pin = pin;
    }

    public String getDay() {
	if (getValidUntil() != null) {
	    int day = getValidUntil().getDayOfMonth();
	    if (day < 10) {
		return "0" + day;
	    }
	    return new Integer(day).toString();
	}
	return "";
    }

    public String getMonth() {
	if (getValidUntil() != null) {
	    int month = getValidUntil().getMonthOfYear();
	    if (month < 10) {
		return "0" + month;
	    }
	    return new Integer(month).toString();
	}
	return "";
    }

    public String getYear() {
	if (getValidUntil() != null) {
	    String a = new Integer(getValidUntil().getYear()).toString();
	    return a.substring(2);
	}
	return "";
    }

    public Boolean getUnlimitedCard() {
	return unlimitedCard;
    }

    public void setUnlimitedCard(Boolean unlimitedCard) {
	this.unlimitedCard = unlimitedCard;
    }

    public YearMonthDay getValidUntil() {
	return validUntil;
    }

    public void setValidUntil(YearMonthDay validUntil) {
	this.validUntil = validUntil;
    }

    public String getChosenUnitName() {
	return chosenUnitName;
    }

    public void setChosenUnitName(String chosenUnitName) {
	if (chosenUnitName != null
		&& (getPartyClassification().equals(PartyClassification.TEACHER) && !getPerson().getPartyClassification().equals(
			PartyClassification.PERSON))) {
	    int position = chosenUnitName.indexOf("-");
	    this.chosenUnitName = chosenUnitName.substring(position + 2);
	} else {
	    this.chosenUnitName = chosenUnitName;
	}
    }

    public String getNumberToPDF() {
	return getNumber() != 0 ? getNumber().toString() : "";
    }

    public void setChosenUnitNameToEdit() {
	List<String> unitsNames = (List<String>) new LibraryCardUnitsProvider().provide(null, null);
	for (String unitName : unitsNames) {
	    if (unitName.contains(getChosenUnitName())) {
		this.chosenUnitName = unitName;
	    }
	}
    }
}
