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
package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.FormationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.PrecedentDegreeInformationBeanFactory;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.joda.time.LocalDate;

public class SecondCycleIndividualCandidacyProcessBean extends IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private Degree selectedDegree;

    private String professionalStatus;

    private String otherEducation;

    private String istStudentNumber;

    private List<CandidacyProcessDocumentUploadBean> habilitationCertificateList;
    private List<CandidacyProcessDocumentUploadBean> reportOrWorkDocumentList;
    private CandidacyProcessDocumentUploadBean handicapProofDocument;
    private CandidacyProcessDocumentUploadBean curriculumVitaeDocument;

    private Set<Degree> selectedDegreeList;

    private SecondCycleCandidacyProcess copyDestinationProcess;

    public SecondCycleIndividualCandidacyProcessBean() {
        setCandidacyDate(new LocalDate());
        setFormationConcludedBeanList(new ArrayList<FormationBean>());
        initializeDocumentUploadBeans();
        setObservations("");
        setIstStudentNumber("");
        setPrecedentDegreeType(PrecedentDegreeType.EXTERNAL_DEGREE);

        this.selectedDegreeList = new HashSet<Degree>();
    }

    public SecondCycleIndividualCandidacyProcessBean(final SecondCycleIndividualCandidacyProcess process) {
        setIndividualCandidacyProcess(process);
        setCandidacyProcess(process.getCandidacyProcess());
        setProfessionalStatus(process.getCandidacyProfessionalStatus());
        setOtherEducation(process.getCandidacyOtherEducation());
        setPrecedentDegreeInformation(PrecedentDegreeInformationBeanFactory.createBean(process.getCandidacy()));
        setPrecedentDegreeType(PrecedentDegreeType.valueOf(process.getPrecedentDegreeInformation()));
        setCandidacyDate(process.getCandidacyDate());
        initializeFormation(process.getCandidacy().getFormations());
        setObservations(process.getCandidacy().getObservations());
        setIstStudentNumber(process.getCandidacy().getFormerStudentNumber());
        setProcessChecked(process.getProcessChecked());
        setPaymentChecked(process.getPaymentChecked());

        this.selectedDegreeList = new HashSet<Degree>();
        this.selectedDegreeList.addAll(process.getSelectedDegrees());
    }

    @Override
    public SecondCycleCandidacyProcess getCandidacyProcess() {
        return (SecondCycleCandidacyProcess) super.getCandidacyProcess();
    }

    public Degree getSelectedDegree() {
        return this.selectedDegree;
    }

    public void setSelectedDegree(Degree selectedDegree) {
        this.selectedDegree = selectedDegree;
    }

    public String getProfessionalStatus() {
        return professionalStatus;
    }

    public void setProfessionalStatus(String professionalStatus) {
        this.professionalStatus = professionalStatus;
    }

    public String getOtherEducation() {
        return otherEducation;
    }

    public void setOtherEducation(String otherEducation) {
        this.otherEducation = otherEducation;
    }

    @Override
    public boolean hasChoosenPerson() {
        return getChoosePersonBean().hasPerson();
    }

    @Override
    public void removeChoosePersonBean() {
        setChoosePersonBean(null);
    }

    @Override
    protected double getMinimumEcts(final CycleType cycleType) {
        if (!cycleType.equals(CycleType.FIRST_CYCLE)) {
            throw new IllegalArgumentException();
        }
        return 150d;
    }

    @Override
    protected List<CycleType> getValidPrecedentCycleTypes() {
        return Collections.singletonList(CycleType.FIRST_CYCLE);
    }

    @Override
    protected boolean isPreBolonhaPrecedentDegreeAllowed() {
        return true;
    }

    public List<CandidacyProcessDocumentUploadBean> getHabilitationCertificateList() {
        return habilitationCertificateList;
    }

    public void setHabilitationCertificateList(List<CandidacyProcessDocumentUploadBean> habilitationCertificateList) {
        this.habilitationCertificateList = habilitationCertificateList;
    }

    public List<CandidacyProcessDocumentUploadBean> getReportOrWorkDocumentList() {
        return reportOrWorkDocumentList;
    }

    public void setReportOrWorkDocumentList(List<CandidacyProcessDocumentUploadBean> reportOrWorkDocumentList) {
        this.reportOrWorkDocumentList = reportOrWorkDocumentList;
    }

    public CandidacyProcessDocumentUploadBean getHandicapProofDocument() {
        return handicapProofDocument;
    }

    public void setHandicapProofDocument(CandidacyProcessDocumentUploadBean handicapProofDocument) {
        this.handicapProofDocument = handicapProofDocument;
    }

    public CandidacyProcessDocumentUploadBean getCurriculumVitaeDocument() {
        return curriculumVitaeDocument;
    }

    public void setCurriculumVitaeDocument(CandidacyProcessDocumentUploadBean curriculumVitaeDocument) {
        this.curriculumVitaeDocument = curriculumVitaeDocument;
    }

    public void addHabilitationCertificateDocument() {
        this.habilitationCertificateList.add(new CandidacyProcessDocumentUploadBean(
                IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT));
    }

    public void removeHabilitationCertificateDocument(final int index) {
        this.habilitationCertificateList.remove(index);
    }

    public void addReportOrWorkDocument() {
        this.reportOrWorkDocumentList.add(new CandidacyProcessDocumentUploadBean(
                IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT));
    }

    public void removeReportOrWorkDocument(final int index) {
        this.reportOrWorkDocumentList.remove(index);
    }

    public String getIstStudentNumber() {
        return this.istStudentNumber;
    }

    public void setIstStudentNumber(String value) {
        this.istStudentNumber = value;
    }

    @Override
    public void initializeDocumentUploadBeans() {
        setDocumentIdentificationDocument(new CandidacyProcessDocumentUploadBean(
                IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION));
        setPaymentDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT));
        setVatCatCopyDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT));

        this.habilitationCertificateList = new ArrayList<CandidacyProcessDocumentUploadBean>();
        addHabilitationCertificateDocument();

        this.reportOrWorkDocumentList = new ArrayList<CandidacyProcessDocumentUploadBean>();
        addReportOrWorkDocument();

        this.handicapProofDocument =
                new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.HANDICAP_PROOF_DOCUMENT);
        this.curriculumVitaeDocument = new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
        setPhotoDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PHOTO));
    }

    public Set<Degree> getSelectedDegreeList() {
        return this.selectedDegreeList;
    }

    public void setSelectedDegreeList(final Set<Degree> selectedDegreeList) {
        this.selectedDegreeList = selectedDegreeList;
    }

    public void addSelectedDegree(final Degree degree) {
        this.selectedDegreeList.add(degree);
    }

    public void removeSelectedDegree(final Degree degree) {
        this.selectedDegreeList.remove(degree);
    }

    @Override
    public boolean isSecondCycle() {
        return true;
    }

    public List<Degree> getAvailableDegrees() {
        final SecondCycleCandidacyProcess candidacyProcess = getCandidacyProcess();
        return candidacyProcess.getAvailableDegrees();
    }

    public SecondCycleCandidacyProcess getCopyDestinationProcess() {
        return copyDestinationProcess;
    }

    public void setCopyDestinationProcess(SecondCycleCandidacyProcess copyDestinationProcess) {
        this.copyDestinationProcess = copyDestinationProcess;
    }

}
