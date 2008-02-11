package net.sourceforge.fenixedu.dataTransferObject.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.lang.StringUtils;

public class LibraryCardSearch implements Serializable {

    private class PersonSearchSet extends HashSet<Person> {

	private List<LibraryCardDTO> libraryCardDTOs = new ArrayList<LibraryCardDTO>();

	@Override
	public boolean add(final Person person) {
	    if (!contains(person) && satisfiesSearch(person)) {
		if (person.hasLibraryCard()) {
		    libraryCardDTOs.add(new LibraryCardDTO(person.getLibraryCard()));
		} else {
		    libraryCardDTOs.add(new LibraryCardDTO(person, getPartyClassification()));
		}
		return super.add(person);
	    }
	    return false;
	}

	public boolean add(final StudentCurricularPlan studentCurricularPlan) {
	    final Person person = studentCurricularPlan.getRegistration().getPerson();
	    if (!contains(person) && satisfiesNumber(person) && satisfiesUserName(person)) {
		if (person.hasLibraryCard()) {
		    libraryCardDTOs.add(new LibraryCardDTO(person.getLibraryCard()));
		} else {
		    libraryCardDTOs.add(new LibraryCardDTO(person, getPartyClassification(), studentCurricularPlan.getDegree().getUnit()));
		}
		return super.add(person);
	    }
	    return false;
	}

	private boolean satisfiesSearch(Person person) {
	    return satisfiesCategory(person) && satisfiesNumber(person) && satisfiesUserName(person);
	}

	private boolean satisfiesUserName(Person person) {
	    return StringUtils.isEmpty(getUserName())
		    || net.sourceforge.fenixedu.util.StringUtils.verifyContainsWithEquality(person.getName(), getUserName());
	}

	private boolean satisfiesCategory(final Person person) {
	    if (getPartyClassification() == PartyClassification.PERSON) {
		if (person.getLibraryCard() != null && person.getLibraryCard().getPartyClassification() == getPartyClassification()) {
		    final PartyClassification personClassification = person.getPartyClassification();
		    if (personClassification == PartyClassification.PERSON) {
			return true;
		    }
		}
	    }
	    final PartyClassification partyClassification = person.getLibraryCard() != null ? person.getLibraryCard()
		    .getPartyClassification() : person.getPartyClassification();
	    return getPartyClassification() == partyClassification;
	}

	private boolean satisfiesNumber(Person person) {
	    //return getNumber() == null || getNumber().equals(person.getMostSignificantNumber());
	    if (getNumber() == null) {
		return true;
	    }
	    final int n = getNumber().intValue();
	    return person.getTeacher() != null && person.getTeacher().getTeacherNumber().intValue() == n
	    		&& person.getEmployee() != null && person.getEmployee().getEmployeeNumber().intValue() == n
	    		&& person.getStudent() != null && person.getStudent().getNumber().intValue() == n
	    		&& person.getGrantOwner() != null && person.getGrantOwner().getNumber().intValue() == n;
	}
    }

    private PartyClassification partyClassification;

    private String userName;

    private Integer number;

    private List<LibraryCardDTO> searchResult;

    public LibraryCardSearch() {
    }

    public LibraryCardSearch(PartyClassification partyClassification) {
	setPartyClassification(partyClassification);
    }

    public LibraryCardSearch(PartyClassification partyClassification, String name, Integer number) {
	setPartyClassification(partyClassification);
	setUserName(name);
	setNumber(number);
    }

    public void doSearch() {
	final PersonSearchSet resultSet = new PersonSearchSet();
	getAssociatedPersons(resultSet, getPartyClassification());
	getPersonsByCardClassification(resultSet, getPartyClassification());
	setSearchResult(resultSet.libraryCardDTOs);
    }

