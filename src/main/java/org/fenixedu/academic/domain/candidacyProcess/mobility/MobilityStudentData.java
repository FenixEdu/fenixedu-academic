/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.candidacyProcess.mobility;

import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;
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

}
