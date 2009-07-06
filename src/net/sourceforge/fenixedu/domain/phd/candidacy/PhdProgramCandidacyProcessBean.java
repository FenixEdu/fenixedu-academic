package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuidingBean;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcessBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private PersonBean personBean;

    private LocalDate candidacyDate;

    private DomainReference<PhdProgram> program;

    private DomainReference<ExecutionYear> executionYear;

    private DomainReference<Degree> degree;

    private String thesisTitle;

    private PhdIndividualProgramCollaborationType collaborationType;

    private String otherCollaborationType;

    private ChoosePersonBean choosePersonBean;

    private String email;

    private String captcha;

    private String institutionId;

    private PhdProgramCandidacyProcessState state = PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION;

    private DomainReference<PhdProgramPublicCandidacyHashCode> candidacyHashCode;

    private DomainReference<PhdProgramFocusArea> focusArea;

    private List<PhdProgramGuidingBean> guidings;

    private List<QualificationBean> qualifications;

    private List<PhdCandidacyRefereeBean> candidacyReferees;

    private PhdCandidacyDocumentUploadBean curriculumVitae;

    private PhdCandidacyDocumentUploadBean identificationDocument;

    private PhdCandidacyDocumentUploadBean motivationLetter;

    private PhdCandidacyDocumentUploadBean socialSecurityDocument;

    private PhdCandidacyDocumentUploadBean researchPlan;

    private PhdCandidacyDocumentUploadBean dissertationOrFinalWorkDocument;

    private List<PhdCandidacyDocumentUploadBean> habilitationCertificateDocuments;

    private List<PhdCandidacyDocumentUploadBean> phdGuidingLetters;

    public PhdProgramCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
    }

    public LocalDate getCandidacyDate() {
	return candidacyDate;
    }

    public void setCandidacyDate(LocalDate candidacyDate) {
	this.candidacyDate = candidacyDate;
    }

    public PhdProgram getProgram() {
	return (this.program != null) ? this.program.getObject() : null;
    }

    public void setProgram(PhdProgram program) {
	this.program = (program != null) ? new DomainReference<PhdProgram>(program) : null;
    }

    public PersonBean getPersonBean() {
	return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
	this.personBean = personBean;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public Person getOrCreatePersonFromBean() {
	if (!getPersonBean().hasPerson()) {
	    Person person = new Person(getPersonBean());
	    getPersonBean().setPerson(person);
	    return person;
	}

	if (getPersonBean().getPerson().hasRole(RoleType.EMPLOYEE)) {
	    return getPersonBean().getPerson();
	}

	return getPersonBean().getPerson().edit(personBean);
    }

    public Degree getDegree() {
	return (this.degree != null) ? this.degree.getObject() : null;
    }

    public boolean hasDegree() {
	return getDegree() != null;
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public ExecutionDegree getExecutionDegree() {
	return hasDegree() ? null : getDegree().getLastActiveDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(
		getExecutionYear().getAcademicInterval());
    }

    public String getThesisTitle() {
	return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
	this.thesisTitle = thesisTitle;
    }

    public PhdIndividualProgramCollaborationType getCollaborationType() {
	return collaborationType;
    }

    public void setCollaborationType(PhdIndividualProgramCollaborationType collaborationType) {
	this.collaborationType = collaborationType;
    }

    public boolean hasCollaborationType() {
	return getCollaborationType() != null;
    }

    public String getOtherCollaborationType() {
	return otherCollaborationType;
    }

    public void setOtherCollaborationType(String otherCollaborationType) {
	this.otherCollaborationType = otherCollaborationType;
    }

    public ChoosePersonBean getChoosePersonBean() {
	return choosePersonBean;
    }

    public void setChoosePersonBean(ChoosePersonBean choosePersonBean) {
	this.choosePersonBean = choosePersonBean;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getCaptcha() {
	return captcha;
    }

    public void setCaptcha(String captcha) {
	this.captcha = captcha;
    }

    public String getInstitutionId() {
	return institutionId;
    }

    public void setInstitutionId(String institutionId) {
	this.institutionId = institutionId;
    }

    public boolean hasInstitutionId() {
	return !StringUtils.isEmpty(this.institutionId);
    }

    public PhdProgramCandidacyProcessState getState() {
	return state;
    }

    public void setState(PhdProgramCandidacyProcessState state) {
	this.state = state;
    }

    public PhdProgramPublicCandidacyHashCode getCandidacyHashCode() {
	return (this.candidacyHashCode != null) ? this.candidacyHashCode.getObject() : null;
    }

    public void setCandidacyHashCode(final PhdProgramPublicCandidacyHashCode candidacyHashCode) {
	this.candidacyHashCode = (candidacyHashCode != null) ? new DomainReference<PhdProgramPublicCandidacyHashCode>(
		candidacyHashCode) : null;
    }

    public boolean hasCandidacyHashCode() {
	return getCandidacyHashCode() != null;
    }

    public PhdProgramFocusArea getFocusArea() {
	return (this.focusArea != null) ? this.focusArea.getObject() : null;
    }

    public void setFocusArea(final PhdProgramFocusArea focusArea) {
	this.focusArea = (focusArea != null) ? new DomainReference<PhdProgramFocusArea>(focusArea) : null;
    }

    public List<PhdProgramGuidingBean> getGuidings() {
	return guidings;
    }

    public void setGuidings(List<PhdProgramGuidingBean> guidings) {
	this.guidings = guidings;
    }

    public boolean hasAnyGuiding() {
	return this.guidings != null && !this.guidings.isEmpty();
    }

    public void addGuiding(final PhdProgramGuidingBean guiding) {
	this.guidings.add(guiding);
    }

    public void removeGuiding(int index) {
	this.guidings.remove(index);
    }

    public List<QualificationBean> getQualifications() {
	return qualifications;
    }

    public void setQualifications(List<QualificationBean> qualifications) {
	this.qualifications = qualifications;
    }

    public void addQualification(final QualificationBean qualification) {
	this.qualifications.add(qualification);
    }

    public void removeQualification(int index) {
	this.qualifications.remove(index);
    }

    public boolean hasAnyQualification() {
	return this.qualifications != null && !this.qualifications.isEmpty();
    }

    public void sortQualificationsByAttendedEnd() {
	Collections.sort(this.qualifications, QualificationBean.COMPARATOR_BY_MOST_RECENT_ATTENDED_END);
    }

    public List<PhdCandidacyRefereeBean> getCandidacyReferees() {
	return candidacyReferees;
    }

    public void setCandidacyReferees(List<PhdCandidacyRefereeBean> candidacyReferees) {
	this.candidacyReferees = candidacyReferees;
    }

    public void addCandidacyReferee(PhdCandidacyRefereeBean phdCandidacyRefereeBean) {
	this.candidacyReferees.add(phdCandidacyRefereeBean);
    }

    public void removeCandidacyReferee(int index) {
	this.candidacyReferees.remove(index);
    }

    public boolean hasAnyCandidacyReferee() {
	return this.candidacyReferees != null && !this.candidacyReferees.isEmpty();
    }

    public void clearPerson() {
	getPersonBean().setPerson(null);
    }

    public PhdCandidacyDocumentUploadBean getCurriculumVitae() {
	return curriculumVitae;
    }

    public void setCurriculumVitae(PhdCandidacyDocumentUploadBean curriculumVitae) {
	this.curriculumVitae = curriculumVitae;
    }

    public PhdCandidacyDocumentUploadBean getIdentificationDocument() {
	return identificationDocument;
    }

    public void setIdentificationDocument(PhdCandidacyDocumentUploadBean identificationDocument) {
	this.identificationDocument = identificationDocument;
    }

    public PhdCandidacyDocumentUploadBean getMotivationLetter() {
	return motivationLetter;
    }

    public void setMotivationLetter(PhdCandidacyDocumentUploadBean motivationLetter) {
	this.motivationLetter = motivationLetter;
    }

    public PhdCandidacyDocumentUploadBean getSocialSecurityDocument() {
	return socialSecurityDocument;
    }

    public void setSocialSecurityDocument(PhdCandidacyDocumentUploadBean socialSecurityDocument) {
	this.socialSecurityDocument = socialSecurityDocument;
    }

    public PhdCandidacyDocumentUploadBean getResearchPlan() {
	return researchPlan;
    }

    public void setResearchPlan(PhdCandidacyDocumentUploadBean researchPlan) {
	this.researchPlan = researchPlan;
    }

    public PhdCandidacyDocumentUploadBean getDissertationOrFinalWorkDocument() {
	return dissertationOrFinalWorkDocument;
    }

    public void setDissertationOrFinalWorkDocument(PhdCandidacyDocumentUploadBean dissertationOrFinalWorkDocument) {
	this.dissertationOrFinalWorkDocument = dissertationOrFinalWorkDocument;
    }

    public List<PhdCandidacyDocumentUploadBean> getHabilitationCertificateDocuments() {
	return habilitationCertificateDocuments;
    }

    public void setHabilitationCertificateDocuments(List<PhdCandidacyDocumentUploadBean> habilitationCertificateDocuments) {
	this.habilitationCertificateDocuments = habilitationCertificateDocuments;
    }

    public void addHabilitationCertificateDocument(PhdCandidacyDocumentUploadBean document) {
	this.habilitationCertificateDocuments.add(document);
    }

    public void removeHabilitationCertificateDocument(int index) {
	this.habilitationCertificateDocuments.remove(index);
    }

    public void removeHabilitationCertificateDocumentFiles() {
	for (final PhdCandidacyDocumentUploadBean bean : getHabilitationCertificateDocuments()) {
	    bean.removeFile();
	}
    }

    public List<PhdCandidacyDocumentUploadBean> getPhdGuidingLetters() {
	return phdGuidingLetters;
    }

    public void setPhdGuidingLetters(List<PhdCandidacyDocumentUploadBean> phdGuidingLetters) {
	this.phdGuidingLetters = phdGuidingLetters;
    }

    public void removePhdGuidingLetters() {
	for (final PhdCandidacyDocumentUploadBean bean : getPhdGuidingLetters()) {
	    bean.removeFile();
	}
    }

    public List<PhdCandidacyDocumentUploadBean> getAllDocuments() {
	final List<PhdCandidacyDocumentUploadBean> result = new ArrayList<PhdCandidacyDocumentUploadBean>();

	result.add(getCurriculumVitae());
	result.add(getIdentificationDocument());
	result.add(getMotivationLetter());

	if (getSocialSecurityDocument().hasAnyInformation()) {
	    result.add(getSocialSecurityDocument());
	}

	if (getResearchPlan().hasAnyInformation()) {
	    result.add(getResearchPlan());
	}

	if (getDissertationOrFinalWorkDocument().hasAnyInformation()) {
	    result.add(getDissertationOrFinalWorkDocument());
	}

	for (final PhdCandidacyDocumentUploadBean bean : getHabilitationCertificateDocuments()) {
	    if (bean.hasAnyInformation()) {
		result.add(bean);
	    }
	}

	for (final PhdCandidacyDocumentUploadBean bean : getPhdGuidingLetters()) {
	    if (bean.hasAnyInformation()) {
		result.add(bean);
	    }
	}

	return result;
    }

}
