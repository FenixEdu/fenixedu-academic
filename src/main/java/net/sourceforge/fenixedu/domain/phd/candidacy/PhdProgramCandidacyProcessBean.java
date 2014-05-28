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
package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.ExternalPhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;
import net.sourceforge.fenixedu.domain.phd.ThesisSubject;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcessBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private PersonBean personBean;

    private LocalDate candidacyDate;

    private PhdProgram program;

    private ExecutionYear executionYear;

    private Degree degree;

    private String thesisTitle;

    private PhdIndividualProgramCollaborationType collaborationType;

    private String otherCollaborationType;

    private ChoosePersonBean choosePersonBean;

    private String email;

    private String captcha;

    private String institutionId;

    private PhdProgramCandidacyProcessState state = PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION;

    private PhdProgramPublicCandidacyHashCode candidacyHashCode;

    private PhdProgramFocusArea focusArea;

    private List<PhdThesisSubjectOrderBean> thesisSubjectBeans;

    private List<PhdParticipantBean> guidings;

    private List<QualificationBean> qualifications;

    private List<PhdCandidacyRefereeBean> candidacyReferees;

    private PhdProgramDocumentUploadBean curriculumVitae;

    private PhdProgramDocumentUploadBean identificationDocument;

    private PhdProgramDocumentUploadBean motivationLetter;

    private PhdProgramDocumentUploadBean socialSecurityDocument;

    private PhdProgramDocumentUploadBean researchPlan;

    private PhdProgramDocumentUploadBean dissertationOrFinalWorkDocument;

    private List<PhdProgramDocumentUploadBean> habilitationCertificateDocuments;

    private List<PhdProgramDocumentUploadBean> phdGuidingLetters;

    private ExternalPhdProgram externalPhdProgram;

    private Boolean migratedProcess = false;

    private Integer phdStudentNumber;

    private LocalDate whenRatified;

    private PhdProgramCandidacyProcess process;

    private PhdCandidacyPeriod phdCandidacyPeriod;

    private LocalDate stateDate;

    public PhdProgramCandidacyProcessBean() {
        setCandidacyDate(new LocalDate());
        thesisSubjectBeans = new ArrayList<PhdThesisSubjectOrderBean>();
    }

    public PhdProgramCandidacyProcessBean(PhdProgramCandidacyProcess process) {
        setCandidacyDate(process.getCandidacyDate());
        setWhenRatified(process.getWhenRatified());
        setCandidacyHashCode(process.getCandidacyHashCode());

        this.process = process;
    }

    public LocalDate getCandidacyDate() {
        return candidacyDate;
    }

    public void setCandidacyDate(LocalDate candidacyDate) {
        this.candidacyDate = candidacyDate;
    }

    public PhdProgram getProgram() {
        return this.program;
    }

    public void setProgram(PhdProgram program) {
        this.program = program;
    }

    public PersonBean getPersonBean() {
        return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
        this.personBean = personBean;
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
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
        return this.degree;
    }

    public boolean hasDegree() {
        return getDegree() != null;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
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
        return this.candidacyHashCode;
    }

    public void setCandidacyHashCode(final PhdProgramPublicCandidacyHashCode candidacyHashCode) {
        this.candidacyHashCode = candidacyHashCode;
    }

    public PhdIndividualProgramProcess getIndividualProgramProcess() {
        return getCandidacyHashCode().getIndividualProgramProcess();
    }

    public boolean hasCandidacyHashCode() {
        return getCandidacyHashCode() != null;
    }

    public PhdProgramFocusArea getFocusArea() {
        return this.focusArea;
    }

    public boolean hasFocusArea() {
        return getFocusArea() != null;
    }

    public void setFocusArea(final PhdProgramFocusArea focusArea) {
        this.focusArea = focusArea;
    }

    public List<PhdParticipantBean> getGuidings() {
        return guidings;
    }

    public void setGuidings(List<PhdParticipantBean> guidings) {
        this.guidings = guidings;
    }

    public boolean hasAnyGuiding() {
        return this.guidings != null && !this.guidings.isEmpty();
    }

    public void addGuiding(final PhdParticipantBean guiding) {
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

    public PhdProgramDocumentUploadBean getCurriculumVitae() {
        return curriculumVitae;
    }

    public void setCurriculumVitae(PhdProgramDocumentUploadBean curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }

    public PhdProgramDocumentUploadBean getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(PhdProgramDocumentUploadBean identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    public PhdProgramDocumentUploadBean getMotivationLetter() {
        return motivationLetter;
    }

    public void setMotivationLetter(PhdProgramDocumentUploadBean motivationLetter) {
        this.motivationLetter = motivationLetter;
    }

    public PhdProgramDocumentUploadBean getSocialSecurityDocument() {
        return socialSecurityDocument;
    }

    public void setSocialSecurityDocument(PhdProgramDocumentUploadBean socialSecurityDocument) {
        this.socialSecurityDocument = socialSecurityDocument;
    }

    public PhdProgramDocumentUploadBean getResearchPlan() {
        return researchPlan;
    }

    public void setResearchPlan(PhdProgramDocumentUploadBean researchPlan) {
        this.researchPlan = researchPlan;
    }

    public PhdProgramDocumentUploadBean getDissertationOrFinalWorkDocument() {
        return dissertationOrFinalWorkDocument;
    }

    public void setDissertationOrFinalWorkDocument(PhdProgramDocumentUploadBean dissertationOrFinalWorkDocument) {
        this.dissertationOrFinalWorkDocument = dissertationOrFinalWorkDocument;
    }

    public List<PhdProgramDocumentUploadBean> getHabilitationCertificateDocuments() {
        return habilitationCertificateDocuments;
    }

    public void setHabilitationCertificateDocuments(List<PhdProgramDocumentUploadBean> habilitationCertificateDocuments) {
        this.habilitationCertificateDocuments = habilitationCertificateDocuments;
    }

    public void addHabilitationCertificateDocument(PhdProgramDocumentUploadBean document) {
        this.habilitationCertificateDocuments.add(document);
    }

    public void removeHabilitationCertificateDocument(int index) {
        this.habilitationCertificateDocuments.remove(index);
    }

    public void removeHabilitationCertificateDocumentFiles() {
        for (final PhdProgramDocumentUploadBean bean : getHabilitationCertificateDocuments()) {
            bean.setFile(null);
        }
    }

    public List<PhdProgramDocumentUploadBean> getPhdGuidingLetters() {
        return phdGuidingLetters;
    }

    public void setPhdGuidingLetters(List<PhdProgramDocumentUploadBean> phdGuidingLetters) {
        this.phdGuidingLetters = phdGuidingLetters;
    }

    public ExternalPhdProgram getExternalPhdProgram() {
        return this.externalPhdProgram;
    }

    public void setExternalPhdProgram(final ExternalPhdProgram externalPhdProgram) {
        this.externalPhdProgram = externalPhdProgram;
    }

    public void removePhdGuidingLetters() {
        for (final PhdProgramDocumentUploadBean bean : getPhdGuidingLetters()) {
            bean.setFile(null);
        }
    }

    public Boolean getMigratedProcess() {
        return migratedProcess;
    }

    public void setMigratedProcess(Boolean migratedProcess) {
        this.migratedProcess = migratedProcess;
    }

    public Integer getPhdStudentNumber() {
        return phdStudentNumber;
    }

    public void setPhdStudentNumber(Integer phdStudentNumber) {
        this.phdStudentNumber = phdStudentNumber;
    }

    public boolean hasPhdStudentNumber() {
        return getPhdStudentNumber() != null;
    }

    public List<PhdProgramDocumentUploadBean> getAllDocuments() {
        final List<PhdProgramDocumentUploadBean> result = new ArrayList<PhdProgramDocumentUploadBean>();

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

        for (final PhdProgramDocumentUploadBean bean : getHabilitationCertificateDocuments()) {
            if (bean.hasAnyInformation()) {
                result.add(bean);
            }
        }

        for (final PhdProgramDocumentUploadBean bean : getPhdGuidingLetters()) {
            if (bean.hasAnyInformation()) {
                result.add(bean);
            }
        }

        return result;
    }

    public LocalDate getWhenRatified() {
        return whenRatified;
    }

    public void setWhenRatified(LocalDate whenRatified) {
        this.whenRatified = whenRatified;
    }

    public PhdProgramCandidacyProcess getProcess() {
        return process;
    }

    public void setProcess(PhdProgramCandidacyProcess process) {
        this.process = process;
    }

    public PhdCandidacyPeriod getPhdCandidacyPeriod() {
        return phdCandidacyPeriod;
    }

    public void setPhdCandidacyPeriod(PhdCandidacyPeriod phdCandidacyPeriod) {
        this.phdCandidacyPeriod = phdCandidacyPeriod;
    }

    public LocalDate getStateDate() {
        return stateDate;
    }

    public void setStateDate(LocalDate stateDate) {
        this.stateDate = stateDate;
    }

    public List<PhdThesisSubjectOrderBean> getThesisSubjectBeans() {
        return thesisSubjectBeans;
    }

    public void addThesisSubjectBean(PhdThesisSubjectOrderBean thesisSubjectBean) {
        thesisSubjectBeans.add(thesisSubjectBean);
        sortThesisSubjectBeans();
    }

    public PhdThesisSubjectOrderBean getThesisSubjectBean(int order) {
        for (PhdThesisSubjectOrderBean bean : getThesisSubjectBeans()) {
            if (bean.getOrder() == order) {
                return bean;
            }
        }

        return null;
    }

    public void sortThesisSubjectBeans() {
        Collections.sort(thesisSubjectBeans, PhdThesisSubjectOrderBean.COMPARATOR_BY_ORDER);
    }

    public void updateThesisSubjectBeans() {
        int order = 1;
        getThesisSubjectBeans().clear();
        if (hasFocusArea()) {
            for (ThesisSubject thesisSubject : getFocusArea().getThesisSubjects()) {
                addThesisSubjectBean(new PhdThesisSubjectOrderBean(order++, thesisSubject));
            }
        }
    }
}
