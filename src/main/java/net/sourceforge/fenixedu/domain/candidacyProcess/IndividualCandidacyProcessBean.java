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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.LocalDate;

abstract public class IndividualCandidacyProcessBean implements Serializable {

    private static final long serialVersionUID = 2860833709120576930L;

    // this must be set to false if you want to use external persons
    private Boolean internalPersonCandidacy = Boolean.TRUE;

    private CandidacyProcess candidacyProcess;

    private DegreeOfficePublicCandidacyHashCode publicCandidacyHashCode;

    private IndividualCandidacyProcess individualCandidacyProcess;

    private ChoosePersonBean choosePersonBean;

    private PersonBean personBean;

    private LocalDate candidacyDate;

    private String observations;

    private Boolean processChecked;

    private String personNumber;

    private Boolean publicCandidacyCreationOrEdition;

    private Integer numberOfPreviousYearEnrolmentsInPrecedentDegree;

    private CandidacyProcessDocumentUploadBean documentIdentificationDocument;
    private CandidacyProcessDocumentUploadBean paymentDocument;
    private CandidacyProcessDocumentUploadBean habilitationCertificationDocument;
    private CandidacyProcessDocumentUploadBean firstCycleAccessHabilitationDocument;
    private CandidacyProcessDocumentUploadBean vatCatCopyDocument;
    private CandidacyProcessDocumentUploadBean photoDocument;

    private List<FormationBean> formationConcludedBeanList;
    private List<FormationBean> formationNonConcludedBeanList;

    protected Boolean honorAgreement;

    protected Boolean utlStudent;

    private Boolean paymentChecked;

    public IndividualCandidacyProcessBean() {
        setFormationConcludedBeanList(new ArrayList<FormationBean>());
        setFormationNonConcludedBeanList(new ArrayList<FormationBean>());
        setPublicCandidacy(Boolean.TRUE);
    }

    public Integer getNumberOfPreviousYearEnrolmentsInPrecedentDegree() {
        return numberOfPreviousYearEnrolmentsInPrecedentDegree;
    }

    public void setNumberOfPreviousYearEnrolmentsInPrecedentDegree(Integer numberOfPreviousYearEnrolmentsInPrecedentDegree) {
        this.numberOfPreviousYearEnrolmentsInPrecedentDegree = numberOfPreviousYearEnrolmentsInPrecedentDegree;
    }

    public Boolean getInternalPersonCandidacy() {
        return internalPersonCandidacy;
    }

    public void setInternalPersonCandidacy(Boolean internalPersonCandidacy) {
        this.internalPersonCandidacy = internalPersonCandidacy;
    }

    public CandidacyProcess getCandidacyProcess() {
        return this.candidacyProcess;
    }

    public void setCandidacyProcess(CandidacyProcess candidacyProcess) {
        this.candidacyProcess = candidacyProcess;
    }

    public boolean hasCandidacyProcess() {
        return getCandidacyProcess() != null;
    }

    public ChoosePersonBean getChoosePersonBean() {
        return choosePersonBean;
    }

    public void setChoosePersonBean(ChoosePersonBean choosePersonBean) {
        this.choosePersonBean = choosePersonBean;
    }

    public boolean hasChoosenPerson() {
        return getChoosePersonBean().hasPerson();
    }

    public void removeChoosePersonBean() {
        setChoosePersonBean(null);
    }

    public PersonBean getPersonBean() {
        return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
        this.personBean = personBean;
    }

    public LocalDate getCandidacyDate() {
        return candidacyDate;
    }

    public void setCandidacyDate(final LocalDate candidacyDate) {
        this.candidacyDate = candidacyDate;
    }

    public Person getOrCreatePersonFromBean() {
        if (!getPersonBean().hasPerson()) {
            // validate email only
            Person person = new Person(getPersonBean(), true, false);
            return person;
        }

        if (getPersonBean().getPerson().hasRole(RoleType.EMPLOYEE)) {
            return getPersonBean().getPerson();
        } else if (!getPersonBean().getPerson().getPersonRoles().isEmpty() && this.isPublicCandidacy()) {
            return getPersonBean().getPerson();
        } else if (getPersonBean().getPerson().getPersonRoles().isEmpty() && this.isPublicCandidacy()) {
            return getPersonBean().getPerson().editByPublicCandidate(personBean);
        } else {
            return getPersonBean().getPerson().edit(getPersonBean());
        }
    }

    public ExecutionInterval getCandidacyExecutionInterval() {
        return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyExecutionInterval() : null;
    }

    public CandidacyProcessDocumentUploadBean getDocumentIdentificationDocument() {
        return documentIdentificationDocument;
    }

    public void setDocumentIdentificationDocument(CandidacyProcessDocumentUploadBean documentIdentificationDocument) {
        this.documentIdentificationDocument = documentIdentificationDocument;
    }

    public CandidacyProcessDocumentUploadBean getPaymentDocument() {
        return paymentDocument;
    }

    public void setPaymentDocument(CandidacyProcessDocumentUploadBean paymentDocument) {
        this.paymentDocument = paymentDocument;
    }

    public CandidacyProcessDocumentUploadBean getHabilitationCertificationDocument() {
        return habilitationCertificationDocument;
    }

