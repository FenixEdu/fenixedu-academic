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
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniSearchBean;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class Alumni extends Alumni_Base {

    public Alumni(Student student) {
        this(student, null, Boolean.FALSE);
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

        setRegisteredWhen(new DateTime());
        setRegistered(registered);
        setRootDomainObject(Bennu.getInstance());
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
        for (Alumni alumni : Bennu.getInstance().getAlumnisSet()) {
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

    public PhysicalAddress getLastPersonalAddress() {
        SortedSet<PhysicalAddress> addressSet = new TreeSet<PhysicalAddress>(DomainObjectUtil.COMPARATOR_BY_ID);
        addressSet.addAll(getStudent().getPerson().getPhysicalAddresses());
        return !addressSet.isEmpty() && addressSet.last() != null ? addressSet.last() : null;
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
        ArrayList<Job> jobs = new ArrayList<Job>(getStudent().getPerson().getJobs());
        Collections.sort(jobs, Job.REVERSE_COMPARATOR_BY_BEGIN_DATE);
        return jobs;
    }

    public Job getLastJob() {
        List<Job> jobs = getJobs();
        return jobs.get(jobs.size() - 1);
    }

    public Boolean canChangePersonalAddress() {

        // TODO: testar se o alumni nao tem rela��o actual com o IST
        // (n�o � docente, nao � aluno, nao � funcionario, ...)

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
        ArrayList<Formation> formations = new ArrayList<Formation>(getStudent().getPerson().getFormations());
        Collections.sort(formations, Formation.COMPARATOR_BY_BEGIN_YEAR);
        return formations;
    }

    public PhysicalAddress getUpdatableAddress() {
        List<? extends PartyContact> partyContacts = getStudent().getPerson().getPartyContacts(PhysicalAddress.class);
        for (int i = 0; i < partyContacts.size(); i++) {
            PhysicalAddress address = (PhysicalAddress) partyContacts.get(i);
            if (!address.isDefault()) {
                return address;
            }
        }

        return null;
    }

    public PartyContact getUpdatablePartyContact(final Class<? extends PartyContact> clazz) {
        List<? extends PartyContact> partyContacts = getStudent().getPerson().getPartyContacts(clazz);
        for (int i = 0; i < partyContacts.size(); i++) {
            if (!partyContacts.get(i).isDefault()) {
                return partyContacts.get(i);
            }
        }

        return null;
    }

    public EmailAddress getUpdatableMobilePhone() {
        List<? extends PartyContact> partyContacts = getStudent().getPerson().getPartyContacts(EmailAddress.class);
        for (int i = 0; i < partyContacts.size(); i++) {
            EmailAddress email = (EmailAddress) partyContacts.get(i);
            if (!email.isDefault()) {
                return email;
            }
        }

        return null;
    }

    public static List<Registration> readAlumniRegistrations(AlumniSearchBean bean) {

        if (StringUtils.isEmpty(bean.getName())) {
            return new ArrayList<Registration>();
        }

        ExecutionYear firstYear =
                bean.getFirstExecutionYear() == null ? ExecutionYear.readFirstExecutionYear() : bean.getFirstExecutionYear();
        ExecutionYear finalYear =
                bean.getFinalExecutionYear() == null ? ExecutionYear.readLastExecutionYear() : bean.getFinalExecutionYear();

        List<Registration> resultRegistrations = new ArrayList<Registration>();
        for (Person person : Person.readPersonsByNameAndRoleType(bean.getName(), RoleType.ALUMNI)) {
            if (person.hasStudent()) {

                if (bean.getStudentNumber() == null || person.getStudent().getNumber().equals(bean.getStudentNumber())) {
                    for (Registration registration : (bean.getDegreeType() == null ? person.getStudent().getRegistrations() : person
                            .getStudent().getRegistrationsByDegreeType(bean.getDegreeType()))) {

                        if (registration.isConcluded()) {
                            if (bean.getDegree() != null) {
                                if (registration.hasStartedBetween(firstYear, finalYear)
                                        && registration.getDegree().equals(bean.getDegree())) {
                                    resultRegistrations.add(registration);
                                }
                            } else {

                                if (registration.hasStartedBetween(firstYear, finalYear)) {
                                    resultRegistrations.add(registration);
                                }
                            }
                        }
                    }
                }
            }
        }

        return resultRegistrations;
    }

    public static List<Registration> readRegistrations(AlumniSearchBean bean) {
        String personName = bean.getName();
        Integer studentNumber = bean.getStudentNumber();
        String documentIdNumber = bean.getDocumentIdNumber();

        List<Registration> resultRegistrations = new ArrayList<Registration>();
        if (!StringUtils.isEmpty(personName)) {
            for (Person person : Person.readPersonsByNameAndRoleType(personName, RoleType.ALUMNI)) {
                if (matchStudentNumber(person, studentNumber) && matchDocumentIdNumber(person, documentIdNumber)) {
                    for (Registration registration : person.getStudent().getRegistrations()) {
                        if (registration.isConcluded()) {
                            resultRegistrations.add(registration);
                        }
                    }
                }
            }
        } else {
            if (studentNumber != null) {
                Student student = Student.readStudentByNumber(studentNumber);
                if (student != null && matchDocumentIdNumber(student.getPerson(), documentIdNumber)) {
                    for (Registration registration : student.getRegistrations()) {
                        if (registration.isConcluded()) {
                            resultRegistrations.add(registration);
                        }
                    }
                }
            } else if (documentIdNumber != null) {
                Collection<Person> persons = Person.readByDocumentIdNumber(documentIdNumber);
                if (!persons.isEmpty()) {
                    Person person = persons.iterator().next();
                    if (matchStudentNumber(person, studentNumber) && person.hasStudent()) {
                        for (Registration registration : person.getStudent().getRegistrations()) {
                            if (registration.isConcluded()) {
                                resultRegistrations.add(registration);
                            }
                        }
                    }
                }
            }
        }

        String telephoneNumber = bean.getTelephoneNumber();
        String mobileNumber = bean.getMobileNumber();
        String email = bean.getEmail();

        List<Registration> filteredResultRegistrations = new ArrayList<Registration>();
        if (!StringUtils.isEmpty(personName) || studentNumber != null || !StringUtils.isEmpty(documentIdNumber)) {
            // Filter the result list
            for (Registration registration : resultRegistrations) {
                boolean match = true;

                if (!StringUtils.isEmpty(telephoneNumber) || !StringUtils.isEmpty(email) || !StringUtils.isEmpty(mobileNumber)) {
                    if (registration.getPerson().getPartyContacts().isEmpty()) {
                        continue;
                    }
                    if (!StringUtils.isEmpty(mobileNumber) && !registration.getPerson().hasAnyPartyContact(MobilePhone.class)) {
                        continue;
                    }
                    if (!StringUtils.isEmpty(telephoneNumber) && !registration.getPerson().hasAnyPartyContact(Phone.class)) {
                        continue;
                    }
                    if (!StringUtils.isEmpty(email) && !registration.getPerson().hasAnyPartyContact(EmailAddress.class)) {
                        continue;
                    }
                    for (PartyContact contact : registration.getPerson().getPartyContacts()) {
                        if (!StringUtils.isEmpty(mobileNumber) && contact.isMobile()
                                && !mobileNumber.equals(contact.getPresentationValue())) {
                            match = false;
                            break;
                        }
                        if (!StringUtils.isEmpty(telephoneNumber) && contact.isPhone()
                                && !telephoneNumber.equals(contact.getPresentationValue())) {
                            match = false;
                            break;
                        }
                        if (!StringUtils.isEmpty(email) && contact.isEmailAddress()
                                && !email.equals(contact.getPresentationValue())) {
                            match = false;
                            break;
                        }
                    }
                }
                if (match) {
                    filteredResultRegistrations.add(registration);
                }
            }
        } else if (!StringUtils.isEmpty(telephoneNumber) || !StringUtils.isEmpty(email) || !StringUtils.isEmpty(mobileNumber)) {
            List<Class<? extends PartyContact>> clazzList = new ArrayList<Class<? extends PartyContact>>();
            if (!StringUtils.isEmpty(telephoneNumber)) {
                clazzList.add(Phone.class);
            }
            if (!StringUtils.isEmpty(email)) {
                clazzList.add(EmailAddress.class);
            }
            if (!StringUtils.isEmpty(mobileNumber)) {
                clazzList.add(MobilePhone.class);
            }
            Set<Party> partyRead = new HashSet<Party>();
            Set<PartyContact> contacts = PartyContact.readPartyContactsOfType(clazzList.toArray(new Class[0]));
            for (PartyContact contact : contacts) {
                if (!StringUtils.isEmpty(mobileNumber)
                        && !(contact.isMobile() && mobileNumber.equals(contact.getPresentationValue()))) {
                    continue;
                }
                if (!StringUtils.isEmpty(telephoneNumber)
                        && !(contact.isPhone() && telephoneNumber.equals(contact.getPresentationValue()))) {
                    continue;
                }
                if (!StringUtils.isEmpty(email) && !(contact.isEmailAddress() && email.equals(contact.getPresentationValue()))) {
                    continue;
                }
                if (!contact.getParty().isPerson()) {
                    continue;
                }
                if (partyRead.contains(contact.getParty())) {
                    continue;
                }
                Person person = (Person) contact.getParty();
                partyRead.add(person);

                if (!person.hasRole(RoleType.ALUMNI) || !person.hasStudent()) {
                    continue;
                }
                for (Registration registration : person.getStudent().getRegistrations()) {
                    if (!registration.isConcluded()) {
                        continue;
                    }
                    filteredResultRegistrations.add(registration);
                }
            }
        }
        return filteredResultRegistrations;
    }

    private static boolean matchDocumentIdNumber(Person person, String documentIdNumber) {
        return StringUtils.isEmpty(documentIdNumber)
                || (!StringUtils.isEmpty(person.getDocumentIdNumber()) && person.getDocumentIdNumber().equals(documentIdNumber));
    }

    private static boolean matchStudentNumber(Person person, Integer studentNumber) {
        return studentNumber == null || (person.hasStudent() && person.getStudent().getNumber().equals(studentNumber));
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
        EmailAddress.createEmailAddress(getStudent().getPerson(), email, PartyContactType.PERSONAL, false);
    }

    public boolean isRegistered() {
        return getRegistered();
    }

    public boolean hasPastLogin() {
        return false;
        /*
         * This slow used to exist in the old user class, however it was always null...
         */
        // return getStudent().getPerson().getUser().getLastLoginDateTimeDateTime() != null;
    }

    public boolean hasAnyPendingIdentityRequests() {
        for (AlumniIdentityCheckRequest request : getIdentityRequests()) {
            if (request.getApproved() == null) {
                return true;
            }
        }
        return false;
    }

    public AlumniIdentityCheckRequest getLastIdentityRequest() {
        Set<AlumniIdentityCheckRequest> orderedSet =
                new TreeSet<AlumniIdentityCheckRequest>(new ReverseComparator(new BeanComparator("creationDateTime")));
        for (AlumniIdentityCheckRequest request : getIdentityRequests()) {
            orderedSet.add(request);
        }
        return orderedSet.size() != 0 ? orderedSet.iterator().next() : null;
    }

    public String getLoginUsername() {
        return getStudent().getPerson().getUsername();
    }

    public boolean hasStartedPublicRegistry() {
        return getUrlRequestToken() != null;
    }

    public boolean hasFinishedPublicRegistry() {
        return hasStartedPublicRegistry() && isRegistered();
    }

    public String getName() {
        return getStudent().getPerson().getName();
    }

    public void delete() {
        setStudent(null);
        getIdentityRequests().clear();
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isRecoveringPassword() {
        boolean hasPasswordRequestAccepted = false;
        boolean hasPasswordRequest = false;
        for (AlumniIdentityCheckRequest request : getIdentityRequests()) {
            if (AlumniRequestType.PASSWORD_REQUEST.equals(request.getRequestType())) {
                if (!hasPasswordRequest) {
                    hasPasswordRequest = true;
                }
                if (request.getApproved()) {
                    hasPasswordRequestAccepted = true;
                    break;
                }
            }
        }
        return hasPasswordRequest && !hasPasswordRequestAccepted;
    }

    @Atomic
    public void validateEmailFromRegistrationProcess() {
        final Person person = getStudent().getPerson();
        for (final EmailAddress address : person.getPendingEmailAddresses()) {
            if (PartyContactType.PERSONAL.equals(address.getType())) {
                address.setValid();
            }
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest> getIdentityRequests() {
        return getIdentityRequestsSet();
    }

    @Deprecated
    public boolean hasAnyIdentityRequests() {
        return !getIdentityRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRegistered() {
        return getRegistered() != null;
    }

    @Deprecated
    public boolean hasIsEmployed() {
        return getIsEmployed() != null;
    }

    @Deprecated
    public boolean hasRegisteredWhen() {
        return getRegisteredWhen() != null;
    }

    @Deprecated
    public boolean hasUrlRequestToken() {
        return getUrlRequestToken() != null;
    }

}
