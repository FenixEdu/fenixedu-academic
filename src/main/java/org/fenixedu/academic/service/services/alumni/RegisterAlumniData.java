/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.service.services.alumni;

import java.util.UUID;

import org.fenixedu.academic.domain.Alumni;
import org.fenixedu.academic.domain.AlumniIdentityCheckRequest;
import org.fenixedu.academic.domain.AlumniManager;
import org.fenixedu.academic.domain.Job;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.PhysicalAddressData;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.alumni.AlumniAddressBean;
import org.fenixedu.academic.dto.alumni.AlumniIdentityCheckRequestBean;
import org.fenixedu.academic.dto.alumni.AlumniJobBean;
import org.fenixedu.academic.dto.alumni.publicAccess.AlumniPasswordBean;
import org.fenixedu.academic.dto.alumni.publicAccess.AlumniPublicAccessBean;
import org.fenixedu.academic.service.services.commons.alumni.AlumniNotificationService;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

public class RegisterAlumniData extends AlumniNotificationService {

    @Atomic
    public static Alumni run(Alumni alumni, final UUID urlRequestToken) throws FenixServiceException {

        if (alumni == null) {
            throw new FenixServiceException("alumni.uuid.update.alumni.null");
        }

        alumni.setUrlRequestToken(urlRequestToken);
        return alumni;
    }

    @Atomic
    public static Alumni run(Alumni alumni, final Boolean registered) throws FenixServiceException {

        alumni.setRegistered(registered);
        if (registered) {
            sendRegistrationSuccessMail(alumni);
        }
        return alumni;
    }

    @Atomic
    public static Alumni run(final Student student) {
        return new AlumniManager().registerAlumni(student);
    }

    @Atomic
    public static Alumni run(final Integer studentNumber, final String documentIdNumber, final String email) {

        final Alumni alumni = new AlumniManager().registerAlumni(studentNumber, documentIdNumber, email);
        sendPublicAccessMail(alumni, email);
        return alumni;
    }

    @Atomic
    public static void run(final AlumniPublicAccessBean alumniBean, Boolean isEmployed) throws FenixServiceException {
        Person person = alumniBean.getAlumni().getStudent().getPerson();
        if (person == null) {
            throw new FenixServiceException("alumni.partyContact.creation.person.null");
        }

        try {
            processAlumniPhone(alumniBean, person);
            processAlumniAddress(alumniBean, person);
            if (isEmployed) {
                processAlumniJob(alumniBean);
            }
            alumniBean.getAlumni().setIsEmployed(isEmployed);
        } catch (DomainException e) {
            throw new FenixServiceException(e.getMessage());
        }

    }

    @Atomic
    public static void run(final AlumniIdentityCheckRequestBean bean) {

        final Alumni alumni = new AlumniManager().checkAlumniIdentity(bean.getDocumentIdNumber(), bean.getContactEmail());
        if (!alumni.hasAnyPendingIdentityRequests()) {

            AlumniIdentityCheckRequest identityRequest = new AlumniIdentityCheckRequest(bean.getContactEmail(),
                    bean.getDocumentIdNumber(), bean.getFullName(), bean.getDateOfBirthYearMonthDay(), bean.getDistrictOfBirth(),
                    bean.getDistrictSubdivisionOfBirth(), bean.getParishOfBirth(), bean.getFiscalCountry(),
                    bean.getSocialSecurityNumber(), bean.getNameOfFather(), bean.getNameOfMother(), bean.getRequestType());

            identityRequest.setAlumni(alumni);
            if (identityRequest.isValid()) {
                identityRequest.validate(Boolean.TRUE);
                sendIdentityCheckEmail(identityRequest, Boolean.TRUE);
            }

        } else {
            throw new DomainException("alumni.has.pending.identity.requests");
        }
    }

