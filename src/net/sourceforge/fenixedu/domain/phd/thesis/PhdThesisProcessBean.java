package net.sourceforge.fenixedu.domain.phd.thesis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

public class PhdThesisProcessBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String remarks;

    private Boolean finalThesis;

    private List<PhdProgramDocumentUploadBean> documents;

    public PhdThesisProcessBean() {
	this.documents = new ArrayList<PhdProgramDocumentUploadBean>();
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

}
