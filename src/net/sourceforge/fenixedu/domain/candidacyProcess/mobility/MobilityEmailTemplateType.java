package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum MobilityEmailTemplateType {

    PREREGISTRATION {

	private static final String APPLICATION_SUBMISSION_LINK = "MobilityIndividualApplicationProcess.const.public.application.submission.link";

	@Override
	public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());

	    String subject = mobilityEmailTemplate.getSubject();
	    String body = mobilityEmailTemplate.getBody();
	    String link = bundle.getString(APPLICATION_SUBMISSION_LINK);

	    link = String.format(link, hashCode.getValue(), Language.getLocale());

	    if (body.contains("[submission_link]")) {
		body = body.replace("[submission_link]", link);
	    }

	    this.sendEmail(subject, body, hashCode.getEmail());
	}

    },

    APPLICATION_SUBMISSION {

	private static final String APPLICATION_ACCESS_LINK = "MobilityIndividualApplicationProcess.const.public.application.access.link";

	@Override
	public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());

	    IndividualCandidacyProcess individualCandidacyProcess = hashCode.getIndividualCandidacyProcess();

	    String subject = mobilityEmailTemplate.getSubject();
	    String body = mobilityEmailTemplate.getBody();
	    String link = String.format(bundle.getString(APPLICATION_ACCESS_LINK), hashCode.getValue(), Language.getLocale()
		    .getLanguage());

	    String processCode = individualCandidacyProcess.getProcessCode();
	    String endDate = individualCandidacyProcess.getCandidacyProcess().getCandidacyEnd()
		    .toString(DateFormatUtil.DEFAULT_DATE_FORMAT);

	    if (body.contains("[process_number]")) {
		body = body.replace("[process_number]", processCode);
	    }

	    if (body.contains("[process_access_link]")) {
		body = body.replace("[process_access_link]", link);
	    }

	    if (body.contains("[application_submission_end_date]")) {
		body = body.replace("[application_submission_end_date]", endDate);
	    }

	    sendEmail(subject, body, hashCode.getEmail());
	}

    },

    CANDIDATE_ACCEPTED {

	private static final String APPLICATION_ACCESS_LINK = "MobilityIndividualApplicationProcess.const.public.application.access.link";

	@Override
	public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());

	    String subject = mobilityEmailTemplate.getSubject();
	    String body = mobilityEmailTemplate.getBody();
	    String link = bundle.getString(APPLICATION_ACCESS_LINK);

	    link = String.format(link, hashCode.getValue(), Language.getLocale());

	    if (body.contains("[access_link]")) {
		body = body.replace("[access_link]", link);
	    }

	    this.sendEmail(subject, body, hashCode.getEmail());
	}

    },

    IST_RECEPTION {

	@Override
	public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {

	}

    };

    public String getName() {
	return name();
    }

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return MobilityEmailTemplateType.class.getSimpleName() + "." + name();
    }

    public String getFullQualifiedName() {
	return MobilityEmailTemplateType.class.getName() + "." + name();
    }

    protected void sendEmail(final String fromSubject, final String body, final String email) {
	SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();
	new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, fromSubject, body, email);
    }

    abstract public void sendEmailFor(final MobilityEmailTemplate mobilityEmailTemplate,
	    final DegreeOfficePublicCandidacyHashCode hashCode);

}