    private void getAssociatedPersons(final PersonSearchSet resultSet, PartyClassification partyClassification) {
	if (partyClassification == null) {
	    resultSet.addAll(Role.getRoleByRoleType(RoleType.TEACHER).getAssociatedPersons());
	} else {
	    switch (partyClassification) {
	    case TEACHER:
		resultSet.addAll(Role.getRoleByRoleType(RoleType.TEACHER).getAssociatedPersons());
		break;
	    case EMPLOYEE:
		resultSet.addAll(Role.getRoleByRoleType(RoleType.EMPLOYEE).getAssociatedPersons());
		break;
	    case RESEARCHER:
		resultSet.addAll(Role.getRoleByRoleType(RoleType.RESEARCHER).getAssociatedPersons());
		break;
	    case GRANT_OWNER:
		getGrantOwners(resultSet);
		break;
	    case MASTER_DEGREE:
		getPersonsFromDegreeType(resultSet, DegreeType.MASTER_DEGREE);
		break;
	    case DEGREE:
		getPersonsFromDegreeType(resultSet, DegreeType.DEGREE);
		break;
	    case BOLONHA_DEGREE:
		getPersonsFromDegreeType(resultSet, DegreeType.BOLONHA_DEGREE);
		break;
	    case BOLONHA_MASTER_DEGREE:
		getPersonsFromDegreeType(resultSet, DegreeType.BOLONHA_MASTER_DEGREE);
		break;
	    case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
		getPersonsFromDegreeType(resultSet, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
		break;
	    case BOLONHA_INTEGRATED_MASTER_DEGREE:
		getPersonsFromDegreeType(resultSet, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
		break;
	    case BOLONHA_PHD_PROGRAM:
		getPersonsFromDegreeType(resultSet, DegreeType.BOLONHA_PHD_PROGRAM);
		break;
	    case BOLONHA_SPECIALIZATION_DEGREE:
		getPersonsFromDegreeType(resultSet, DegreeType.BOLONHA_SPECIALIZATION_DEGREE);
		break;
	    case PERSON:
		getPersons(resultSet, getUserName(), 200);
		break;
	    default:
		resultSet.addAll(Role.getRoleByRoleType(RoleType.TEACHER).getAssociatedPersons());
	    }
	}
    }

    private void getGrantOwners(PersonSearchSet resultSet) {
	resultSet.addAll(Role.getRoleByRoleType(RoleType.GRANT_OWNER).getAssociatedPersonsSet());
	getPersonsByCardClassification(resultSet, PartyClassification.GRANT_OWNER);
    }

    private void getPersonsByCardClassification(PersonSearchSet resultSet, PartyClassification partyClassification) {
	for (LibraryCard libraryCard : RootDomainObject.getInstance().getLibraryCards()) {
	    if (libraryCard.getPartyClassification() == partyClassification) {
		resultSet.add(libraryCard.getPerson());
	    }
	}
    }

    private void getPersons(PersonSearchSet resultSet, String userName, int size) {
	if (StringUtils.isEmpty(userName)) {
	    getPersonsByCardClassification(resultSet, PartyClassification.PERSON);
	} else {
	    Collection<PersonName> personNames = PersonName.find(userName, size);
	    for (PersonName personName : personNames) {
		resultSet.add(personName.getPerson());
	    }
	}
    }

    private void getPersonsFromDegreeType(PersonSearchSet resultSet, DegreeType degreeType) {
	ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

	for (final Degree degree : RootDomainObject.getInstance().getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (degreeCurricularPlan.hasExecutionDegreeFor(executionYear)) {
			for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan
				.getStudentCurricularPlansSet()) {
			    if (studentCurricularPlan.isActive(executionYear)) {
				if (studentCurricularPlan.getRegistration() != null
					&& studentCurricularPlan.getRegistration().isActive()) {
				    resultSet.add(studentCurricularPlan);
				}
			    }
			}
		    }
		}
	    }
	}
    }

    public PartyClassification getPartyClassification() {
	return partyClassification;
    }

    public void setPartyClassification(PartyClassification partyClassification) {
	this.partyClassification = partyClassification;
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

    public String getNumberString() {
	if (getNumber() != null) {
	    return getNumber().toString();
	} else {
	    return "";
	}
    }

    public List<LibraryCardDTO> getSearchResult() {
	return searchResult;
    }

    public void setSearchResult(List<LibraryCardDTO> searchResult) {
	this.searchResult = searchResult;
    }
}
