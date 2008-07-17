package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniSearchBean;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.lang.StringUtils;

public class Alumni extends Alumni_Base {

    public Alumni(Student student) {
	this(student, null, Boolean.FALSE);
    }

    public Alumni(Student student, UUID uuid) {
	this(student, uuid, Boolean.FALSE);
    }

    public Alumni(Student student, UUID uuid, Boolean registered) {
	super();

	if (student == null) {
	    throw new DomainException("alumni.creation.student.null");
	}

	setStudent(student);

	if (uuid != null) {
	    setUrlRequestToken(uuid);
	}

	setRegistered(registered);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Alumni readByStudentNumber(Integer studentNumber) {
	return Student.readStudentByNumber(studentNumber).getAlumni();
    }

    public Boolean hasDocumentIdNumber(String documentIdNumber) {
	return getDocumentIdNumber().equals(documentIdNumber);
    }

    public Boolean hasStudentNumber(Integer studentNumber) {
	return getStudentNumber().equals(studentNumber);
    }

    public static Alumni readAlumniByStudentNumber(Integer studentNumber) {
	for (Alumni alumni : RootDomainObject.getInstance().getAlumnis()) {
	    if (alumni.getStudent().getNumber().equals(studentNumber)) {
		return alumni;
	    }
	}
	return null;
    }

    public Integer getStudentNumber() {
	return getStudent().getNumber();
    }

    public String getDocumentIdNumber() {
	return getStudent().getPerson().getDocumentIdNumber();
    }

    public MobilePhone getPersonalMobilePhone() {
	for (MobilePhone phone : getStudent().getPerson().getMobilePhones()) {
	    if (phone.isPersonalType()) {
		return phone;
	    }
	}
	return null;
    }

    public Boolean hasPersonalMobilePhone() {
	return getPersonalMobilePhone() != null;
    }

    public EmailAddress getPersonalEmail() {
	for (EmailAddress email : getStudent().getPerson().getEmailAddresses()) {
	    if (email.isPersonalType()) {
		return email;
	    }
	}
	return null;
    }

    public Boolean hasPersonalEmail() {
	return getPersonalEmail() != null;
    }

    public Phone getPersonalPhone() {
	for (Phone phone : getStudent().getPerson().getPhones()) {
	    if (phone.isPersonalType()) {
		return phone;
	    }
	}
	return null;
    }

    public Boolean hasPersonalPhone() {
	return getPersonalPhone() != null;
    }

    public PhysicalAddress getPersonalAddress() {
	if (getStudent().getPerson().hasDefaultPhysicalAddress()) {
	    return getStudent().getPerson().getDefaultPhysicalAddress();
	}
	return null;
    }

    public Boolean hasAnyJobs() {
	return getStudent().getPerson().hasAnyJobs();
    }

    public List<Job> getJobs() {
	return getStudent().getPerson().getJobs();
    }

    public Job getLastJob() {
	List<Job> jobs = getJobs();
	return jobs.get(jobs.size() - 1);
    }

    public Boolean canChangePersonalAddress() {

	// TODO: testar se o alumni nao tem relação actual com o IST
	// (não é docente, nao é aluno, nao é funcionario, ...)

	Person p = getStudent().getPerson();
	if (p.hasRole(RoleType.EMPLOYEE)) {
	    return Boolean.FALSE;
	}

	if (p.hasRole(RoleType.TEACHER)) {
	    return Boolean.FALSE;
	}

	if (p.hasRole(RoleType.STUDENT)) {
	    return Boolean.FALSE;
	}

	return Boolean.TRUE;
    }

    public List<Formation> getFormations() {
	return getStudent().getPerson().getFormations();
    }

    public PhysicalAddress getUpdatableAddress() {
	List<? extends PartyContact> partyContacts = getStudent().getPerson().getPartyContacts(PhysicalAddress.class);
	for (int i = 0; i < partyContacts.size(); i++) {
	    PhysicalAddress address = (PhysicalAddress) partyContacts.get(i);
	    if (!address.isDefault())
		return address;
	}

	return null;
    }

    public PartyContact getUpdatablePartyContact(final Class<? extends PartyContact> clazz) {
	List<? extends PartyContact> partyContacts = getStudent().getPerson().getPartyContacts(clazz);
	for (int i = 0; i < partyContacts.size(); i++) {
	    if (!partyContacts.get(i).isDefault())
		return partyContacts.get(i);
	}

	return null;
    }

    public EmailAddress getUpdatableMobilePhone() {
	List<? extends PartyContact> partyContacts = getStudent().getPerson().getPartyContacts(EmailAddress.class);
	for (int i = 0; i < partyContacts.size(); i++) {
	    EmailAddress email = (EmailAddress) partyContacts.get(i);
	    if (!email.isDefault())
		return email;
	}

	return null;
    }

    public static List<Registration> readAlumniRegistrations(AlumniSearchBean searchBean) {

	if (StringUtils.isEmpty(searchBean.getName())) {
	    return new ArrayList<Registration>();
	}

	ExecutionYear firstExecutionYear = searchBean.getFirstExecutionYear() == null ? ExecutionYear.readFirstExecutionYear()
		: searchBean.getFirstExecutionYear();
	ExecutionYear finalExecutionYear = searchBean.getFinalExecutionYear() == null ? ExecutionYear.readLastExecutionYear()
		: searchBean.getFinalExecutionYear();

	List<Registration> resultRegistrations = new ArrayList<Registration>();
	for (Person person : Person.readPersonsByNameAndRoleType(searchBean.getName(), RoleType.ALUMNI)) {
	    if (person.hasStudent()) {
		for (Registration registration : (searchBean.getDegreeType() == null ? person.getStudent().getRegistrations()
			: person.getStudent().getRegistrationsByDegreeType(searchBean.getDegreeType()))) {
		    if (registration.hasStartedBetween(firstExecutionYear, finalExecutionYear)) {
			resultRegistrations.add(registration);
		    }
		}
	    }
	}

	return resultRegistrations;
    }

    public Boolean hasEmailAddress(String emailAddress) {
	for (EmailAddress personEmail : (List<EmailAddress>) getStudent().getPerson().getPartyContacts(EmailAddress.class)) {
	    if (personEmail.getValue().equals(emailAddress)) {
		return Boolean.TRUE;
	    }
	}
	return Boolean.FALSE;
    }

    public void addIfNotExistsEmail(String email) {
	if (!hasEmailAddress(email)) {
	    PartyContact.createEmailAddress(getStudent().getPerson(), PartyContactType.PERSONAL, Boolean.FALSE, email);
	}

    }

    public boolean isRegistered() {
	return getRegistered();
    }

    public boolean hasPastLogin() {
	return getStudent().getPerson().getUser().getLastLoginDateTimeDateTime() != null;
    }

    public boolean hasAnyPendingIdentityRequests() {
	for (AlumniIdentityCheckRequest request : getIdentityRequests()) {
	    if (request.getApproved() == null) {
		return true;
	    }
	}
	return false;
    }
}
