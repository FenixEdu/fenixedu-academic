package net.sourceforge.fenixedu.domain.phd.access;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

public class PhdExternalOperationBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private PhdParticipant participant;
    private PhdProgramDocumentUploadBean documentBean;

    private String email;
    private String password;


    public PhdExternalOperationBean(PhdParticipant participant) {
	setParticipant(participant);
    }

    public PhdParticipant getParticipant() {
	return participant;
    }

    public void setParticipant(PhdParticipant participant) {
	this.participant = participant;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public PhdProgramDocumentUploadBean getDocumentBean() {
	return documentBean;
    }

    public void setDocumentBean(PhdProgramDocumentUploadBean documentBean) {
	this.documentBean = documentBean;
    }

}
