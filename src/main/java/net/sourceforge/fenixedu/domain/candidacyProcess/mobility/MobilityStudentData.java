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

import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;

import org.fenixedu.bennu.core.domain.Bennu;

public class MobilityStudentData extends MobilityStudentData_Base {

    public MobilityStudentData() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MobilityStudentData(MobilityIndividualApplication erasmusIndividualCandidacy,
            MobilityStudentDataBean erasmusStudentDataBean, MobilityQuota selectedOpening) {
        setDateOfArrival(erasmusStudentDataBean.getDateOfArrival());
        setDateOfDeparture(erasmusStudentDataBean.getDateOfDeparture());
        setDiplomaConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
        setDiplomaName(erasmusStudentDataBean.getDiplomaName());
        setMobilityIndividualApplication(erasmusIndividualCandidacy);
        setExperienceCarryingOutProject(erasmusStudentDataBean.getExperienceCarryingOutProject());
        setHasContactedOtherStaff(erasmusStudentDataBean.getHasContactedOtherStaff());
        setHasDiplomaOrDegree(erasmusStudentDataBean.getHasDiplomaOrDegree());
        setHomeInstitutionAddress(erasmusStudentDataBean.getHomeInstitutionAddress());
        setHomeInstitutionEmail(erasmusStudentDataBean.getHomeInstitutionEmail());
        setHomeInstitutionCoordinatorName(erasmusStudentDataBean.getHomeInstitutionExchangeCoordinatorName());
        setHomeInstitutionFax(erasmusStudentDataBean.getHomeInstitutionFax());
        setHomeInstitutionName(erasmusStudentDataBean.getHomeInstitutionName());
        setHomeInstitutionPhone(erasmusStudentDataBean.getHomeInstitutionPhone());
        setMobilityCountryHighSchool(erasmusStudentDataBean.getCountryWhereFinishedHighSchoolLevel());
        setMainSubjectThesis(erasmusStudentDataBean.getMainSubjectThesis());
        setNameOfContact(erasmusStudentDataBean.getNameOfContact());
        setTypesOfProgramme(erasmusStudentDataBean.getTypeOfProgrammeList());
        setSelectedOpening(selectedOpening);
        setCurrentSchoolLevel(erasmusStudentDataBean.getCurrentSchoolLevel());
        setCurrentOtherSchoolLevel(erasmusStudentDataBean.getCurrentOtherSchoolLevel());
        setSchoolLevel(erasmusStudentDataBean.getSchoolLevel());
        setOtherSchoolLevel(erasmusStudentDataBean.getOtherSchoolLevel());

        setPtStudyingLanguage(erasmusStudentDataBean.getPtStudyingLanguage());
        setPtAbleFollowLecures(erasmusStudentDataBean.getPtAbleFollowLecures());
        setPtAbleToFollowLectureWithExtraPreparation(erasmusStudentDataBean.getPtAbleToFollowLectureWithExtraPreparation());
        setEnStudyingLanguage(erasmusStudentDataBean.getEnStudyingLanguage());
        setEnAbleFollowLecures(erasmusStudentDataBean.getEnAbleFollowLecures());
        setEnAbleToFollowLectureWithExtraPreparation(erasmusStudentDataBean.getEnAbleToFollowLectureWithExtraPreparation());
        setIntensivePortugueseCourseSeptember(erasmusStudentDataBean.getIntensivePortugueseCourseSeptember());
        setIntensivePortugueseCourseFebruary(erasmusStudentDataBean.getIntensivePortugueseCourseFebruary());

        if (((MobilityApplicationProcess) erasmusIndividualCandidacy.getCandidacyProcess().getCandidacyProcess())
                .getForSemester().equals(ErasmusApplyForSemesterType.FIRST_SEMESTER)) {
            setApplyFor(erasmusStudentDataBean.getApplyFor());
        } else {
            setApplyFor(ErasmusApplyForSemesterType.SECOND_SEMESTER);
        }
    }

