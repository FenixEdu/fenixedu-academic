package net.sourceforge.fenixedu.domain.phd.thesis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdThesisProcessBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private PhdIndividualProgramProcess process;
    private PhdThesisProcess thesisProcess;

    private boolean toNotify = true;
    private String remarks;
    private Boolean finalThesis;
    private LocalDate whenJuryValidated;
    private LocalDate whenJuryDesignated;

    private List<PhdProgramDocumentUploadBean> documents;

    private ThesisJuryElement juryElement;

    private DateTime scheduledDate;
    private String scheduledPlace;

    private String mailSubject, mailBody;

    private LocalDate whenFinalThesisRatified;

    public PhdThesisProcessBean() {
	this.documents = new ArrayList<PhdProgramDocumentUploadBean>();
    }

    public PhdIndividualProgramProcess getProcess() {
	return process;
    }

    public void setProcess(PhdIndividualProgramProcess process) {
	this.process = process;
    }

    public PhdThesisProcess getThesisProcess() {
	return thesisProcess;
    }

    public void setThesisProcess(PhdThesisProcess thesisProcess) {
	this.thesisProcess = thesisProcess;
    }

    public boolean isToNotify() {
	return toNotify;
    }

    public void setToNotify(boolean toNotify) {
	this.toNotify = toNotify;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public List<PhdProgramDocumentUploadBean> getDocuments() {
	return documents;
    }

    public void addDocument(PhdProgramDocumentUploadBean document) {
	this.documents.add(document);
    }

    public void setDocuments(List<PhdProgramDocumentUploadBean> documents) {
	this.documents = documents;
    }

    public Boolean getFinalThesis() {
	return finalThesis;
    }

    public void setFinalThesis(Boolean finalThesis) {
	this.finalThesis = finalThesis;
    }

    public LocalDate getWhenJuryValidated() {
	return whenJuryValidated;
    }

    public void setWhenJuryValidated(LocalDate whenJuryValidated) {
	this.whenJuryValidated = whenJuryValidated;
    }

    public LocalDate getWhenJuryDesignated() {
	return whenJuryDesignated;
    }

    public void setWhenJuryDesignated(LocalDate whenJuryDesignated) {
	this.whenJuryDesignated = whenJuryDesignated;
    }

    public ThesisJuryElement getJuryElement() {
	return juryElement;
    }

    public void setJuryElement(ThesisJuryElement juryElement) {
	this.juryElement = juryElement;
    }

    public DateTime getScheduledDate() {
	return scheduledDate;
    }

    public void setScheduledDate(DateTime scheduledDate) {
	this.scheduledDate = scheduledDate;
    }

    public String getScheduledPlace() {
	return scheduledPlace;
    }

    public void setScheduledPlace(String scheduledPlace) {
	this.scheduledPlace = scheduledPlace;
    }

    public String getMailSubject() {
	return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
	this.mailSubject = mailSubject;
    }

    public String getMailBody() {
	return mailBody;
    }

    public void setMailBody(String mailBody) {
	this.mailBody = mailBody;
    }

    public LocalDate getWhenFinalThesisRatified() {
	return whenFinalThesisRatified;
    }

    public void setWhenFinalThesisRatified(LocalDate whenFinalThesisRatified) {
	this.whenFinalThesisRatified = whenFinalThesisRatified;
    }

}
