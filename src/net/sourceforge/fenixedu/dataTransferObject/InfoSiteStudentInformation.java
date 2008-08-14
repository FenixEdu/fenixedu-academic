/*
 * Created on 4/Ago/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author asnr and scpo
 * 
 */
public class InfoSiteStudentInformation extends DataTranferObject implements ISiteComponent {
    private String name;
    private Integer number;
    private String email;
    private String username;

    public InfoSiteStudentInformation() {
    }

    public InfoSiteStudentInformation(String name, String email, String username, Integer number) {
	this.name = name;
	this.email = email;
	this.username = username;
	this.number = number;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Integer getNumber() {
	return number;
    }

    public void setNumber(Integer number) {
	this.number = number;
    }

    public boolean equals(Object arg0) {
	boolean result = false;
	if (arg0 instanceof InfoSiteStudentInformation) {
	    result = (getName().equals(((InfoSiteStudentInformation) arg0).getName()))
		    && (getNumber().equals(((InfoSiteStudentInformation) arg0).getNumber()))
		    && (getEmail().equals(((InfoSiteStudentInformation) arg0).getEmail()))
		    && (getUsername().equals(((InfoSiteStudentInformation) arg0).getUsername()));
	}
	return result;
    }
}