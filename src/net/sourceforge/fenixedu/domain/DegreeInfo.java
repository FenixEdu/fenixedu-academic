package net.sourceforge.fenixedu.domain;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Tania Pousao Created on 30/Out/2003
 */
public class DegreeInfo extends DegreeInfo_Base {

    protected DegreeInfo(Degree degree, ExecutionYear executionYear) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDegree(degree);
	setExecutionYear(executionYear);
	new DegreeInfoCandidacy(this);
	new DegreeInfoFuture(this);
    }

    protected DegreeInfo(DegreeInfo degreeInfo, ExecutionYear executionYear) {
	this(degreeInfo.getDegree(), executionYear);

	setDescription(degreeInfo.getDescription());
	setHistory(degreeInfo.getHistory());
	setObjectives(degreeInfo.getObjectives());
	setDesignedFor(degreeInfo.getDesignedFor());
	setProfessionalExits(degreeInfo.getProfessionalExits());
	setOperationalRegime(degreeInfo.getOperationalRegime());
	setGratuity(degreeInfo.getGratuity());
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
    }

    public void delete() {
	removeRootDomainObject();
	removeDegree();
	removeExecutionYear();
	deleteDomainObject();
    }

    public MultiLanguageString getAccessRequisites() {
        return getDegreeInfoCandidacy().getAccessRequisites();
    }

    public MultiLanguageString getCandidacyDocuments() {
        return getDegreeInfoCandidacy().getCandidacyDocuments();
    }

    public MultiLanguageString getCandidacyPeriod() {
        return getDegreeInfoCandidacy().getCandidacyPeriod();
    }

    public MultiLanguageString getClassifications() {
        return getDegreeInfoFuture().getClassifications();
    }

    public MultiLanguageString getDesignedFor() {
        return getDegreeInfoFuture().getDesignedFor();
    }

    public MultiLanguageString getEnrolmentPeriod() {
        return getDegreeInfoCandidacy().getEnrolmentPeriod();
    }

    public MultiLanguageString getObjectives() {
        return getDegreeInfoFuture().getObjectives();
    }

    public MultiLanguageString getProfessionalExits() {
        return getDegreeInfoFuture().getProfessionalExits();
    }

    public MultiLanguageString getQualificationLevel() {
        return getDegreeInfoFuture().getQualificationLevel();
    }

    public MultiLanguageString getRecognitions() {
        return getDegreeInfoFuture().getRecognitions();
    }

    public MultiLanguageString getSelectionResultDeadline() {
        return getDegreeInfoCandidacy().getSelectionResultDeadline();
    }

    public MultiLanguageString getTestIngression() {
        return getDegreeInfoCandidacy().getTestIngression();
    }

    public void setAccessRequisites(MultiLanguageString accessRequisites) {
        getDegreeInfoCandidacy().setAccessRequisites(accessRequisites);
    }

    public void setCandidacyDocuments(MultiLanguageString candidacyDocuments) {
        getDegreeInfoCandidacy().setCandidacyDocuments(candidacyDocuments);
    }

    public void setCandidacyPeriod(MultiLanguageString candidacyPeriod) {
        getDegreeInfoCandidacy().setCandidacyPeriod(candidacyPeriod);
    }

    public void setClassifications(MultiLanguageString classifications) {
        getDegreeInfoFuture().setClassifications(classifications);
    }

    public void setDesignedFor(MultiLanguageString designedFor) {
        getDegreeInfoFuture().setDesignedFor(designedFor);
    }

    public void setEnrolmentPeriod(MultiLanguageString enrolmentPeriod) {
        getDegreeInfoCandidacy().setEnrolmentPeriod(enrolmentPeriod);
    }

    public void setObjectives(MultiLanguageString objectives) {
        getDegreeInfoFuture().setObjectives(objectives);
    }

    public void setProfessionalExits(MultiLanguageString professionalExits) {
        getDegreeInfoFuture().setProfessionalExits(professionalExits);
    }

    public void setQualificationLevel(MultiLanguageString qualificationLevel) {
        getDegreeInfoFuture().setQualificationLevel(qualificationLevel);
    }

    public void setRecognitions(MultiLanguageString recognitions) {
        getDegreeInfoFuture().setRecognitions(recognitions);
    }

    public void setSelectionResultDeadline(MultiLanguageString selectionResultDeadline) {
        getDegreeInfoCandidacy().setSelectionResultDeadline(selectionResultDeadline);
    }

    public void setTestIngression(MultiLanguageString testIngression) {
        getDegreeInfoCandidacy().setTestIngression(testIngression);
    }

}
