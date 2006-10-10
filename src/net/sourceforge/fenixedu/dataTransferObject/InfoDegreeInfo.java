package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Language;

/**
 * @author T�nia Pous�o Created on 30/Out/2003
 */
public class InfoDegreeInfo extends InfoObject implements ISiteComponent {

    private InfoDegree infoDegree;
    private DomainReference<ExecutionYear> executionYearDomainReference;
    
    private String description;
    private String history;
    private String objectives;
    private String designedFor;
    private String professionalExits;
    private String operationalRegime;
    private String gratuity;
    private String schoolCalendar;
    private String candidacyPeriod;
    private String selectionResultDeadline;
    private String enrolmentPeriod;
    private String additionalInfo;
    private String links;

    private String testIngression;
    private String classifications;
    private String accessRequisites;
    private String candidacyDocuments;
    private Integer driftsInitial;
    private Integer driftsFirst;
    private Integer driftsSecond;
    private Double markMin;
    private Double markMax;
    private Double markAverage;

    private String qualificationLevel;
    private String recognitions;

    // in english
    private String descriptionEn;
    private String historyEn;
    private String objectivesEn;
    private String designedForEn;
    private String professionalExitsEn;
    private String operationalRegimeEn;
    private String gratuityEn;
    private String schoolCalendarEn;
    private String candidacyPeriodEn;
    private String selectionResultDeadlineEn;
    private String enrolmentPeriodEn;
    private String additionalInfoEn;
    private String linksEn;

    private String testIngressionEn;
    private String classificationsEn;
    private String accessRequisitesEn;
    private String candidacyDocumentsEn;
    
    private String qualificationLevelEn;
    private String recognitionsEn;
    
