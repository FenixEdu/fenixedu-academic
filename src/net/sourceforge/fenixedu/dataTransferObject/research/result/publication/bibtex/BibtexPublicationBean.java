package net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;

public class BibtexPublicationBean implements Serializable {

    private ResultPublicationBean publicationBean;

    private List<BibtexParticipatorBean> authors;

    private List<BibtexParticipatorBean> editors;

    private boolean processedParticipators = false;

    private String bibtex;

    public BibtexPublicationBean() {
    	authors = new ArrayList<BibtexParticipatorBean>();
    	editors = new ArrayList<BibtexParticipatorBean>();
    }
    
    public List<BibtexParticipatorBean> getAuthors() {
	return authors;
    }

    public void setAuthors(List<BibtexParticipatorBean> authors) {
	this.authors = authors;
    }

    public List<BibtexParticipatorBean> getEditors() {
	return editors;
    }

    public void setEditors(List<BibtexParticipatorBean> editors) {
	this.editors = editors;
    }

    public ResultPublicationBean getPublicationBean() {
	return publicationBean;
    }

    public void setPublicationBean(ResultPublicationBean publicationBean) {
	this.publicationBean = publicationBean;
    }

    public boolean isProcessedParticipators() {
	return processedParticipators;
    }

    public void setProcessedParticipators(boolean processedParticipators) {
	this.processedParticipators = processedParticipators;
    }

    public String getBibtex() {
	return bibtex;
    }

    public void setBibtex(String bibtex) {
	this.bibtex = bibtex;
    }

    public List<BibtexParticipatorBean> getParticipators() {
	List<BibtexParticipatorBean> participators = new ArrayList<BibtexParticipatorBean>();
	if (authors != null)
	    participators.addAll(authors);
	if (editors != null)
	    participators.addAll(editors);
	if (participators.size() > 0)
	    return participators;
	return null;
    }

}
