package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class ContactsForm extends Form {

    private String phoneNumber;

    private String mobileNumber;

    private String email;

    private String webAddress;

    private boolean isEmailAvailable;

    private boolean isHomepageAvailable;

    private boolean isPhotoAvailable;

    public ContactsForm() {
        super();
    }

    public static ContactsForm createFromPerson(Person person) {
        final boolean availableEmail = person.getAvailableEmail() == null ? false : person
                .getAvailableEmail();
        final boolean availableWebSite = person.getAvailableWebSite() == null ? false : person
                .getAvailableWebSite();
        final boolean availablePhoto = person.getAvailablePhoto() == null ? false : person
                .getAvailablePhoto();
        return new ContactsForm(person.getEmail(), person.getWebAddress(), availableEmail,
                availableWebSite, availablePhoto, person.getMobile(), person.getPhone());
    }

    private ContactsForm(String email, String homepage, boolean isEmailAvailable,
            boolean isHomepageAvailable, boolean isPhotoAvailable, String mobileNumber,
            String phoneNumber) {
        this();
        this.email = email;
        this.webAddress = homepage;
        this.isEmailAvailable = isEmailAvailable;
        this.isHomepageAvailable = isHomepageAvailable;
        this.isPhotoAvailable = isPhotoAvailable;
        this.mobileNumber = mobileNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String homepage) {
        this.webAddress = homepage;
    }

    public boolean isEmailAvailable() {
        return isEmailAvailable;
    }

    public void setEmailAvailable(boolean isEmailAvailable) {
        this.isEmailAvailable = isEmailAvailable;
    }

    public boolean isHomepageAvailable() {
        return isHomepageAvailable;
    }

    public void setHomepageAvailable(boolean isHomepageAvailable) {
        this.isHomepageAvailable = isHomepageAvailable;
    }

    public boolean isPhotoAvailable() {
        return isPhotoAvailable;
    }

    public void setPhotoAvailable(boolean isPhotoAvailable) {
        this.isPhotoAvailable = isPhotoAvailable;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public List<LabelFormatter> validate() {
        return new ArrayList<LabelFormatter>();
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.contactsForm";
    }

}