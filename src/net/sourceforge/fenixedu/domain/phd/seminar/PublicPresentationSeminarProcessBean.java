package net.sourceforge.fenixedu.domain.phd.seminar;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

import org.joda.time.LocalDate;

public class PublicPresentationSeminarProcessBean implements Serializable {

    private static final long serialVersionUID = -7837778662130742070L;

    private String remarks;

    private PhdProgramDocumentUploadBean document;

    private LocalDate presentationDate;

    private Boolean generateAlert;

    public PublicPresentationSeminarProcessBean() {
	this.document = new PhdProgramDocumentUploadBean();
    }

    public PublicPresentationSeminarProcessBean(final PhdIndividualProgramProcess process) {
	this();
	setGenerateAlert(process.getPhdConfigurationIndividualProgramProcess().getGenerateAlert());
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public PhdProgramDocumentUploadBean getDocument() {
	return document;
    }

    public void setDocument(PhdProgramDocumentUploadBean document) {
	this.document = document;
    }

    public LocalDate getPresentationDate() {
	return presentationDate;
    }

    public void setPresentationDate(LocalDate presentationDate) {
	this.presentationDate = presentationDate;
    }

    public Boolean getGenerateAlert() {
	return generateAlert;
    }

    public void setGenerateAlert(Boolean generateAlert) {
	this.generateAlert = generateAlert;
    }
}
