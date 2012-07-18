package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;

public class MobilityStudentData extends MobilityStudentData_Base {

    public MobilityStudentData() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
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
	setMainSubjectThesis(erasmusStudentDataBean.getMainSubjectThesis());
	setNameOfContact(erasmusStudentDataBean.getNameOfContact());
	setTypesOfProgramme(erasmusStudentDataBean.getTypeOfProgrammeList());
	setSelectedOpening(selectedOpening);
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
	setMainSubjectThesis(erasmusStudentDataBean.getMainSubjectThesis());
	setNameOfContact(erasmusStudentDataBean.getNameOfContact());
	setTypesOfProgramme(erasmusStudentDataBean.getTypeOfProgrammeList());
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
