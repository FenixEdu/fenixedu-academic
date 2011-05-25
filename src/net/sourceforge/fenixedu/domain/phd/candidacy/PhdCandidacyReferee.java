package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.ResourceBundle;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCandidacyRefereeAlert;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdCandidacyReferee extends PhdCandidacyReferee_Base {

    private PhdCandidacyReferee() {
	super();
    }

    public PhdCandidacyReferee(final PhdProgramCandidacyProcess process, final PhdCandidacyRefereeBean bean) {
	this();

	check(process, "error.PhdCandidacyReferee.invalid.process");
	check(bean.getName(), "error.PhdCandidacyReferee.invalid.name");
	check(bean.getEmail(), "error.PhdCandidacyReferee.invalid.email");

	setPhdProgramCandidacyProcess(process);
	setName(bean.getName());
	setEmail(bean.getEmail());
	setInstitution(bean.getInstitution());
	setValue(UUID.randomUUID().toString());

	new PhdCandidacyRefereeAlert(this);
	sendEmail();
    }

    @Override
    public boolean hasCandidacyProcess() {
	return hasPhdProgramCandidacyProcess();
    }

    public boolean isLetterAvailable() {
	return hasLetter();
    }

    public PhdIndividualProgramProcess getIndividualProgramProcess() {
	return getPhdProgramCandidacyProcess().getIndividualProgramProcess();
    }

    @Service
    public void sendEmail() {
	sendEmail(createSubject(), createBody());
    }

    private String createSubject() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	return String.format(bundle.getString("message.phd.email.subject.referee"), getCandidatePerson().getName(),
		getCandidatePerson().getName());
    }

    public Person getCandidatePerson() {
	return getPhdProgramCandidacyProcess().getPerson();
    }

    private String createBody() {
	return getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod().getEmailMessageBodyForRefereeForm(this);
    }
}
