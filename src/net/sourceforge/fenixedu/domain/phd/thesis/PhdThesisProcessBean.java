package net.sourceforge.fenixedu.domain.phd.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

public class PhdThesisProcessBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String remarks;

    private Boolean finalThesis;

    private PhdProgramDocumentUploadBean document;

    public PhdThesisProcessBean() {
	this.document = new PhdProgramDocumentUploadBean();
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

    public Boolean getFinalThesis() {
	return finalThesis;
    }

    public void setFinalThesis(Boolean finalThesis) {
	this.finalThesis = finalThesis;
    }

}
