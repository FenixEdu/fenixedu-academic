/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.TypeOfProgramme;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.TypeOfProgrammeList;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class MobilityStudentDataBean implements Serializable {
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
    private Country countryWhereFinishedHighSchoolLevel;

    private SchoolLevelType currentSchoolLevel;
    private String currentOtherSchoolLevel;
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

    private MobilityProgram selectedMobilityProgram;
    private MobilityAgreement mobilityAgreement;
    private SchoolLevelType schoolLevel;
    private String otherSchoolLevel;

    private CandidacyProcess parentProcess;

    private Boolean ptStudyingLanguage;
    private Boolean ptAbleFollowLecures;
    private Boolean ptAbleToFollowLectureWithExtraPreparation;
    private Boolean enStudyingLanguage;
    private Boolean enAbleFollowLecures;
    private Boolean enAbleToFollowLectureWithExtraPreparation;
    private Boolean intensivePortugueseCourseSeptember;
    private Boolean intensivePortugueseCourseFebruary;

    private ErasmusApplyForSemesterType applyFor;

    public MobilityStudentDataBean(CandidacyProcess process, ErasmusApplyForSemesterType applyFor) {
        setParentProcess(process);
        setApplyFor(applyFor);
    }

    public MobilityStudentDataBean(final MobilityStudentData erasmusStudentData) {
        this.setDateOfArrival(erasmusStudentData.getDateOfArrival());
        this.setDateOfDeparture(erasmusStudentData.getDateOfDeparture());
        this.setDiplomaConclusionYear(erasmusStudentData.getDiplomaConclusionYear());
        this.setDiplomaName(erasmusStudentData.getDiplomaName());
        this.setExperienceCarryingOutProject(erasmusStudentData.getExperienceCarryingOutProject());
        this.setHasContactedOtherStaff(erasmusStudentData.getHasContactedOtherStaff());
        this.setCurrentSchoolLevel(erasmusStudentData.getCurrentSchoolLevel());
        this.setCurrentOtherSchoolLevel(erasmusStudentData.getCurrentOtherSchoolLevel());
        this.setHasDiplomaOrDegree(erasmusStudentData.getHasDiplomaOrDegree());
        this.setHomeInstitutionAddress(erasmusStudentData.getHomeInstitutionAddress());
        this.setHomeInstitutionEmail(erasmusStudentData.getHomeInstitutionEmail());
        this.setHomeInstitutionExchangeCoordinatorName(erasmusStudentData.getHomeInstitutionCoordinatorName());
        this.setHomeInstitutionFax(erasmusStudentData.getHomeInstitutionFax());
        this.setHomeInstitutionName(erasmusStudentData.getHomeInstitutionName());
        this.setHomeInstitutionPhone(erasmusStudentData.getHomeInstitutionPhone());
        this.setCountryWhereFinishedHighSchoolLevel(erasmusStudentData.getMobilityCountryHighSchool());
        this.setMainSubjectThesis(erasmusStudentData.getMainSubjectThesis());
        this.setNameOfContact(erasmusStudentData.getNameOfContact());
        this.setTypesOfProgramme(erasmusStudentData.getTypesOfProgramme());
        this.setSelectedUniversity(erasmusStudentData.getSelectedOpening() != null ? erasmusStudentData.getSelectedOpening()
                .getMobilityAgreement().getUniversityUnit() : null);
        this.setSelectedCountry(erasmusStudentData.getSelectedOpening() != null ? erasmusStudentData.getSelectedOpening()
                .getMobilityAgreement().getUniversityUnit().getCountry() : null);
        setParentProcess(erasmusStudentData.getMobilityIndividualApplication().getCandidacyProcess().getCandidacyProcess());
        this.setSelectedMobilityProgram(erasmusStudentData.getSelectedOpening().getMobilityAgreement().getMobilityProgram());

        this.setPtStudyingLanguage(erasmusStudentData.getPtStudyingLanguage());
        this.setPtAbleFollowLecures(erasmusStudentData.getPtAbleFollowLecures());
        this.setPtAbleToFollowLectureWithExtraPreparation(erasmusStudentData.getPtAbleToFollowLectureWithExtraPreparation());
        this.setEnStudyingLanguage(erasmusStudentData.getEnStudyingLanguage());
        this.setEnAbleFollowLecures(erasmusStudentData.getEnAbleFollowLecures());
        this.setEnAbleToFollowLectureWithExtraPreparation(erasmusStudentData.getEnAbleToFollowLectureWithExtraPreparation());
        this.setIntensivePortugueseCourseSeptember(erasmusStudentData.getIntensivePortugueseCourseSeptember());
        this.setIntensivePortugueseCourseFebruary(erasmusStudentData.getIntensivePortugueseCourseFebruary());
        this.setApplyFor(erasmusStudentData.getApplyFor());
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

    public Country getCountryWhereFinishedHighSchoolLevel() {
        return this.countryWhereFinishedHighSchoolLevel;
    }

    public void setCountryWhereFinishedHighSchoolLevel(Country countryHighSchool) {
        this.countryWhereFinishedHighSchoolLevel = countryHighSchool;
    }

    public SchoolLevelType getCurrentSchoolLevel() {
        return currentSchoolLevel;
    }

    public void setCurrentSchoolLevel(SchoolLevelType currentSchoolLevel) {
        this.currentSchoolLevel = currentSchoolLevel;
    }

    public String getCurrentOtherSchoolLevel() {
        return currentOtherSchoolLevel;
    }

    public void setCurrentOtherSchoolLevel(String currentOtherSchoolLevel) {
        this.currentOtherSchoolLevel = currentOtherSchoolLevel;
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

    public MobilityProgram getSelectedMobilityProgram() {
        return selectedMobilityProgram;
    }

    public void setSelectedMobilityProgram(MobilityProgram selectedMobilityProgram) {
        this.selectedMobilityProgram = selectedMobilityProgram;
        setMobilityAgreement();
    }

    public MobilityAgreement getMobilityAgreement() {
        return mobilityAgreement;
    }

    public void setMobilityAgreement() {
        if (selectedMobilityProgram == null || selectedUniversity == null) {
            mobilityAgreement = null;
            return;
        }
        for (MobilityAgreement agreement : selectedMobilityProgram.getMobilityAgreements()) {
            if (agreement.getUniversityUnit() == getSelectedUniversity()) {
                mobilityAgreement = agreement;
                return;
            }
        }
        mobilityAgreement = null;
    }

    public CandidacyProcess getParentProcess() {
        return parentProcess;
    }

    public void setParentProcess(CandidacyProcess parentProcess) {
        this.parentProcess = parentProcess;
    }

    public Boolean getPtStudyingLanguage() {
        return ptStudyingLanguage;
    }

    public void setPtStudyingLanguage(Boolean ptStudyingLanguage) {
        this.ptStudyingLanguage = ptStudyingLanguage;
    }

    public Boolean getPtAbleFollowLecures() {
        return ptAbleFollowLecures;
    }

    public void setPtAbleFollowLecures(Boolean ptAbleFollowLecures) {
        this.ptAbleFollowLecures = ptAbleFollowLecures;
    }

    public Boolean getPtAbleToFollowLectureWithExtraPreparation() {
        return ptAbleToFollowLectureWithExtraPreparation;
    }

    public void setPtAbleToFollowLectureWithExtraPreparation(Boolean ptAbleToFollowLectureWithExtraPreparation) {
        this.ptAbleToFollowLectureWithExtraPreparation = ptAbleToFollowLectureWithExtraPreparation;
    }

    public Boolean getEnStudyingLanguage() {
        return enStudyingLanguage;
    }

    public void setEnStudyingLanguage(Boolean enStudyingLanguage) {
        this.enStudyingLanguage = enStudyingLanguage;
    }

    public Boolean getEnAbleFollowLecures() {
        return enAbleFollowLecures;
    }

    public void setEnAbleFollowLecures(Boolean enAbleFollowLecures) {
        this.enAbleFollowLecures = enAbleFollowLecures;
    }

    public Boolean getEnAbleToFollowLectureWithExtraPreparation() {
        return enAbleToFollowLectureWithExtraPreparation;
    }

    public void setEnAbleToFollowLectureWithExtraPreparation(Boolean enAbleToFollowLectureWithExtraPreparation) {
        this.enAbleToFollowLectureWithExtraPreparation = enAbleToFollowLectureWithExtraPreparation;
    }

    public Boolean getIntensivePortugueseCourseSeptember() {
        return intensivePortugueseCourseSeptember;
    }

    public void setIntensivePortugueseCourseSeptember(Boolean intensivePortugueseCourseSeptember) {
        this.intensivePortugueseCourseSeptember = intensivePortugueseCourseSeptember;
    }

    public Boolean getIntensivePortugueseCourseFebruary() {
        return intensivePortugueseCourseFebruary;
    }

    public void setIntensivePortugueseCourseFebruary(Boolean intensivePortugueseCourseFebruary) {
        this.intensivePortugueseCourseFebruary = intensivePortugueseCourseFebruary;
    }

    public ErasmusApplyForSemesterType getApplyFor() {
        return applyFor;
    }

    public void setApplyFor(ErasmusApplyForSemesterType applyFor) {
        this.applyFor = applyFor;
    }

    public SchoolLevelType getSchoolLevel() {
        return schoolLevel;
    }

    public void setSchoolLevel(SchoolLevelType schoolLevel) {
        this.schoolLevel = schoolLevel;
    }

    public String getOtherSchoolLevel() {
        return otherSchoolLevel;
    }

    public void setOtherSchoolLevel(String otherSchoolLevel) {
        this.otherSchoolLevel = otherSchoolLevel;
    }

    public boolean isSchoolLevelDefined() {
        return (getSchoolLevel() != SchoolLevelType.OTHER || !StringUtils.isEmpty(getOtherSchoolLevel()));
    }
}
