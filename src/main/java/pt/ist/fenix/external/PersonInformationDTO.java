package pt.ist.fenix.external;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.idcards.domain.SantanderCardInformation;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import com.google.common.io.BaseEncoding;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PersonInformationDTO {

    private String name;

    private String givenName;

    private String surName;

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

    private String photo;

    private String identificationDocumentNumber;

    private String identificationDocumentType;

    private String identificationDocumentExtraDigit;

    private String identificationDocumentSeriesNumber;

    private String teacherDepartment;

    private String employeeUnit;

    private List<String> studentDegrees;

    private String campus;

    private String eIdentifier;

    private String istCardMifareSerialNumber;

    private List<String> workingCostCenters = new ArrayList<String>();

    public PersonInformationDTO(final Person person) {
        this.name = person.getName();
        this.displayName = person.getNickname();

        final UserProfile profile = person.getProfile();
        if (profile != null) {
            this.givenName = profile.getGivenNames();
            this.surName = profile.getFamilyNames();
        }

        final Phone defaultPhone = person.getDefaultPhone();
        this.phone = defaultPhone != null ? defaultPhone.getPresentationValue() : StringUtils.EMPTY;

        final MobilePhone defaultMobilePhone = person.getDefaultMobilePhone();
        this.mobile = defaultMobilePhone != null ? defaultMobilePhone.getPresentationValue() : StringUtils.EMPTY;

        final WebAddress defaultWebAddress = person.getDefaultWebAddress();
        this.webAddress = defaultWebAddress != null ? defaultWebAddress.getPresentationValue() : StringUtils.EMPTY;

        final EmailAddress defaultEmailAddress = person.getDefaultEmailAddress();
        this.email = defaultEmailAddress != null ? defaultEmailAddress.getPresentationValue() : StringUtils.EMPTY;

        this.gender = person.getGender() != null ? person.getGender().name() : null;
        this.userUID = person.getUsername();

        this.identificationDocumentNumber = person.getDocumentIdNumber();
        this.identificationDocumentType =
                person.getIdDocumentType() != null ? person.getIdDocumentType().name() : StringUtils.EMPTY;
        this.identificationDocumentExtraDigit = person.getIdentificationDocumentExtraDigitValue();
        this.identificationDocumentSeriesNumber = person.getIdentificationDocumentSeriesNumberValue();

        fillPersonalAndWorkContacts(person.getPhones(), this.personalPhones, this.workPhones);
        fillPersonalAndWorkContacts(person.getMobilePhones(), this.personalMobiles, this.workMobiles);
        fillPersonalAndWorkContacts(person.getWebAddresses(), this.personalWebAdresses, this.workWebAdresses);
        fillPersonalAndWorkContacts(person.getEmailAddresses(), this.personalEmails, this.workEmails);

        this.roles = new ArrayList<String>();
        for (Role role : person.getPersonRolesSet()) {
            roles.add(role.getRoleType().name());
        }

        for (final Accountability accountability : person.getParentsSet()) {
            if (accountability instanceof Invitation) {
                Invitation invitation = (Invitation) accountability;
                if (invitation.isActive(new YearMonthDay())) {
                    roles.add("INVITED_PERSON");
                    Unit invitationUnit = invitation.getUnit();
                    if (invitationUnit != null && invitationUnit.getCostCenterCode() != null) {
                        workingCostCenters.add(invitationUnit.getCostCenterCode().toString());
                    }
                    break;
                }
            }
        }

        this.studentDegrees = new ArrayList<String>();
        if (person.getStudent() != null) {
            for (Registration registration : person.getStudent().getActiveRegistrations()) {
                studentDegrees.add(registration.getDegree().getPresentationName());
            }

            final Registration lastActiveRegistration = person.getStudent().getLastActiveRegistration();
            if (lastActiveRegistration != null) {
                this.campus = lastActiveRegistration.getCampus().getName();
            }

        }

        if (person.getTeacher() != null && person.getTeacher().getCurrentWorkingDepartment() != null) {
            this.teacherDepartment = person.getTeacher().getCurrentWorkingDepartment().getRealName();
        }

        if (person.getEmployee() != null) {
            final Unit currentWorkingPlace = person.getEmployee().getCurrentWorkingPlace();
            if (currentWorkingPlace != null) {
                this.employeeUnit = currentWorkingPlace.getName();
                if (currentWorkingPlace.getCostCenterCode() != null) {
                    workingCostCenters.add(currentWorkingPlace.getCostCenterCode().toString());
                }
            }

            Space currentCampus = person.getEmployee().getCurrentCampus();
            if (currentCampus != null) {
                setCampus(currentCampus.getName());
            }
        }

        if (person.hasRole(RoleType.RESEARCHER) && !person.hasRole(RoleType.TEACHER)) {
            final Collection<? extends Accountability> accountabilities =
                    person.getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);
            final YearMonthDay currentDate = new YearMonthDay();
            for (final Accountability accountability : accountabilities) {
                if (accountability.isActive(currentDate)) {
                    final Unit unit = (Unit) accountability.getParentParty();
                    final Integer costCenterCode = unit.getCostCenterCode();
                    if (costCenterCode != null) {
                        workingCostCenters.add(costCenterCode.toString());
                    }
                }
            }
        }

        this.photo =
                person.getPersonalPhoto() != null ? BaseEncoding.base64().encode(getJpegPhoto(person.getPersonalPhoto())) : null;

        this.eIdentifier = person.getEidentifier();

        this.istCardMifareSerialNumber = getLastMifareSerialNumber(person);
    }

    private static int compareDHCPLines(final String l1, String l2) {
        return l1.substring(1, 9).compareTo(l2.substring(1, 9));
    }

    private static String getLastMifareSerialNumber(final Person person) {
        final Stream<SantanderCardInformation> infos = person.getSantanderCardsInformationSet().stream();
        final String line = infos.map(i -> i.getDchpRegisteLine()).max(PersonInformationDTO::compareDHCPLines).orElse(null);
        return line == null ? null : getMifareSerialNumber(line);
    }

    private static String getMifareSerialNumber(String line) {
        final int offset = line.length() - 550 -1;
        return line.substring(offset - 10, offset);
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

    private byte[] getJpegPhoto(final Photograph personalPhoto) {
        return personalPhoto.exportAsJPEG(personalPhoto.getDefaultAvatar());
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getEIdentifier() {
        return eIdentifier;
    }

    public void setEIdentifier(String eIdentifier) {
        this.eIdentifier = eIdentifier;
    }

    public List<String> getWorkingCostCenters() {
        return workingCostCenters;
    }

    public void setWorkingCostCenter(List<String> workingCostCenters) {
        this.workingCostCenters = workingCostCenters;
    }

    public String getIdentificationDocumentExtraDigit() {
        return identificationDocumentExtraDigit;
    }

    public void setIdentificationDocumentExtraDigit(String identificationDocumentExtraDigit) {
        this.identificationDocumentExtraDigit = identificationDocumentExtraDigit;
    }

    public String getIdentificationDocumentSeriesNumber() {
        return identificationDocumentSeriesNumber;
    }

    public void setIdentificationDocumentSeriesNumber(String identificationDocumentSeriesNumber) {
        this.identificationDocumentSeriesNumber = identificationDocumentSeriesNumber;
    }

    public String getIstCardMifareSerialNumber() {
        return istCardMifareSerialNumber;
    }

    public void setIstCardMifareSerialNumber(String istCardMifareSerialNumber) {
        this.istCardMifareSerialNumber = istCardMifareSerialNumber;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

}
