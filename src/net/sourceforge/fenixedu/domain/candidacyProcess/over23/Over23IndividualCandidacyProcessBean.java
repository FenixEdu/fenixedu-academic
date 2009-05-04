package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.FormationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;

import org.joda.time.LocalDate;

public class Over23IndividualCandidacyProcessBean extends IndividualCandidacyProcessBean {

    private DomainReference<Degree> degreeToAdd;

    private List<DomainReference<Degree>> selectedDegrees;

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
    
    private Boolean honorAgreement;
    

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
    }

    @Override
    public Over23CandidacyProcess getCandidacyProcess() {
	return (Over23CandidacyProcess) super.getCandidacyProcess();
    }

    public Degree getDegreeToAdd() {
	return (this.degreeToAdd != null) ? this.degreeToAdd.getObject() : null;
    }

    public void setDegreeToAdd(Degree degreeToAdd) {
	this.degreeToAdd = (degreeToAdd != null) ? new DomainReference<Degree>(degreeToAdd) : null;
    }

    public boolean hasDegreeToAdd() {
	return getDegreeToAdd() != null;
    }

    public void removeDegreeToAdd() {
	degreeToAdd = null;
    }

    public List<Degree> getSelectedDegrees() {
	final List<Degree> result = new ArrayList<Degree>();
	for (final DomainReference<Degree> degree : selectedDegrees) {
	    result.add(degree.getObject());
	}
	return result;
    }

    public void setSelectedDegrees(final List<Degree> degrees) {
	selectedDegrees = new ArrayList<DomainReference<Degree>>();
	for (final Degree degree : degrees) {
	    selectedDegrees.add(new DomainReference<Degree>(degree));
	}
    }

    public void addDegree(final Degree degree) {
	selectedDegrees.add(new DomainReference<Degree>(degree));
    }

    public void addDegrees(final Collection<Degree> degrees) {
	for (final Degree degree : degrees) {
	    addDegree(degree);
	}
    }

    public void removeDegree(final Degree degree) {
	final Iterator<DomainReference<Degree>> iter = selectedDegrees.iterator();
	while (iter.hasNext()) {
	    if (iter.next().getObject() == degree) {
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
    
    public Boolean getHonorAgreement() {
	return this.honorAgreement;
    }
    
    public void setHonorAgreement(Boolean value) {
	this.honorAgreement = value;
    }
    
    public void addHabilitationCertificateDocument() {
	this.habilitationCertificateList.add(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT));
    }
    
    public void removeHabilitationCertificateDocument(final int index) {
	this.habilitationCertificateList.remove(index);
    }
    
    public void addReportOrWorkDocument() {
	this.reportOrWorkDocumentList.add(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT));
    }
    
    public void removeReportOrWorkDocument(final int index) {
	this.reportOrWorkDocumentList.remove(index);
    }
    
    @Override
    protected void initializeDocumentUploadBeans() {
        setDocumentIdentificationDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION));
        setPaymentDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT));
        setVatCatCopyDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT));
        this.habilitationCertificateList = new ArrayList<CandidacyProcessDocumentUploadBean>();
        addHabilitationCertificateDocument();
        this.reportOrWorkDocumentList = new ArrayList<CandidacyProcessDocumentUploadBean>();
        addReportOrWorkDocument();
        this.handicapProofDocument = new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.HANDICAP_PROOF_DOCUMENT);
        this.curriculumVitaeDocument = new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
        setPhotoDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PHOTO));
    }
}
