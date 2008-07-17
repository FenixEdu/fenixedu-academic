package net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.DomainReference;

public class AlumniLinkRequestBean implements Serializable {

    private DomainReference<Alumni> alumni;
    private String documentIdNumber;
    private Integer studentNumber;
    private String email;
    private Boolean privacyPolicy;

    public AlumniLinkRequestBean() {
    }

    public AlumniLinkRequestBean(Alumni alumni) {
	setAlumni(alumni);
    }

    public String getDocumentIdNumber() {
	return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
	this.documentIdNumber = documentIdNumber;
    }

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Alumni getAlumni() {
	return (this.alumni != null) ? this.alumni.getObject() : null;
    }

    public void setAlumni(Alumni alumni) {
	this.alumni = new DomainReference<Alumni>(alumni);
    }

    public Boolean getPrivacyPolicy() {
	return privacyPolicy;
    }

    public void setPrivacyPolicy(Boolean privacyPolicy) {
	this.privacyPolicy = privacyPolicy;
    }
}
