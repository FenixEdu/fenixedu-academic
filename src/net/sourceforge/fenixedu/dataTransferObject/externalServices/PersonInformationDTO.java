/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PersonInformationDTO {

    private String name;

    private String displayName;

    private String phone;

    private String workPhone;

    private String mobile;

    private String webAddress;

    private String email;

    private String gender;

    private Boolean availableEmail;

    private Boolean availableWebSite;

    private Boolean availablePhoto;

    private String userUID;

    private List<String> roles;

    private List<String> alias;

    private byte[] photo;

    public PersonInformationDTO(final Person person) {
	this.name = person.getName();
	this.displayName = person.getNickname();
	this.phone = person.getPhone();
	this.workPhone = person.getWorkPhone();
	this.mobile = person.getMobile();
	this.webAddress = person.getWebAddress();
	this.email = person.getEmail();
	this.gender = person.getGender() != null ? person.getGender().name() : null;
	this.availableEmail = person.getAvailableEmail();
	this.availableWebSite = person.getAvailableWebSite();
	this.availablePhoto = person.getAvailablePhoto();
	this.userUID = person.getIstUsername();

	this.roles = new ArrayList<String>();
	for (Role role : person.getPersonRoles()) {
	    roles.add(role.getRoleType().name());
	}

	this.photo = person.getPersonalPhoto() != null ? person.getPersonalPhoto().getContent().getBytes() : null;

	this.alias = new ArrayList<String>();
	for (LoginAlias loginAlias : person.getLoginAliasOrderByImportance()) {
	    this.alias.add(loginAlias.getAlias());
	}

    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getWorkPhone() {
	return workPhone;
    }

    public void setWorkPhone(String workPhone) {
	this.workPhone = workPhone;
    }

    public String getMobile() {
	return mobile;
    }

    public void setMobile(String mobile) {
	this.mobile = mobile;
    }

    public String getWebAddress() {
	return webAddress;
    }

    public void setWebAddress(String webAddress) {
	this.webAddress = webAddress;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getGender() {
	return gender;
    }

    public void setGender(String gender) {
	this.gender = gender;
    }

    public Boolean getAvailableEmail() {
	return availableEmail;
    }

    public void setAvailableEmail(Boolean availableEmail) {
	this.availableEmail = availableEmail;
    }

    public Boolean getAvailableWebSite() {
	return availableWebSite;
    }

    public void setAvailableWebSite(Boolean availableWebSite) {
	this.availableWebSite = availableWebSite;
    }

    public Boolean getAvailablePhoto() {
	return availablePhoto;
    }

    public void setAvailablePhoto(Boolean availablePhoto) {
	this.availablePhoto = availablePhoto;
    }

    public String getUserUID() {
	return userUID;
    }

    public void setUserUID(String userUID) {
	this.userUID = userUID;
    }

    public List<String> getRoles() {
	return roles;
    }

    public void setRoles(List<String> roles) {
	this.roles = roles;
    }

    public byte[] getPhoto() {
	return photo;
    }

    public void setPhoto(byte[] photo) {
	this.photo = photo;
    }

    public List<String> getAlias() {
	return alias;
    }

    public void setAlias(List<String> alias) {
	this.alias = alias;
    }

}
