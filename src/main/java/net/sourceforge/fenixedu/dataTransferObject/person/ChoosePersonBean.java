package net.sourceforge.fenixedu.dataTransferObject.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.YearMonthDay;

public class ChoosePersonBean implements Serializable {

    private Person person;

    private String name;

    private String identificationNumber;

    private IDDocumentType documentType;

    private YearMonthDay dateOfBirth;

    private boolean firstTimeSearch = true;

    private Integer studentNumber;

    public ChoosePersonBean() {
        super();
    }

    public ChoosePersonBean(IndividualCandidacyPersonalDetails personalDetails) {
        this.name = personalDetails.getName();

        this.identificationNumber = personalDetails.getDocumentIdNumber();
        this.documentType = personalDetails.getIdDocumentType();
        this.dateOfBirth = personalDetails.getDateOfBirthYearMonthDay();
    }

    public ChoosePersonBean(Person person) {
        this();
        setPerson(person);
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean hasPerson() {
        return getPerson() != null;
    }

    public IDDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(IDDocumentType documentType) {
        this.documentType = documentType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public YearMonthDay getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(YearMonthDay dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFirstTimeSearch() {
        return firstTimeSearch;
    }

    public boolean getFirstTimeSearch() {
        return this.firstTimeSearch;
    }

    public void setFirstTimeSearch(boolean firstTimeSearch) {
        this.firstTimeSearch = firstTimeSearch;
    }

    public boolean isEmployee() {
        return getPerson() != null && getPerson().hasRole(RoleType.EMPLOYEE);
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

}
