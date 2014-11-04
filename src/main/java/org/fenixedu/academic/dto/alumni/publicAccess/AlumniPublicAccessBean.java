/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniAddressBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;

public class AlumniPublicAccessBean implements Serializable {

    private Alumni alumni;

    private String phone;
    private Phone currentPhone;

    private String email;
    private EmailAddress currentEmail;

    private AlumniAddressBean addressBean;
    private PhysicalAddress currentAddress;

    private AlumniJobBean jobBean;
    private Job currentJob;

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
        return this.alumni;
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
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
        return this.currentPhone;
    }

    private void setCurrentPhone(Phone phone) {
        this.currentPhone = phone;
    }

    public EmailAddress getCurrentEmail() {
        return this.currentEmail;
    }

    private void setCurrentEmail(EmailAddress email) {
        this.currentEmail = email;
    }

    public PhysicalAddress getCurrentPhysicalAddress() {
        return this.currentAddress;
    }

    private void setCurrentPhysicalAddress(PhysicalAddress address) {
        this.currentAddress = address;
    }

    public Job getCurrentJob() {
        return this.currentJob;
    }

    private void setCurrentJob(Job job) {
        this.currentJob = job;
    }

}