    public void prepareEnglishPresentation(Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            this.description = this.descriptionEn;
            this.history = this.historyEn;
            this.objectives = this.objectivesEn;
            this.designedFor = this.designedForEn;
            this.professionalExits = this.professionalExitsEn;
            this.operationalRegime = this.operationalRegimeEn;
            this.gratuity = this.gratuityEn;
            this.schoolCalendar = this.schoolCalendarEn;
            this.candidacyPeriod = this.candidacyPeriodEn;
            this.selectionResultDeadline = this.selectionResultDeadlineEn;
            this.enrolmentPeriod = this.enrolmentPeriodEn;
            this.additionalInfo = this.additionalInfoEn;
            this.links = this.linksEn;
            
            this.testIngression = this.testIngressionEn;
            this.classifications = this.classificationsEn;
            this.accessRequisites = this.accessRequisitesEn;
            this.candidacyDocuments = this.candidacyDocumentsEn;
            
            this.qualificationLevel = this.qualificationLevelEn;
            this.recognitions = this.recognitionsEn;
        }
    }

    public void copyFromDomain(DegreeInfo degreeInfo) {
        super.copyFromDomain(degreeInfo);
        if (degreeInfo != null) {
            setInfoDegree(InfoDegree.newInfoFromDomain(degreeInfo.getDegree()));

            setDescription((degreeInfo.getDescription() != null) ? degreeInfo.getDescription().getContent(Language.pt) : null);
            setHistory((degreeInfo.getHistory() != null) ? degreeInfo.getHistory().getContent(Language.pt) : null);
            setObjectives((degreeInfo.getObjectives() != null) ? degreeInfo.getObjectives().getContent(Language.pt) : null);
            setDesignedFor((degreeInfo.getDesignedFor() != null) ? degreeInfo.getDesignedFor().getContent(Language.pt) : null);
            setProfessionalExits((degreeInfo.getProfessionalExits() != null) ? degreeInfo.getProfessionalExits().getContent(Language.pt) : null);
            setOperationalRegime((degreeInfo.getOperationalRegime() != null) ? degreeInfo.getOperationalRegime().getContent(Language.pt) : null);
            setGratuity((degreeInfo.getGratuity() != null) ? degreeInfo.getGratuity().getContent(Language.pt) : null);
            setSchoolCalendar((degreeInfo.getSchoolCalendar() != null) ? degreeInfo.getSchoolCalendar().getContent(Language.pt) : null);
            setCandidacyPeriod((degreeInfo.getCandidacyPeriod() != null) ? degreeInfo.getCandidacyPeriod().getContent(Language.pt) : null);
            setSelectionResultDeadline((degreeInfo.getSelectionResultDeadline() != null) ? degreeInfo.getSelectionResultDeadline().getContent(Language.pt) : null);
            setEnrolmentPeriod((degreeInfo.getEnrolmentPeriod() != null) ? degreeInfo.getEnrolmentPeriod().getContent(Language.pt) : null);
            setAdditionalInfo((degreeInfo.getAdditionalInfo() != null) ? degreeInfo.getAdditionalInfo().getContent(Language.pt) : null);
            setLinks((degreeInfo.getLinks() != null) ? degreeInfo.getLinks().getContent(Language.pt) : null);

            setTestIngression((degreeInfo.getTestIngression() != null) ? degreeInfo.getTestIngression().getContent(Language.pt) : null);
            setClassifications((degreeInfo.getClassifications() != null) ? degreeInfo.getClassifications().getContent(Language.pt) : null);
            setAccessRequisites((degreeInfo.getAccessRequisites() != null) ? degreeInfo.getAccessRequisites().getContent(Language.pt) : null);
            setCandidacyDocuments((degreeInfo.getCandidacyDocuments() != null) ? degreeInfo.getCandidacyDocuments().getContent(Language.pt) : null);
            setDriftsInitial(degreeInfo.getDriftsInitial());
            setDriftsFirst(degreeInfo.getDriftsFirst());
            setDriftsSecond(degreeInfo.getDriftsSecond());
            setMarkMin(degreeInfo.getMarkMin());
            setMarkMax(degreeInfo.getMarkMax());
            setMarkAverage(degreeInfo.getMarkAverage());

            setQualificationLevel((degreeInfo.getQualificationLevel() != null) ? degreeInfo.getQualificationLevel().getContent(Language.pt) : null);
            setRecognitions((degreeInfo.getRecognitions() != null) ? degreeInfo.getRecognitions().getContent(Language.pt) : null);

            // in english
            setDescriptionEn((degreeInfo.getDescription() != null) ? degreeInfo.getDescription().getContent(Language.en) : null);
            setHistoryEn((degreeInfo.getHistory() != null) ? degreeInfo.getHistory().getContent(Language.en) : null);
            setObjectivesEn((degreeInfo.getObjectives() != null) ? degreeInfo.getObjectives().getContent(Language.en) : null);
            setDesignedForEn((degreeInfo.getDesignedFor() != null) ? degreeInfo.getDesignedFor().getContent(Language.en) : null);
            setProfessionalExitsEn((degreeInfo.getProfessionalExits() != null) ? degreeInfo.getProfessionalExits().getContent(Language.en) : null);
            setOperationalRegimeEn((degreeInfo.getOperationalRegime() != null) ? degreeInfo.getOperationalRegime().getContent(Language.en) : null);
            setGratuityEn((degreeInfo.getGratuity() != null) ? degreeInfo.getGratuity().getContent(Language.en) : null);
            setSchoolCalendarEn((degreeInfo.getSchoolCalendar() != null) ? degreeInfo.getSchoolCalendar().getContent(Language.en) : null);
            setCandidacyPeriodEn((degreeInfo.getCandidacyPeriod() != null) ? degreeInfo.getCandidacyPeriod().getContent(Language.en) : null);
            setSelectionResultDeadlineEn((degreeInfo.getSelectionResultDeadline() != null) ? degreeInfo.getSelectionResultDeadline().getContent(Language.en) : null);
            setEnrolmentPeriodEn((degreeInfo.getEnrolmentPeriod() != null) ? degreeInfo.getEnrolmentPeriod().getContent(Language.en) : null);
            setAdditionalInfoEn((degreeInfo.getAdditionalInfo() != null) ? degreeInfo.getAdditionalInfo().getContent(Language.en) : null);
            setLinksEn((degreeInfo.getLinks() != null) ? degreeInfo.getLinks().getContent(Language.en) : null);
            
            setTestIngressionEn((degreeInfo.getTestIngression() != null) ? degreeInfo.getTestIngression().getContent(Language.en) : null);
            setClassificationsEn((degreeInfo.getClassifications() != null) ? degreeInfo.getClassifications().getContent(Language.en) : null);            
            setAccessRequisitesEn((degreeInfo.getAccessRequisites() != null) ? degreeInfo.getAccessRequisites().getContent(Language.en) : null);
            setCandidacyDocumentsEn((degreeInfo.getCandidacyDocuments() != null) ? degreeInfo.getCandidacyDocuments().getContent(Language.en) : null);
            
            setQualificationLevelEn((degreeInfo.getQualificationLevel() != null) ? degreeInfo.getQualificationLevel().getContent(Language.en) : null);
            setRecognitionsEn((degreeInfo.getRecognitions() != null) ? degreeInfo.getRecognitions().getContent(Language.en) : null);
        }
    }

    public static InfoDegreeInfo newInfoFromDomain(DegreeInfo degreeInfo) {
        InfoDegreeInfo infoDegreeInfo = null;
        if (degreeInfo != null) {
            infoDegreeInfo = new InfoDegreeInfo();
            infoDegreeInfo.copyFromDomain(degreeInfo);
        }
        return infoDegreeInfo;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoDegreeInfo) {
            InfoDegreeInfo infoDegreeInfo = (InfoDegreeInfo) obj;
            result = getInfoDegree().equals(infoDegreeInfo.getInfoDegree());
        }
        return result;
    }

    public String toString() {
        String result = "[INFODEGREE_INFO:";
        result += " codigo interno= " + getIdInternal();
        result += " degree= " + getInfoDegree();
        result += " descri��o= " + getDescription();
        result += " objectivos= " + getObjectives();
        result += " historial= " + getHistory();
        result += " saidas profissionais=" + getProfessionalExits();
        result += " informa��o adicional= " + getAdditionalInfo();
        result += " links= " + getLinks();
        result += " provas de ingresso= " + getTestIngression();
        result += " classifica��es= " + getClassifications();
        result += " descri��o(En)= " + getDescriptionEn();
        result += " objectivos(En)= " + getObjectivesEn();
        result += " historial(En)= " + getHistoryEn();
        result += " saidas profissionais(En)=" + getProfessionalExitsEn();
        result += " informa��o adicional(En)= " + getAdditionalInfoEn();
        result += " links(En)= " + getLinksEn();
        result += " provas de ingresso(En)= " + getTestIngressionEn();
        result += " classifica��es(En)= " + getClassificationsEn();
        result += " vagas iniciais= " + getDriftsInitial();
        result += " vagas 1� fase= " + getDriftsFirst();
        result += " vagas 2�fase= " + getDriftsSecond();
        result += " nota minima= " + getMarkMin();
        result += " nota m�xima= " + getMarkMax();
        result += " nota m�dia= " + getMarkAverage();
        result += "]";
        return result;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getClassifications() {
        return classifications;
    }

    public void setClassifications(String classifications) {
        this.classifications = classifications;
    }

    public Integer getDriftsFirst() {
        return driftsFirst;
    }

    public void setDriftsFirst(Integer driftsFirst) {
        this.driftsFirst = driftsFirst;
    }

    public Integer getDriftsInitial() {
        return driftsInitial;
    }

    public void setDriftsInitial(Integer driftsInitial) {
        this.driftsInitial = driftsInitial;
    }

    public Integer getDriftsSecond() {
        return driftsSecond;
    }

    public void setDriftsSecond(Integer driftsSecond) {
        this.driftsSecond = driftsSecond;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public InfoDegree getInfoDegree() {
        return infoDegree;
    }

    public void setInfoDegree(InfoDegree infoDegree) {
        this.infoDegree = infoDegree;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public Double getMarkAverage() {
        return markAverage;
    }

    public void setMarkAverage(Double markAverage) {
        this.markAverage = markAverage;
    }

    public Double getMarkMax() {
        return markMax;
    }

    public void setMarkMax(Double markMax) {
        this.markMax = markMax;
    }

    public Double getMarkMin() {
        return markMin;
    }

    public void setMarkMin(Double markMin) {
        this.markMin = markMin;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getProfessionalExits() {
        return professionalExits;
    }

    public void setProfessionalExits(String professionalExits) {
        this.professionalExits = professionalExits;
    }

    public String getTestIngression() {
        return testIngression;
    }

    public void setTestIngression(String testIngression) {
        this.testIngression = testIngression;
    }

    public String getAdditionalInfoEn() {
        return additionalInfoEn;
    }

    public void setAdditionalInfoEn(String additionalInfoEn) {
        this.additionalInfoEn = additionalInfoEn;
    }

    public String getClassificationsEn() {
        return classificationsEn;
    }

    public void setClassificationsEn(String classificationsEn) {
        this.classificationsEn = classificationsEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getHistoryEn() {
        return historyEn;
    }

    public void setHistoryEn(String historyEn) {
        this.historyEn = historyEn;
    }

    public String getLinksEn() {
        return linksEn;
    }

    public void setLinksEn(String linksEn) {
        this.linksEn = linksEn;
    }

    public String getObjectivesEn() {
        return objectivesEn;
    }

    public void setObjectivesEn(String objectivesEn) {
        this.objectivesEn = objectivesEn;
    }

    public String getProfessionalExitsEn() {
        return professionalExitsEn;
    }

    public void setProfessionalExitsEn(String professionalExitsEn) {
        this.professionalExitsEn = professionalExitsEn;
    }

    public String getTestIngressionEn() {
        return testIngressionEn;
    }

    public void setTestIngressionEn(String testIngressionEn) {
        this.testIngressionEn = testIngressionEn;
    }

    public String getQualificationLevel() {
        return qualificationLevel;
    }

    public void setQualificationLevel(String qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }

    public String getQualificationLevelEn() {
        return qualificationLevelEn;
    }

    public void setQualificationLevelEn(String qualificationLevelEn) {
        this.qualificationLevelEn = qualificationLevelEn;
    }

    public String getRecognitions() {
        return recognitions;
    }

    public void setRecognitions(String recognitions) {
        this.recognitions = recognitions;
    }

    public String getRecognitionsEn() {
        return recognitionsEn;
    }

    public void setRecognitionsEn(String recognitionsEn) {
        this.recognitionsEn = recognitionsEn;
    }

    public String getAccessRequisites() {
        return accessRequisites;
    }

    public void setAccessRequisites(String accessRequisites) {
        this.accessRequisites = accessRequisites;
    }

    public String getAccessRequisitesEn() {
        return accessRequisitesEn;
    }

    public void setAccessRequisitesEn(String accessRequisitesEn) {
        this.accessRequisitesEn = accessRequisitesEn;
    }

    public String getCandidacyDocuments() {
        return candidacyDocuments;
    }

    public void setCandidacyDocuments(String candidacyDocuments) {
        this.candidacyDocuments = candidacyDocuments;
    }

    public String getCandidacyDocumentsEn() {
        return candidacyDocumentsEn;
    }

    public void setCandidacyDocumentsEn(String candidacyDocumentsEn) {
        this.candidacyDocumentsEn = candidacyDocumentsEn;
    }

    public String getCandidacyPeriod() {
        return candidacyPeriod;
    }

    public void setCandidacyPeriod(String candidacyPeriod) {
        this.candidacyPeriod = candidacyPeriod;
    }

    public String getCandidacyPeriodEn() {
        return candidacyPeriodEn;
    }

    public void setCandidacyPeriodEn(String candidacyPeriodEn) {
        this.candidacyPeriodEn = candidacyPeriodEn;
    }

    public String getDesignedFor() {
        return designedFor;
    }

    public void setDesignedFor(String designedFor) {
        this.designedFor = designedFor;
    }

    public String getDesignedForEn() {
        return designedForEn;
    }

    public void setDesignedForEn(String designedForEn) {
        this.designedForEn = designedForEn;
    }

    public String getEnrolmentPeriod() {
        return enrolmentPeriod;
    }

    public void setEnrolmentPeriod(String enrolmentPeriod) {
        this.enrolmentPeriod = enrolmentPeriod;
    }

    public String getEnrolmentPeriodEn() {
        return enrolmentPeriodEn;
    }

    public void setEnrolmentPeriodEn(String enrolmentPeriodEn) {
        this.enrolmentPeriodEn = enrolmentPeriodEn;
    }

    public String getGratuity() {
        return gratuity;
    }

    public void setGratuity(String gratuity) {
        this.gratuity = gratuity;
    }

    public String getGratuityEn() {
        return gratuityEn;
    }

    public void setGratuityEn(String gratuityEn) {
        this.gratuityEn = gratuityEn;
    }

    public String getOperationalRegime() {
        return operationalRegime;
    }

    public void setOperationalRegime(String operationalRegime) {
        this.operationalRegime = operationalRegime;
    }

    public String getOperationalRegimeEn() {
        return operationalRegimeEn;
    }

    public void setOperationalRegimeEn(String operationalRegimeEn) {
        this.operationalRegimeEn = operationalRegimeEn;
    }

    public String getSchoolCalendar() {
        return schoolCalendar;
    }

    public void setSchoolCalendar(String schoolCalendar) {
        this.schoolCalendar = schoolCalendar;
    }

    public String getSchoolCalendarEn() {
        return schoolCalendarEn;
    }

    public void setSchoolCalendarEn(String schoolCalendarEn) {
        this.schoolCalendarEn = schoolCalendarEn;
    }

    public String getSelectionResultDeadline() {
        return selectionResultDeadline;
    }

    public void setSelectionResultDeadline(String selectionResultDeadline) {
        this.selectionResultDeadline = selectionResultDeadline;
    }

    public String getSelectionResultDeadlineEn() {
        return selectionResultDeadlineEn;
    }

    public void setSelectionResultDeadlineEn(String selectionResultDeadlineEn) {
        this.selectionResultDeadlineEn = selectionResultDeadlineEn;
    }

}