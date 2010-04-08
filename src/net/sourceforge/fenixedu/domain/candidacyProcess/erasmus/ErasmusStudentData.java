package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ErasmusStudentData extends ErasmusStudentData_Base {

    public ErasmusStudentData() {
	super();
	this.setRootDomainObject(RootDomainObject.getInstance());
    }

    public ErasmusStudentData(ErasmusIndividualCandidacy erasmusIndividualCandidacy, ErasmusStudentDataBean erasmusStudentDataBean, ErasmusVacancy erasmusVacancy) {
	this.setDateOfArrival(erasmusStudentDataBean.getDateOfArrival());
	this.setDateOfDeparture(erasmusStudentDataBean.getDateOfDeparture());
	this.setDiplomaConclusionYear(erasmusStudentDataBean.getDiplomaConclusionYear());
	this.setDiplomaName(erasmusStudentDataBean.getDiplomaName());
	this.setErasmusIndividualCandidacy(erasmusIndividualCandidacy);
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
	this.setSelectedVacancy(erasmusVacancy);

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

    public void edit(ErasmusStudentDataBean erasmusStudentDataBean) {
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
