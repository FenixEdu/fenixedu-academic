package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateConversationThreadAndMessageBean implements Serializable {

	private MultiLanguageString subject;

	private MultiLanguageString body;

	private Person creatorReference;

	private Forum forumReference;

	public CreateConversationThreadAndMessageBean() {
		super();
		creatorReference = null;
		forumReference = null;
	}

	public MultiLanguageString getSubject() {
		return subject;
	}

	public void setSubject(MultiLanguageString subject) {
		this.subject = subject;
	}

	public MultiLanguageString getBody() {
		return body;
	}

	public void setBody(MultiLanguageString body) {
		this.body = body;
	}

	public Person getCreator() {
		return this.creatorReference;

	}

	public void setCreator(Person creator) {
		this.creatorReference = creator;
	}

	public Forum getForum() {
		return this.forumReference;

	}

	public void setForum(Forum forum) {
		this.forumReference = forum;
	}

}