    @Atomic
    public static void run(final AlumniPasswordBean bean) {

        bean.getAlumni().setRegistered(Boolean.TRUE);
        if (!bean.getAlumni().hasAnyPendingIdentityRequests()) {

            final EmailAddress personalEmail = bean.getAlumni().getPersonalEmail();

            String email;

            if (personalEmail == null) {
                email = bean.getAlumni().getStudent().getPerson().getEmailForSendingEmails();
            } else {
                email = personalEmail.getValue();
            }

            AlumniIdentityCheckRequest identityRequest = new AlumniIdentityCheckRequest(email,
                    bean.getAlumni().getStudent().getPerson().getDocumentIdNumber(), bean.getFullName(),
                    bean.getDateOfBirthYearMonthDay(), bean.getDistrictOfBirth(), bean.getDistrictSubdivisionOfBirth(),
                    bean.getParishOfBirth(), bean.getFiscalCountry(), bean.getSocialSecurityNumber(), bean.getNameOfFather(),
                    bean.getNameOfMother(), bean.getRequestType());

            identityRequest.setAlumni(bean.getAlumni());
            if (identityRequest.isValid()) {
                identityRequest.validate(Boolean.TRUE);
                sendIdentityCheckEmail(identityRequest, Boolean.TRUE);
            }
        }
    }

    private static void processAlumniJob(final AlumniPublicAccessBean alumniBean) {

        if (alumniBean.getCurrentJob() == null) {
            final AlumniJobBean jobBean = alumniBean.getJobBean();
            new Job(jobBean.getAlumni().getStudent().getPerson(), jobBean.getEmployerName(), jobBean.getCity(),
                    jobBean.getCountry(), jobBean.getChildBusinessArea(), jobBean.getParentBusinessArea(), jobBean.getPosition(),
                    jobBean.getBeginDateAsLocalDate(), jobBean.getEndDateAsLocalDate(), jobBean.getApplicationType(),
                    jobBean.getContractType(), jobBean.getSalary());
        } else {
            final AlumniJobBean jobBean = alumniBean.getJobBean();
            alumniBean.getCurrentJob().setEmployerName(jobBean.getEmployerName());
            alumniBean.getCurrentJob().setCity(jobBean.getCity());
            alumniBean.getCurrentJob().setCountry(jobBean.getCountry());
            alumniBean.getCurrentJob().setBusinessArea(jobBean.getChildBusinessArea());
            alumniBean.getCurrentJob().setParentBusinessArea(jobBean.getParentBusinessArea());
            alumniBean.getCurrentJob().setPosition(jobBean.getPosition());
            alumniBean.getCurrentJob().setBeginDate(jobBean.getBeginDateAsLocalDate());
            alumniBean.getCurrentJob().setEndDate(jobBean.getEndDateAsLocalDate());
            alumniBean.getCurrentJob().setJobApplicationType(jobBean.getApplicationType());
            alumniBean.getCurrentJob().setContractType(jobBean.getContractType());
            alumniBean.getCurrentJob().setSalary(jobBean.getSalary());

        }
    }

    private static void processAlumniAddress(final AlumniPublicAccessBean alumniBean, final Person person) {

        final AlumniAddressBean addressBean = alumniBean.getAddressBean();
        if (alumniBean.getCurrentPhysicalAddress() == null) {
            PhysicalAddress.createPhysicalAddress(person, new PhysicalAddressData(addressBean.getAddress(),
                    addressBean.getAreaCode(), addressBean.getAreaOfAreaCode(), null), PartyContactType.PERSONAL, false);
        } else {
            PhysicalAddress address = alumniBean.getCurrentPhysicalAddress();
            address.setAddress(addressBean.getAddress());
            address.setAreaCode(addressBean.getAreaCode());
            address.setAreaOfAreaCode(addressBean.getAreaOfAreaCode());
            address.setCountryOfResidence(addressBean.getCountry());
        }
    }

    private static void processAlumniPhone(final AlumniPublicAccessBean alumniBean, final Person person) {

        if (alumniBean.getCurrentPhone() == null) {
            Phone.createPhone(person, alumniBean.getPhone(), PartyContactType.PERSONAL, true);
        } else {
            alumniBean.getCurrentPhone().setNumber(alumniBean.getPhone());
        }
    }

    // Service Invokers migrated from Berserk

    private static final RegisterAlumniData serviceInstance = new RegisterAlumniData();

    @Atomic
    public static Alumni runRegisterAlumniData(Alumni alumni, UUID urlRequestToken) throws FenixServiceException {
        return serviceInstance.run(alumni, urlRequestToken);
    }

}