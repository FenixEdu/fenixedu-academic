package net.sourceforge.fenixedu.dataTransferObject.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearcherContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.LibraryCardUnitsProvider;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class LibraryCardDTO implements Serializable {

    private LibraryCard libraryCard;

    private Person person;

    private Unit unit;

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

    private static String separator = " / ";

    private static String nullValue = "-";

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
	setPhone(person.getDefaultPhone() != null ? person.getDefaultPhone().getNumber() : null);
	setMobile(person.getDefaultMobilePhone() != null ? person.getDefaultMobilePhone().getNumber() : null);
    }

    public LibraryCardDTO(LibraryCard libraryCard) {
	setLibraryCard(libraryCard);
	setPerson(libraryCard.getPerson());
	setPartyClassification(libraryCard.getPartyClassification());
	setUserName(libraryCard.getUserName());
	setUnitName(libraryCard.getUnitName());
	setChosenUnitName(libraryCard.getUnitName());
	setNumber(libraryCard.getPerson().getMostSignificantNumber());
	setValidUntil(libraryCard.getValidUntil());
	setPhone(libraryCard.getPerson().getDefaultPhone() != null ? libraryCard.getPerson().getDefaultPhone().getNumber() : null);
	setMobile(libraryCard.getPerson().getDefaultMobilePhone() != null ? libraryCard.getPerson().getDefaultMobilePhone()
		.getNumber() : null);
    }

    private Integer getMostSignificantNumber() {
	if (getPartyClassification() == PartyClassification.TEACHER && getPerson().getEmployee() != null) {
	    return getPerson().getEmployee().getEmployeeNumber();
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
		|| partyClassification == PartyClassification.DEGREE || partyClassification == PartyClassification.MASTER_DEGREE;
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
	return getPerson() != null ? getPerson().getLibraryCard() : null;
    }

    public void setLibraryCard(LibraryCard libraryCard) {
	this.libraryCard = libraryCard;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
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
	final ResourceBundle enumerationBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
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
	return this.pin == null ? getLibraryCard() != null ? getLibraryCard().getPin() : null : pin;
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
	    return Integer.toString(day);
	}
	return "";
    }

    public String getMonth() {
	if (getValidUntil() != null) {
	    int month = getValidUntil().getMonthOfYear();
	    if (month < 10) {
		return "0" + month;
	    }
	    return Integer.toString(month);
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
	if (!StringUtils.isEmpty(chosenUnitName)
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

    public String getLastContractOrEnrolmentDates() {
	StringBuilder dates = new StringBuilder();
	if (partyClassification.equals(PartyClassification.TEACHER)) {
	    if (getPerson().getTeacher() != null) {
		PersonContractSituation currentOrLastTeacherContractSituation = getPerson().getTeacher()
			.getCurrentOrLastTeacherContractSituation();
		dates.append(currentOrLastTeacherContractSituation.getBeginDate()).append(separator);
		if (currentOrLastTeacherContractSituation.getEndDate() != null) {
		    dates.append(currentOrLastTeacherContractSituation.getEndDate());
		} else {
		    dates.append(nullValue);
		}
	    }
	} else if (partyClassification.equals(PartyClassification.EMPLOYEE)) {
	    PersonContractSituation currentEmployeeContractSituation = getPerson().hasEmployee() ? getPerson().getEmployee()
		    .getCurrentEmployeeContractSituation() : null;
	    if (currentEmployeeContractSituation != null) {
		dates.append(currentEmployeeContractSituation.getBeginDate()).append(separator);
		if (currentEmployeeContractSituation.getEndDate() != null) {
		    dates.append(currentEmployeeContractSituation.getEndDate());
		} else {
		    dates.append(nullValue);
		}
	    }
	} else if (partyClassification.equals(PartyClassification.GRANT_OWNER)) {
	    GrantOwner grantOwner = getPerson().getGrantOwner();
	    if (grantOwner != null) {
		List<GrantContractRegime> contractRegimeList = new ArrayList<GrantContractRegime>();

		for (GrantContract contract : grantOwner.getGrantContracts()) {
		    contractRegimeList.addAll(contract.getContractRegimes());
		}
		Collections.sort(contractRegimeList, new BeanComparator("dateBeginContractYearMonthDay"));
		GrantContractRegime currentOrLastContractRegime = null;
		LocalDate today = new LocalDate();
		for (GrantContractRegime contractRegime : contractRegimeList) {
		    if (currentOrLastContractRegime == null) {
			currentOrLastContractRegime = contractRegime;
		    } else if (contractRegime.getDateBeginContractYearMonthDay().isAfter(
			    currentOrLastContractRegime.getDateBeginContractYearMonthDay())
			    && contractRegime.getDateBeginContractYearMonthDay().isBefore(today)) {
			currentOrLastContractRegime = contractRegime;
		    }
		}
		if (currentOrLastContractRegime != null) {
		    dates.append(currentOrLastContractRegime.getDateBeginContractYearMonthDay()).append(separator)
			    .append(currentOrLastContractRegime.getDateEndContractYearMonthDay());
		}
	    }
	} else if (partyClassification.equals(PartyClassification.MASTER_DEGREE)
		|| partyClassification.equals(PartyClassification.DEGREE)
		|| partyClassification.equals(PartyClassification.BOLONHA_SPECIALIZATION_DEGREE)
		|| partyClassification.equals(PartyClassification.BOLONHA_ADVANCED_FORMATION_DIPLOMA)
		|| partyClassification.equals(PartyClassification.BOLONHA_MASTER_DEGREE)
		|| partyClassification.equals(PartyClassification.BOLONHA_INTEGRATED_MASTER_DEGREE)
		|| partyClassification.equals(PartyClassification.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA)
		|| partyClassification.equals(PartyClassification.BOLONHA_DEGREE)) {
	    Student student = getPerson().getStudent();
	    if (student != null) {
		Registration registration = student.getLastRegistrationForDegreeType(DegreeType.valueOf(partyClassification
			.name()));
		if (registration != null) {
		    ExecutionYear executionYear = registration.getLastEnrolmentExecutionYear();
		    if (executionYear != null) {
			dates.append(executionYear.getBeginDateYearMonthDay()).append(separator)
				.append(executionYear.getEndDateYearMonthDay());
		    }
		}
	    }
	}

	return dates.toString();
    }

    public PartyClassification getPersonPartyClassification() {
	return getPerson().getPartyClassification();
    }

    public PartyClassification getLibraryCardPartyClassification() {
	return getLibraryCard() != null ? getLibraryCard().getPartyClassification() : null;
    }

    public boolean getHasEqualPartyClassification() {
	return getPersonPartyClassification().equals(getLibraryCardPartyClassification());
    }

}