    public void edit(MobilityStudentDataBean erasmusStudentDataBean) {
        setDateOfArrival(erasmusStudentDataBean.getDateOfArrival());
        setDateOfDeparture(erasmusStudentDataBean.getDateOfDeparture());
        setDiplomaConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
        setDiplomaName(erasmusStudentDataBean.getDiplomaName());
        setExperienceCarryingOutProject(erasmusStudentDataBean.getExperienceCarryingOutProject());
        setHasContactedOtherStaff(erasmusStudentDataBean.getHasContactedOtherStaff());
        setHasDiplomaOrDegree(erasmusStudentDataBean.getHasDiplomaOrDegree());
        setHomeInstitutionAddress(erasmusStudentDataBean.getHomeInstitutionAddress());
        setHomeInstitutionEmail(erasmusStudentDataBean.getHomeInstitutionEmail());
        setHomeInstitutionCoordinatorName(erasmusStudentDataBean.getHomeInstitutionExchangeCoordinatorName());
        setHomeInstitutionFax(erasmusStudentDataBean.getHomeInstitutionFax());
        setHomeInstitutionName(erasmusStudentDataBean.getHomeInstitutionName());
        setHomeInstitutionPhone(erasmusStudentDataBean.getHomeInstitutionPhone());
        setMobilityCountryHighSchool(erasmusStudentDataBean.getCountryWhereFinishedHighSchoolLevel());
        setMainSubjectThesis(erasmusStudentDataBean.getMainSubjectThesis());
        setNameOfContact(erasmusStudentDataBean.getNameOfContact());
        setTypesOfProgramme(erasmusStudentDataBean.getTypeOfProgrammeList());
        setCurrentSchoolLevel(erasmusStudentDataBean.getCurrentSchoolLevel());
        setCurrentOtherSchoolLevel(erasmusStudentDataBean.getCurrentOtherSchoolLevel());
        setSchoolLevel(erasmusStudentDataBean.getSchoolLevel());
        setOtherSchoolLevel(erasmusStudentDataBean.getOtherSchoolLevel());

        setPtStudyingLanguage(erasmusStudentDataBean.getPtStudyingLanguage());
        setPtAbleFollowLecures(erasmusStudentDataBean.getPtAbleFollowLecures());
        setPtAbleToFollowLectureWithExtraPreparation(erasmusStudentDataBean.getPtAbleToFollowLectureWithExtraPreparation());
        setEnStudyingLanguage(erasmusStudentDataBean.getEnStudyingLanguage());
        setEnAbleFollowLecures(erasmusStudentDataBean.getEnAbleFollowLecures());
        setEnAbleToFollowLectureWithExtraPreparation(erasmusStudentDataBean.getEnAbleToFollowLectureWithExtraPreparation());
        setIntensivePortugueseCourseSeptember(erasmusStudentDataBean.getIntensivePortugueseCourseSeptember());
        setIntensivePortugueseCourseFebruary(erasmusStudentDataBean.getIntensivePortugueseCourseFebruary());
        setApplyFor(erasmusStudentDataBean.getApplyFor());

    }

    @Deprecated
    public boolean hasIntensivePortugueseCourseFebruary() {
        return getIntensivePortugueseCourseFebruary() != null;
    }

    @Deprecated
    public boolean hasSelectedOpening() {
        return getSelectedOpening() != null;
    }

    @Deprecated
    public boolean hasHomeInstitutionPhone() {
        return getHomeInstitutionPhone() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExperienceCarryingOutProject() {
        return getExperienceCarryingOutProject() != null;
    }

    @Deprecated
    public boolean hasHomeInstitutionAddress() {
        return getHomeInstitutionAddress() != null;
    }

    @Deprecated
    public boolean hasDateOfArrival() {
        return getDateOfArrival() != null;
    }

    @Deprecated
    public boolean hasHomeInstitutionFax() {
        return getHomeInstitutionFax() != null;
    }

    @Deprecated
    public boolean hasPtAbleFollowLecures() {
        return getPtAbleFollowLecures() != null;
    }

    @Deprecated
    public boolean hasDiplomaConclusionYear() {
        return getDiplomaConclusionYear() != null;
    }

    @Deprecated
    public boolean hasSelectedVacancy() {
        return getSelectedVacancy() != null;
    }

    @Deprecated
    public boolean hasHomeInstitutionEmail() {
        return getHomeInstitutionEmail() != null;
    }

    @Deprecated
    public boolean hasSchoolLevel() {
        return getSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasHasDiplomaOrDegree() {
        return getHasDiplomaOrDegree() != null;
    }

    @Deprecated
    public boolean hasDateOfDeparture() {
        return getDateOfDeparture() != null;
    }

    @Deprecated
    public boolean hasPtStudyingLanguage() {
        return getPtStudyingLanguage() != null;
    }

    @Deprecated
    public boolean hasNameOfContact() {
        return getNameOfContact() != null;
    }

    @Deprecated
    public boolean hasIntensivePortugueseCourseSeptember() {
        return getIntensivePortugueseCourseSeptember() != null;
    }

    @Deprecated
    public boolean hasOtherSchoolLevel() {
        return getOtherSchoolLevel() != null;
    }

    @Deprecated
    public boolean hasPtAbleToFollowLectureWithExtraPreparation() {
        return getPtAbleToFollowLectureWithExtraPreparation() != null;
    }

    @Deprecated
    public boolean hasHomeInstitutionCoordinatorName() {
        return getHomeInstitutionCoordinatorName() != null;
    }

    @Deprecated
    public boolean hasHasContactedOtherStaff() {
        return getHasContactedOtherStaff() != null;
    }

    @Deprecated
    public boolean hasApplyFor() {
        return getApplyFor() != null;
    }

    @Deprecated
    public boolean hasMobilityIndividualApplication() {
        return getMobilityIndividualApplication() != null;
    }

    @Deprecated
    public boolean hasTypesOfProgramme() {
        return getTypesOfProgramme() != null;
    }

    @Deprecated
    public boolean hasHomeInstitutionName() {
        return getHomeInstitutionName() != null;
    }

    @Deprecated
    public boolean hasMainSubjectThesis() {
        return getMainSubjectThesis() != null;
    }

    @Deprecated
    public boolean hasDiplomaName() {
        return getDiplomaName() != null;
    }

    @Deprecated
    public boolean hasEnAbleFollowLecures() {
        return getEnAbleFollowLecures() != null;
    }

    @Deprecated
    public boolean hasEnStudyingLanguage() {
        return getEnStudyingLanguage() != null;
    }

    @Deprecated
    public boolean hasEnAbleToFollowLectureWithExtraPreparation() {
        return getEnAbleToFollowLectureWithExtraPreparation() != null;
    }

}
