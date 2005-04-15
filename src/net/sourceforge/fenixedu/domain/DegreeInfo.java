package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;

/**
 * @author Tania Pousao Created on 30/Out/2003
 */
public class DegreeInfo extends DegreeInfo_Base {
    private IDegree degree;

    private Timestamp lastModificationDate;

    public DegreeInfo(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public DegreeInfo() {
    }

    public DegreeInfo(IDegree degree, String objectives, String history, String professionalExits,
            String additionalInfo, String links, String testIngression, Integer driftsInitial,
            Integer driftsFirst, Integer driftsSecond, String classifications, Double markMin,
            Double markMax, Double markAverage, Timestamp lastModificationDate) {
        this.setDegreeKey(degree.getIdInternal());
        this.setDegree(degree);
        this.setObjectives(objectives);
        this.setHistory(history);
        this.setProfessionalExits(professionalExits);
        this.setAdditionalInfo(additionalInfo);
        this.setLinks(links);
        this.setTestIngression(testIngression);
        this.setDriftsInitial(driftsInitial);
        this.setDriftsFirst(driftsFirst);
        this.setDriftsSecond(driftsSecond);
        this.setClassifications(classifications);
        this.setMarkMin(markMin);
        this.setMarkMax(markMax);
        this.setMarkAverage(markAverage);
        this.setLastModificationDate(lastModificationDate);
    }

    public DegreeInfo(Integer degreeKey, IDegree degree, String objectives, String history,
            String professionalExits, String additionalInfo, String links, String testIngression,
            Integer driftsInitial, Integer driftsFirst, Integer driftsSecond, String classifications,
            Double markMin, Double markMax, Double markAverage, Timestamp lastModificationDate) {
        this.setDegreeKey(degreeKey);
        this.setDegree(degree);
        this.setObjectives(objectives);
        this.setHistory(history);
        this.setProfessionalExits(professionalExits);
        this.setAdditionalInfo(additionalInfo);
        this.setLinks(links);
        this.setTestIngression(testIngression);
        this.setDriftsInitial(driftsInitial);
        this.setDriftsFirst(driftsFirst);
        this.setDriftsSecond(driftsSecond);
        this.setClassifications(classifications);
        this.setMarkMin(markMin);
        this.setMarkMax(markMax);
        this.setMarkAverage(markAverage);
        this.setLastModificationDate(lastModificationDate);
    }

    public IDegree getDegree() {
        return degree;
    }

    public void setDegree(IDegree degree) {
        this.degree = degree;
    }

    public Timestamp getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Timestamp lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IDegreeInfo) {
            IDegreeInfo degreeInfo = (IDegreeInfo) obj;
            result = (getDegree() != null && getDegree().equals(degreeInfo.getDegree()));
        }

        return result;
    }

    public String toString() {
        String result = "[DEGREE_INFO:";
        result += " codigo interno= " + getIdInternal();
        result += " degree= " + getDegreeKey();
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
}