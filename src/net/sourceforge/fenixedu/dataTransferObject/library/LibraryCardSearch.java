package net.sourceforge.fenixedu.dataTransferObject.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.lang.StringUtils;

public class LibraryCardSearch implements Serializable {

    private PartyClassification partyClassification;

    private String userName;

    private Integer number;

    public LibraryCardSearch() {
    }

    public LibraryCardSearch(PartyClassification partyClassification) {
        setPartyClassification(partyClassification);
    }

    public List<LibraryCardDTO> getSearch() {
        List<LibraryCardDTO> libraryCardDTOList = new ArrayList<LibraryCardDTO>();
        for (Person person : getAssociatedPersons(getPartyClassification())) {
            if (satisfiesSearch(person)) {
                if (person.hasLibraryCard()) {
                    libraryCardDTOList.add(new LibraryCardDTO(person.getLibraryCard()));
                } else {
                    libraryCardDTOList.add(new LibraryCardDTO(person, getPartyClassification()));
                }
            }
        }
        return libraryCardDTOList;
    }

    private List<Person> getAssociatedPersons(PartyClassification partyClassification) {
        if (partyClassification == null) {
            return Role.getRoleByRoleType(RoleType.TEACHER).getAssociatedPersons();
        } else {
            switch (partyClassification) {
            case TEACHER:
                return Role.getRoleByRoleType(RoleType.TEACHER).getAssociatedPersons();
            case EMPLOYEE:
                return Role.getRoleByRoleType(RoleType.EMPLOYEE).getAssociatedPersons();
            case RESEARCHER:
                return Role.getRoleByRoleType(RoleType.RESEARCHER).getAssociatedPersons();
            case GRANT_OWNER:
                return Role.getRoleByRoleType(RoleType.GRANT_OWNER).getAssociatedPersons();
            case MASTER_DEGREE:
                return getPersonsFromDegreeType(DegreeType.MASTER_DEGREE);
            case DEGREE:
                return getPersonsFromDegreeType(DegreeType.DEGREE);
            case BOLONHA_DEGREE:
                return getPersonsFromDegreeType(DegreeType.BOLONHA_DEGREE);
            case BOLONHA_MASTER_DEGREE:
                return getPersonsFromDegreeType(DegreeType.BOLONHA_MASTER_DEGREE);
            case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
                return getPersonsFromDegreeType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
            case BOLONHA_INTEGRATED_MASTER_DEGREE:
                return getPersonsFromDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
            case BOLONHA_SPECIALIZATION_DEGREE:
                return getPersonsFromDegreeType(DegreeType.BOLONHA_SPECIALIZATION_DEGREE);
            case PERSON:
                return Person.readAllExternalPersons();
            default:
                return Role.getRoleByRoleType(RoleType.TEACHER).getAssociatedPersons();
            }
        }
    }

    private List<Person> getPersonsFromDegreeType(DegreeType degreeType) {
        List<Person> persons = new ArrayList<Person>();        
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        //TODO *remove in 2008/2009 when the type DEGREE will no longer be relevant to obtain current data
        if (degreeType.equals(DegreeType.DEGREE) || degreeType.equals(DegreeType.MASTER_DEGREE)) {
            executionYear = executionYear.getPreviousExecutionYear();
        }
        for (Degree degree : Degree.readAllByDegreeType(degreeType)) {
            for (StudentCurricularPlan scp : degree.getStudentCurricularPlans(executionYear)) {
                if (scp.getRegistration().isActive()) {
                    if (!degreeType.equals(DegreeType.DEGREE) && !degreeType.equals(DegreeType.MASTER_DEGREE)) {
                        persons.add(scp.getRegistration().getPerson());
                    } else { // *                                       
                        if (scp.getRegistration().getStudent().getTransitedRegistrations().isEmpty()) {
                            persons.add(scp.getRegistration().getPerson());
                        }
                    }
                }
            }
        }
        return persons;
    }

    private boolean satisfiesSearch(Person person) {
        return satisfiesCategory(person) && satisfiesNumber(person) && satisfiesUserName(person);
    }

    private boolean satisfiesUserName(Person person) {
        return StringUtils.isEmpty(getUserName())
                || net.sourceforge.fenixedu.util.StringUtils.verifyContainsWithEquality(
                        person.getName(), getUserName());
    }

    private boolean satisfiesCategory(Person person) {
        return getPartyClassification() == null
                || getPartyClassification().equals(person.getPartyClassification());
    }

    private boolean satisfiesNumber(Person person) {
        return getNumber() == null || getNumber().equals(person.getMostSignificantNumber());
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

}
