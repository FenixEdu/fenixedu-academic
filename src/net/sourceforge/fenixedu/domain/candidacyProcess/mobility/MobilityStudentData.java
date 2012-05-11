package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;

public class MobilityStudentData extends MobilityStudentData_Base {

    public MobilityStudentData() {
	super();
	this.setRootDomainObject(RootDomainObject.getInstance());
    }

    public MobilityStudentData(MobilityIndividualApplication erasmusIndividualCandidacy,
	    MobilityStudentDataBean erasmusStudentDataBean, MobilityQuota selectedOpening) {
	this.setDateOfArrival(erasmusStudentDataBean.getDateOfArrival());
	this.setDateOfDeparture(erasmusStudentDataBean.getDateOfDeparture());
	this.setDiplomaConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
	this.setDiplomaName(erasmusStudentDataBean.getDiplomaName());
	this.setMobilityIndividualApplication(erasmusIndividualCandidacy);
	this.setExperienceCarryingOutProject(erasmusStudentDataBean.getExperienceCarryingOutProject());
	this.setHasContactedOtherStaff(erasmusStudentDataBean.getHasContactedOtherStaff());
	this.setHasDiplomaOrDegree(erasmusStudentDataBean.getHasDiplomaOrDegree());
	this.setHomeInstitutionAddress(erasmusStudentDataBean.getHomeInstitutionAddress());
	this.setHomeInstitutionEmail(erasmusStudentDataBean.getHomeInstitutionEmail());
	this.setHomeInstitutionCoordinatorName(erasmusStudentDataBean.getHomeInstitutionExchangeCoordinatorName());
	this.setHomeInstitutionFax(erasmusStudentDataBean.getHomeInstitutionFax());
	this.setHomeInstitutionName(erasmusStudentDataBean.getHomeInstitutionName());
	this.setHomeInstitutionPhone(erasmusStudentDataBean.getHomeInstitutionPhone());
	this.setMainSubjectThesis(erasmusStudentDataBean.getMainSubjectThesis());
	this.setNameOfContact(erasmusStudentDataBean.getNameOfContact());
	this.setTypesOfProgramme(erasmusStudentDataBean.getTypeOfProgrammeList());
	this.setSelectedOpening(selectedOpening);

	this.setPtStudyingLanguage(erasmusStudentDataBean.getPtStudyingLanguage());
	this.setPtAbleFollowLecures(erasmusStudentDataBean.getPtAbleFollowLecures());
	this.setPtAbleToFollowLectureWithExtraPreparation(erasmusStudentDataBean.getPtAbleToFollowLectureWithExtraPreparation());
	this.setEnStudyingLanguage(erasmusStudentDataBean.getEnStudyingLanguage());
	this.setEnAbleFollowLecures(erasmusStudentDataBean.getEnAbleFollowLecures());
	this.setEnAbleToFollowLectureWithExtraPreparation(erasmusStudentDataBean.getEnAbleToFollowLectureWithExtraPreparation());
	this.setIntensivePortugueseCourseSeptember(erasmusStudentDataBean.getIntensivePortugueseCourseSeptember());
	this.setIntensivePortugueseCourseFebruary(erasmusStudentDataBean.getIntensivePortugueseCourseFebruary());

	if (((MobilityApplicationProcess) erasmusIndividualCandidacy.getCandidacyProcess().getCandidacyProcess())
		.getForSemester().equals(ErasmusApplyForSemesterType.FIRST_SEMESTER)) {
	    this.setApplyFor(erasmusStudentDataBean.getApplyFor());
	} else {
	    this.setApplyFor(ErasmusApplyForSemesterType.SECOND_SEMESTER);
	}
    }

    public void edit(MobilityStudentDataBean erasmusStudentDataBean) {
	this.setDateOfArrival(erasmusStudentDataBean.getDateOfArrival());
	this.setDateOfDeparture(erasmusStudentDataBean.getDateOfDeparture());
	this.setDiplomaConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
	this.setDiplomaName(erasmusStudentDataBean.getDiplomaName());
	this.setExperienceCarryingOutProject(erasmusStudentDataBean.getExperienceCarryingOutProject());
	this.setHasContactedOtherStaff(erasmusStudentDataBean.getHasContactedOtherStaff());
	this.setHasDiplomaOrDegree(erasmusStudentDataBean.getHasDiplomaOrDegree());
	this.setHomeInstitutionAddress(erasmusStudentDataBean.getHomeInstitutionAddress());
	this.setHomeInstitutionEmail(erasmusStudentDataBean.getHomeInstitutionEmail());
	this.setHomeInstitutionCoordinatorName(erasmusStudentDataBean.getHomeInstitutionExchangeCoordinatorName());
	this.setHomeInstitutionFax(erasmusStudentDataBean.getHomeInstitutionFax());
	this.setHomeInstitutionName(erasmusStudentDataBean.getHomeInstitutionName());
	this.setHomeInstitutionPhone(erasmusStudentDataBean.getHomeInstitutionPhone());
	this.setMainSubjectThesis(erasmusStudentDataBean.getMainSubjectThesis());
	this.setNameOfContact(erasmusStudentDataBean.getNameOfContact());
	this.setTypesOfProgramme(erasmusStudentDataBean.getTypeOfProgrammeList());

	this.setPtStudyingLanguage(erasmusStudentDataBean.getPtStudyingLanguage());
	this.setPtAbleFollowLecures(erasmusStudentDataBean.getPtAbleFollowLecures());
	this.setPtAbleToFollowLectureWithExtraPreparation(erasmusStudentDataBean.getPtAbleToFollowLectureWithExtraPreparation());
	this.setEnStudyingLanguage(erasmusStudentDataBean.getEnStudyingLanguage());
	this.setEnAbleFollowLecures(erasmusStudentDataBean.getEnAbleFollowLecures());
	this.setEnAbleToFollowLectureWithExtraPreparation(erasmusStudentDataBean.getEnAbleToFollowLectureWithExtraPreparation());
	this.setIntensivePortugueseCourseSeptember(erasmusStudentDataBean.getIntensivePortugueseCourseSeptember());
	this.setIntensivePortugueseCourseFebruary(erasmusStudentDataBean.getIntensivePortugueseCourseFebruary());
	this.setApplyFor(erasmusStudentDataBean.getApplyFor());

    }
}