    public void setHabilitationCertificationDocument(CandidacyProcessDocumentUploadBean habilitationCertificationDocument) {
        this.habilitationCertificationDocument = habilitationCertificationDocument;
    }

    public CandidacyProcessDocumentUploadBean getFirstCycleAccessHabilitationDocument() {
        return firstCycleAccessHabilitationDocument;
    }

    public void setFirstCycleAccessHabilitationDocument(CandidacyProcessDocumentUploadBean firstCycleAccessHabilitationDocument) {
        this.firstCycleAccessHabilitationDocument = firstCycleAccessHabilitationDocument;
    }

    public CandidacyProcessDocumentUploadBean getVatCatCopyDocument() {
        return vatCatCopyDocument;
    }

    public void setVatCatCopyDocument(CandidacyProcessDocumentUploadBean vatCatCopyDocument) {
        this.vatCatCopyDocument = vatCatCopyDocument;
    }

    public IndividualCandidacyProcess getIndividualCandidacyProcess() {
        return this.individualCandidacyProcess;
    }

    public void setIndividualCandidacyProcess(IndividualCandidacyProcess individualCandidacyProcess) {
        this.individualCandidacyProcess = individualCandidacyProcess;
    }

    public DegreeOfficePublicCandidacyHashCode getPublicCandidacyHashCode() {
        return this.publicCandidacyHashCode;
    }

    public void setPublicCandidacyHashCode(DegreeOfficePublicCandidacyHashCode publicCandidacyHashCode) {
        this.publicCandidacyHashCode = publicCandidacyHashCode;
    }

    public String getObservations() {
        return this.observations;
    }

    public void setObservations(String value) {
        this.observations = value;
    }

    public CandidacyProcessDocumentUploadBean getPhotoDocument() {
        return this.photoDocument;
    }

    public void setPhotoDocument(CandidacyProcessDocumentUploadBean bean) {
        this.photoDocument = bean;
    }

    public List<FormationBean> getFormationConcludedBeanList() {
        return this.formationConcludedBeanList;
    }

    public void setFormationConcludedBeanList(List<FormationBean> formationConcludedBeanList) {
        this.formationConcludedBeanList = formationConcludedBeanList;
    }

    public List<FormationBean> getFormationNonConcludedBeanList() {
        return this.formationNonConcludedBeanList;
    }

    public void setFormationNonConcludedBeanList(List<FormationBean> formationNonConcludedBeanList) {
        this.formationNonConcludedBeanList = formationNonConcludedBeanList;
    }

    public void addConcludedFormationBean() {
        this.formationConcludedBeanList.add(new FormationBean(Boolean.TRUE));
    }

    public void addNonConcludedFormationBean() {
        this.formationNonConcludedBeanList.add(new FormationBean(Boolean.FALSE));
    }

    public void removeFormationConcludedBean(final int index) {
        this.formationConcludedBeanList.remove(index);
    }

    public void removeFormationNonConcludedBean(final int index) {
        this.formationNonConcludedBeanList.remove(index);
    }

    protected void initializeFormation(Collection<Formation> formations) {
        this.formationConcludedBeanList = new ArrayList<FormationBean>();
        this.formationNonConcludedBeanList = new ArrayList<FormationBean>();

        for (Formation formation : formations) {
            if (formation.getConcluded()) {
                this.formationConcludedBeanList.add(new FormationBean(formation));
            } else {
                this.formationNonConcludedBeanList.add(new FormationBean(formation));
            }
        }
    }

    protected void initializeDocumentUploadBeans() {
        this.documentIdentificationDocument =
                new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
        this.paymentDocument = new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
        this.habilitationCertificationDocument =
                new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
        this.firstCycleAccessHabilitationDocument =
                new CandidacyProcessDocumentUploadBean(
                        IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_DOCUMENT);
        this.vatCatCopyDocument = new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
        setPhotoDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PHOTO));
    }

    public Boolean getProcessChecked() {
        return this.processChecked;
    }

    public void setProcessChecked(Boolean value) {
        this.processChecked = value;
    }

    public String getPersonNumber() {
        return this.personNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    public Boolean isPublicCandidacy() {
        return this.publicCandidacyCreationOrEdition;
    }

    public void setPublicCandidacy(Boolean value) {
        this.publicCandidacyCreationOrEdition = value;
    }

    public Boolean getHonorAgreement() {
        return this.honorAgreement;
    }

    public void setHonorAgreement(Boolean value) {
        this.honorAgreement = value;
    }

    public Boolean getUtlStudent() {
        return utlStudent;
    }

    public void setUtlStudent(Boolean utlStudent) {
        this.utlStudent = utlStudent;
    }

    public Boolean getPaymentChecked() {
        return this.paymentChecked;
    }

    public void setPaymentChecked(Boolean value) {
        this.paymentChecked = value;
    }

    public boolean isDegreeCandidacyForGraduatedPerson() {
        return false;
    }

    public boolean isDegreeChange() {
        return false;
    }

    public boolean isDegreeTransfer() {
        return false;
    }

    public boolean isErasmus() {
        return false;
    }

    public boolean isOver23() {
        return false;
    }

    public boolean isSecondCycle() {
        return false;
    }

    public boolean isStandalone() {
        return false;
    }

}
