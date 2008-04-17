/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.student.Registration;

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

    private String citizenIdNumber;

    private String teacherDepartment;

    private String employeeDepartment;

    private List<String> studentDegrees;

    public PersonInformationDTO(final Person person) {
	this.name = person.getName();
	this.displayName = person.getNickname();
	this.phone = person.getPhone();
	this.workPhone = person.getWorkPhone();
	this.mobile = person.getMobile();
	this.webAddress = person.getWebAddress();
	this.email = person.getDefaultEmailAddress().getValue();
	this.gender = person.getGender() != null ? person.getGender().name() : null;
	this.availableEmail = person.getAvailableEmail();
	this.availableWebSite = person.getAvailableWebSite();
	this.availablePhoto = person.getAvailablePhoto();
	this.userUID = person.getIstUsername();

	if (person.getIdDocumentType() == IDDocumentType.IDENTITY_CARD) {
	    this.citizenIdNumber = person.getDocumentIdNumber();
	}

	this.roles = new ArrayList<String>();
	for (Role role : person.getPersonRoles()) {
	    roles.add(role.getRoleType().name());
	}

	if (person.hasTeacher()) {
	    this.teacherDepartment = person.getTeacher().getCurrentWorkingDepartment().getName();
	}

	if (person.hasEmployee()) {
	    this.employeeDepartment = person.getEmployee().getCurrentDepartmentWorkingPlace().getName();
	}

	this.studentDegrees = new ArrayList<String>();
	if (person.hasStudent()) {
	    for (Registration registration : person.getStudent().getActiveRegistrations()) {
		studentDegrees.add(registration.getDegree().getPresentationName());
	    }
	}

	this.photo = person.getPersonalPhoto() != null ? getJpegPhoto(person.getPersonalPhoto()) : null;

	this.alias = new ArrayList<String>();
	for (LoginAlias loginAlias : person.getLoginAliasOrderByImportance()) {
	    this.alias.add(loginAlias.getAlias());
	}

    }

    private byte[] getJpegPhoto(final FileEntry personalPhoto) {
	if (personalPhoto.getContentType() != net.sourceforge.fenixedu.util.ContentType.JPG) {
	    try {
		final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(personalPhoto.getContents()));
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	    } catch (IOException e) {
		return null;
	    }
	}
	return personalPhoto.getContents();
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

    public String getCitizenIdNumber() {
	return citizenIdNumber;
    }

    public void setCitizenIdNumber(String citizenIdNumber) {
	this.citizenIdNumber = citizenIdNumber;
    }

    public String getTeacherDepartment() {
	return teacherDepartment;
    }

    public void setTeacherDepartment(String teacherDepartment) {
	this.teacherDepartment = teacherDepartment;
    }

    public String getEmployeeDepartment() {
	return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
	this.employeeDepartment = employeeDepartment;
    }

    public List<String> getStudentDegrees() {
	return studentDegrees;
    }

    public void setStudentDegrees(List<String> studentDegrees) {
	this.studentDegrees = studentDegrees;
    }

}
