package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * @author T�nia Pous�o Created on 30/Out/2003
 */
public class InfoDegreeInfo extends InfoObject implements ISiteComponent {

    private InfoDegree infoDegree;
    private ExecutionYear executionYear;
    
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

            this.infoDegree.prepareEnglishPresentation(locale);
        }
    }

    public void copyFromDomain(DegreeInfo degreeInfo) {
        super.copyFromDomain(degreeInfo);
        if (degreeInfo != null) {
            setInfoDegree(InfoDegree.newInfoFromDomain(degreeInfo.getDegree()));
            
            setDescription(degreeInfo.getDescription());
            setHistory(degreeInfo.getHistory());
            setObjectives(degreeInfo.getObjectives());
            setDesignedFor(degreeInfo.getDesignedFor());
            setProfessionalExits(degreeInfo.getProfessionalExits());
            setOperationalRegime(degreeInfo.getOperationalRegime());
            setGratuity(degreeInfo.getGratuity());
            setSchoolCalendar(degreeInfo.getSchoolCalendar());
            setCandidacyPeriod(degreeInfo.getCandidacyPeriod());
            setSelectionResultDeadline(degreeInfo.getSelectionResultDeadline());
            setEnrolmentPeriod(degreeInfo.getEnrolmentPeriod());
            setAdditionalInfo(degreeInfo.getAdditionalInfo());
            setLinks(degreeInfo.getLinks());

            setTestIngression(degreeInfo.getTestIngression());
            setClassifications(degreeInfo.getClassifications());
            setAccessRequisites(degreeInfo.getAccessRequisites());
            setCandidacyDocuments(degreeInfo.getCandidacyDocuments());
            setDriftsInitial(degreeInfo.getDriftsInitial());
            setDriftsFirst(degreeInfo.getDriftsFirst());
            setDriftsSecond(degreeInfo.getDriftsSecond());
            setMarkMin(degreeInfo.getMarkMin());
            setMarkMax(degreeInfo.getMarkMax());
            setMarkAverage(degreeInfo.getMarkAverage());

            setQualificationLevel(degreeInfo.getQualificationLevel());
            setRecognitions(degreeInfo.getRecognitions());
            
            // in english
            setDescriptionEn(degreeInfo.getDescriptionEn());
            setHistoryEn(degreeInfo.getHistoryEn());
            setObjectivesEn(degreeInfo.getObjectivesEn());
            setDesignedForEn(degreeInfo.getDesignedForEn());
            setProfessionalExitsEn(degreeInfo.getProfessionalExitsEn());
            setOperationalRegimeEn(degreeInfo.getOperationalRegimeEn());
            setGratuityEn(degreeInfo.getGratuityEn());
            setSchoolCalendarEn(degreeInfo.getSchoolCalendarEn());
            setCandidacyPeriodEn(degreeInfo.getCandidacyPeriodEn());
            setSelectionResultDeadlineEn(degreeInfo.getSelectionResultDeadlineEn());
            setEnrolmentPeriodEn(degreeInfo.getEnrolmentPeriodEn());
            setAdditionalInfoEn(degreeInfo.getAdditionalInfoEn());
            setLinksEn(degreeInfo.getLinksEn());
            
            setTestIngressionEn(degreeInfo.getTestIngressionEn());
            setClassificationsEn(degreeInfo.getClassificationsEn());            
            setAccessRequisitesEn(degreeInfo.getAccessRequisitesEn());
            setCandidacyDocumentsEn(degreeInfo.getCandidacyDocumentsEn());
            
            setQualificationLevelEn(degreeInfo.getQualificationLevelEn());
            setRecognitionsEn(degreeInfo.getRecognitionsEn());
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
        result += " data �ltima modifica��o= " + getLastModificationDate();
        result += "]";
        return result;
    }

    public InfoExecutionYear getLastModificationDate() {
        return InfoExecutionYear.newInfoFromDomain(executionYear);
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
