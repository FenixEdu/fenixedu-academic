package net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniAddressBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;

public class AlumniPublicAccessBean implements Serializable {

    private DomainReference<Alumni> alumni;

    private String phone;
    private DomainReference<Phone> currentPhone;

    private String email;
    private DomainReference<EmailAddress> currentEmail;

    private AlumniAddressBean addressBean;
    private DomainReference<PhysicalAddress> currentAddress;

    private AlumniJobBean jobBean;
    private DomainReference<Job> currentJob;

    // private String password;
    // private String passwordConfirmation;

    public AlumniPublicAccessBean(Alumni alumni) {
	setAlumni(alumni);
	initEmail(alumni);
	initPhone(alumni);
	initAddress(alumni);
	initJob(alumni);
	// setPassword("");
	// setPasswordConfirmation("");
    }

    private void initPhone(Alumni alumni) {
	PartyContact phone = alumni.getUpdatablePartyContact(Phone.class);
	if (phone != null) {
	    setCurrentPhone((Phone) phone);
	    setPhone(((Phone) phone).getNumber());
	}
    }

    private void initEmail(Alumni alumni) {
	PartyContact email = alumni.getUpdatablePartyContact(EmailAddress.class);
	if (email != null) {
	    setCurrentEmail((EmailAddress) email);
	    setEmail(((EmailAddress) email).getValue());
	}
    }

    private void initAddress(Alumni alumni) {
	PartyContact address = alumni.getUpdatablePartyContact(PhysicalAddress.class);
	if (address != null) {
	    setCurrentPhysicalAddress((PhysicalAddress) address);
	    setAddressBean(new AlumniAddressBean(alumni, (PhysicalAddress) address));
	} else {
	    setAddressBean(new AlumniAddressBean(alumni));
	}
    }

    private void initJob(Alumni alumni) {
	if (alumni.hasAnyJobs()) {
	    Job job = alumni.getLastJob();
	    setCurrentJob(job);
	    setJobBean(new AlumniJobBean(alumni, job));
	} else {
	    setJobBean(new AlumniJobBean(alumni));
	}
    }

    public Alumni getAlumni() {
	return (this.alumni != null) ? this.alumni.getObject() : null;
    }

    public void setAlumni(Alumni alumni) {
	this.alumni = new DomainReference<Alumni>(alumni);
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public AlumniAddressBean getAddressBean() {
	return this.addressBean;
    }

    public void setAddressBean(AlumniAddressBean alumniAddressBean) {
	this.addressBean = alumniAddressBean;
    }

    public AlumniJobBean getJobBean() {
	return this.jobBean;
    }

    public void setJobBean(AlumniJobBean alumniJobBean) {
	this.jobBean = alumniJobBean;
    }

    // public String getPassword() {
    // return password;
    // }
    //
    // public void setPassword(String password) {
    // this.password = password;
    // }
    //
    // public String getPasswordConfirmation() {
    // return passwordConfirmation;
    // }
    //
    // public void setPasswordConfirmation(String passwordConfirmation) {
    // this.passwordConfirmation = passwordConfirmation;
    // }

    public Phone getCurrentPhone() {
	return (this.currentPhone != null) ? this.currentPhone.getObject() : null;
    }

    private void setCurrentPhone(Phone phone) {
	this.currentPhone = (phone != null) ? new DomainReference<Phone>(phone) : null;
    }

    public EmailAddress getCurrentEmail() {
	return (this.currentEmail != null) ? this.currentEmail.getObject() : null;
    }

    private void setCurrentEmail(EmailAddress email) {
	this.currentEmail = (email != null) ? new DomainReference<EmailAddress>(email) : null;
    }

    public PhysicalAddress getCurrentPhysicalAddress() {
	return (this.currentAddress != null) ? this.currentAddress.getObject() : null;
    }

    private void setCurrentPhysicalAddress(PhysicalAddress address) {
	this.currentAddress = (address != null) ? new DomainReference<PhysicalAddress>(address) : null;
    }

    public Job getCurrentJob() {
	return (this.currentJob != null) ? this.currentJob.getObject() : null;
    }

    private void setCurrentJob(Job job) {
	this.currentJob = (job != null) ? new DomainReference<Job>(job) : null;
    }

}
