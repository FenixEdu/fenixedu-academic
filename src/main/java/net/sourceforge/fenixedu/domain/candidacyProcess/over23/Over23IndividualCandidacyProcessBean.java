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
package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.FormationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;

import org.joda.time.LocalDate;

public class Over23IndividualCandidacyProcessBean extends IndividualCandidacyProcessBean {

    private Degree degreeToAdd;

    private List<Degree> selectedDegrees;

    private String disabilities;

    private String education;

    private String languages;

    private String languagesRead;
    private String languagesWrite;
    private String languagesSpeak;

    private List<CandidacyProcessDocumentUploadBean> habilitationCertificateList;
    private List<CandidacyProcessDocumentUploadBean> reportOrWorkDocumentList;
    private CandidacyProcessDocumentUploadBean handicapProofDocument;
    private CandidacyProcessDocumentUploadBean curriculumVitaeDocument;

    public Over23IndividualCandidacyProcessBean() {
        setCandidacyDate(new LocalDate());
        setSelectedDegrees(Collections.EMPTY_LIST);
        setFormationConcludedBeanList(new ArrayList<FormationBean>());
        setFormationNonConcludedBeanList(new ArrayList<FormationBean>());
        initializeDocumentUploadBeans();
        this.honorAgreement = false;
    }

    public Over23IndividualCandidacyProcessBean(Over23IndividualCandidacyProcess process) {
        setIndividualCandidacyProcess(process);
        setCandidacyDate(process.getCandidacyDate());
        setSelectedDegrees(Collections.EMPTY_LIST);
        addDegrees(process.getSelectedDegreesSortedByOrder());
        setDisabilities(process.getDisabilities());
        setEducation(process.getEducation());
        setLanguages(process.getLanguages());
        initializeFormation(process.getCandidacy().getFormations());
        setLanguagesRead(process.getLanguagesRead());
        setLanguagesWrite(process.getLanguagesWrite());
        setLanguagesSpeak(process.getLanguagesSpeak());
        setProcessChecked(process.getProcessChecked());
        setPaymentChecked(process.getPaymentChecked());
    }

    @Override
    public Over23CandidacyProcess getCandidacyProcess() {
        return (Over23CandidacyProcess) super.getCandidacyProcess();
    }

    public Degree getDegreeToAdd() {
        return this.degreeToAdd;
    }

    public void setDegreeToAdd(Degree degreeToAdd) {
        this.degreeToAdd = degreeToAdd;
    }

    public boolean hasDegreeToAdd() {
        return getDegreeToAdd() != null;
    }

    public void removeDegreeToAdd() {
        degreeToAdd = null;
    }

    public List<Degree> getSelectedDegrees() {
        final List<Degree> result = new ArrayList<Degree>();
        for (final Degree degree : selectedDegrees) {
            result.add(degree);
        }
        return result;
    }

    public void setSelectedDegrees(final List<Degree> degrees) {
        selectedDegrees = new ArrayList<Degree>();
        for (final Degree degree : degrees) {
            selectedDegrees.add(degree);
        }
    }

    public void addDegree(final Degree degree) {
        selectedDegrees.add(degree);
    }

    public void addDegrees(final Collection<Degree> degrees) {
        for (final Degree degree : degrees) {
            addDegree(degree);
        }
    }

    public void removeDegree(final Degree degree) {
        final Iterator<Degree> iter = selectedDegrees.iterator();
        while (iter.hasNext()) {
            if (iter.next() == degree) {
                iter.remove();
                break;
            }
        }
    }

    public boolean containsDegree(final Degree value) {
        for (final Degree degree : getSelectedDegrees()) {
            if (degree == value) {
                return true;
            }
        }
        return false;
    }

    public void removeSelectedDegrees() {
        selectedDegrees.clear();
    }

    public String getDisabilities() {
        return disabilities;
    }

    public void setDisabilities(String disabilities) {
        this.disabilities = disabilities;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
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

    public String getLanguagesRead() {
        return this.languagesRead;
    }

    public void setLanguagesRead(String value) {
        this.languagesRead = value;
    }

    public String getLanguagesWrite() {
        return this.languagesWrite;
    }

    public void setLanguagesWrite(String value) {
        this.languagesWrite = value;
    }

    public String getLanguagesSpeak() {
        return this.languagesSpeak;
    }

    public void setLanguagesSpeak(String value) {
        this.languagesSpeak = value;
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

    @Override
    protected void initializeDocumentUploadBeans() {
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

    @Override
    public boolean isOver23() {
        return true;
    }
}
