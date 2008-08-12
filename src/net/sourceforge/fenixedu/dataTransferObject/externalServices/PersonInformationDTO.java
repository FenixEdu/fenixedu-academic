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
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PersonInformationDTO {

    private String name;

    private String displayName;

    private String phone;

    private String mobile;

    private String webAddress;

    private String email;

    private List<String> personalPhones = new ArrayList<String>();

    private List<String> workPhones = new ArrayList<String>();

    private List<String> personalMobiles = new ArrayList<String>();

    private List<String> workMobiles = new ArrayList<String>();

    private List<String> personalWebAdresses = new ArrayList<String>();

    private List<String> workWebAdresses = new ArrayList<String>();

    private List<String> personalEmails = new ArrayList<String>();

    private List<String> workEmails = new ArrayList<String>();

    private String gender;

    private String userUID;

    private List<String> roles;

    private List<String> alias;

    private byte[] photo;

    private String identificationDocumentNumber;

    private String identificationDocumentType;

    private String teacherDepartment;

    private String employeeUnit;

    private List<String> studentDegrees;

    public PersonInformationDTO(final Person person) {
	this.name = person.getName();
	this.displayName = person.getNickname();

	final Phone defaultPhone = person.getDefaultPhone();
	this.phone = defaultPhone != null ? defaultPhone.getPresentationValue() : StringUtils.EMPTY;

	final MobilePhone defaultMobilePhone = person.getDefaultMobilePhone();
	this.mobile = defaultMobilePhone != null ? defaultMobilePhone.getPresentationValue() : StringUtils.EMPTY;

	final WebAddress defaultWebAddress = person.getDefaultWebAddress();
	this.webAddress = defaultWebAddress != null ? defaultWebAddress.getPresentationValue() : StringUtils.EMPTY;

	final EmailAddress defaultEmailAddress = person.getDefaultEmailAddress();
	this.email = defaultEmailAddress != null ? defaultEmailAddress.getPresentationValue() : StringUtils.EMPTY;

	this.gender = person.getGender() != null ? person.getGender().name() : null;
	this.userUID = person.getIstUsername();

	this.identificationDocumentNumber = person.getDocumentIdNumber();
	this.identificationDocumentType = person.getIdDocumentType() != null ? person.getIdDocumentType().name()
		: StringUtils.EMPTY;

	fillPersonalAndWorkContacts(person.getPhones(), this.personalPhones, this.workPhones);
	fillPersonalAndWorkContacts(person.getMobilePhones(), this.personalMobiles, this.workMobiles);
	fillPersonalAndWorkContacts(person.getWebAddresses(), this.personalWebAdresses, this.workWebAdresses);
	fillPersonalAndWorkContacts(person.getEmailAddresses(), this.personalEmails, this.workEmails);

	this.roles = new ArrayList<String>();
	for (Role role : person.getPersonRoles()) {
	    roles.add(role.getRoleType().name());
	}

	if (person.hasTeacher() && person.getTeacher().getCurrentWorkingDepartment() != null) {
	    this.teacherDepartment = person.getTeacher().getCurrentWorkingDepartment().getRealName();
	}

	if (person.hasEmployee() && person.getEmployee().getCurrentWorkingPlace() != null) {
	    this.employeeUnit = person.getEmployee().getCurrentWorkingPlace().getName();
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

    private void fillPersonalAndWorkContacts(final List<? extends PartyContact> contacts, List<String> personalContacts,
	    List<String> workContacts) {
	for (final PartyContact partyContact : contacts) {
	    if (partyContact.getType() == PartyContactType.PERSONAL) {
		personalContacts.add(partyContact.getPresentationValue());
	    } else if (partyContact.getType() == PartyContactType.WORK) {
		workContacts.add(partyContact.getPresentationValue());
	    }
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

    public String getIdentificationDocumentNumber() {
	return identificationDocumentNumber;
    }

    public void setIdentificationDocumentNumber(String identificationDocumentNumber) {
	this.identificationDocumentNumber = identificationDocumentNumber;
    }

    public String getIdentificationDocumentType() {
	return identificationDocumentType;
    }

    public void setIdentificationDocumentType(String identificationDocumentType) {
	this.identificationDocumentType = identificationDocumentType;
    }

    public String getTeacherDepartment() {
	return teacherDepartment;
    }

    public void setTeacherDepartment(String teacherDepartment) {
	this.teacherDepartment = teacherDepartment;
    }

    public String getEmployeeUnit() {
	return employeeUnit;
    }

    public void setEmployeeUnit(String employeeUnit) {
	this.employeeUnit = employeeUnit;
    }

    public List<String> getStudentDegrees() {
	return studentDegrees;
    }

    public void setStudentDegrees(List<String> studentDegrees) {
	this.studentDegrees = studentDegrees;
    }

    public List<String> getPersonalPhones() {
	return personalPhones;
    }

    public void setPersonalPhones(List<String> personalPhones) {
	this.personalPhones = personalPhones;
    }

    public List<String> getWorkPhones() {
	return workPhones;
    }

    public void setWorkPhones(List<String> workPhones) {
	this.workPhones = workPhones;
    }

    public List<String> getPersonalMobiles() {
	return personalMobiles;
    }

    public void setPersonalMobiles(List<String> personalMobiles) {
	this.personalMobiles = personalMobiles;
    }

    public List<String> getWorkMobiles() {
	return workMobiles;
    }

    public void setWorkMobiles(List<String> workMobiles) {
	this.workMobiles = workMobiles;
    }

    public List<String> getPersonalWebAdresses() {
	return personalWebAdresses;
    }

    public void setPersonalWebAdresses(List<String> personalWebAdresses) {
	this.personalWebAdresses = personalWebAdresses;
    }

    public List<String> getWorkWebAdresses() {
	return workWebAdresses;
    }

    public void setWorkWebAdresses(List<String> workWebAdresses) {
	this.workWebAdresses = workWebAdresses;
    }

    public List<String> getPersonalEmails() {
	return personalEmails;
    }

    public void setPersonalEmails(List<String> personalEmails) {
	this.personalEmails = personalEmails;
    }

    public List<String> getWorkEmails() {
	return workEmails;
    }

    public void setWorkEmails(List<String> workEmails) {
	this.workEmails = workEmails;
    }

}
