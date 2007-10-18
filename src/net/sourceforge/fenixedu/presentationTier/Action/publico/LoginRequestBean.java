package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.LoginRequest;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class LoginRequestBean implements Serializable {

	private DomainReference<Person> person;

	private String name;

	private String documentIDNumber;

	private String email;

	private IDDocumentType documentType;

	private Gender gender;

	private String phone;
	
	private String username;

	private String password;

	private String passwordConfirmation;
	
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public LoginRequestBean(LoginRequest request) {
		setPerson(request.getUser().getPerson());
	}

	public String getDocumentIDNumber() {
		return documentIDNumber;
	}

	public void setDocumentIDNumber(String documentIDNumber) {
		this.documentIDNumber = documentIDNumber;
	}

	public IDDocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(IDDocumentType documentType) {
		this.documentType = documentType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Person getPerson() {
		return person.getObject();
	}

	public void setPerson(Person person) {
		this.person = new DomainReference<Person>(person);
		this.name = person.getName();
		this.documentIDNumber = person.getIdDocuments().get(0).getValue();
		this.documentType = person.getIdDocuments().get(0).getIdDocumentType().getValue();
		this.email = person.getEmail();
		this.gender = null;
		this.username = person.getUser().readUserLoginIdentification().getUsername();
		this.password = null;
		this.phone = person.getPhone();
	}

	public String getPhone() {
	    return phone;
	}

	public void setPhone(String phone) {
	    this.phone = phone;
	}
	
	
}
