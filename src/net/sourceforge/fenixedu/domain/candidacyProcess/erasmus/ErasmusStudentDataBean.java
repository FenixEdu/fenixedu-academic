package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

import org.joda.time.LocalDate;

public class ErasmusStudentDataBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String homeInstitutionName;
    private String homeInstitutionAddress;
    private String homeInstitutionExchangeCoordinatorName;
    private String homeInstitutionPhone;
    private String homeInstitutionFax;
    private String homeInstitutionEmail;

    private Boolean hasDiplomaOrDegree;
    private Integer diplomaConclusionYear;
    private String diplomaName;
    private Boolean experienceCarryingOutProject;

    private LocalDate dateOfArrival;
    private LocalDate dateOfDeparture;
    List<TypeOfProgramme> typesOfProgramme;
    private String mainSubjectThesis;
    private Boolean hasContactedOtherStaff;
    private String nameOfContact;

    private UniversityUnit selectedUniversity;
    private Country selectedCountry;

    private CandidacyProcess parentProcess;

    public ErasmusStudentDataBean(CandidacyProcess process) {
	setParentProcess(process);
    }

    public ErasmusStudentDataBean(final ErasmusStudentData erasmusStudentData) {
	this.setDateOfArrival(erasmusStudentData.getDateOfArrival());
	this.setDateOfDeparture(erasmusStudentData.getDateOfDeparture());
	this.setDiplomaConclusionYear(erasmusStudentData.getDiplomaConclusionYear());
	this.setDiplomaName(erasmusStudentData.getDiplomaName());
	this.setExperienceCarryingOutProject(erasmusStudentData.getExperienceCarryingOutProject());
	this.setHasContactedOtherStaff(erasmusStudentData.getHasContactedOtherStaff());
	this.setHasDiplomaOrDegree(erasmusStudentData.getHasDiplomaOrDegree());
	this.setHomeInstitutionAddress(erasmusStudentData.getHomeInstitutionAddress());
	this.setHomeInstitutionEmail(erasmusStudentData.getHomeInstitutionEmail());
	this.setHomeInstitutionExchangeCoordinatorName(erasmusStudentData.getHomeInstitutionCoordinatorName());
	this.setHomeInstitutionFax(erasmusStudentData.getHomeInstitutionFax());
	this.setHomeInstitutionName(erasmusStudentData.getHomeInstitutionName());
	this.setHomeInstitutionPhone(erasmusStudentData.getHomeInstitutionPhone());
	this.setMainSubjectThesis(erasmusStudentData.getMainSubjectThesis());
	this.setNameOfContact(erasmusStudentData.getNameOfContact());
	this.setTypesOfProgramme(erasmusStudentData.getTypesOfProgramme());
	this.setSelectedUniversity(erasmusStudentData.getSelectedVacancy().getUniversityUnit());
	this.setSelectedCountry(erasmusStudentData.getSelectedVacancy().getUniversityUnit().getCountry());
	setParentProcess(erasmusStudentData.getErasmusIndividualCandidacy().getCandidacyProcess().getCandidacyProcess());
    }

    private void setTypesOfProgramme(TypeOfProgrammeList typesOfProgramme) {
	this.typesOfProgramme = new ArrayList<TypeOfProgramme>(typesOfProgramme.getTypes());
    }

    public String getHomeInstitutionName() {
	return homeInstitutionName;
    }

    public void setHomeInstitutionName(String homeInstitutionName) {
	this.homeInstitutionName = homeInstitutionName;
    }

    public String getHomeInstitutionAddress() {
	return homeInstitutionAddress;
    }

    public void setHomeInstitutionAddress(String homeInstitutionAddress) {
	this.homeInstitutionAddress = homeInstitutionAddress;
    }

    public String getHomeInstitutionExchangeCoordinatorName() {
	return homeInstitutionExchangeCoordinatorName;
    }

    public void setHomeInstitutionExchangeCoordinatorName(String homeInstitutionExchangeCoordinatorName) {
	this.homeInstitutionExchangeCoordinatorName = homeInstitutionExchangeCoordinatorName;
    }

    public String getHomeInstitutionPhone() {
	return homeInstitutionPhone;
    }

    public void setHomeInstitutionPhone(String homeInstitutionPhone) {
	this.homeInstitutionPhone = homeInstitutionPhone;
    }

    public String getHomeInstitutionFax() {
	return homeInstitutionFax;
    }

    public void setHomeInstitutionFax(String homeInstitutionFax) {
	this.homeInstitutionFax = homeInstitutionFax;
    }

    public String getHomeInstitutionEmail() {
	return homeInstitutionEmail;
    }

    public void setHomeInstitutionEmail(String homeInstitutionEmail) {
	this.homeInstitutionEmail = homeInstitutionEmail;
    }

    public Boolean getHasDiplomaOrDegree() {
	return hasDiplomaOrDegree;
    }

    public void setHasDiplomaOrDegree(Boolean hasDiplomaOrDegree) {
	this.hasDiplomaOrDegree = hasDiplomaOrDegree;
    }

    public Integer getDiplomaConclusionYear() {
	return diplomaConclusionYear;
    }

    public void setDiplomaConclusionYear(Integer diplomaConclusionYear) {
	this.diplomaConclusionYear = diplomaConclusionYear;
    }

    public String getDiplomaName() {
	return diplomaName;
    }

    public void setDiplomaName(String diplomaName) {
	this.diplomaName = diplomaName;
    }

    public Boolean getExperienceCarryingOutProject() {
	return experienceCarryingOutProject;
    }

    public void setExperienceCarryingOutProject(Boolean experienceCarryingOutProject) {
	this.experienceCarryingOutProject = experienceCarryingOutProject;
    }

    public LocalDate getDateOfArrival() {
	return dateOfArrival;
    }

    public void setDateOfArrival(LocalDate dateOfArrival) {
	this.dateOfArrival = dateOfArrival;
    }

    public LocalDate getDateOfDeparture() {
	return dateOfDeparture;
    }

    public void setDateOfDeparture(LocalDate dateOfDeparture) {
	this.dateOfDeparture = dateOfDeparture;
    }

    public List<TypeOfProgramme> getTypesOfProgramme() {
	return typesOfProgramme;
    }

    public void setTypesOfProgramme(List<TypeOfProgramme> typesOfProgramme) {
	this.typesOfProgramme = typesOfProgramme;
    }

    public TypeOfProgrammeList getTypeOfProgrammeList() {
	return new TypeOfProgrammeList(this.typesOfProgramme);
    }

    public String getMainSubjectThesis() {
	return mainSubjectThesis;
    }

    public void setMainSubjectThesis(String mainSubjectThesis) {
	this.mainSubjectThesis = mainSubjectThesis;
    }

    public Boolean getHasContactedOtherStaff() {
	return hasContactedOtherStaff;
    }

    public void setHasContactedOtherStaff(Boolean hasContactedOtherStaff) {
	this.hasContactedOtherStaff = hasContactedOtherStaff;
    }

    public String getNameOfContact() {
	return nameOfContact;
    }

    public void setNameOfContact(String nameOfContact) {
	this.nameOfContact = nameOfContact;
    }

    public UniversityUnit getSelectedUniversity() {
	return selectedUniversity;
    }

    public void setSelectedUniversity(UniversityUnit unit) {
	this.selectedUniversity = unit;
    }

    public Country getSelectedCountry() {
	return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
	this.selectedCountry = selectedCountry;
    }

    public CandidacyProcess getParentProcess() {
	return parentProcess;
    }

    public void setParentProcess(CandidacyProcess parentProcess) {
	this.parentProcess = parentProcess;
    }

}
