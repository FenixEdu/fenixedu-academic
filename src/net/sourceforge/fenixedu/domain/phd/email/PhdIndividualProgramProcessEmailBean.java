package net.sourceforge.fenixedu.domain.phd.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.EMail;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdIndividualProgramProcessEmailBean extends PhdEmailBean implements Serializable {

    public static enum PhdEmailTemplate {
	FINAL_THESIS_DELIVERY("message.phd.template.final.thesis.delivery"),

	FINAL_THESIS_DELIVERY_WITH_CHANGES("message.phd.template.final.thesis.delivery.changes"),

	FINAL_THESIS_DELIVERY_AFTER_DISCUSSION("message.phd.template.final.thesis.delivery.after.discussion");

	private String label;

	private PhdEmailTemplate(String label) {
	    this.label = label;
	}

	public String getLabel() {
	    return label;
	}

	public String getLabelForSubject() {
	    return label + ".subject";
	}

	public String getLabelForBody() {
	    return label + ".body";
	}

	public String getTemplateSubject() {
	    return AlertMessage.get(getLabelForSubject());
	}

	public String getTemplateBody() {
	    return AlertMessage.get(getLabelForBody());
	}

	public String toString() {
	    return AlertMessage.get(getLabel() + ".label");
	}
    }

    public static abstract class PhdEmailParticipantsGroup implements Serializable, DataProvider {

	private static final long serialVersionUID = -8990666659412753954L;
	protected String label;

	public PhdEmailParticipantsGroup() {
	}

	public String getGroupLabel() {
	    return AlertMessage.get(this.label);
	}
	
	public String getName() {
	    return this.getClass().getSimpleName();
	}
	
	public abstract List<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process);

	public String getEmailsAsBccs(PhdIndividualProgramProcess process) {
	    StringBuilder bccs = new StringBuilder();
	    Boolean hasParticipantsEmails = false;
	    for (PhdParticipant participant : this.getGroupParticipants(process)) {
		String email = participant.getEmail();
		if (email == null) {
		    continue;
		}

		hasParticipantsEmails = true;
		bccs.append(email);
		bccs.append(",");
	    }

	    if (hasParticipantsEmails) {
		bccs.deleteCharAt(bccs.length() - 1);
	    }

	    return bccs.toString();
	}

	@Override
	public Converter getConverter() {
	    return null;
	}

	@Override
	public Object provide(final Object source, final Object currentValue) {
	    final PhdIndividualProgramProcessEmailBean emailBean = (PhdIndividualProgramProcessEmailBean) source;
	    return this.getGroupParticipants(emailBean.getProcess());
	}

    }
    
    public static class PhdEmailParticipantsCoordinatorsGroup extends PhdEmailParticipantsGroup {

	private static final long serialVersionUID = 4961478244113914645L;

	public PhdEmailParticipantsCoordinatorsGroup() {
	    super();
	    super.label = "label.phd.email.group.coordinators";
	}

	@Override
	public List<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process) {
	    List<PhdParticipant> participants = new ArrayList<PhdParticipant>();
	    for (Person person : process.getCoordinatorsFor(ExecutionYear.readCurrentExecutionYear())) {
		participants.add(process.getParticipant(person));
	    }

	    return participants;
	}

    }
    
    public static class PhdEmailParticipantsGuidersGroup extends PhdEmailParticipantsGroup {

	private static final long serialVersionUID = -3022014810736464210L;

	public PhdEmailParticipantsGuidersGroup() {
	    super();
	    super.label = "label.phd.email.group.guiders";
	}

	@Override
	public List<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process) {
	    List<PhdParticipant> participants = new ArrayList<PhdParticipant>();
	    participants.addAll(process.getGuidingsAndAssistantGuidings());

	    return participants;
	}
	
    }
    
    public static class PhdEmailParticipantsAllGroup extends PhdEmailParticipantsGroup {

	private static final long serialVersionUID = -6806003598437992476L;

	public PhdEmailParticipantsAllGroup() {
	    super();
	    super.label = "label.phd.email.group.all.participants";
	}

	@Override
	public List<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process) {
	    return process.getParticipants();
	}
	
    }
    
    public static class PhdEmailParticipantsJuryMembersGroup extends PhdEmailParticipantsGroup {

	private static final long serialVersionUID = 3417426578342610353L;

	public PhdEmailParticipantsJuryMembersGroup() {
	    super();
	    super.label = "label.phd.email.group.jury.members";
	}

	@Override
	public List<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process) {
	    List<PhdParticipant> participants = new ArrayList<PhdParticipant>();
	    if (!process.hasThesisProcess()) {
		throw new DomainException("phd.individualProcess.does.not.have.thesisProcess");
	    }

	    if (process.getThesisProcess().hasPresidentJuryElement()) {
		participants.add(process.getThesisProcess().getPresidentJuryElement().getParticipant());
	    }

	    for (ThesisJuryElement element : process.getThesisProcess().getThesisJuryElements()) {
		participants.add(element.getParticipant());
	    }

	    return participants;
	}

    }

    private static int MAX_EMAILS_PER_LINE = 5;

    private Sender sender;
    private Set<Recipient> recipients;
    private String tos, ccs;
    private Set<ReplyTo> replyTos;
    private DateTime createdDate;
    private Person creator;

    private PhdIndividualProgramProcess process;

    private PhdEmailTemplate template;
    private Set<PhdEmailParticipantsGroup> participantsGroup;
    private Set<PhdParticipant> selectedParticipants;

    public PhdIndividualProgramProcessEmailBean() {
    }

    public PhdIndividualProgramProcessEmailBean(Message message) {
	this.subject = message.getSubject();
	this.message = message.getBody();
	this.bccs = message.getBccs();
	this.createdDate = message.getCreated();
	this.creator = message.getPerson();
    }

    public Sender getSender() {
	return sender;
    }

    public void setSender(final Sender sender) {
	this.sender = sender;
    }

    public List<Recipient> getRecipients() {
	final List<Recipient> result = new ArrayList<Recipient>();
	if (recipients != null) {
	    for (final Recipient recipient : recipients) {
		result.add(recipient);
	    }
	}
	return result;
    }

    public void setRecipients(List<Recipient> recipients) {
	if (recipients == null) {
	    this.recipients = null;
	} else {
	    this.recipients = new HashSet<Recipient>();
	    for (final Recipient recipient : recipients) {
		this.recipients.add(recipient);
	    }
	}
    }

    public List<ReplyTo> getReplyTos() {
	final List<ReplyTo> result = new ArrayList<ReplyTo>();
	if (replyTos != null) {
	    for (final ReplyTo replyTo : replyTos) {
		result.add(replyTo);
	    }
	}
	return result;
    }

    public void setReplyTos(List<ReplyTo> replyTos) {
	if (replyTos == null) {
	    this.replyTos = null;
	} else {
	    this.replyTos = new HashSet<ReplyTo>();
	    for (final ReplyTo replyTo : replyTos) {
		this.replyTos.add(replyTo);
	    }
	}
    }

    public String getTos() {
	return tos;
    }

    public void setTos(String tos) {
	this.tos = tos;
    }

    public String getCcs() {
	return ccs;
    }

    public void setCcs(String ccs) {
	this.ccs = ccs;
    }

    public String getBccsView() {
	StringTokenizer tokenizer = new StringTokenizer(getBccs(), ",");
	StringBuilder result = new StringBuilder();
	int emailsCurrentLine = 0;
	while (tokenizer.hasMoreTokens()) {
	    if (emailsCurrentLine == MAX_EMAILS_PER_LINE) {
		result.append('\n');
		emailsCurrentLine = 0;
	    }
	    result.append(tokenizer.nextToken());
	    result.append(',');
	    emailsCurrentLine++;
	}
	if (!StringUtils.isEmpty(result.toString())) {
	    result.deleteCharAt(result.length() - 1);
	}
	return result.toString();
    }

    public String validate() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());

	String bccs = getBccs();
	if (getParticipantsGroup().isEmpty() && getSelectedParticipants().isEmpty() && getRecipients().isEmpty()
		&& StringUtils.isEmpty(bccs)) {
	    return resourceBundle.getString("error.email.validation.no.recipients");
	}

	if (!StringUtils.isEmpty(bccs)) {
	    String[] emails = bccs.split(",");
	    for (String emailString : emails) {
		final String email = emailString.trim();
		if (!email.matches(EMail.W3C_EMAIL_SINTAX_VALIDATOR)) {
		    StringBuilder builder = new StringBuilder(resourceBundle.getString("error.email.validation.bcc.invalid"));
		    builder.append(email);
		    return builder.toString();
		}
	    }
	}

	if (StringUtils.isEmpty(getSubject())) {
	    return resourceBundle.getString("error.email.validation.subject.empty");
	}

	if (StringUtils.isEmpty(getMessage())) {
	    return resourceBundle.getString("error.email.validation.message.empty");
	}

	return null;
    }

    public DateTime getCreatedDate() {
	return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
	this.createdDate = createdDate;
    }

    @Service
    public Message send(PhdIndividualProgramProcess process) {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());

	final StringBuilder message = new StringBuilder();
	message.append(getMessage());
	// message.append("\n\n---\n");
	// message.append(resourceBundle.getString("message.email.footer.prefix"));
	// message.append(getSender().getFromName());
	// message.append(resourceBundle.getString("message.email.footer.prefix.suffix"));
	// for (final Recipient recipient : getRecipients()) {
	// message.append("\n\t");
	// message.append(recipient.getToName());
	// }
	//
	// for (PhdEmailParticipantsGroup participantGroup :
	// getParticipantsGroup()) {
	// message.append("\n\t");
	// message.append(participantGroup.getGroupLabel());
	// }
	// message.append("\n");

	String bccs = getBccs() == null ? null : getBccs().replace(" ", "");
	
	if (!StringUtils.isEmpty(bccs)) {
	    bccs += ",";
	}

	// for (PhdEmailParticipantsGroup participantGroup :
	// getParticipantsGroup()) {
	// bccs += participantGroup.getEmailsAsBccs(process);
	// bccs += ",";
	// }

	for (PhdParticipant participant : getSelectedParticipants()) {
	    bccs += participant.getEmail();
	    bccs += ",";
	}
	if (getParticipantsGroup().size() != 0) {
	    bccs = bccs.substring(0, bccs.length() - 1);
	}

	return new Message(getSender(), getReplyTos(), getRecipients(), getSubject(), message.toString(), bccs);
    }

    @Service
    public void removeRecipients() {
	for (Recipient recipient : getRecipients()) {
	    getSender().removeRecipients(recipient);
	}
	setRecipients(null);
    }

    public PhdIndividualProgramProcess getProcess() {
	return process;
    }

    public void setProcess(PhdIndividualProgramProcess process) {
	this.process = process;
    }

    public PhdEmailTemplate getTemplate() {
	return template;
    }

    public void setTemplate(PhdEmailTemplate template) {
	this.template = template;
    }

    public Person getCreator() {
	return creator;
    }

    public void setCreator(Person creator) {
	this.creator = creator;
    }

    public Set<PhdEmailParticipantsGroup> getParticipantsGroup() {
	final Set<PhdEmailParticipantsGroup> result = new TreeSet<PhdEmailParticipantsGroup>(COMPARATOR_BY_NAME);
	if (this.participantsGroup != null) {
	    for (final PhdEmailParticipantsGroup participantGroup : participantsGroup) {
		result.add(participantGroup);
	    }
	}
	return result;
    }

    public void setParticipantsGroup(List<PhdEmailParticipantsGroup> participantsGroup) {
	if (participantsGroup == null) {
	    this.participantsGroup = null;
	} else {
	    this.participantsGroup = new HashSet<PhdEmailParticipantsGroup>();
	    for (final PhdEmailParticipantsGroup participantGroup : participantsGroup) {
		this.participantsGroup.add(participantGroup);
	    }
	}
    }

    public static final Comparator<PhdEmailParticipantsGroup> COMPARATOR_BY_NAME = new Comparator<PhdEmailParticipantsGroup>() {

	public int compare(PhdEmailParticipantsGroup g1, PhdEmailParticipantsGroup g2) {
	    return g1.getGroupLabel().compareTo(g2.getGroupLabel());
	}

    };

    public Set<PhdEmailParticipantsGroup> getPossibleParticipantsGroups() {
	final Set<PhdEmailParticipantsGroup> groups = new TreeSet<PhdEmailParticipantsGroup>(COMPARATOR_BY_NAME);

	groups.add(new PhdEmailParticipantsCoordinatorsGroup());
	if (getProcess().hasAnyGuidings()) {
	    groups.add(new PhdEmailParticipantsGuidersGroup());
	}
	groups.add(new PhdEmailParticipantsAllGroup());
	if (getProcess().hasThesisProcess()) {
	    groups.add(new PhdEmailParticipantsJuryMembersGroup());
	}

	return groups;
    }

    public List<PhdEmailParticipantsGroup> getPossibleParticipantsGroupsList() {
	List<PhdEmailParticipantsGroup> groups = new ArrayList<PhdEmailParticipantsGroup>();

	groups.add(new PhdEmailParticipantsCoordinatorsGroup());
	groups.add(new PhdEmailParticipantsGuidersGroup());
	groups.add(new PhdEmailParticipantsAllGroup());

	if (getProcess().hasThesisProcess()) {
	    groups.add(new PhdEmailParticipantsJuryMembersGroup());
	}

	return groups;
    }

    public List<PhdParticipant> getSelectedParticipants() {
	final List<PhdParticipant> result = new ArrayList<PhdParticipant>();
	if (this.selectedParticipants != null) {
	    for (final PhdParticipant participant : selectedParticipants) {
		result.add(participant);
	    }
	}
	return result;
    }

    public void setSelectedParticipants(List<PhdParticipant> selectedParticipants) {
	if (selectedParticipants == null) {
	    this.selectedParticipants = null;
	} else {
	    this.selectedParticipants = new HashSet<PhdParticipant>();
	    for (final PhdParticipant participant : selectedParticipants) {
		this.selectedParticipants.add(participant);
	    }
	}
    }


}
