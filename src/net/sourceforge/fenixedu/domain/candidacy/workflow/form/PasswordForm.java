package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.PasswordVerifierUtil;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class PasswordForm extends Form {

    private String username;

    private String storedOldPassword;

    private String oldPassword;

    private String newPassword;

    private String confirmationPassword;

    private String documentIdNumber;

    public PasswordForm() {
	super();
    }

    private PasswordForm(final String username, final String storedOldPassword,
	    final String documentIdNumber) {
	this.username = username;
	this.storedOldPassword = storedOldPassword;
	this.documentIdNumber = documentIdNumber;
    }

    public static PasswordForm createFromPerson(final Person person) {
	return new PasswordForm(person.getUsername(), person.getPassword(), person.getDocumentIdNumber());
    }

    public String getConfirmationPassword() {
	return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmPassword) {
	this.confirmationPassword = confirmPassword;
    }

    public String getNewPassword() {
	return newPassword;
    }

    public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
    }

    public String getOldPassword() {
	return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
	this.oldPassword = oldPassword;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    @Override
    public String getFormName() {
	return "label.candidacy.workflow.passwordForm";
    }

    @Override
    public List<LabelFormatter> validate() {
	if (!PasswordEncryptor.encryptPassword(this.oldPassword).equals(this.storedOldPassword)) {
	    return Collections.singletonList(new LabelFormatter().appendLabel(
		    "error.candidacy.workflow.passwordForm.incorrect.oldPassword", "application"));
	}

	if (PasswordEncryptor.encryptPassword(this.newPassword).equals(this.storedOldPassword)
		|| this.newPassword.equals(this.documentIdNumber)) {
	    return Collections
		    .singletonList(new LabelFormatter()
			    .appendLabel(
				    "error.candidacy.workflow.passwordForm.newPassword.cannot.be.equal.to.old.or.documentIdNumber",
				    "application"));
	}

	if (!this.newPassword.equals(this.confirmationPassword)) {
	    return Collections
		    .singletonList(new LabelFormatter()
			    .appendLabel(
				    "error.candidacy.workflow.passwordForm.newPassword.and.confirmationPassword.does.not.match",
				    "application"));
	}

	if (!PasswordVerifierUtil.isValid(this.newPassword)) {
	    return Collections
		    .singletonList(new LabelFormatter()
			    .appendLabel(
				    "error.candidacy.workflow.passwordForm.newPassword.must.have.three.characters.of.classes.and.minLength",
				    "application"));
	}

	return Collections.EMPTY_LIST;
    }

}