package net.sourceforge.fenixedu.domain.phd.email;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.services.Service;

public class PhdProgramEmail extends PhdProgramEmail_Base {
    
    protected PhdProgramEmail() {
        super();
    }
    
    protected PhdProgramEmail(String subject, String body, String additionalBccs, PhdProgram phdProgram,
	    List<PhdIndividualProgramProcess> individualProcessList) {
	init(subject, body, "", additionalBccs, individualProcessList);
    }

    protected void init(String subject, String body, String additionalTo, String additionalBcc,
	    List<PhdIndividualProgramProcess> indiivualProcessList) {
	super.init(subject, body, additionalTo, additionalBcc);
    }

    @Override
    protected Collection<? extends ReplyTo> getReplyTos() {
	return getPhdProgram().getPhdProgramUnit().getUnitBasedSender().get(0).getReplyTos();
    }

    @Override
    protected Sender getSender() {
	return getPhdProgram().getPhdProgramUnit().getUnitBasedSender().get(0);
    }

    @Override
    protected Collection<Recipient> getRecipients() {
	return Collections.EMPTY_LIST;
    }

    @Override
    protected String getBccs() {
	StringBuilder builder = new StringBuilder();

	for (PhdIndividualProgramProcess process : getPhdIndividualProgramProcesses()) {
	    builder.append(process.getPerson().getEmailAddressForSendingEmails()).append(",");
	}

	builder.append(getAdditionalBcc());

	return builder.toString();
    }

    @Service
    static public PhdProgramEmail createEmail(PhdProgramEmailBean bean) {
	return new PhdProgramEmail(bean.getSubject(), bean.getMessage(), bean.getBccs(), bean.getPhdProgram(),
		bean.getIndividualProcesses());
    }

}
