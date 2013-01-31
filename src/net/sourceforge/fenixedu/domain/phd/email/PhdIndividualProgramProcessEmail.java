package net.sourceforge.fenixedu.domain.phd.email;

import java.util.Collection;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdIndividualProgramProcessEmail extends PhdIndividualProgramProcessEmail_Base {

	protected PhdIndividualProgramProcessEmail() {
		super();
	}

	protected PhdIndividualProgramProcessEmail(String subject, String message, String additionalTo, String additionalBcc,
			Person creator, PhdIndividualProgramProcess individualProcess) {
		init(subject, message, additionalTo, additionalBcc, creator, new DateTime());
		setPhdIndividualProgramProcess(individualProcess);
	}

	@Override
	protected Collection<? extends ReplyTo> getReplyTos() {
		return getSender().getReplyTosSet();
	}

	@Override
	protected Sender getSender() {
		return this.getPhdIndividualProgramProcess().getAdministrativeOffice().getUnit().getUnitBasedSender().get(0);
	}

	@Override
	protected Collection<Recipient> getRecipients() {
		return null;
	}

	@Override
	protected String getBccs() {
		return getAdditionalBcc();
	}

	@Service
	static public PhdIndividualProgramProcessEmail createEmail(PhdIndividualProgramProcessEmailBean bean) {
		final String errorWhileValidating = validateEmailBean(bean);
		if (errorWhileValidating != null) {
			throw new DomainException(errorWhileValidating);
		}

		final Person creator = AccessControl.getPerson();

		return new PhdIndividualProgramProcessEmail(bean.getSubject(), bean.getMessage(), null,
				bean.getBccsWithSelectedParticipants(), creator, bean.getProcess());
	}

	private static String validateEmailBean(PhdIndividualProgramProcessEmailBean bean) {
		final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());

		if (bean.getParticipantsGroup().isEmpty() && bean.getSelectedParticipants().isEmpty()
				&& StringUtils.isEmpty(bean.getBccs())) {
			return resourceBundle.getString("error.email.validation.no.recipients");
		}

		if (StringUtils.isEmpty(bean.getSubject())) {
			return resourceBundle.getString("error.email.validation.subject.empty");
		}

		if (StringUtils.isEmpty(bean.getMessage())) {
			return resourceBundle.getString("error.email.validation.message.empty");
		}

		return null;
	}

}
